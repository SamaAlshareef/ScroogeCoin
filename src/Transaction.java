
public class Transaction {
	
	String id;
	String sender;
	String receiver;
	double amount;
	
	public Transaction(String id, String sender, String receiver, double amount)
	{
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}
}
