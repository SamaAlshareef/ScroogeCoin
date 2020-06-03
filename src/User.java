import java.security.*;
import java.util.ArrayList;

public class User {
	PrivateKey privateKey;
	PublicKey publicKey;
	ArrayList<Coin> coins;
	String id = "User ";
	
	public User() throws Exception
	{
		coins = new ArrayList<Coin>();
		KeyPair pair = Initialiser.generateKeyPair();
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	}
	
	void sendCoin(int amount, String receiverId) throws Exception
	{
		if(amount < this.coins.size())
		{
			for(int i = 0; i < amount; i++)
			{
				Coin coinToSend = this.coins.get(this.coins.size()-1-i);
				Transaction trans = new Transaction(this.id, receiverId, coinToSend);
				String signTrans = Initialiser.sign(trans.toString(), this.privateKey);
				trans.signature = signTrans;
				Scrooge.verifyBlock(trans, i);
			}
		}
		else
			System.out.println("Transaction Failed insufficient amount of coins");
	}
}
