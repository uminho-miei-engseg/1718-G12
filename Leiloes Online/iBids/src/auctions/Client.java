/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

//ES

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SealedObject;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class Client {

    final int PORT = 6063;
    private final String TRUSTSTORE_LOCATION = System.getProperty("user.dir") + "\\Certificates\\ClientKeyStore.jks";

    private final String SERVER_FULL_MESSAGE = "Server is full, please try again later.";
    private final String INVALID_SIGNATURE_MESSAGE = "Server could not validate signature.";
    private final String ALREADY_REGISTERED_MESSAGE = "User is already registered in server.";
    private final String USER_NOT_REGISTERED_MESSAGE = "User is not registered in server";
    private final String AUCTION_DOESNT_EXIST_MESSAGE = "The requested auction does not exist.";
    private final String WRONG_USER_ACCESS_MESSAGE = "The chosen auction does not belong to you.";
    private final String NO_AUCTIONS_AVAILABLE_MESSAGE = "There are currently no auctions available.";
    private final String FAILURE_CREATING_AUCTION_MESSAGE = "Could not create auction.";
    private final String INVALID_SERVER_SIGNATURE_MESSAGE = "Server signature is not valid.";
    private final String AUCTION_IS_INACTIVE_MESSAGE = "The chosen auction is inactive.";
    private final String AUCTION_STILL_VALID_MESSAGE = "The chosen auction is still active.";
    private final String RECEIPT_IS_FALSE_MESSAGE = "The receipt provided is invalid.";


    private final int TYPE_NOT_VALID = -1;
    private final int SERVER_FULL_CODE = -2;
    private final int NO_AUCTIONS_AVAILABLE = -3;
    private final int SIGNATURE_NOT_VALID = -4;
    private final int USER_ALREADY_REGISTERED = -5;
    private final int USER_NOT_REGISTERED = -6;
    private final int AUCTION_DOESNT_EXIST = -7;
    private final int WRONG_USER_ACCESS = -8;
    private final int FAILURE_CREATING_AUCTION = -9;
    private final int INVALID_SERVER_SIGNATURE = -10;
    private final int AUCTION_IS_INACTIVE = -11;
    private final int AUCTION_STILL_VALID = -12;
    private final int RECEIPT_IS_FALSE = -13;

    private File file_key_store = new File(TRUSTSTORE_LOCATION);

    private KeyStore keystore;

    private boolean registered = false;

    private SSLSocketFactory sslSocketFactory;
    private SSLSocket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private InputStream is;
    private OutputStream os;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int randomNumber;

    //private Certificate cert = keystore.getCertificate(alias);
    private PublicKey serverPub;
    private KeyPair clientKeys;
    private PrivateKey pk;
    private PublicKey pub;
    private String publicKey;

    private String description, time, basePrice;// , auctionToBid, price;
    
    private HashMap<String, AuctionClient> auctions = new HashMap<>();
    private HashMap<String, AuctionClient> myAuctions = new HashMap<>();

    public boolean tryParse(String value, String type) {
        try {
            switch (type) {
                case "INT":
                    Integer.parseInt(value);
                    return true;
                case "LONG":
                    Long.parseLong(value);
                    return true;
                case "DOUBLE":
                    Double.parseDouble(value);
                    return true;
                default:
                    return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String errorHandle(int code) {
        switch (code) {
            case SIGNATURE_NOT_VALID:
                //activates if the user's signature is not valid
                return (INVALID_SIGNATURE_MESSAGE);
            case SERVER_FULL_CODE:
                //activates if the server is full
                return(SERVER_FULL_MESSAGE);
            case USER_ALREADY_REGISTERED:
                //activates if the server already registered the user
                return(ALREADY_REGISTERED_MESSAGE);
            case NO_AUCTIONS_AVAILABLE:
                return(NO_AUCTIONS_AVAILABLE_MESSAGE);
            case USER_NOT_REGISTERED:
                return(USER_NOT_REGISTERED_MESSAGE);
            case WRONG_USER_ACCESS:
                return(WRONG_USER_ACCESS_MESSAGE);
            case AUCTION_DOESNT_EXIST:
                return(AUCTION_DOESNT_EXIST_MESSAGE);
            case FAILURE_CREATING_AUCTION:
                return(FAILURE_CREATING_AUCTION_MESSAGE);
            case INVALID_SERVER_SIGNATURE:
                return(INVALID_SERVER_SIGNATURE_MESSAGE);
            case AUCTION_IS_INACTIVE:
                return AUCTION_IS_INACTIVE_MESSAGE;
            case AUCTION_STILL_VALID:
                return AUCTION_STILL_VALID_MESSAGE;
            case RECEIPT_IS_FALSE:
                return RECEIPT_IS_FALSE_MESSAGE;
            default:
                return("Unknown error ocurred.");
        }

    }

    public String printAuctions(){
    
        int i = 0;
        StringBuilder sb = new StringBuilder();
        String[] pubKeys = new String[auctions.size() < 65535 ? auctions.size() : 65534];
        
        for(Map.Entry<String, AuctionClient> entry : auctions.entrySet()) 
        {
            pubKeys[i] = entry.getKey();
            String value = entry.getValue().toString();
            sb.append("Auction item: [" + Integer.toString(i+1) + "] - " + "\n" + value + "\n");
            i++;
        }
        return sb.toString();
    }
    

    public String printMyAuctions() {
        
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, AuctionClient> entry : myAuctions.entrySet()) {
        
            String value = entry.getValue().toString();
            sb.append(value);
        }
        return sb.toString();
    }

    
    
    public void printKeys() {
        System.out.printf("Server public key (hash): %s\n", Crypto.hash(Crypto.getStringFromKey(serverPub),"SHA-256"));
        System.out.printf("Your public key (hash): %s", Crypto.hash(Crypto.getStringFromKey(pub),"SHA-256"));        
    }
    
    public PublicKey getPub(){
        return this.pub;
    }
    public PublicKey getSpub(){
        return this.serverPub;
    }
    public PrivateKey getPk(){
        return this.pk;
    }
    public Map<String,AuctionClient> getAuctions(){
        
        return this.auctions;
    }
    
    public Map<String,AuctionClient> getMyAuctions(){
        
        return this.myAuctions;
    }
        
    public void setDescription(String description){
        this.description = description;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setBasePrice(String basePrice){
        this.basePrice = basePrice;
    }
//    public void setPrice(String price){
//        this.price = price;
//    }
//    public void setAuctionBid(String auctionToBid){
//        this.auctionToBid = auctionToBid;
//    }
    
    public String register(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            byte[] keyBytes = pub.getEncoded();
            String pubString = Crypto.toBase64String(keyBytes);
            int r = SecureRandom.getInstance("SHA1PRNG").nextInt(65535);
            Data send = new Data(1, pubString, null, r);
            String toSign = Crypto.toBase64String(Crypto.toByteArray(send));
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            send.setSignature(signature);
            
            oos.writeObject(send);
            
            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 1:
                    //validate server signature (proof of registration)
                    String serverSignature = received.getSignature();
                    received.setSignature(null);
                    received.setSecRandom(r);
                    String toVerify = Crypto.toBase64String(Crypto.toByteArray(received));
                    if (!Crypto.verify(serverPub, toVerify, serverSignature, "SHA256withDSA")) {
                        return errorHandle(INVALID_SERVER_SIGNATURE);
                    }
                    randomNumber = r;
                    return null;
                default:
                    return errorHandle(received.getType());
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public int getAuctionList(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            Data send = new Data(2, pubString, null, randomNumber);
            String toSign = Crypto.toBase64String(Crypto.toByteArray(send));
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            send.setSignature(signature);
            send.setSecRandom(-1);            
            oos.writeObject(send);
            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 2:
                    //validate server signature (proof of registration)
                    String serverSignature = received.getSignature();
                    received.setSignature(null);
                    received.setSecRandom(randomNumber);
                    String toVerify = Crypto.toBase64String(Crypto.toByteArray(received));
                    if (!Crypto.verify(serverPub, toVerify, serverSignature, "SHA256withDSA")) {
                        errorHandle(INVALID_SERVER_SIGNATURE);
                        return -1;
                    }
                    StringTokenizer st;
                    String[] r = received.getAuctions(0);
                    String key,hash,seller,active;
                    for (int i = 0; i < r.length; i++) {
                        st = new StringTokenizer(r[i], ":::");
                        active = st.nextToken();
                        key = st.nextToken();
                        hash = Crypto.hash(key, "SHA-256");
                        seller = st.nextToken();
                        if (!seller.equals(pubString)) {
                            if (auctions.containsKey(hash)) auctions.get(hash).updateAuction(active.equals("1"), key, seller, st.nextToken(),st.nextToken(), st.nextToken(), (st.hasMoreElements()) ? st.nextToken() : null);
                            else auctions.put(hash, new AuctionClient(key, seller, st.nextToken(),st.nextToken(), st.nextToken()));
                        }
                        else {
                            if (myAuctions.containsKey(hash)) myAuctions.get(hash).updateAuction(active.equals("1"), key, seller, st.nextToken(),st.nextToken(), st.nextToken(), (st.hasMoreElements()) ? st.nextToken() : null);
                            else myAuctions.put(hash, new AuctionClient(key, seller, st.nextToken(),st.nextToken(), st.nextToken()));
                        }
                    }
                    return auctions.size() + myAuctions.size();
                default:
                    errorHandle(received.getType());
                    return -1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    public String createAuction(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            String seller = Crypto.getStringFromKey(pub);
                        
            Data send = new Data(4, seller, null, randomNumber);
            send.setAuctions(1);
            send.setAuction(description + ":::" + basePrice + ":::" + time, 0);
            String toSign = Crypto.toBase64String(Crypto.toByteArray(send));
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            send.setSignature(signature);
            send.setSecRandom(-1);            
            oos.writeObject(send);

            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 4:
                    //validate server signature (proof of registration)
                    String serverSignature = received.getSignature();
                    received.setSignature(null);
                    received.setSecRandom(randomNumber);
                    String toVerify = Crypto.toBase64String(Crypto.toByteArray(received));
                    if (!Crypto.verify(serverPub, toVerify, serverSignature, "SHA256withDSA")) {
                        return errorHandle(INVALID_SERVER_SIGNATURE);
                    }
                    StringTokenizer tok = new StringTokenizer(received.getAuctions(1)[0], ":::");
                    String auctionPub = tok.nextToken();
                    System.out.println(received.getAuctions(1)[0]);
                    myAuctions.put(Crypto.hash(auctionPub, "SHA-256"), new AuctionClient(auctionPub, seller, description, basePrice, time, tok.nextToken()));
                    return null;
                default:
                    return errorHandle(received.getType());
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String bid(PublicKey pub, PrivateKey pk, PublicKey serverPub, String auctionToBid, Double price) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            String[] pubKeys = new String[auctions.size() < 65535 ? auctions.size() : 65534];
            Bid bid = new Bid(price, randomNumber, auctionToBid);
            Data send = new Data(3, pubString, null, randomNumber);
            send.setBid(Crypto.encryptBid(bid, (PublicKey) Crypto.getKeyFromString(auctions.get(auctionToBid).getPublicKey(), "RSA", "public"), "RSA"));
            send.setAuctions(1);
            send.setAuction(auctionToBid, 0);
            
            String toSign = Crypto.toBase64String(Crypto.toByteArray(send));
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            send.setSignature(signature);
            send.setSecRandom(-1);

            oos.writeObject(send);

            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 3:
                    String serverSignature = received.getSignature();
                    received.setSignature(null);
                    received.setSecRandom(randomNumber);
                    String toVerify = Crypto.toBase64String(Crypto.toByteArray(received));
                    if (!Crypto.verify(serverPub, toVerify, serverSignature, "SHA256withDSA")) {
                        return errorHandle(INVALID_SERVER_SIGNATURE);
                    }
                    auctions.get(auctionToBid).setLastBid(price);
                    auctions.get(auctionToBid).setReceipt(received.getAuctions(1)[0]);
                    return null;
                default:
                    return errorHandle(received.getType());                    
            }
        } catch (Exception e) {
            return "Exception.";
        }
    }

    public String checkAuction(PublicKey pub, PrivateKey pk, PublicKey serverPub, String auctionToCheck) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            String[] pubKeys = new String[auctions.size() < 65535 ? auctions.size() : 65534];  
            Data send = new Data(5, pubString, null, randomNumber);
            send.setAuctions(1);
            send.setAuction(auctionToCheck, 0); 
            
            String toSign = Crypto.toBase64String(Crypto.toByteArray(send));
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            send.setSignature(signature);
            send.setSecRandom(-1);
            
            oos.writeObject(send);

            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 5:
                    String serverSignature = received.getSignature();
                    received.setSignature(null);
                    received.setSecRandom(randomNumber);
                    String toVerify = Crypto.toBase64String(Crypto.toByteArray(received));
                    if (!Crypto.verify(serverPub, toVerify, serverSignature, "SHA256withDSA")) {
                        return errorHandle(INVALID_SERVER_SIGNATURE);
                    }
                    String key, hash, seller, active;
                    StringTokenizer st = new StringTokenizer(received.getAuctions(1)[0], ":::");
                    active = st.nextToken();
                    key = st.nextToken();
                    hash = Crypto.hash(key, "SHA-256");
                    seller = st.nextToken();
                    myAuctions.get(auctionToCheck).updateAuction(active.equals("1"), key, seller, st.nextToken(),st.nextToken(), st.nextToken(), (st.hasMoreElements()) ? st.nextToken() : null);
                    System.out.println(myAuctions.get(auctionToCheck));
                    return null;
                default:
                    return errorHandle(received.getType());
            }
        } catch (Exception e) {
            return ("Exception");
        }

    }
    
    public String claimPrize (PublicKey pub, PrivateKey pk, PublicKey serverPub, String auctionHash) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            Data send = new Data(6, pubString, null, randomNumber);
            send.setAuctions(3);
            send.setAuction(auctionHash, 0);
            send.setAuction(auctions.get(auctionHash).getReceipt(), 1);
            System.out.println(auctions.get(auctionHash).getReceipt());
            String toSign = Crypto.toBase64String(Crypto.toByteArray(send));
            String signature = Crypto.sign(pk, toSign , "SHA256withDSA");
            send.setSignature(signature);
            send.setSecRandom(-1);
            oos.writeObject(send);
            Data received = (Data)ois.readObject();
            switch (received.getType()) {
                case 6:
                    //validate server signature (proof of registration)
                    String serverSignature = received.getSignature();
                    received.setSignature(null);
                    received.setSecRandom(randomNumber);
                    String toVerify = Crypto.toBase64String(Crypto.toByteArray(received));
                    if (!Crypto.verify(serverPub, toVerify, serverSignature, "SHA256withDSA")) {
                        return errorHandle(INVALID_SERVER_SIGNATURE);
                    }
                    auctions.get(auctionHash).setVerified(true);
                    return null;
                default:
                    return errorHandle(received.getType());
            }
            
        }
        catch (Exception e) {
            return "Exception";
        }
    }


public void init() throws KeyStoreException, NoSuchAlgorithmException, CertificateException{

    try{
            System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_LOCATION);

            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(new FileInputStream(file_key_store), "client123".toCharArray());

            String alias = "ServerC";

            Certificate cert = keystore.getCertificate(alias);
            serverPub = cert.getPublicKey();
            clientKeys = KeyPairGenerator.getInstance("DSA").generateKeyPair();
            pk = clientKeys.getPrivate();
            pub = clientKeys.getPublic();
            publicKey = Crypto.getStringFromKey(pub);
        
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslSocketFactory.createSocket("localhost", PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            ois = new ObjectInputStream(is);
            oos = new ObjectOutputStream(os);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
    
