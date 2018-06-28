/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author luise
 */
public class TrustedEntity {
    
    private HashMap<String, AuctionInfo> auctions;
    
    
    public TrustedEntity () {
        auctions = new HashMap<>();
    }
    
    public boolean setAuction (String pubKeyString, long begin, long end, SecretShare share) {
        String hash = Crypto.hash(pubKeyString,"SHA-256");
        if (!auctions.containsKey(hash)) {
            auctions.put(hash, new AuctionInfo(pubKeyString, begin, end, share));
            return auctions.containsKey(hash);
        }
        return false;
    }
    
    public boolean removeAuction (String pubKeyHash) {
        String hash = Crypto.hash(pubKeyHash,"SHA-256");
        if (!auctions.get(hash).canGiveShare()) return false;
        if (auctions.containsKey(hash)) {
            auctions.remove(hash);
            return true;
        }
        else return false;
    }
    
    public void printAuctions () {
        auctions.forEach((k, v) -> System.out.println(k));
    }
    
    public SecretShare getShare (String pubKeyHash) {
        if (!auctions.containsKey(pubKeyHash)) return null;
        if (!auctions.get(pubKeyHash).canGiveShare()) return null;
        return auctions.get(pubKeyHash).getShare();
    }
    
}
