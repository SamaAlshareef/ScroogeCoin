import java.util.ArrayList;

public class Coin {
	
	int id;
	String signature;
	ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
	
	public Coin()
	{
		this.id = Initialiser.coinId ++;
	}

	void addTransaction(Transaction trans)
	{
		this.transactionsList.add(trans);
	}
	
	
}
