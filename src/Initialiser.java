import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;

import javax.crypto.Cipher;

public class Initialiser {

	public  LinkedList<Block> blockChain = new LinkedList<Block>(); 
	public static ArrayList<User> users = new ArrayList<User>();
	public static int transId = 0;
	public static int blockId = 0;
	public static int coinId = 0;
	public static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	public Initialiser() throws Exception
	{
		userInitialiser();
		
	}
		
	public void userInitialiser() throws Exception
	{
		for (int i = 0; i < 100; i++)
		{
			User user = new User();
			user.id += i;
			scroogeSendsCoins(Scrooge.id, user);
		}
	}
	
	// Generate private/public keys, sign/verify signatures
	public static KeyPair generateKeyPair() throws Exception {
	    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
	    generator.initialize(2048, new SecureRandom());
	    KeyPair pair = generator.generateKeyPair();
	    
	    return pair;
	}
	
	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
	    Cipher encryptCipher = Cipher.getInstance("RSA");
	    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

	    byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

	    return Base64.getEncoder().encodeToString(cipherText);
	}
	
	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
	    byte[] bytes = Base64.getDecoder().decode(cipherText);

	    Cipher decriptCipher = Cipher.getInstance("RSA");
	    decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

	    return new String(decriptCipher.doFinal(bytes), StandardCharsets.UTF_8);
	}
	
	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("SHA256withRSA");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signature = privateSignature.sign();

	    return Base64.getEncoder().encodeToString(signature);
	}
	
	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
	    Signature publicSignature = Signature.getInstance("SHA256withRSA");
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signatureBytes = Base64.getDecoder().decode(signature);

	    return publicSignature.verify(signatureBytes);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ArrayList<Coin> createCoins() throws Exception
	{
		ArrayList<Coin> tempCoinsList = new ArrayList<Coin>();
		for(int i = 0; i < 10; i++)
		{
			Coin coin = new Coin();
			String sign = Initialiser.sign(coin.toString(), Scrooge.privateKey);
			coin.signature = sign;
			tempCoinsList.add(coin);
			Transaction trans = new Transaction(Scrooge.id, coin);
			String signTrans = Initialiser.sign(trans.toString(), Scrooge.privateKey);
			trans.signature = signTrans;
			transactions.add(trans);
			checkTransactionsList();
		}
		return tempCoinsList;
	}
	
	void scroogeSendsCoins(String sender, User receiver) throws Exception 
	{	
		ArrayList<Coin> coinsList = createCoins();
		for(int i = 0 ; i < coinsList.size(); i++)
		{
			Transaction trans = new Transaction(sender, receiver.id, coinsList.get(i));
			String sign = Initialiser.sign(trans.toString(), Scrooge.privateKey);
			trans.signature = sign;
			transactions.add(trans);
			checkTransactionsList();
			receiver.coins.add(coinsList.get(i));
		}
		Initialiser.users.add(receiver);
		
		
	}
	
	void checkTransactionsList() throws NoSuchAlgorithmException
	{
		if(transactions.size() == 10 && blockChain.size() == 0)
		{
			createFirstBlock();
			transactions.clear();
			
		}
		else if(transactions.size() == 10 )
		{
			createBlock();
			transactions.clear();
		}
			
	}
	
	void createFirstBlock() throws NoSuchAlgorithmException
	{
		ArrayList<Transaction> blockTrans = (ArrayList<Transaction>) transactions.clone();;
		Block block = new Block(blockTrans, null);
		blockChain.add(block);
	}
	
	void createBlock() throws NoSuchAlgorithmException
	{
		ArrayList<Transaction> blockTrans = (ArrayList<Transaction>) transactions.clone();
		Block block = new Block(blockTrans, blockChain.get(blockChain.size()-1).hashOfBlock);
		blockChain.add(block);
	}
	
	
	public static void main(String[] args) throws Exception
	{
		//Initialise Scrooge
		Scrooge scrooge = new Scrooge();
		Initialiser initialiser = new Initialiser();
		System.out.println(initialiser.blockChain.size());
	}
}
