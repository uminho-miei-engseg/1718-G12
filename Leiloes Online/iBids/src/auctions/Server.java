/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

/**
 *
 * @author Esquivel
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class Server {
    
    private static final int PORT = 6063;

    private static final int TYPE_NOT_VALID = -1;
    private static final int SERVER_FULL_CODE = -2;
    private static final int NO_AUCTIONS_AVAILABLE = -3;
    private static final int SIGNATURE_NOT_VALID = -4;
    private static final int USER_ALREADY_REGISTERED = -5;
    private static final int USER_NOT_REGISTERED = -6;
    private static final int AUCTION_DOESNT_EXIST = -7;
    private static final int WRONG_USER_ACCESS = -8;
    private static final int FAILURE_CREATING_AUCTION = -9;
    private static final int INVALID_SERVER_SIGNATURE = -10;
    private static final int AUCTION_IS_INACTIVE = -11;
    private static final int AUCTION_STILL_VALID = -12;
    private static final int RECEIPT_IS_FALSE = -13;

    private static final int maxThreads = 2000;

    private static final int TENUMBER = 2;

    private static ServerThread[] threads = new ServerThread[maxThreads];

    private static ReentrantLock threadsLock = new ReentrantLock();

    private static final String KEYSTORE_LOCATION = System.getProperty("user.dir") + "\\Certificates\\ServerKeyStore.jks";
    private static final String KEYSTORE_PASSWORD = "server123";

    private static File file_key_store = new File(KEYSTORE_LOCATION);

    private static KeyStore keystore;
    private static PrivateKey pk;
    private static PublicKey pub;
    private static Certificate cert;
    private static SecretKey serverAES;

    private static HashMap<String, User> users = new HashMap<>();
    private static HashMap<String, Auction> auctions = new HashMap<>();
    private static HashMap<String, Timer> timers = new HashMap<>();
    private static TrustedEntity[] tes = new TrustedEntity[TENUMBER];
    //private static ServerVars sv = ServerVars.getInstanceOf();
    

    
    public static void endAuction(String auctionHash) {
        int notNull = 0;
        Auction auction = auctions.get(auctionHash);
        auction.setActive(false);
        SecretShare[] sss = new SecretShare[TENUMBER + 2];
        for (int i = 0; i < tes.length; i++) {
            sss[i + 1] = tes[i].getShare(auctionHash);
            if (sss[i + 1] != null) {
                notNull++;
            }
        }
        //System.out.println("Trusted Entity shares: " + notNull);
        if (notNull >= TENUMBER) {
            //não é preciso o vendedor
            try {
                String winner = auction.getWinner(sss);
                //System.out.println(winner.length());
                if (winner != null) {                    
                    System.out.printf("Auction [%s] finished, and the winner is user [%s].", auctionHash, winner);
                    
                } else {
                    System.out.printf("Auction [%s] finished, and there was no winner.", auctionHash);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                auction.setActive(true);
            }
        }
        else auction.setActive(true);
    }

    public static String testCreateAuction(String pubKeySeller, String description, double basePrice, long time) {
            try {
                SecretKey key = Crypto.keyGen("AES", 128);
                KeyPair kp = Crypto.buildKeyPair("RSA", 2048);
                PublicKey auctionPub = kp.getPublic();
                String encPrivateKey = Crypto.encryptText(key, Crypto.getStringFromKey(kp.getPrivate()), "AES");
                ShamirInfo si = Crypto.getShamirShares(key, TENUMBER + 1, TENUMBER + 2);
                SecretShare[] ss = si.getShares(-1);
                si.eraseShares();
                
                si.setShare(ss[0]);
                //si.printShares();
                if (ss[0]==null) System.out.println("No");
                key = null;
                //
                String auctionPubString = Crypto.getStringFromKey(auctionPub);
                String auctionHash = Crypto.hash(auctionPubString, "SHA-256");
                long begin = System.currentTimeMillis();
                long end = System.currentTimeMillis() + time;
                auctions.put(auctionHash, new Auction((PublicKey) Crypto.getKeyFromString(pubKeySeller, "DSA", "public"), basePrice,
                        description, begin, end,
                        auctionPub, encPrivateKey, si));
                
                for (int i = 0; i < TENUMBER; i++) {
                    //System.out.printf("\n\n\n%s - %s - %s - %s - %s\n", TENUMBER, auctionHash, begin, end, ss[i + 1]);
                    if (tes[i]==null) tes[i] = new TrustedEntity();
                    tes[i].setAuction(auctionPubString, begin, end, ss[i + 1]);
                    //tes[i].printAuctions();
                }
                Timer timer = new Timer();
                timers.put(auctionHash, timer);
                timers.get(auctionHash).schedule(new TimerTask() {
                    @Override
                    public void run () {
                        //System.out.println("Timer started.");
                        endAuction(auctionHash);
                        timer.cancel();
                    }
                }, time);
                SecretShare toSeller = ss[ss.length - 1];
                //System.out.println("HEYYYYY" + toSeller.getNumber());
                //System.out.println(toSeller.getShare().toString());
                //nesta parte falta a share do Seller, que é retornada neste return
                //1 share está no leilão, 1 no seller e o resto nas TE, e precisamos de todas menos uma para recuperar
                //endAuction(auctionHash);
                return String.format("%s:::%s:::%s", auctionPubString, toSeller.getNumber(), toSeller.getShare().toString());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    private static class ServerThread extends Thread {

        SSLSocket s;
        int threadNumber = -1;
        private BufferedReader in;
        private PrintWriter out;
        private boolean active;
        private long stimestamp = 0;
        private StringTokenizer st;
        private int aux = 0;
        private InputStream sis;
        private OutputStream sos;
        private ObjectOutputStream soos;
        private ObjectInputStream sois;

        public ServerThread(Socket socket, int threadNumber) {
            this.s = (SSLSocket) socket;
            this.threadNumber = threadNumber;
            System.out.printf("Thread %d is active.\n", threadNumber);
            try {
                sis = s.getInputStream();
                sos = s.getOutputStream();
                soos = new ObjectOutputStream(sos);
                sois = new ObjectInputStream(sis);
            } catch (Exception e) {
                System.out.println("Problem opening server streams!");
            }
        }

        public ServerThread(Socket socket) {
            this.s = (SSLSocket) socket;
        }

        private void deleteThread() {
            try {
                in.close();
                out.close();
                sois.close();
                soos.close();
                sos.close();
                sis.close();
                s.close();
            } catch (Exception e2) {
            }

            try {
                //threadsLock.lock();
                threads[threadNumber] = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                //threadsLock.unlock();
            }
        }
        
        

        public String createAuction(String pubKeySeller, String description, double basePrice, long time) {
            try {
                SecretKey key = Crypto.keyGen("AES", 128);
                KeyPair kp = Crypto.buildKeyPair("RSA", 2048);
                PublicKey auctionPub = kp.getPublic();
                String encPrivateKey = Crypto.encryptText(key, Crypto.getStringFromKey(kp.getPrivate()), "AES");
                kp = null;
                ShamirInfo si = Crypto.getShamirShares(key, TENUMBER + 1, TENUMBER + 2);
                try {
                    key.destroy();
                }
                catch (Exception e) {
                    key = null;
                }
                SecretShare[] ss = si.getShares(-1);
                si.eraseShares();
                si.setShare(ss[0]);
                //si.printShares();
                if (ss[0]==null) System.out.println("OOPS");
                //
                String auctionPubString = Crypto.getStringFromKey(auctionPub);
                String auctionHash = Crypto.hash(auctionPubString, "SHA-256");
                long begin = System.currentTimeMillis();
                long end = System.currentTimeMillis() + time;
                //System.out.println("Duration: " + encPrivateKey);
                auctions.put(auctionHash, new Auction((PublicKey) Crypto.getKeyFromString(pubKeySeller, "DSA", "public"), basePrice,
                        description, begin, end,
                        auctionPub, encPrivateKey, si));
                
                for (int i = 0; i < TENUMBER; i++) {
                    //System.out.printf("\n\n\n%s - %s - %s - %s - %s\n", TENUMBER, auctionHash, begin, end, ss[i + 1]);
                    if (tes[i]==null) tes[i] = new TrustedEntity();
                    tes[i].setAuction(auctionPubString, begin, end, ss[i + 1]);
                    //tes[i].printAuctions();
                }
                Timer timer = new Timer();
                timers.put(auctionHash, timer);
                timers.get(auctionHash).schedule(new TimerTask() {
                    @Override
                    public void run () {
                        System.out.println("Timer started.");
                        endAuction(auctionHash);
                        timer.cancel();
                    }
                }, time);
                SecretShare toSeller = ss[ss.length - 1];
                //System.out.println("HEYYYYY" + toSeller.getNumber());
                //System.out.println(toSeller.getShare().toString());
                //nesta parte falta a share do Seller, que é retornada neste return
                //1 share está no leilão, 1 no seller e o resto nas TE, e precisamos de todas menos uma para recuperar
                //endAuction(auctionHash);
                return String.format("%s:::%s", auctionPubString, Crypto.encryptText(serverAES, toSeller.getNumber() + ":::" + toSeller.getShare().toString(), "AES"));
            } 
            
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void run() {

            try {
                //in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                //out = new PrintWriter(s.getOutputStream(), true);
                if (threadNumber < 0) {
                    soos.writeObject(SERVER_FULL_CODE);
                    return;
                }
                String line, signature, pubKeyHash, toVerify;
                StringTokenizer tok;
                while (true) {
                    //pode haver um problema com o ";", porque pode ser que este apareça na string codificada da chave
                    Data clientData = (Data) sois.readObject();
                    System.out.println(clientData.getType());
                    switch (clientData.getType()) {
                        case 1:
                            line = clientData.getPubString();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            signature = clientData.getSignature();
                            if (users.containsKey(pubKeyHash)) {
                                clientData.setType(USER_ALREADY_REGISTERED);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setSignature(null);                            
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            if (Crypto.verify((PublicKey)Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), toVerify, signature, "SHA256withDSA")) {
                                users.put(pubKeyHash, new User(line, clientData.getSecRandom()));
                                clientData.setSignature(Crypto.sign(pk, toVerify, "SHA256withDSA"));
                                clientData.setSecRandom(-1);
                                soos.writeObject(clientData);
                            } else {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                            }
                            break;
                        case 2:
                            line = clientData.getPubString();
                            signature = clientData.getSignature();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            signature = clientData.getSignature();
                            if (!users.containsKey(pubKeyHash)) {
                                clientData.setType(USER_NOT_REGISTERED);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setSignature(null); 
                            clientData.setSecRandom(users.get(pubKeyHash).getRandomNumber());
                            System.out.println(clientData.getSecRandom());
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            if (!Crypto.verify((PublicKey)Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), toVerify, signature, "SHA256withDSA")) {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            if (!auctions.isEmpty()) {
                                int size = auctions.size();
                                clientData.setAuctions(size);
                                auctions.forEach((k, v) -> clientData.setAuction(v.getAuctionData(), -1));
                                toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                                clientData.setSignature(Crypto.sign(pk, toVerify, "SHA256withDSA"));
                                clientData.setSecRandom(-1);
                                soos.writeObject(clientData);
                            } else {
                                clientData.setType(NO_AUCTIONS_AVAILABLE);
                                soos.writeObject(clientData);
                            }
                            break;
                        case 3:
                            line = clientData.getPubString();
                            signature = clientData.getSignature();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            if (!users.containsKey(pubKeyHash)) {
                                clientData.setType(USER_NOT_REGISTERED);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setSignature(null); 
                            clientData.setSecRandom(users.get(pubKeyHash).getRandomNumber());
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            if (!Crypto.verify((PublicKey)Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), toVerify, signature, "SHA256withDSA")) {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            String auction = clientData.getAuctions(1)[0];
                            if (!auctions.containsKey(auction)) {
                                clientData.setType(AUCTION_DOESNT_EXIST);
                                soos.writeObject(clientData);
                                break;
                            }
                            if (!auctions.get(auction).isActive()) {
                                clientData.setType(AUCTION_IS_INACTIVE);
                                soos.writeObject(clientData);
                                break;
                            }
                            auctions.get(auction).setBid(clientData.getPubString(), clientData.getBid());
                            String receipt = Crypto.sign(pk, Crypto.toBase64String(Crypto.toByteArray(clientData.getBid())), "SHA256withDSA");
                            clientData.setAuction(receipt, 0);
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            clientData.setSignature(Crypto.sign(pk, toVerify, "SHA256withDSA"));
                            clientData.setSecRandom(-1);
                            soos.writeObject(clientData);
                            break;
                        case 4:
                            line = clientData.getPubString();
                            signature = clientData.getSignature();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            if (!users.containsKey(pubKeyHash)) {
                                clientData.setType(USER_NOT_REGISTERED);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setSignature(null); 
                            clientData.setSecRandom(users.get(pubKeyHash).getRandomNumber());
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            if (!Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), toVerify, signature, "SHA256withDSA")) {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            tok = new StringTokenizer(clientData.getAuctions(1)[0], ":::");
                            String description = tok.nextToken();
                            double basePrice = Double.parseDouble(tok.nextToken());
                            long time = Long.parseLong(tok.nextToken());
                            String auctionData = createAuction(line, description, basePrice, time);
                            if (auctionData == null) {
                                clientData.setType(FAILURE_CREATING_AUCTION);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setAuction(auctionData, 0);
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            clientData.setSignature(Crypto.sign(pk, toVerify, "SHA256withDSA"));
                            clientData.setSecRandom(-1);
                            soos.writeObject(clientData);
                            break;
                        case 5:
                            line = clientData.getPubString();
                            signature = clientData.getSignature();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            if (!users.containsKey(pubKeyHash)) {
                                clientData.setType(USER_NOT_REGISTERED);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setSignature(null); 
                            clientData.setSecRandom(users.get(pubKeyHash).getRandomNumber());
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            if (!Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), toVerify, signature, "SHA256withDSA")) {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            String auctionToCheck = clientData.getAuctions(1)[0];
                            if (!auctions.containsKey(auctionToCheck)) {
                                clientData.setType(AUCTION_DOESNT_EXIST);
                                soos.writeObject(clientData);
                                break;
                            }
                            if (!Crypto.hash(Crypto.getStringFromKey(auctions.get(auctionToCheck).getSeller()), "SHA-256").equals(Crypto.hash(clientData.getPubString(),"SHA-256"))) {
                                clientData.setType(WRONG_USER_ACCESS);
                                soos.writeObject(clientData);
                                break;
                            }
                            clientData.setAuction(auctions.get(auctionToCheck).getAuctionData(),0);
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            clientData.setSignature(Crypto.sign(pk, toVerify, "SHA256withDSA"));
                            clientData.setSecRandom(-1);
                            soos.writeObject(clientData);
                            break;
                        case 6:
                            line = clientData.getPubString();
                            signature = clientData.getSignature();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            clientData.setSignature(null);
                            clientData.setSecRandom(users.get(pubKeyHash).getRandomNumber());
                            toVerify = Crypto.toBase64String(Crypto.toByteArray(clientData));
                            if (!Crypto.verify((PublicKey)Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), toVerify, signature, "SHA256withDSA")) {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            String auctionHash = clientData.getAuctions(1)[0];
                            if (auctions.get(auctionHash).isActive()) {
                                clientData.setType(AUCTION_STILL_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            String receivedReceipt = clientData.getAuctions(-1)[1];
                            String serverReceipt = Crypto.sign(pk, Crypto.toBase64String(Crypto.toByteArray(auctions.get(clientData.getAuctions(-1)[0]).getBid(line))), "SHA256withDSA");
                           
                            if ((receivedReceipt == null || serverReceipt == null) && !receivedReceipt.equals(serverReceipt)) {
                                clientData.setType(RECEIPT_IS_FALSE);
                                soos.writeObject(clientData);
                                break;
                            }
                            signature = Crypto.sign(pk, toVerify, "SHA256withDSA");
                            clientData.setSignature(signature);
                            soos.writeObject(clientData);
                            break;
                        default:
                            soos.writeObject(TYPE_NOT_VALID);
                            break;
                    }
                    if (clientData.getType() == -1) {
                        break;
                    }
                }
                System.out.println("While ended.");
                deleteThread();

            } catch (Exception ioe) {
                System.out.println(ioe.getMessage() + " -> this is an exception");
                deleteThread();
                return;
            }
        }

    }

    private static int nextFree(Object[] array) {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            if (array[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {

        System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
        System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

        keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(new FileInputStream(file_key_store), KEYSTORE_PASSWORD.toCharArray());

        String alias = "ServerC";

        pk = (PrivateKey) keystore.getKey(alias, KEYSTORE_PASSWORD.toCharArray());
        cert = keystore.getCertificate(alias);
        pub = cert.getPublicKey();
        serverAES = Crypto.keyGen("AES", 128);

        KeyPair kp = Crypto.buildKeyPair("RSA", 2048);
        PublicKey p = kp.getPublic();
        PrivateKey p2 = kp.getPrivate();
        SecretKey aesKey = Crypto.keyGen("AES", 128);
        
        for (int i = 0; i < TENUMBER; i++) {
            tes[i] = new TrustedEntity();
        }

        // test auctions
        testCreateAuction (Crypto.getStringFromKey(pub), "This is the Patrick Star description.", 4.20, 15000);
        //endAuction (Crypto.hash(Crypto.getStringFromKey(pub), "SHA-256"));
        System.out.println("Test auction was created.");
        
        try {
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            ServerSocket sslServerSocket
                    = sslServerSocketFactory.createServerSocket(PORT);
            System.out.printf("Server is running on %s:%d\n", InetAddress.getLocalHost().getHostAddress(), PORT);
            int threadID = 0;
            threads = new ServerThread[maxThreads];

            while (true) {
                try {
                    threadsLock.lock();
                    threadID = nextFree(threads);
                    if (threadID >= 0) {
                        threads[threadID] = new ServerThread(sslServerSocket.accept(), threadID);
                        threads[threadID].start();
                    } else {
                        new ServerThread(sslServerSocket.accept()).start();
                    }
                } finally {
                    threadsLock.unlock();
                }

            }
            //System.out.println("Closed");

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

}
