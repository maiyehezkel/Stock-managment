import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;



public class Stockmanagement {

	public JFrame frame;
	private JTextField search;
	private static JTable productTable;
	private MyDB database;
	private Timer timer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Stockmanagement window = new Stockmanagement();
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
	public Stockmanagement() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("ALMA");
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 14));
		frame.setBounds(100, 100, 748, 513);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel title = new JLabel("\u05E0\u05D9\u05D4\u05D5\u05DC \u05DE\u05DC\u05D0\u05D9");
		title.setFont(new Font("Tahoma", Font.BOLD, 30));
		title.setBounds(290, 14, 170, 36);
		frame.getContentPane().add(title);
		
		JButton add = new JButton("\u05D4\u05D5\u05E1\u05E3 \u05DC\u05DE\u05DC\u05D0\u05D9");
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddStock.main(null,database);
				try {
					MyDB.getCategories();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		add.setFont(new Font("Tahoma", Font.BOLD, 13));
		add.setBounds(567, 144, 119, 30);
		frame.getContentPane().add(add);
		
		JButton newOrder = new JButton("\u05D4\u05D6\u05DE\u05E0\u05D4 \u05D7\u05D3\u05E9\u05D4");
		newOrder.setFont(new Font("Tahoma", Font.BOLD, 13));
		newOrder.setBounds(567, 197, 119, 30);
		newOrder.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				AddOrder.main(null);
			}
		});
		frame.getContentPane().add(newOrder);
		
		
		JButton cancel = new JButton("\u05D1\u05D9\u05D8\u05D5\u05DC \u05D4\u05D6\u05DE\u05E0\u05D4");
		cancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		cancel.setBounds(567, 251, 119, 30);
		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				DeleteOrder.main(null);
			}
		});
		frame.getContentPane().add(cancel);
		
		JButton delete = new JButton("\u05DE\u05D7\u05E7 \u05DE\u05D4\u05DE\u05DC\u05D0\u05D9");
		delete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				DeleteStock.main(null,database);
			}
		});
		delete.setFont(new Font("Tahoma", Font.BOLD, 13));
		delete.setBounds(567, 305, 119, 30);
		frame.getContentPane().add(delete);
		
		final JLabel refreshMessage = new JLabel("המלאי התעדכן בהצלחה");
		refreshMessage.setBounds(204, 396, 167, 30);
		frame.getContentPane().add(refreshMessage);
		refreshMessage.setFont(new Font("Tahoma", Font.BOLD, 13));
		refreshMessage.setVisible(false);
		
        final ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
        		refreshMessage.setVisible(false);
            }
        };
        
		JButton refresh = new JButton("\u05E8\u05E2\u05E0\u05DF \u05DE\u05DC\u05D0\u05D9");
		refresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MyDB.showTable();
				refreshMessage.setVisible(true);
				timer = new Timer(5000, taskPerformer);
				timer.setRepeats(false);
				timer.start();
			}
		});
		refresh.setFont(new Font("Tahoma", Font.BOLD, 13));
		refresh.setBounds(213, 425, 119, 30);
		frame.getContentPane().add(refresh);
		
		JLabel searchLabel = new JLabel("חיפוש:");
		searchLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		searchLabel.setBounds(368, 67, 62, 17);
		frame.getContentPane().add(searchLabel);
		
		search = new JTextField();
		searchLabel.setLabelFor(search);
		search.setBounds(92, 67, 266, 19);
		frame.getContentPane().add(search);
		search.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u05E0\u05D9\u05D4\u05D5\u05DC \u05DE\u05DC\u05D0\u05D9", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(48, 100, 457, 303);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 22, 437, 272);
		panel.add(scrollPane);
		
		setProductTable(new JTable());
		getProductTable().setFont(new Font("Tahoma", Font.PLAIN, 12));
		getProductTable().setEnabled(false);
		database = MyDB.getInstance();
		scrollPane.setViewportView(getProductTable());
		


		 
		getProductTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
		    @Override
		    public Component getTableCellRendererComponent(JTable table,
		            Object value, boolean isSelected, boolean hasFocus, int row, int col) {				 
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		        
		        setHorizontalAlignment(JLabel.CENTER);
				 
		        String status = (String)table.getModel().getValueAt(row, 0);
		        if ("0".equals(status)) {
		            setBackground(Color.RED);
		            setForeground(Color.WHITE);
		        } else {
		            setBackground(table.getBackground());
		            setForeground(table.getForeground());
		        }       
		        

				 
		        return this;
		    }   
		});




		
		productTable.setRowSorter(MyDB.sorter);
	    search.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search(search.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search(search.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search(search.getText());
			}
			public void search(String str) {
		          if (str.length() == 0) {
		               MyDB.sorter.setRowFilter(null);
		            } else {
		               MyDB.sorter.setRowFilter(RowFilter.regexFilter(str));
		            }
		         }

	       });
		

	}

	public static JTable getProductTable() {
		return productTable;
	}

	public void setProductTable(JTable productTable) {
		this.productTable = productTable;
	}
}
