import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.LinkedList;

public class Scrooge {
	
	public static String id;
	public static PrivateKey privateKey;
	public static PublicKey publicKey;
	
	
	public Scrooge() throws Exception
	{
		id = "Scrooge";
		KeyPair pair =Initialiser.generateKeyPair();
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	}
	
	public static boolean verifyTransaction(String plainText, String signature, PublicKey publicKey) throws Exception {
	    Signature publicSignature = Signature.getInstance("SHA256withRSA");
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signatureBytes = Base64.getDecoder().decode(signature);

	    return publicSignature.verify(signatureBytes);
	}

	public static void verifyBlock(Transaction trans, int pos) throws Exception
	{
			String [] userInfo = trans.sender.split(" ");
			int id = Integer.parseInt(userInfo[1]);
			User currUser =  Initialiser.users.get(id);
			System.out.println("verifying transaction: "+verifyTransaction(trans.toString(), trans.signature, currUser.publicKey));
			System.out.println("ownership: "+(currUser.coins.get(currUser.coins.size()-1-pos).id == trans.coin.id));
			System.out.println("Sender coin: "+currUser.coins.get(currUser.coins.size()-1).id);
			System.out.println("coin id: "+trans.coin.id);
			System.out.println("Double Spending: "+doubleSpending(trans));
			if(verifyTransaction(trans.toString(), trans.signature, currUser.publicKey)
					&& (currUser.coins.get(currUser.coins.size()-1-pos).id == trans.coin.id)
					&& doubleSpending(trans))
			{
				Initialiser.buffer.add(trans);
				Initialiser.checkUsersTransactions();
			}
	}
	
	public static boolean doubleSpending(Transaction trans)
	{
		boolean coinExists = false;
		for(int i = 0; i < Initialiser.buffer.size(); i++)
		{
			if(Initialiser.buffer.get(i).coin.id == trans.coin.id)
			{
				coinExists = true;
				break;
			}
		}
		if(coinExists)
			return false;
		return true;	
	}
}
