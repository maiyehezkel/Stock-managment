import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JProgressBar;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;

public class Testclass {

	private JFrame frame;
	private JTable table;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Testclass window = new Testclass();
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
	public Testclass() {
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
		
		JButton deleteButton = new JButton("מחק");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				
			}
		});
		deleteButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		deleteButton.setBounds(224, 398, 118, 35);
		frame.getContentPane().add(deleteButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 78, 477, 250);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
			},
			new String[] {
				"\u05DB\u05DE\u05D5\u05EA", "\u05DE\u05E9\u05E7\u05DC", "\u05DE\u05D5\u05E6\u05E8", "\u05E7\u05D8\u05D2\u05D5\u05E8\u05D9\u05D4", "\u05EA\u05D0\u05E8\u05D9\u05DA", "\u05E1\u05DE\u05DF"
			}
		));
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		chckbxNewCheckBox.setBounds(91, 362, 93, 21);
		frame.getContentPane().add(chckbxNewCheckBox);
			

	}
}
