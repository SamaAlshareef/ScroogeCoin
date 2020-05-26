
public class Transaction {
	
	int id;
	String sender;
	String receiver;
	String signature;
	Coin coin;
	boolean verified;
	
	//Pay Coins Transaction
	public Transaction( String sender, String receiver,Coin coin)
	{
		this.id = Initialiser.transId ++;
		this.sender = sender;
		this.receiver = receiver;
		this.coin = coin;
		this.verified = false;
	}
	
	//Create Coins Transactions
	public Transaction(String creator, Coin coin)
	{
		this.id = Initialiser.transId ++;
		this.sender = creator;
		this.coin = coin;
	}
}
