/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.math.BigInteger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SealedObject;
import java.util.Base64;
import java.security.Signature;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Esquivel
 */

//class that implements some methods regarding cryptographic operations
public class Crypto {
    
    public static void main(String[] args) throws Exception {
        KeyPair kp = buildKeyPair("DSA",1024);
        PrivateKey pk = kp.getPrivate();
        PublicKey pub = kp.getPublic();
        
        byte [] keyBytes = pub.getEncoded();
        PublicKey publicKey = KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        System.out.println(hash(getStringFromKey(pub),"SHA-256").equals(hash(getStringFromKey(publicKey),"SHA-256")));
        
        
    }
    
    public static String toBase64String (byte [] bytes) {
        if (bytes.length < 1) return "";
        return new String(Base64.getEncoder().encode(bytes));
    }
    
    public static byte [] reverseBase64 (String s) {
        if (s.length() < 1) return new byte[0];
        return Base64.getDecoder().decode(s.getBytes());
    }
    
    public static Key getKeyFromBytes (byte [] bytes, String algorithm, String type) {
        try {
            if (type.equals("private")) return KeyFactory.getInstance(algorithm).generatePrivate(new PKCS8EncodedKeySpec(bytes));
            if (type.equals("public")) return KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(bytes));
            else return new SecretKeySpec(bytes, 0, bytes.length, "AES"); 
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public static Key getKeyFromString (String key, String algorithm, String type) {
        return getKeyFromBytes(reverseBase64(key), algorithm, type);
    }
    
    public static String getStringFromKey (Key key) {
        return toBase64String (key.getEncoded());
    }
    
    public static ShamirInfo getShamirShares (Key key, int quorum, int number) {
        SecureRandom random = new SecureRandom();
        BigInteger secret = new BigInteger (key.getEncoded());
        BigInteger prime = new BigInteger(secret.bitLength() + 1, 256, random);
        boolean invert = false;
        if (secret.compareTo(BigInteger.ZERO) < 0) {
            secret = secret.negate();
            invert = true;
        }
        return new ShamirInfo(Shamir.split(secret, quorum, number, prime, random), prime, invert,quorum,number);
    }
    
    public static SecretKey reverseShamir (ShamirInfo si) {
        BigInteger secret = Shamir.combine(si.getShares(si.getQuorum()), si.getPrime());
        if (si.invert) secret = secret.negate();
        return (SecretKey)Crypto.getKeyFromBytes(secret.toByteArray(), "AES", "");
    }
    
    public static KeyPair buildKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        //final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }
    
    public static SecretKey keyGen(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(new SecureRandom());
        return keyGen.generateKey();
    }

    public static byte[] encrypt(Key key, String message, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);  
        cipher.init(Cipher.ENCRYPT_MODE, key);  

        return cipher.doFinal(message.getBytes("UTF-8"));  
    }
    
    public static String encryptText (Key key, String message, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);  

        return new String(Base64.getEncoder().encode(cipher.doFinal(message.getBytes("UTF-8"))));
    }
    
    public static byte[] decrypt(Key key, byte [] encrypted, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);  
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        return cipher.doFinal(encrypted);
    }
    
    public static String decryptText(Key key, String ciphertext, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);  
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        return new String (cipher.doFinal(Base64.getDecoder().decode(ciphertext.getBytes())));
    }
    
    
    public static String sign (PrivateKey pk, String message, String algorithm) throws Exception {
        Signature sign = Signature.getInstance(algorithm);
        sign.initSign(pk);
        sign.update(message.getBytes());
        return new String(Base64.getEncoder().encode(sign.sign()));
    }
    
    public static boolean verify (PublicKey pub, String message, String signature, String algorithm) throws Exception {
        Signature sign = Signature.getInstance(algorithm);
        sign.initVerify(pub);
        sign.update(message.getBytes());
        return (sign.verify(Base64.getDecoder().decode(signature.getBytes())));
    }
    
    public static SealedObject encryptBid (Bid bid, Key key, String algorithm) {
        try {
            if (algorithm.equals("RSA")) {
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return new SealedObject(bid, cipher);
            }
            if (algorithm.equals("AES")) {
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return new SealedObject(bid, cipher);
            }
            else return null;
            
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public static Bid decryptBid (SealedObject so, Key key, String algorithm) {
        try {
            if (!so.getAlgorithm().equals(algorithm)) {
                System.out.println("Algorithm provided does not match object encryption.");
                return null;
            }
            return (Bid)so.getObject(key);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public static String hash (String text, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return toBase64String (digest.digest(text.getBytes("UTF-8")));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }
    
    
    
}
