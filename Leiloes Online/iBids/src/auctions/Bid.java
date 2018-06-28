/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.io.Serializable;

/**
 *
 * @author luise
 */
public class Bid implements Serializable {
    
    private double value;
    private int randomNumber;
    private String auctionPub;

    public Bid(double value, int randomNumber, String auctionPub) {
        this.value = value;
        this.randomNumber = randomNumber;
        this.auctionPub = auctionPub;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getAuctionPub() {
        return auctionPub;
    }

    public void setAuctionPub(String auctionPub) {
        this.auctionPub = auctionPub;
    }    
    
    public boolean equals (Bid bid) {
        return this.value==bid.getValue() && this.randomNumber == bid.getRandomNumber() &&
                this.auctionPub.equals(bid.getAuctionPub());
    }
}
