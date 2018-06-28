/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

//AUCTIONS

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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
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

    static final int PORT = 6063;
    private static final String TRUSTSTORE_LOCATION = System.getProperty("user.dir") + "//Certificates//ClientKeyStore.jks";

    private static final String SERVER_FULL_MESSAGE = "Server is full, please try again later.";
    private static final String INVALID_SIGNATURE_MESSAGE = "Server could not validate signature.";
    private static final String ALREADY_REGISTERED_MESSAGE = "User is already registered in server.";
    private static final String USER_NOT_REGISTERED_MESSAGE = "User is not registered in server";
    private static final String AUCTION_DOESNT_EXIST_MESSAGE = "The requested auction does not exist.";
    private static final String WRONG_USER_ACCESS_MESSAGE = "The chosen auction does not belong to you.";
    private static final String NO_AUCTIONS_AVAILABLE_MESSAGE = "There are currently no auctions available.";
    private static final String FAILURE_CREATING_AUCTION_MESSAGE = "Could not create auction.";



    private static final int TYPE_NOT_VALID = -1;
    private static final int SERVER_FULL_CODE = -2;
    private static final int NO_AUCTIONS_AVAILABLE = -3;
    private static final int SIGNATURE_NOT_VALID = -4;
    private static final int USER_ALREADY_REGISTERED = -5;
    private static final int USER_NOT_REGISTERED = -6;
    private static final int AUCTION_DOESNT_EXIST = -7;
    private static final int WRONG_USER_ACCESS = -8;
    private static final int FAILURE_CREATING_AUCTION = -9;

    private static File file_key_store = new File(TRUSTSTORE_LOCATION);

    private static KeyStore keystore;

    private static boolean registered = false;

    private static SSLSocketFactory sslSocketFactory;
    private static SSLSocket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static Scanner scanner;
    private static InputStream is;
    private static OutputStream os;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static int randomNumber;

    //private static Certificate cert = keystore.getCertificate(alias);
    private static PublicKey serverPub;
    private static KeyPair clientKeys;
    private static PrivateKey pk;
    private static PublicKey pub;
    private static String publicKey;

    private static HashMap<String, AuctionClient> auctions = new HashMap<>();
    private static HashMap<String, AuctionClient> myAuctions = new HashMap<>();

    public static boolean tryParse(String value, String type) {
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

    public static void errorHandle(int code) {
        switch (code) {
            case SIGNATURE_NOT_VALID:
                //activates if the user's signature is not valid
                System.out.println(INVALID_SIGNATURE_MESSAGE);
                break;
            case SERVER_FULL_CODE:
                //activates if the server is full
                System.out.println(SERVER_FULL_MESSAGE);
                break;
            case USER_ALREADY_REGISTERED:
                //activates if the server already registered the user
                System.out.println(ALREADY_REGISTERED_MESSAGE);
                break;
            case NO_AUCTIONS_AVAILABLE:
                System.out.println(NO_AUCTIONS_AVAILABLE_MESSAGE);
                break;
            case USER_NOT_REGISTERED:
                System.out.println(USER_NOT_REGISTERED_MESSAGE);
                break;
            case WRONG_USER_ACCESS:
                System.out.println(WRONG_USER_ACCESS_MESSAGE);
                break;
            case AUCTION_DOESNT_EXIST:
                System.out.println(AUCTION_DOESNT_EXIST_MESSAGE);
                break;
            case FAILURE_CREATING_AUCTION:
                System.out.println(FAILURE_CREATING_AUCTION_MESSAGE);
                break;
            default:
                System.out.println("Unknown error ocurred.");
                break;
        }

    }

    public static void printAuctions() {
        auctions.forEach((k, v) -> System.out.println(v.toString()));
    }

    public static void printMyAuctions() {
        myAuctions.forEach((k, v) -> System.out.println(v.toString()));
    }

    public static void printKeys() {
        System.out.printf("Server public key (hash): %s\n", Crypto.hash(Crypto.getStringFromKey(serverPub),"SHA-256"));
        System.out.printf("Your public key (hash): %s", Crypto.hash(Crypto.getStringFromKey(pub),"SHA-256"));        
    }

    public static boolean register(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            byte[] keyBytes = pub.getEncoded();
            String pubString = Crypto.toBase64String(keyBytes);
            int r = SecureRandom.getInstance("SHA1PRNG").nextInt(65535);
            String toSign = Crypto.hash(pubString, "SHA-256") + Integer.toString(r);
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            Data send = new Data(1, pubString, signature, r);
            //String send = "1:::" + pubString + ":::" + signature + ":::" + Integer.toString(r);
            oos.writeObject(send);
            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 1:
                    //validate server signature (proof of registration)
                    if (!Crypto.verify(serverPub, toSign, received.getSignature(), "SHA256withDSA")) {
                        System.out.println("Server signature is not valid.");
                        return false;
                    }
                    randomNumber = r;
                    return true;
                default:
                    errorHandle(received.getType());
                    return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static int getAuctionList(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            String toSign = Crypto.hash(pubString, "SHA-256") + Integer.toString(randomNumber);
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            Data send = new Data(2, pubString, signature, 0);
            oos.writeObject(send);
            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 2:
                    //validate server signature (proof of registration)
                    if (!Crypto.verify(serverPub, toSign, received.getSignature(), "SHA256withDSA")) {
                        System.out.println("Server signature is not valid.");
                        return -1;
                    }
                    StringTokenizer st;
                    String[] r = received.getAuctions(0);
                    String key,hash,seller;
                    for (int i = 0; i < r.length; i++) {
                        st = new StringTokenizer(r[i], ":::");
                        key = st.nextToken();
                        hash = Crypto.hash(key, "SHA-256");
                        seller = st.nextToken();
                        if (!seller.equals(pubString)) {
                            if (auctions.containsKey(hash)) auctions.get(hash).updateAuction(key, seller, st.nextToken(),st.nextToken(), st.nextToken(), (st.hasMoreElements()) ? st.nextToken() : null);
                            else auctions.put(hash, new AuctionClient(key, seller, st.nextToken(),st.nextToken(), st.nextToken()));
                        }
                        else {
                            if (myAuctions.containsKey(hash)) myAuctions.get(hash).updateAuction(key, seller, st.nextToken(),st.nextToken(), st.nextToken(), (st.hasMoreElements()) ? st.nextToken() : null);
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

    public static boolean createAuction(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            String seller, description, time, basePrice;
            seller = Crypto.getStringFromKey(pub);
            System.out.println("Write a description for the item:");
            description = scanner.nextLine();
            if (description.equals("q")) {
                return false;
            }
            while (true) {
                System.out.print("Choose the duration of the auction: ");
                time = scanner.nextLine();
                if (time.equals("q")) {
                    return false;
                }
                if (tryParse(time, "LONG") && Long.parseLong(time) >= 0) {
                    break;
                }
            }
            while (true) {
                System.out.print("Choose the base price: ");
                basePrice = scanner.nextLine();
                if (basePrice.equals("q")) {
                    return false;
                }
                if (tryParse(basePrice, "DOUBLE") && Double.parseDouble(basePrice) >= 0) {
                    break;
                }
            }
            String toSign = Crypto.hash(seller, "SHA-256") + Integer.toString(randomNumber);
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            Data send = new Data(4, seller, signature, 0);
            send.setAuctions(1);
            send.setAuction(description + ":::" + basePrice + ":::" + time, 0);
            oos.writeObject(send);

            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 4:
                    //validate server signature (proof of registration)
                    if (!Crypto.verify(serverPub, toSign, received.getSignature(), "SHA256withDSA")) {
                        System.out.println("Server signature is not valid.");
                        return false;
                    }
                    StringTokenizer tok = new StringTokenizer(received.getAuctions(1)[0], ":::");
                    String auctionPub = tok.nextToken();
                    System.out.println(received.getAuctions(1)[0]);
                    myAuctions.put(Crypto.hash(auctionPub, "SHA-256"), new AuctionClient(auctionPub, seller, description, basePrice, time, tok.nextToken()));
                    return true;
                default:
                    errorHandle(received.getType());
                    return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean bid(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            String[] pubKeys = new String[auctions.size() < 65535 ? auctions.size() : 65534];
            String auctionToBid, price;
            int i = 0;
            for (Map.Entry<String, AuctionClient> pair : auctions.entrySet()) {
                pubKeys[i] = pair.getKey();
                i++;
            }
            while (true) {
                System.out.println("What auction do you want to bid in?");
                for (i = 0; i < pubKeys.length; i++) {
                    System.out.printf("\n[%d] - %s\n", i + 1, pubKeys[i]);
                }
                auctionToBid = scanner.nextLine();
                if (auctionToBid.equals("q")) {
                    return false;
                }

                if (tryParse(auctionToBid, "INT") && Integer.parseInt(auctionToBid) > 0 && Integer.parseInt(auctionToBid) <= pubKeys.length) {
                    i = Integer.parseInt(auctionToBid);
                    System.out.printf("Auction %d is:\n\n%s\n\nAre you sure? [y|n]", i, auctions.get(pubKeys[i - 1]).toString());
                    auctionToBid = scanner.nextLine();
                    if (auctionToBid.equals("y")) {
                        break;
                    }
                }
            }
            //user already decided the auction to bin in
            while (true) {
                System.out.print("Insert a price: ");
                price = scanner.nextLine();
                if (price.equals("q")) {
                    break;
                }
                if (tryParse(price, "DOUBLE") && Double.parseDouble(price) >= auctions.get(pubKeys[i - 1]).getBasePrice()) {
                    System.out.printf("You want to bid %f. Is that right? [y|n]", Double.parseDouble(price));
                    auctionToBid = scanner.nextLine();
                    if (auctionToBid.equals("y")) {
                        break;
                    }
                }
            }
            //user decided on a price
            Bid bid = new Bid(Double.parseDouble(price), randomNumber, pubKeys[i - 1]);
            Data send = new Data(3, pubString, null, 0);
            send.setBid(Crypto.encryptBid(bid, (PublicKey) Crypto.getKeyFromString(auctions.get(pubKeys[i - 1]).getPublicKey(), "RSA", "public"), "RSA"));
            String toSign = Crypto.hash(pubString + Integer.toString(randomNumber) + Crypto.toBase64String(Crypto.toByteArray(send.getBid())), "SHA-256");
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            send.setSignature(signature);
            send.setAuctions(1);
            send.setAuction(pubKeys[i - 1], 0);
            oos.writeObject(send);

            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 3:
                    //validate server signature (proof of registration)
                    if (!Crypto.verify(serverPub, toSign, received.getSignature(), "SHA256withDSA")) {
                        System.out.println("Server signature is not valid.");
                        return false;
                    }
                    auctions.get(pubKeys[i - 1]).setLastBid(Double.parseDouble(price));
                    auctions.get(pubKeys[i - 1]).setReceipt(received.getSignature());
                    return true;
                default:
                    errorHandle(received.getType());
                    return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean checkAuction(PublicKey pub, PrivateKey pk, PublicKey serverPub) {
        try {
            String pubString = Crypto.getStringFromKey(pub);
            String toSign = Crypto.hash(pubString, "SHA-256") + Integer.toString(randomNumber);
            String signature = Crypto.sign(pk, toSign, "SHA256withDSA");
            String[] pubKeys = new String[auctions.size() < 65535 ? auctions.size() : 65534];
            String auctionToCheck;
            int i = 0;
            for (Map.Entry<String, AuctionClient> pair : myAuctions.entrySet()) {
                pubKeys[i] = pair.getKey();
                i++;
            }
            while (true) {
                System.out.println("What auction do you want to check?");
                for (i = 0; i < pubKeys.length; i++) {
                    System.out.printf("\n[%d] - %s\n", i + 1, pubKeys[i]);
                }
                auctionToCheck = scanner.nextLine();
                if (auctionToCheck.equals("q")) {
                    return false;
                }
                if (tryParse(auctionToCheck, "INT") && Integer.parseInt(auctionToCheck) > 0 && Integer.parseInt(auctionToCheck) <= pubKeys.length) {
                    i = Integer.parseInt(auctionToCheck);
                    System.out.printf("Auction %d is:\n\n%s\n\nAre you sure? [y|n]", i, myAuctions.get(pubKeys[i - 1]).toString());
                    auctionToCheck = scanner.nextLine();
                    if (auctionToCheck.equals("y")) {
                        break;
                    }
                }
            }

            auctionToCheck = pubKeys[i - 1];
            Data send = new Data(5, pubString, signature, 0);
            send.setAuctions(1);
            send.setAuction(auctionToCheck, 0); 
            oos.writeObject(send);

            Data received = (Data) ois.readObject();
            switch (received.getType()) {
                case 5:
                    //validate server signature (proof of registration)
                    if (!Crypto.verify(serverPub, toSign, received.getSignature(), "SHA256withDSA")) {
                        System.out.println("Server signature is not valid.");
                        return false;
                    }
                    String key, hash, seller;
                    StringTokenizer st = new StringTokenizer(received.getAuctions(1)[0], ":::");
                    key = st.nextToken();
                    hash = Crypto.hash(key, "SHA-256");
                    seller = st.nextToken();
                    myAuctions.get(auctionToCheck).updateAuction(key, seller, st.nextToken(),st.nextToken(), st.nextToken(), (st.hasMoreElements()) ? st.nextToken() : null);
                    System.out.println(myAuctions.get(auctionToCheck));
                    return true;
                default:
                    errorHandle(received.getType());
                    return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public static void main(String[] args) throws Exception {

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

        try {
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslSocketFactory.createSocket("localhost", PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            ois = new ObjectInputStream(is);
            oos = new ObjectOutputStream(os);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            //All IO is initialized
            String received, inputLine;

            while (true) {
                System.out.println("Insert operation [1 - Register || 2 - Update Auction List || 3 - Bid || 4 - Create auction || 5 - Check Your Auctions || 6 - Print Other Auctions ||"
                        + " 7 - Print Your Auctions || 8 - Print Keys || q - Quit]:");
                inputLine = scanner.nextLine();
                if (inputLine.equals("q")) {
                    break;
                }
                switch (inputLine) {
                    case "1":
                        boolean before = registered;
                        registered = register(pub, pk, serverPub);
                        if (before && !registered) {
                            registered = true;
                        }
                        if (registered && !before) {
                            System.out.println("Registration was successful.");
                            int size = getAuctionList(pub, pk, serverPub);
                            if (size >= 0) {
                                System.out.printf("Received %d auctions from server.\n", size);
                                //printAuctions();
                            }
                        } else {
                            System.out.println("Could not register.");
                        }
                        break;
                    case "2":
                        if (!registered) {
                            System.out.println("You need to register in the server first.");
                            break;
                        }
                        int size = getAuctionList(pub, pk, serverPub);
                        if (size >= 0) {
                            System.out.printf("Received %d auctions from server.\n", size);
                            //printAuctions();
                        }
                        break;
                    case "3":
                        if (!registered) {
                            System.out.println("You need to register in the server first.");
                            break;
                        }
                        boolean success = bid(pub, pk, serverPub);
                        System.out.println(success);
                        break;
                    case "4":
                        if (!registered) {
                            System.out.println("You need to register in the server first.");
                            break;
                        }
                        boolean created = createAuction(pub, pk, serverPub);
                        if (!created) {
                            System.out.println("Auction was not created.");
                        } else {
                            System.out.println("Auction was created.");
                        }
                        break;
                    case "5":
                        if (!registered) {
                            System.out.println("You need to register in the server first.");
                            break;
                        }
                        if (myAuctions.size() < 1) {
                            System.out.println("You have no active auctions.");
                            break;
                        }
                        checkAuction(pub, pk, serverPub);
                        break;
                    case "6":
                        printAuctions();
                        break;
                    case "7":
                        printMyAuctions();
                        break;
                    case "8":
                        printKeys();
                        break;
                    case "10":
                        break;
                    default:
                        System.out.println("Command is not valid.");
                        break;
                }
            }

        } catch (IOException ex) {;
            Logger.getLogger(Client.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

    }
}
