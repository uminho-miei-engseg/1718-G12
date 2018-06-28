/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.math.BigInteger;

/**
 *
 * @author luise
 */
public class ShamirInfo {
    
    SecretShare [] shares;
    BigInteger prime;
    boolean invert = false;
    int quorum, number;
    
    public ShamirInfo (SecretShare [] shares, BigInteger prime, boolean invert, int quorum, int number) {
        this.shares = new SecretShare[shares.length];
        for (int i=0; i < shares.length;i++) {
            this.shares[i] = new SecretShare(shares[i].getNumber(),shares[i].getShare());
        }
        this.prime = new BigInteger(prime.toByteArray());
        this.invert = invert;
        this.quorum = quorum;
        this.number = number;
    }
    
    public ShamirInfo (ShamirInfo si) {
        SecretShare [] ss = si.getShares(-1);
        this.shares = new SecretShare[ss.length];
        for (int i=0; i < shares.length; i++) {
            this.shares[i] = (ss[i] != null) ? new SecretShare(ss[i].getNumber(),ss[i].getShare()) : null;
        }
        this.prime = si.getPrime();
        this.invert = si.isInvert();
        this.quorum = si.getQuorum();
        this.number = si.getNumber();
    }
    
    public ShamirInfo () {
        
    }

    public SecretShare[] getShares(int number) {
//        int notNull = 0;
//        for (int i = 0; i < this.shares.length; i++) {
//            if (this.shares[i]!=null) notNull++;
//        }
        SecretShare [] r = new SecretShare[(number <= this.shares.length && number > 0) ? number : this.shares.length];
        for (int i=0; i < r.length; i++) {
            r[i] = (this.shares[i] != null) ? new SecretShare(this.shares[i].getNumber(),this.shares[i].getShare()) : null;
        }
        return r;
    }

    public void printShares () {
        for (int i = 0; i < this.shares.length; i++) {
            System.out.println(this.shares[i]);
        }
    }
    
    public void setShares(SecretShare[] shares) {
        this.shares = new SecretShare[shares.length];
        for (int i = 0; i < shares.length; i++) {
            this.shares[i] = new SecretShare(shares[i].getNumber(),shares[i].getShare());
        }
    }
    
    public void setShare (SecretShare share) {
        for (int i = 0; i < this.shares.length; i++) {
            if (this.shares[i]==null) {
                this.shares[i] = new SecretShare(share.getNumber(), share.getShare());
                break;
            }
            
        }
    }
    
    public int numberOfShares () {
        int notNull = 0;
        for (SecretShare share : this.shares) {
            if (share != null) notNull++;
        }
        return notNull;
    }
    
    public void deleteNulls () {
        SecretShare [] sss = new SecretShare[numberOfShares()];
        for (int i = 0, j = 0; i < this.shares.length; i++) {
            if (this.shares[i] != null) {
                sss[j] = this.shares[i].clone();
                j++;
            }
        }
        this.shares = sss;
    }
    
    public void eraseShares () {
        for (int i = 0; i < this.shares.length; i++) {
            this.shares[i] = null;
        }
    }

    public BigInteger getPrime() {
        return new BigInteger(this.prime.toByteArray());
    }

    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }
    

    public int getQuorum() {
        return quorum;
    }

    public void setQuorum(int quorum) {
        this.quorum = quorum;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    @Override
    
    public ShamirInfo clone () {
        return new ShamirInfo(this);
    }
    
    
    
    
}
