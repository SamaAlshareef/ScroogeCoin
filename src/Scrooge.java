import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedList;

public class Scrooge {
	
	public static String id;
	public static LinkedList<Block> blockChain;
	public static PrivateKey privateKey;
	public static PublicKey publicKey;
	
	
	public Scrooge() throws Exception
	{
		id = "Scrooge";
		blockChain = new LinkedList<Block>();
		KeyPair pair =Initialiser.generateKeyPair();
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	}
	void verifyTransaction()
	{
		
	}

	void accumulateBlock()
	{
		
	}
}
