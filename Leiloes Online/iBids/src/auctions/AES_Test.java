/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.security.*;
import javax.crypto.*;
import java.nio.ByteBuffer;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ChronoField;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.*;

/**
 *
 * @author Esquivel
 */
public class AES_Test {

    /**
     * @param args the command line arguments
     */
    
    
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
    
    public static String getStringTime () {
        long millis = System.currentTimeMillis();
        double days = (double) millis / 86400000;
        double hours = ((days % 1) * 86400000) / 3600000;
        double minutes = ((hours % 1) * 3600000) / 60000;
        double seconds = ((minutes % 1) * 60000) / 1000;
        return String.format("%dd,%dh,%dm,%ds", (int)days, (int)hours, (int)minutes, (int)seconds);
    }
    
    private static class ToDo {

        Timer timer;

        public ToDo(int seconds) {
            timer = new Timer();
            timer.schedule(new ToDoTask(), seconds * 1000);
        }

        class ToDoTask extends TimerTask {

            public void run() {
                System.out.println("OK, It's time to do something!");
                timer.cancel(); //Terminate the thread
            }
        }
    }
    
    public static void shiftArray(Object [] array) {
        for (int i = array.length - 2; i >=0; i--) {
            array[i+1] = array[i];
        }
    }
    
    
    public static void main(String[] args) throws Exception {
        
//        final int CERTAINTY = 256;
//        final SecureRandom random = new SecureRandom();
//
//        //KeyPair kp = Crypto.buildKeyPair("RSA", 512);
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(random);
//        SecretKey sk = keyGen.generateKey();
//        
//        String message = "this be patrick's message";
//        
//        String ciphertext = Crypto.encryptText(sk, message, "AES");
//        
//        
//        
//        int i = 0;
//        for (i=0; i < 100; i++) {
//            ShamirInfo si = Crypto.getShamirShares(sk, 3, 4);
//
//            SecretKey afterShamir = Crypto.reverseShamir(si);
//
//            if(!afterShamir.equals(sk)) break;
//        }
//        System.out.println(i);

        
        
        
        
        
    }
    
}
