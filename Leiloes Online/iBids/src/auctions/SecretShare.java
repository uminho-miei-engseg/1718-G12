package auctions;

import java.math.BigInteger;

public class SecretShare
{
    public SecretShare(final int number, final BigInteger share)
    {
        this.number = number;
        this.share = share;
    }
    
    public SecretShare (SecretShare ss) {
        this.number = ss.getNumber();
        this.share = ss.getShare();
    }

    public SecretShare () {
        this.number = -1;
        this.share = BigInteger.ZERO;
    }
    
    public int getNumber()
    {
        return number;
    }

    public BigInteger getShare()
    {
        return share;
    }
    
    @Override
    public SecretShare clone () {
        return new SecretShare(this);
    }

    @Override
    public String toString()
    {
        return "SecretShare [num=" + number + ", share=" + share + "]";
    }

    private final int number;
    private final BigInteger share;
}