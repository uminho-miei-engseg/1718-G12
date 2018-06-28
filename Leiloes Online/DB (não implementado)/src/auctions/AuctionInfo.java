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
public class AuctionInfo {

    String pubKeyHash;
    long end;
    SecretShare share;
    boolean canGiveShare;
    Timer timer;

    public AuctionInfo(String pubKeyHash, long begin, long end, SecretShare share) {
        this.pubKeyHash = pubKeyHash;
        this.end = end;
        this.share = (share != null) ? new SecretShare(share.getNumber(), share.getShare()) : null;
        this.canGiveShare = false;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                canGiveShare = true;
                timer.cancel();
            }
        }, end-begin);
    }

    public boolean canGiveShare() {
        return this.canGiveShare;
    }

    public SecretShare getShare() {
        if (canGiveShare) {
            return new SecretShare(share.getNumber(), share.getShare());
        } else {
            System.out.println("Cannot give share.");
            return null;
        }
    }

    public String getpubKeyHash() {
        return pubKeyHash;
    }

    public void setpubKeyHash(String pubKeyHash) {
        this.pubKeyHash = pubKeyHash;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
