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
import java.math.BigInteger;
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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.sql.rowset.serial.SerialBlob;

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

    private static final int maxThreads = 2000;

    private static final int TENUMBER = 2;

    private static ServerThread[] threads = new ServerThread[maxThreads];

    private static ReentrantLock threadsLock = new ReentrantLock();

    private static final String KEYSTORE_LOCATION = System.getProperty("user.dir") + "//Certificates//ServerKeyStore.jks";
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
        System.out.println("Entered endAuction");
        System.out.println(auctionHash);
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
            System.out.println("Seller won't be needed.");
            try {
                String winner = auction.getWinner(sss);
                if (winner != null) {
                    System.out.printf("Auction [%s] finished, and the winner is user [%s].", auctionHash, winner);
                } else {
                    System.out.printf("Auction [%s] finished, and there was no winner.", auctionHash);
                };
            } catch (Exception e) {
                System.out.println(e.getMessage());
                auction.setActive(true);
            }
        }
        auction.setActive(true);
    }

    public static String testCreateAuction(String pubKeySeller, String description, double basePrice, long time) {
            try {
                SecretKey key = Crypto.keyGen("AES", 128);
                System.out.println(Crypto.getStringFromKey(key));
                KeyPair kp = Crypto.buildKeyPair("RSA", 2048);
                PublicKey auctionPub = kp.getPublic();
                String encPrivateKey = Crypto.encryptText(key, Crypto.getStringFromKey(kp.getPrivate()), "AES");
                ShamirInfo si = Crypto.getShamirShares(key, TENUMBER + 1, TENUMBER + 2);
                SecretShare[] ss = si.getShares(-1);
                si.eraseShares();
                
                si.setShare(ss[0]);
                //si.printShares();
                if (ss[0]==null) System.out.println("SHIT");
                key = null;
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
                System.out.println(Crypto.getStringFromKey(key));
                KeyPair kp = Crypto.buildKeyPair("RSA", 2048);
                PublicKey auctionPub = kp.getPublic();
                String encPrivateKey = Crypto.encryptText(key, Crypto.getStringFromKey(kp.getPrivate()), "AES");
                ShamirInfo si = Crypto.getShamirShares(key, TENUMBER + 1, TENUMBER + 2);
                SecretShare[] ss = si.getShares(-1);
                si.eraseShares();
                
                si.setShare(ss[0]);
                //si.printShares();
                if (ss[0]==null) System.out.println("OOPS");
                key = null;
                //
                String auctionPubString = Crypto.getStringFromKey(auctionPub);
                String auctionHash = Crypto.hash(auctionPubString, "SHA-256");
                long begin = System.currentTimeMillis();
                long end = System.currentTimeMillis() + time;
                String shamir_part = ss[0].getShare().toString();
                int secret_share_number = ss[0].getNumber();
                //System.out.println("Duration: " + encPrivateKey);
                  /* ------------------------------------------------------------------------------
                                                INSERÇÃO DE AUCTION NA BD                      
                    ------------------------------------------------------------------------------
                 */
                try{
                    
                    
                    Connection conn = DB_Connection.getConnection();
                    String query = String.format("INSERT INTO auctions.shamir(part_shamir_key,secret_share_number, prime, quorum, invert, shamir_number) VALUES(\"%s\", %s, \"%s\", %s, %s, %s)",
                            shamir_part,secret_share_number,si.getPrime(),si.getQuorum(),si.isInvert(),si.getNumber());
                    PreparedStatement ps1 = conn.prepareStatement(query);
                    ps1.executeUpdate(query);
                    ps1.close();
                    
                    query = String.format("SELECT id FROM auctions.shamir WHERE part_shamir_key= \"%s\" ",shamir_part);
                    PreparedStatement ps2 = conn.prepareStatement(query);
                    ResultSet rs1 = ps2.executeQuery(query);                    
                    if(!rs1.next()) throw new Exception(" Shamir exception");
                    int shamir_id = rs1.getInt("id");
                    ps2.close();
                    
                    query = String.format("SELECT id FROM auctions.user WHERE public_key= \"%s\" ",pubKeySeller);
                    PreparedStatement ps3 = conn.prepareStatement(query);
                    ResultSet rs2 = ps3.executeQuery(query);
                    if(!rs2.next()) throw new Exception("Seller does not exist");
                    int seller_id = rs2.getInt("id");
                    ps3.close();
                    
                    
                    long db_time = end / 1000;
                    query = String.format("INSERT INTO auctions.auction(base_price, description, public_key, private_key, end_d, part_shamir_key, seller) VALUES(%s,\"%s\", \"%s \", \"%s\", from_unixtime(%s), %s, %s)", basePrice,description,auctionPubString,encPrivateKey,db_time,shamir_id,seller_id);
                    PreparedStatement ps4 = conn.prepareStatement(query);
                    ps4.executeUpdate();
                    ps4.close();
                    
                    }catch(Exception e){
                    System.out.println(e);
                }
                
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
                String line, signature, pubKeyHash;
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
                            if (Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), pubKeyHash + Integer.toString(clientData.getSecRandom()), signature, "SHA256withDSA")) {
                                
                                /* ------------------------------------------------------------------------------
                                                            INSERÇÃO DE USER NA BD                      
                                     ------------------------------------------------------------------------------
                                 */
                                Connection conn = null;
                                PreparedStatement ps = null;
                                String query = String.format("INSERT INTO auctions.user(public_key, random_number) VALUES(\"%s\", %s)",line,clientData.getSecRandom());
                                try{
                                    conn = DB_Connection.getConnection();
                                    ps = conn.prepareStatement(query);
                                    ps.executeUpdate();
                                    ps.close();
                                    conn.close();
                                } catch(SQLException e){
                                    System.out.println("SQLException: " + e.getMessage());  
                                }
                                
                                
                                users.put(pubKeyHash, new User(line, clientData.getSecRandom()));
                                clientData.setSignature(Crypto.sign(pk, pubKeyHash + Integer.toString(clientData.getSecRandom()), "SHA256withDSA"));
                                soos.writeObject(clientData);
                            } else {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                            }
                            break;
                        case 2:
                            /* ------------------------------------------------------------------------------
                                                    PREENCHER HASH DAS AUCTIONS
                            * Problema é transformar a string da chave publica num objeto chave publica
                            ---------------------------------------------------------------------------------
                            */
                            
                            try{
                                Connection conn = DB_Connection.getConnection();
                                String query = String.format("SELECT * FROM auctions.auction WHERE end_d > CURRENT_TIMESTAMP");
                                PreparedStatement ps1 = conn.prepareStatement(query);
                                ResultSet rs = ps1.executeQuery(query);
                                if(!rs.next()) 
                                    System.out.println("Não existem leilões a decorrer!!");
                                else{
                                        
                                    while (rs.next()) {
                                        query = String.format("SELECT * FROM auctions.shamir WHERE id = %s", rs.getInt("part_shamir_key"));

                                        PreparedStatement ps2 = conn.prepareStatement(query);
                                        ResultSet rs2 = ps2.executeQuery(query);
                                        if (!rs2.next()) {
                                            throw new Exception(" Shamir exception");
                                        }
                                        BigInteger part_shamir_key = new BigInteger(rs2.getString("part_shamir_key"));

                                        SecretShare[] s = new SecretShare[(1)];
                                        SecretShare ss2 = new SecretShare(rs2.getInt("secret_share_number"), part_shamir_key);
                                        s[0] = ss2;
                                        BigInteger prime = new BigInteger(rs2.getString("prime"));
                                        ShamirInfo shamir = new ShamirInfo(s, prime, rs2.getBoolean("invert"), rs2.getInt("quorum"), rs2.getInt("shamir_number"));
                                       
                                        //PROBLEMA AQUI!!! PASSAR A STRING DA CHAVE PARA PUBLICKEY 
                                        PublicKey auctionPub = (PublicKey) Crypto.getKeyFromString(rs.getString("public_key"), "RSA", "public");
                                   
                                        String auctionPubString = Crypto.getStringFromKey(auctionPub);
                                        String auctionHash = Crypto.hash(auctionPubString, "SHA-256");
                                        
                                        double base_price = rs.getFloat("base_price");
                                        
                                        query = String.format("SELECT public_key FROM auctions.user WHERE id = %s", rs.getInt("seller"));
                                        PreparedStatement ps3 = conn.prepareStatement(query);
                                        ResultSet rs3 = ps3.executeQuery(query);
                                        if (!rs3.next()) {
                                            throw new Exception(" Shamir exception");
                                        }
                                        PublicKey pubKeySeller = (PublicKey) Crypto.getKeyFromString(rs3.getString("public_key"), "DSA", "public");

                                        auctions.put(auctionHash, new Auction(pubKeySeller, base_price, rs.getString("description"), rs.getLong("begin_d"), rs.getLong("end_d"), auctionPub, rs.getString("private_key"), shamir));
                                        System.out.println("INSERIU");
                                        ps2.close();
                                        ps3.close();

                                    }
                                }
                                ps1.close();                    
                                conn.close();
                            } catch (Exception e) {
                                System.out.println("NAO INSERIU");
                                System.out.println(e);
                            }
                            
                            
                            line = clientData.getPubString();
                            signature = clientData.getSignature();
                            pubKeyHash = Crypto.hash(line, "SHA-256");
                            if (!users.containsKey(pubKeyHash)) {
                                clientData.setType(USER_NOT_REGISTERED);
                                soos.writeObject(clientData);
                                break;
                            }
                            if (!Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), pubKeyHash + Integer.toString(users.get(pubKeyHash).getRandomNumber()), signature, "SHA256withDSA")) {
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            if (!auctions.isEmpty()) {
                                int size = auctions.size();
                                clientData.setAuctions(size);
                                auctions.forEach((k, v) -> clientData.setAuction(v.getAuctionData(), -1));
                                clientData.setSignature(Crypto.sign(pk, pubKeyHash
                                        + Integer.toString(users.get(pubKeyHash).getRandomNumber()), "SHA256withDSA"));
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
                            if (!Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), Crypto.hash(line
                                    + Integer.toString(users.get(pubKeyHash).getRandomNumber())
                                    + Crypto.toBase64String(Crypto.toByteArray(clientData.getBid())), "SHA-256"), signature, "SHA256withDSA")) {
                                //System.out.println("Siganture not valid.");
                                clientData.setType(SIGNATURE_NOT_VALID);
                                soos.writeObject(clientData);
                                break;
                            }
                            String auction = clientData.getAuctions(1)[0];
                            
                            /* ------------------------------------------------------------------------------
                                                    INSERÇÂO DE BID NA BD
                            * a variavel auction é um hash da pubKey;
                            * a bid que vem no clientData é um sealedObjec, 
                                não posso fazer decrypt para retirar a chave da auction pq não tenho a chave
                                usada para encapsular
                            ---------------------------------------------------------------------------------
                            */
                            
                           
                            try{
                                Connection conn = DB_Connection.getConnection();
                                String query = String.format("SELECT id FROM auctions.user WHERE public_key= \"%s\" ", line);
                                PreparedStatement ps1 = conn.prepareStatement(query);
                                ResultSet rs = ps1.executeQuery(query);
                                if (!rs.next()) {
                                    throw new Exception("User does not exist");
                                }
                                int user_id = rs.getInt("id");
                                ps1.close();
                                
                                //XXXXXXX PROBLEMA: RETIRAR CHAVE PUBLICA DA AUCTION DE UMA BID
                                byte[] key = Crypto.reverseBase64(auction);
                                String  auction_pub = new String(key);
                                //XXXXXX
                                query = String.format("SELECT id FROM auctions.auction WHERE public_key= \"%s\" ", auction_pub);
                                PreparedStatement ps2 = conn.prepareStatement(query);
                                rs = ps2.executeQuery(query);
                                if (!rs.next()) {
                                    throw new Exception("Auction does not exist");
                                }
                                int auction_id = rs.getInt("id");
                                ps2.close();
                                
                                byte[] offer = Crypto.toByteArray(clientData.getBid());                           

                                Blob blob = new SerialBlob(offer);


                                query = String.format("INSERT INTO auctions.bids(user_id,auction_id,sealed_offer) VALUES(\"%s\", \"%s\", %s)", user_id, auction_id, blob );
                                PreparedStatement ps3 = conn.prepareStatement(query);
                                ps3.executeUpdate();
                                
                                ps3.close();
                                conn.close();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            
                            auctions.get(auction).setBid(clientData.getPubString(), clientData.getBid());
                            String toSign = Crypto.hash(line + Integer.toString(users.get(pubKeyHash).getRandomNumber()) + Crypto.toBase64String(Crypto.toByteArray(clientData.getBid())), "SHA-256");
                            clientData.setSignature(Crypto.sign(pk, toSign, "SHA256withDSA"));
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
                            if (!Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), pubKeyHash + Integer.toString(users.get(pubKeyHash).getRandomNumber()), signature, "SHA256withDSA")) {
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
                            clientData.setSignature(Crypto.sign(pk, pubKeyHash
                                    + Integer.toString(users.get(pubKeyHash).getRandomNumber()), "SHA256withDSA"));
                            clientData.setAuction(auctionData, 0);
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
                            if (!Crypto.verify((PublicKey) Crypto.getKeyFromBytes(Crypto.reverseBase64(line), "DSA", "public"), pubKeyHash + Integer.toString(users.get(pubKeyHash).getRandomNumber()), signature, "SHA256withDSA")) {
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
                            clientData.setSignature(Crypto.sign(pk, pubKeyHash + Integer.toString(users.get(pubKeyHash).getRandomNumber()), "SHA256withDSA"));
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
        testCreateAuction (Crypto.getStringFromKey(pub), "This is the Patrick Star description.", 4.20, 1);
        //endAuction (Crypto.hash(Crypto.getStringFromKey(pub), "SHA-256"));
        System.out.println("Test auction was created.");
        
//        auctions.put(Crypto.hash(Crypto.getStringFromKey(p), "SHA-256"), new Auction(pub, 30.0,
//                "This is the Patrick Star description.", System.currentTimeMillis(), System.currentTimeMillis() + 20000,
//                p, Crypto.encryptText(aesKey, Crypto.getStringFromKey(p2), "AES"), null));

        /*auctions.put(Crypto.getStringFromPublicKey(Crypto.buildKeyPair("DSA",2048).getPublic()), new Auction(p, 50.0,
                "This is the other description.", System.currentTimeMillis(), System.currentTimeMillis() + 86400000,
                Crypto.buildKeyPair("RSA",2048).getPublic()));
        p = Crypto.buildKeyPair("DSA",2048).getPublic();
        
        auctions.put(Crypto.getStringFromPublicKey(Crypto.buildKeyPair("DSA",2048).getPublic()), new Auction(p, 70.0,
                "Patrick wrtoe this description.", System.currentTimeMillis(), System.currentTimeMillis() + 2*86400000,
                Crypto.buildKeyPair("RSA",2048).getPublic()));
        p = Crypto.buildKeyPair("DSA",2048).getPublic();
        
        auctions.put(Crypto.getStringFromPublicKey(Crypto.buildKeyPair("DSA",2048).getPublic()), new Auction(p, 10000.0,
                "Patrick was here.", System.currentTimeMillis(), System.currentTimeMillis() + 3*86400000,
                Crypto.buildKeyPair("RSA",2048).getPublic()));*/
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
