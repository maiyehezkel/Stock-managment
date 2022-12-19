
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class MyDB {
	private static MyDB data = null;
	private static Connection connection;
	private static Statement st;
	private static Statement st2;
	public static ArrayList<String> weights;
	public static ArrayList<String> categories;
	public static TableRowSorter sorter;
	
	private static ArrayList<String> products;

	private MyDB() {
		
	}
    public static MyDB getInstance() {
        if(data == null) {
            data = new MyDB();
            start();
        }
        return data;
    }
	
	public static void start() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock", "root", "315528539My");//Establishing connection
			System.out.println("Connected With the database successfully");
			st = connection.createStatement();
			st2 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			showTable();
			getCategories();
		} catch (SQLException e) {
			System.out.println("Error while connecting to the database");
	    }

	}
	public static void showTable() {
		try {
			Stockmanagement.getProductTable().setModel(new DefaultTableModel());
			String query = "select * from product order by קטגוריה,מוצר";
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsmd =rs.getMetaData();
			DefaultTableModel model = (DefaultTableModel) Stockmanagement.getProductTable().getModel();
			int col = rsmd.getColumnCount();
			String[] colName = new String[col];
			for(int i=0;i<col;i++) {
				colName[i]= rsmd.getColumnName(i+1);
			}
			model.setColumnIdentifiers(colName);
			String cat,name,weight,quantity;
			while(rs.next()) {
				quantity = rs.getString(1);
				weight = rs.getString(2);
				name = rs.getString(3);
				cat = rs.getString(4);
				String[] row = {quantity,weight,name,cat};
				model.addRow(row);				
			}
			
			sorter = new TableRowSorter<>(model);
			Stockmanagement.getProductTable().getColumnModel().getColumn(0).setPreferredWidth(30);
			Stockmanagement.getProductTable().getColumnModel().getColumn(1).setPreferredWidth(30);
			Stockmanagement.getProductTable().getColumnModel().getColumn(2).setPreferredWidth(110);
			Stockmanagement.getProductTable().getColumnModel().getColumn(3).setPreferredWidth(30);
			getCategories();


		} catch (SQLException e) {
			System.out.println("Error while connecting to the database");
	    }

	}
	
	public static void showPreTable() {
		try {
			Stockmanagement.getProductTable().setModel(new DefaultTableModel());
			String query = "select * from previousorder order by תאריך";
			ResultSet rs = st2.executeQuery(query);
			DefaultTableModel model = (DefaultTableModel) DeleteOrder.getTable().getModel();
			String cat,name,weight,quantity, date;
			String preDate = null;
			int i=0;
			while(rs.next()) {
				quantity = rs.getString(1);
				weight = rs.getString(2);
				name = rs.getString(3);
				cat = rs.getString(4);
				date = rs.getString(5);
				if(i==0) {
					preDate = rs.getString(5);
					Object[] row = {quantity,weight,name,cat,date,Boolean.FALSE};
					model.addRow(row);
				}
				else {
					if(preDate.equals(date)) {
						Object[] row = {quantity,weight,name,cat,null,Boolean.FALSE};
						model.addRow(row);
					}
					else {	
						preDate = date;
						Object[] row = {quantity,weight,name,cat,date,Boolean.FALSE};
						model.addRow(row);
					}
				}
				i++;

										
			}
			DeleteOrder.getTable().getColumnModel().getColumn(0).setPreferredWidth(30);
			DeleteOrder.getTable().getColumnModel().getColumn(1).setPreferredWidth(30);
			DeleteOrder.getTable().getColumnModel().getColumn(2).setPreferredWidth(90);
			DeleteOrder.getTable().getColumnModel().getColumn(3).setPreferredWidth(30);
			DeleteOrder.getTable().getColumnModel().getColumn(4).setPreferredWidth(90);
			DeleteOrder.getTable().getColumnModel().getColumn(5).setPreferredWidth(30);
			showTable();
		} catch (SQLException e) {
			System.out.println(e + "Error!! while connecting to the database");
	    }

	}
	
	public static void getCategories() throws SQLException {
		categories = new ArrayList<String>();
		String query = "select distinct קטגוריה from product order by קטגוריה";
		ResultSet rs = st.executeQuery(query);
		categories.add(null);
		while(rs.next()) {
			categories.add(rs.getString(1));
		}
		

	}
	public void add(String cate,String name, float weight, int amount){
		try {
			String query = "insert into product values (" + amount +","+ weight+","+name+","+cate+")";
			st.execute(query);
			showTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	public void update(String name, float weight, int amount, String whattodo) {
		try {
			Optional<String> what = Optional.ofNullable(whattodo);
			String whatToDo = "add"; 
			int sum = sumFun(amount,name,whatToDo);
			String query = "update product set כמות="+sum+" where מוצר='"+name+"' and משקל="+weight;
			st.executeUpdate(query);
			if(what  != null) {
				cancelOrder(name,weight);
			}
			showTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	public void cancelOrder(String name, float weight) throws SQLException {
		String query = "delete from previousorder where מוצר='"+name+"' and משקל="+weight;
		st.executeUpdate(query);
		showTable();
	}
	public void deleteOrderStock(ArrayList<String> order) {
		Date date = new Date();
		Timestamp today = new Timestamp(date.getTime());
		try {
			for(int i=0;i<order.size();i+=4) {
				delete(order.get(i+2), Float.parseFloat(order.get(i+1)),Integer.parseInt(order.get(i)));	
				try {
					saveOrder(order.get(i+2),Integer.parseInt(order.get(i)),Float.parseFloat(order.get(i+1)),order.get(i+3),today);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		     }
		}catch(NumberFormatException e1) {
			AddOrder.getAlarm().setText("נא למלא את כל התאים בשורה.");
		}
		
	}
	private void saveOrder(String name, int amount, float weight, String category, Timestamp today) throws SQLException {
		String query = "insert into previousorder values (" + amount +","+ weight+",'"+name+"','"+category+"','"+today+"')";
		st.execute(query);
	}
	public void delete(String name, float weight, int amount) {
		try {
			String whatToDo = "delete";
			int sum = sumFun(amount,name,whatToDo);
			String query = "update product set כמות="+sum+" where מוצר='"+name+"' and משקל="+weight;
			st.executeUpdate(query);
			System.out.println(query);
			showTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	private int sumFun(int a,String nameP,String whatToDo) throws SQLException {
		String query = "select כמות from product where מוצר='"+nameP+"'";
		ResultSet rs = st.executeQuery(query);
		rs.next();
		int quantity = Integer.parseInt(rs.getString(1));
		int sum = 0;
		if(whatToDo == "delete") {
			sum = quantity - a; 

		}
		if(whatToDo == "add") {
			sum = quantity + a; 

		}
		return sum;
		
	}
	public void deleteAll(String name, float weight) {
		try {
			String query = "delete from product where מוצר="+name+"and משקל="+weight;
			st.executeUpdate(query);
			showTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static String searchProduct(String cate,String txt,String whatToDo) throws SQLException {
		ArrayList<String> products= searchByCat(cate);
		String complete = "";
		int start = txt.length();
		int last = txt.length();
		int i;
		for(i=0;i<products.size();i++) {
			if(products.get(i).toString().startsWith(txt)) {
				complete = products.get(i).toString();
				last = complete.length();
				break;
			}
		}
		if(last>start) {
			if(whatToDo == "delete") {
				DeleteStock.proName.setText(complete);
				DeleteStock.proName.setCaretPosition(last);
				DeleteStock.proName.moveCaretPosition(start);
			}
			else if(whatToDo == "add"){
				AddStock.proName.setText(complete);
				AddStock.proName.setCaretPosition(last);
				AddStock.proName.moveCaretPosition(start);
			}
			else if(whatToDo == "insert") {
				System.out.printf("we are here but nothing happend", complete);
			}
		}
		return complete;
	}
	public static ArrayList<String> searchByCat(String cate) throws SQLException {
		products= new ArrayList<String>();
		String query = "select מוצר from product where קטגוריה='"+cate+"'";
		ResultSet rs = st.executeQuery(query);
		products.add(null);
		while(rs.next()) {
			products.add(rs.getString(1));
		}
		for(int i=0;i<AddOrder.getTable().getRowCount();i++) {
			String name = (String) AddOrder.getTable().getValueAt(i, 2);
			if(name==null) {
				break;
			}
			products.remove(name);

		}
		return products;
	}
	public static void searchWeight(String name) {
		try {
			String query = "select משקל from product where מוצר='"+name+"'";
			ResultSet rs = st.executeQuery(query);
			weights = new ArrayList<String>();
			String weight;
			weights.add(null);
			while(rs.next()) {
				weight = rs.getString(1);
				weights.add(weight.toString());
			}

			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public static void closeCon() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public static void closeSt() {
		try {
			if(st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	public static int chechIfInStock(String name,float weight) {
		String query = "select כמות from product where מוצר='"+name+"' and משקל='"+weight+"'";
		int inStock = 0;
		try {
			ResultSet rs = st.executeQuery(query);
			rs.next();
			inStock = Integer.parseInt(rs.getString(1));
		} catch (SQLException e) {
			System.out.println("תו לא חוקי.");
		}
		
		return inStock;
		
	}

	public static void main(String[] args) {
	}
	public static ArrayList<String> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<String> products) {
		this.products = products;
	}

}
