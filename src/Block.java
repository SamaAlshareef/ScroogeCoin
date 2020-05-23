import java.util.ArrayList;

public class Block {

		String id;
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		String ptrToPrevBlock;
		String hashOfBlock;
		
		public Block(String id, ArrayList<Transaction> transactions, String ptrToPrevBlock, String hashofBlock)
		{
			this.id = id;
			this.transactions = transactions;
			this.ptrToPrevBlock = ptrToPrevBlock;
			this.hashOfBlock = hashofBlock;
		}
}
