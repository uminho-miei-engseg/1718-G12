/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.security.*;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.util.Arrays;

/**
 *
 * @author Esquivel
 */
public class Auction {

    /**
     * @param args the command line arguments
     */
    
    private class BidUser {
        private String user;
        private Double bid;

        public BidUser(String user, Double bid) {
            this.user = user;
            this.bid = bid;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Double getBid() {
            return bid;
        }

        public void setBid(Double bid) {
            this.bid = bid;
        }
        
        
    }
    
    private double basePrice;
    private String description;
    private BidUser [] results;
    private PublicKey seller;
    private GregorianCalendar begin, end;
    private ShamirInfo shamir_info;
    private PublicKey publicKey;
    private String encPrivateKey;
    private boolean active;
    private int nextWinner;
    //o ciphertext pode ficar como string mas também como byte array
    private HashMap<String, SealedObject> offers;
    
    public Auction (PublicKey seller, double basePrice, String description, GregorianCalendar begin, GregorianCalendar end, PublicKey publicKey) {
        this.seller = seller;
        this.basePrice = basePrice;
        this.description = description;
        this.begin = begin;
        this.end = end;
        this.publicKey = publicKey;
        this.active = true;
        offers = new HashMap<>();
    }
    
    public Auction (PublicKey seller, double basePrice, String description, long begin, long end, PublicKey publicKey, String encPrivateKey, ShamirInfo shamir_info) {
        this.seller = seller;
        this.basePrice = basePrice;
        this.description = description;
        this.begin = new GregorianCalendar();
        this.end = new GregorianCalendar();
        this.begin.setTimeInMillis(begin);
        this.end.setTimeInMillis(end);
        this.publicKey = publicKey;
        this.active = true;
        this.encPrivateKey = encPrivateKey;
        this.shamir_info = (shamir_info!=null) ? shamir_info.clone() : null;
        System.out.println(shamir_info);
        offers = new HashMap<>();
    }
    
    public Auction () {
        offers = new HashMap<>();
        active = true;
    }
    
    public String getBegin() {
        return begin.toString();
    }
    
    public long getTimeLeft () {
        return end.getTimeInMillis() - System.currentTimeMillis();
    }
    
    public String getStringTime () {
        long millis = this.getTimeLeft();
        double days = (double) millis / 86400000;
        double hours = ((days % 1) * 86400000) / 3600000;
        double minutes = ((hours % 1) * 3600000) / 60000;
        double seconds = ((minutes % 1) * 60000) / 1000;
        return String.format("%dd,%dh,%dm,%ds", (int)days, (int)hours, (int)minutes, (int)seconds);
    }
    
    public String getAuctionData () {
        if (results == null) return String.format("%s:::%s:::%s:::%s:::%s", Crypto.getStringFromKey(publicKey),
                                Crypto.getStringFromKey(seller), description, Double.toString(basePrice), Long.toString(this.getTimeLeft()));
        else return String.format("%s:::%s:::%s:::%s:::%s:::%s", Crypto.getStringFromKey(publicKey),
                                Crypto.getStringFromKey(seller), description, Double.toString(basePrice), Long.toString(this.getTimeLeft()), results[nextWinner].getUser());
    }
    
    public String [] getAuctionData2 () {
        String [] r = new String [5];
        r[0] = Crypto.getStringFromKey(publicKey);
        r[1] = Crypto.getStringFromKey(seller);
        r[2] = description;
        r[3] = Double.toString(basePrice);
        r[4] = Long.toString(this.getTimeLeft());
        System.out.println(r);
        return r;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String pubString = Crypto.getStringFromKey(publicKey);
        sb.append(String.format("Public Key: %s\n\nSHA-256: %s\n\n", pubString, Crypto.hash(pubString, "SHA-256")));
        return sb.toString();
    }
    
    public String getWinner (SecretShare [] sss) throws Exception {
        if (results != null) {
            if (nextWinner >= results.length) {
                return null;
            }
            nextWinner++;
            return results[nextWinner].getUser();
        }
        try {
            for (int i = 0; i < sss.length; i++) {
                if (sss[i] != null) {
                    this.shamir_info.setShare(sss[i]);
                }
            }
            shamir_info.deleteNulls();
            SecretKey aesKey = Crypto.reverseShamir(shamir_info);
            PrivateKey rsaPK = (PrivateKey)Crypto.getKeyFromString(Crypto.decryptText(aesKey, encPrivateKey, "AES"),"RSA","private");
            if (offers.isEmpty()) throw new Exception("There are no bids in this auction.");
            double biggestBid = this.basePrice;
            results = new BidUser[(offers.size() < 100) ? offers.size() : 100];
            int i = 0;
            for (HashMap.Entry<String, SealedObject> encBid : offers.entrySet()) {
                Bid bid = Crypto.decryptBid(encBid.getValue(), rsaPK, "RSA");
                results[i] = new BidUser(encBid.getKey(), bid.getValue());
                i++;
            }
            Arrays.sort(results, (a,b) -> b.getBid().compareTo(a.getBid()));
            nextWinner=0;
            return results[nextWinner].getUser();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    public String whoWon () {
        return (results != null) ? results[nextWinner].getUser() : null;
    }
    
    public boolean isActive () {
        return this.active;
    }
    
    public void setActive (boolean value) {
        this.active = value;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PublicKey getSeller() {
        return this.seller;
    }

    public void setSeller(PublicKey seller) {
        this.seller = seller;
    }

    public ShamirInfo getShamir_Info() {
        return shamir_info.clone();
    }

    public void setShamir_Info(ShamirInfo shamir_info) {
        this.shamir_info = shamir_info.clone();
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
    
    public void setBid (String user, SealedObject offer) {
        offers.put(Crypto.hash(user,"SHA-256"), offer);
    }
    
    public void shiftArray(Object [] array, int position) {
        if (array.length - 2 <= position) return;
        for (int i = array.length - 2; i >= position; i--) {
            array[i+1] = array[i];
        }
    }
    
    
    
    
}
