import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Transaction {
	
	int id;
	String sender;
	String receiver;
	String signature;
	String hashOfPrevTrans;
	Coin coin;
	boolean verified;
	
	//Pay Coins Transaction
	public Transaction( String sender, String receiver,Coin coin) throws NoSuchAlgorithmException
	{
		this.id = Initialiser.transId ++;
		this.sender = sender;
		this.receiver = receiver;
		this.coin = coin;
		this.hashOfPrevTrans = calculateHash(coin.transactionsList.get(coin.transactionsList.size()-1));
		this.verified = false;
	}
	
	//Create Coins Transactions
	public Transaction(String creator, Coin coin) throws NoSuchAlgorithmException
	{
		this.id = Initialiser.transId ++;
		this.sender = creator;
		this.coin = coin;
	}
	
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
    {  
        // Static getInstance method is called with hashing SHA  
        MessageDigest md = MessageDigest.getInstance("SHA-256");  
  
        // digest() method called  
        // to calculate message digest of an input  
        // and return array of byte 
        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
    } 
    
    public static String toHexString(byte[] hash) 
    { 
        // Convert byte array into signum representation  
        BigInteger number = new BigInteger(1, hash);  
  
        // Convert message digest into hex value  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
  
        // Pad with leading zeros 
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString();  
    } 
	
    String calculateHash(Transaction trans) throws NoSuchAlgorithmException
    {
    	String dataToHash = ""+ trans.id + trans.sender + trans.receiver + trans.coin;   	
    	return toHexString(getSHA(dataToHash));
    }


}
