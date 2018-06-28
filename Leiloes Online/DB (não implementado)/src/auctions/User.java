/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.util.ArrayList;

/**
 *
 * @author luise
 */
public class User {
    
    private String publicKey;
    private int randomNumber;
    private ArrayList<String> auctions;
    
    public User (String publicKey, int randomNumber, String auction) {
        this.publicKey = publicKey;
        this.randomNumber = randomNumber;
        this.auctions = new ArrayList<>();
        auctions.add(auction);
    }
    
    public User (String publicKey, int randomNumber) {
        this.publicKey = publicKey;
        this.randomNumber = randomNumber;
        this.auctions = new ArrayList<>();
    }
    
    
    //adicionar leilão já validado pelo servidor
    public boolean addAuction (String auction) {
        try {
            auctions.add(auction);
            return auctions.contains(auction);
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public String getPublicKey () {
        return this.publicKey;
    }
    
    public void setRandomNumber() {
        this.randomNumber = randomNumber;
    }
    
    public int getRandomNumber () {
        return this.randomNumber;
    }
    
    public String toString() {
        return (Integer.toString(randomNumber));
    }
    
    
    
}
