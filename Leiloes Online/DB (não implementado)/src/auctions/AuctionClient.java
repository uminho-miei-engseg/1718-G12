/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author luise
 */
public class AuctionClient {
    
    private String publicKey;
    private String seller;
    private String description;
    private double basePrice;
    private long timeleft;
    private long bids;
    private String shamir_part;
    private double lastBid;
    private String receipt;
    private boolean canGiveShare;
    private String winner;
    private Timer timer;

    public AuctionClient(String publicKey, String seller, String description, String basePrice, String timeleft) {
        this.publicKey = publicKey;
        this.seller = seller;
        this.description = description;
        this.basePrice = Double.parseDouble(basePrice);
        this.timeleft = Long.parseLong(timeleft);
        this.bids = 0;
        this.shamir_part = null;
        this.lastBid = 0;
        this.receipt = null;
        this.canGiveShare = false;
        this.timer = null;
        this.winner = null;
    }
    
    public AuctionClient(String publicKey, String seller, String description, String basePrice, String timeleft, String share) {
        this.publicKey = publicKey;
        this.seller = seller;
        this.description = description;
        this.basePrice = Double.parseDouble(basePrice);
        this.timeleft = Long.parseLong(timeleft);
        this.bids = 0;
        this.shamir_part = share;
        this.lastBid = 0;
        this.receipt = null;
        this.canGiveShare = false;
        this.winner = null;
        Timer timer = new Timer ();
        timer.schedule(new TimerTask () {
            @Override
            public void run () {
                canGiveShare = true;
                timer.cancel();
            }
        }, Long.parseLong(timeleft));
    }
    
    public void updateAuction(String publicKey, String seller, String description, String basePrice, String timeleft, String winner) {
        this.publicKey = publicKey;
        this.seller = seller;
        this.description = description;
        this.basePrice = Double.parseDouble(basePrice);
        this.timeleft = Long.parseLong(timeleft);
        if (winner != null) this.winner = winner;
    }
    
    

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public long getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(long timeleft) {
        this.timeleft = timeleft;
    }

    public long getBids() {
        return bids;
    }

    public void setBid () {
        this.bids++;
    }

    public String getShamir_part() {
        return shamir_part;
    }

    public double getLastBid() {
        return lastBid;
    }

    public void setLastBid(double lastBid) {
        this.lastBid = lastBid;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
    
   
    
    
    
    public static String getStringTime (long millis) {
        double days = (double) millis / 86400000;
        double hours = ((days % 1) * 86400000) / 3600000;
        double minutes = ((hours % 1) * 3600000) / 60000;
        double seconds = ((minutes % 1) * 60000) / 1000;
        return String.format("%dd,%dh,%dm,%ds", (int)days, (int)hours, (int)minutes, (int)seconds);
    }
    
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Public Key (Hash): "); sb.append(Crypto.hash(this.publicKey,"SHA-256"));
        sb.append("\nSeller (Hash): "); sb.append (Crypto.hash(this.seller,"SHA-256"));
        sb.append("\nDescription: "); sb.append(this.description);
        sb.append("\nBase price: "); sb.append (this.basePrice);
        sb.append("\nTime left: "); sb.append (((this.timeleft > 0) ? getStringTime(this.timeleft) : "Finished"));
        sb.append("\nLast bid: "); sb.append(this.lastBid);
        if (this.shamir_part != null) {
            sb.append("\nShamir Share: ");sb.append(this.shamir_part);
            sb.append("\nCan give share: ");sb.append((canGiveShare) ? "Yes" : "No");
        }
        if (winner != null) {
            sb.append("\nWinner: ");sb.append(this.winner);
        }
        return sb.toString();
    }
    
    
}
