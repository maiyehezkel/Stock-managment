import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class AddOrder {

	private JFrame frame;
	private static JTable table;


	private JComboBox<String> cateCombo;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"כמות", "משקל", "מוצר", "קטגוריה"
			}
		);
	private static JTextField product;
	private JComboBox<Float> weightCombo;
	private JComboBox productCombo;
	private static JLabel alarm;
	private Timer timer;
	private ActionListener taskPerformer;
	private PreviousOrder preOr = PreviousOrder.getInstance();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddOrder window = new AddOrder();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AddOrder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 501);
		frame.getContentPane().setLayout(null);
		
		
		JLabel addTitle = new JLabel("\u05D4\u05D6\u05DE\u05E0\u05D4 \u05D7\u05D3\u05E9\u05D4");
		addTitle.setFont(new Font("Tahoma", Font.BOLD, 27));
		addTitle.setBounds(130, 22, 202, 46);
		frame.getContentPane().add(addTitle);
		
		JButton addButton = new JButton("\u05E9\u05DC\u05D7");
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ArrayList<String> order = new ArrayList<String>();
				for(int i=0;i<table.getModel().getRowCount();i++) {
					if(table.getModel().getValueAt(i, 3)!=null) {
						order.add(String.valueOf(table.getModel().getValueAt(i, 0)));
						order.add(String.valueOf(table.getModel().getValueAt(i, 1)));
						order.add( (String) table.getModel().getValueAt(i, 2));
						order.add( (String) table.getModel().getValueAt(i, 3));
				}
			   }
				preOr.addNewOrder(order);
				frame.dispose();
				
			}
		});
		addButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		addButton.setBounds(154, 396, 118, 35);
		frame.getContentPane().add(addButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(39, 95, 357, 254);
		frame.getContentPane().add(scrollPane);
		
		cateCombo = new JComboBox();
		cateCombo.setModel(new DefaultComboBoxModel(MyDB.categories.toArray()));
		
		weightCombo = new JComboBox<Float>();
		productCombo = new JComboBox();

		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setModel(model);
		model.addTableModelListener(new TableModelListener() {
			private int row;

			public void tableChanged(TableModelEvent e) {
				row = e.getFirstRow();
	            if (e.getType() == TableModelEvent.UPDATE) {
	                System.out.println("Cell " + e.getFirstRow() + ", "
	                        + e.getColumn() + " changed. The new value: "
	                        + table.getModel().getValueAt(e.getFirstRow(),
	                        e.getColumn()));

            	if(e.getColumn()==0) {
            		try {
        				float weightP = Float.parseFloat((String) table.getModel().getValueAt(e.getFirstRow(), 1));
						checkStock((String) table.getModel().getValueAt(e.getFirstRow(), 2),weightP,e.getFirstRow());
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (NullPointerException e2) {
						getAlarm().setText("נא למלא קודם משקל של המוצר");
						resetAmount(row);
						
					}catch(NumberFormatException e3) {
						getAlarm().setText("נא למלא קודם משקל של המוצר");
						resetAmount(row);
					}
	            	}
	            }
	        }
		});
		

		
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cateCombo));
		productCombo.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				String cate = (String) cateCombo.getSelectedItem();
				try {
					
					productCombo.setModel(new DefaultComboBoxModel(MyDB.searchByCat(cate).toArray()));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		weightCombo.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				String name = (String) productCombo.getSelectedItem();
				MyDB.searchWeight(name);
				weightCombo.setModel(new DefaultComboBoxModel(MyDB.weights.toArray()));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		setAlarm(new JLabel());
		getAlarm().setBounds(70, 373, 350, 13);
		frame.getContentPane().add(getAlarm());
		getAlarm().setFont(new Font("Tahoma", Font.BOLD, 13));


		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(productCombo));
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(weightCombo));

		scrollPane.setViewportView(table);
		
	    taskPerformer = new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	getAlarm().setText("");
	        }
	    };

	}


	private void resetAmount(int row) {
		if(Integer.parseInt((String) table.getModel().getValueAt(row, 0))!=0) {
			table.getModel().setValueAt("0", row, 0);
		}
	}
	private void checkStock(String name,float weight,int row) throws SQLException {
		int amount = MyDB.chechIfInStock(name,weight);
		String temp = table.getModel().getValueAt(row, 0).toString();
		int needAmount = Integer.valueOf(temp);
		if(amount < needAmount) {
			getAlarm().setText("אין מספיק מלאי למוצר '"+name+"' במשקל "+weight+".");
			timer = new Timer(5000, taskPerformer);
			timer.setRepeats(false);
			timer.start();
			table.getModel().setValueAt(amount, row, 0);
			
		}
		
	}

	public static JLabel getAlarm() {
		return alarm;
	}

	public void setAlarm(JLabel alarm) {
		this.alarm = alarm;
	}
	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		AddOrder.table = table;
	}





}