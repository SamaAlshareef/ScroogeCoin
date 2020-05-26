import java.security.*;
import java.util.ArrayList;

public class User {
	PrivateKey privateKey;
	PublicKey publicKey;
	ArrayList<Coin> coins;
	String id = "User";

	
	public User() throws Exception
	{
		coins = new ArrayList<Coin>();
		KeyPair pair = Initialiser.generateKeyPair();
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	}
	public static void main(String[] args) throws Exception
	{
		//First generate a public/private key pair
		//KeyPair pair = generateKeyPair();

		//Our secret message
//		String message = "the answer to life the universe and everything";
//
//		//Encrypt the message
//		String cipherText = encrypt(message, pair.getPublic());
//
//		//Now decrypt it
//		String decipheredMessage = decrypt(cipherText, pair.getPrivate());
//		
//		String signature = sign("foobar", pair.getPrivate());
//
//		System.out.println("Signature: "+ signature);
//		//Let's check the signature
//		boolean isCorrect = verify("fobar", signature, pair.getPublic());
//		System.out.println("Signature correct: " + isCorrect);
//		
//		System.out.println(decipheredMessage);
	}
}
