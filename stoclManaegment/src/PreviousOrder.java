import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class PreviousOrder {
	private ArrayList<ArrayList<String>> preOrder = new ArrayList<ArrayList<String>>();
	private static PreviousOrder pre = null;

	private MyDB data = MyDB.getInstance();
	
	private PreviousOrder() {
	}
	
    public static PreviousOrder getInstance() {
        if(pre == null) {
            pre = new PreviousOrder();
        }  
        return pre;
    }
    

	
    public void addNewOrder(ArrayList<String> order) {
    	preOrder.add(order);
    	data.deleteOrderStock(order);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ArrayList<ArrayList<String>> getPreOrder() {
		return preOrder;
	}

	public void setPreOrder(ArrayList<ArrayList<String>> preOrder) {
		this.preOrder = preOrder;
	}

}
