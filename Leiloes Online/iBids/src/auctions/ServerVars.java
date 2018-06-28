/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author luise
 */
public class ServerVars {
   
    private static ServerVars singleton = new ServerVars();
    private TrustedEntity[] tes = new TrustedEntity[2];

    public TrustedEntity[] getTes() {
        return tes;
    }

    public void setTes(TrustedEntity[] tes) {
        this.tes = tes;
    }

    
    

    private ServerVars() {
      
    }
    
    public static ServerVars getInstanceOf () {
        return singleton;
    }
    
    
    
    
    
    
}
