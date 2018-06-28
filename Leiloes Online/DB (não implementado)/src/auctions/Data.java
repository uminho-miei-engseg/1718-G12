/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.SealedObject;
import java.io.Serializable;
import java.util.logging.Logger;


/**
 *
 * @author luise
 */
public class Data implements Serializable{
    
    private int type;
    private String pubString;
    private String signature;
    private int secRandom;
    private String [] auctions;
    private SealedObject bid;
    
    public Data () {
        type = 0;
        secRandom = 0;
    }

    public Data (int type, String pubString, String signature, int secRandom) {
        this.type = type;
        this.pubString = pubString;
        this.signature = signature;
        this.secRandom = secRandom;
        this.auctions = new String[0];
        this.bid = null;
    }
    
    public Data (int type, String pubString, String signature, int secRandom, int auctions) {
        this.type = type;
        this.pubString = pubString;
        this.signature = signature;
        this.secRandom = secRandom;
        if (auctions > 0) this.auctions = new String[auctions];
        this.bid = null;
    }
    
    public Data (Data origin) {
        this.type = origin.type;
        this.pubString = origin.pubString;
        this.signature = origin.signature;
        this.secRandom = origin.secRandom;
        this.auctions = origin.getAuctions(0);
        this.bid = origin.getBid();
    }

    public SealedObject getBid() {
        return bid;
    }

    public void setBid(SealedObject bid) {
        this.bid = bid;
    }
       

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPubString() {
        return pubString;
    }

    public void setPubString(String pubString) {
        this.pubString = pubString;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSecRandom() {
        return secRandom;
    }

    public void setSecRandom(int secRandom) {
        this.secRandom = secRandom;
    }
    
    public void setAuctions (int number) {
        this.auctions = new String[number];
    }
    
    public void setAuction (String auction, int index) {
        if (index < 0 || index >= this.auctions.length) {
            for (int i = 0; i < this.auctions.length; i++) {
                if (this.auctions[i] == null) {
                    this.auctions[i] = auction;
                    break;
                }
            }
        }
        else {
            this.auctions[index] = auction;
        }        
    }
    
    public String [] getAuctions (int number) {
        String [] r = new String [(number <= this.auctions.length && number > 0) ? number : this.auctions.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = this.auctions[i];
        }
        return r;        
    }
    
    public int getAuctionsNumber () {
        return this.auctions.length;
    }
    
    public Data clone () {
        return new Data(this);
    }
    
    public void recycle (int type, String pubString, String signature, int secRandom, int auctions) {
        
        this.type = type;
        this.pubString = pubString;
        this.signature = signature;
        this.secRandom = secRandom;
        if (auctions > 0) this.auctions = new String[auctions];
        
    }
    
    /*public Data clear () {
        return new Data
    }*/
    
    
    
    
    
    
    
}
