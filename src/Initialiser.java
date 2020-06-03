import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Initialiser {

	public static  LinkedList<Block> blockChain = new LinkedList<Block>(); 
	public static ArrayList<User> users = new ArrayList<User>();
	public static int transId = 0;
	public static int blockId = 0;
	public static int coinId = 0;
	public static ArrayList<Transaction> buffer = new ArrayList<Transaction>();
	public static Timer timer = new Timer(); 
	
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
			coin.addTransaction(trans);
			String signTrans = Initialiser.sign(trans.toString(), Scrooge.privateKey);
			trans.signature = signTrans;
			buffer.add(trans);
			checkScroogeTransactions();
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
			buffer.add(trans);
			coinsList.get(i).addTransaction(trans);
			checkScroogeTransactions();
			receiver.coins.add(coinsList.get(i));
		}
		Initialiser.users.add(receiver);	
	}
	
	public static void payCoins(ArrayList<Transaction> transactions) throws Exception
	{
		for(int i = 0; i < transactions.size(); i++)
		{
			String [] senderInfo = transactions.get(i).sender.split(" ");
			String [] receiverInfo = transactions.get(i).receiver.split(" ");
			int senderId = Integer.parseInt(senderInfo[1]);
			int receiverId = Integer.parseInt(receiverInfo[1]);
			System.out.println("Number of Coins before receiving: "+ users.get(receiverId).coins.size());
			users.get(senderId).coins.remove(transactions.get(i).coin);
			users.get(receiverId).coins.add(transactions.get(i).coin);
			String signature = sign(transactions.get(i).coin.toString(),users.get(receiverId).privateKey);
			transactions.get(i).coin.signature = signature;
			System.out.println("Sender: "+senderId);
			System.out.println("Receiver: "+receiverId);
			System.out.println("Coin id Transmitted: "+users.get(receiverId).coins.get(users.get(receiverId).coins.size()-1).id);
			System.out.println("//////////////////////////////////////////////////////////////");
			//			for(int j = 0; j < users.get(receiverId).coins.size(); j++)
//			{
//				System.out.println("Trans in Coins :" + j);
//			}
		}
	}
	
	public static void checkScroogeTransactions() throws NoSuchAlgorithmException
	{
		if(buffer.size() == 10 && blockChain.size() == 0)
		{
			createFirstBlock();
			buffer.clear();
		}
		else if(buffer.size() == 10 )
		{
			createScroogeBlock();
			buffer.clear();
		}	
	}
	
	public static void createFirstBlock() throws NoSuchAlgorithmException
	{
		ArrayList<Transaction> blockTrans = (ArrayList<Transaction>) buffer.clone();;
		Block block = new Block(blockTrans, null);
		blockChain.add(block);
	}
	
	public static void createScroogeBlock() throws NoSuchAlgorithmException
	{
		ArrayList<Transaction> blockTrans = (ArrayList<Transaction>) buffer.clone();
		Block block = new Block(blockTrans, blockChain.get(blockChain.size()-1).hashOfBlock);
		blockChain.add(block);
	}
	
	public static void checkUsersTransactions() throws Exception
	{
		if(buffer.size() == 10 )
		{
			createUsersBlock();
			buffer.clear();
		}	
	}
	
	public static void createUsersBlock() throws Exception
	{
		ArrayList<Transaction> blockTrans = (ArrayList<Transaction>) buffer.clone();
		Block block = new Block(blockTrans, blockChain.get(blockChain.size()-1).hashOfBlock);
		payCoins(blockTrans);
		blockChain.add(block);
	}
	
	
		static TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				int randomOne;
				int randomTwo;
				while(true)
				{
					randomOne = (int)(Math.random() * (99-0)) + 0;
					randomTwo = (int)(Math.random() * (99-0)) + 0;
					if(randomOne != randomTwo)
						break;
				}
				User sender = users.get(randomOne);
				User receiver = users.get(randomTwo);
				
				int amountOfCoins = (int)(Math.random() * (10-1)) + 1;
				
				
				try {
					sender.sendCoin(amountOfCoins, receiver.id);
					System.out.println("Amount to be send: "+ amountOfCoins);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
	
	public static void main(String[] args) throws Exception
	{
//		
//		JFrame frame = new JFrame("My First GUI");
//	       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	       frame.setSize(300,300);
//	       JButton button = new JButton("Press");
//	       frame.getContentPane().add(button); // Adds Button to content pane of frame
//	       frame.setVisible(true);
//	       button.addActionListener(e -> System.exit(0));
		Scrooge scrooge = new Scrooge();
		Initialiser initialiser = new Initialiser();
		
		
		
		//users.get(0).sendCoin(10, "User 2");
		//users.get(0).sendCoin(5, "User 3");
		timer.scheduleAtFixedRate(task,10000, 10000);
		
		
	}
}
