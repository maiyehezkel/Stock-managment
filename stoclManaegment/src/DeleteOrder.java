import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class DeleteOrder {

	private JFrame frame;
	private static JTable table;
	DefaultTableModel model;
	private MyDB data= MyDB.getInstance();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteOrder window = new DeleteOrder();
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
	public DeleteOrder() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 583, 507);
		frame.getContentPane().setLayout(null);
		
		
		JLabel deleteTitle = new JLabel("ביטול הזמנה");
		deleteTitle.setFont(new Font("Tahoma", Font.BOLD, 27));
		deleteTitle.setBounds(194, 22, 202, 46);
		frame.getContentPane().add(deleteTitle);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 78, 477, 250);
		frame.getContentPane().add(scrollPane);
		
		
		table = new JTable() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                    	return String.class;
                    default:
                        return Boolean.class;
                }
            }
        };
		scrollPane.setViewportView(table);
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		model = new DefaultTableModel();
		String[] colName = {"כמות","משקל","מוצר","קטגוריה","תאריך","סמן"};
		model.setColumnIdentifiers(colName);
		table.setModel(model);
		MyDB.showPreTable();
		model.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {

	            if (e.getType() == TableModelEvent.UPDATE) {
	                System.out.println("Cell " + e.getFirstRow() + ", "
	                        + e.getColumn() + " changed. The new value: "
	                        + table.getModel().getValueAt(e.getFirstRow(),
	                        e.getColumn()));
	            }
	            if(e.getColumn()==5) {
	            	Boolean value = (Boolean) table.getModel().getValueAt(e.getFirstRow(), e.getColumn());
	            	for(int i=1;i<table.getRowCount()-e.getFirstRow();i++) {
	            		String predate = (String) table.getModel().getValueAt(e.getFirstRow()+i, 4);
	            		if(predate == null) {
	            			markAllOrder(value,e.getFirstRow()+i);
	            		}
	            		else {
	                		break;
	                	}
	                	

		            		
		            	}
	            		
	            	}
	            	

	            }
			
			
		});
	
		JButton deleteButton = new JButton("מחק");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				for(int i=0;i<table.getRowCount();i++) {
					boolean check = (boolean) table.getValueAt(i, 5);
					String name = (String) table.getValueAt(i, 2);
					float weight =  Float.parseFloat((String) table.getValueAt(i, 1));
					int amount = Integer.parseInt((String) table.getValueAt(i, 0));
					String whattodo = "cancel";
					if(check) {
						data.update(name, weight, amount,whattodo);
					}
				}
				frame.dispose();
			}
		});
		deleteButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		deleteButton.setBounds(224, 398, 118, 35);
		frame.getContentPane().add(deleteButton);
	}
		
	private void markAllOrder(Boolean value, int row) {
    	if(table.getModel().getValueAt(row, 5).equals(value)) {
    	}
    	else {
    		table.getModel().setValueAt(value,row, 5);
    	}
	}
	public static JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
}