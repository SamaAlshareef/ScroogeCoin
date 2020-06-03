import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Block {

		int id;
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		String hasfOfPrevBlock;
		String hashOfBlock;
		String signature;
		
		public Block(ArrayList<Transaction> transactions, String hasfOfPrevBlock) throws NoSuchAlgorithmException
		{
			this.id = Initialiser.blockId ++;
			this.transactions = transactions;
			this.hasfOfPrevBlock = hasfOfPrevBlock;
			this.hashOfBlock = calculateHash();
		}
		
		public Block(String hashOfPreviousBlock)
		{
			this.id = Initialiser.blockId + 1;
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
		
	    String calculateHash() throws NoSuchAlgorithmException
	    {
	    	String dataToHash = ""+ this.id + this.transactions + this.hasfOfPrevBlock;   	
	    	return toHexString(getSHA(dataToHash));
	    }

}
