import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JRadioButton;

public class DeleteStock {

	private JFrame frame;
	public static JTextField proName;
	private JTextField quantity;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args,final MyDB data) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteStock window = new DeleteStock(data);
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
	public DeleteStock(final MyDB data) {
		initialize(data);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(final MyDB data) {
		frame = new JFrame();
		frame.setBounds(100, 100, 359, 435);
		frame.getContentPane().setLayout(null);
		
		final JLabel quantityLabel = new JLabel("\u05DB\u05DE\u05D5\u05EA \u05DC\u05D4\u05D5\u05E8\u05D3\u05D4 \u05DE\u05D4\u05DE\u05DC\u05D0\u05D9:");
		quantityLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		quantityLabel.setBounds(154, 291, 181, 35);
		frame.getContentPane().add(quantityLabel);

		
		quantity = new JTextField();
		quantity.setBounds(49, 296, 96, 27);
		frame.getContentPane().add(quantity);
		quantity.setColumns(10);

		
		final JComboBox weightBox = new JComboBox();
		weightBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		weightBox.setEditable(true);
		weightBox.setBounds(49, 245, 96, 25);
		frame.getContentPane().add(weightBox);
		
		final JComboBox cateComboBox = new JComboBox();
		cateComboBox.setModel(new DefaultComboBoxModel(MyDB.categories.toArray()));
		cateComboBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		cateComboBox.setEditable(true);
		cateComboBox.setBounds(49, 137, 96, 29);
		frame.getContentPane().add(cateComboBox);
		
		proName = new JTextField();
		proName.setBounds(49, 189, 96, 27);
		frame.getContentPane().add(proName);
		proName.setColumns(10);
		proName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
					case KeyEvent.VK_BACK_SPACE:
						break;
					case KeyEvent.VK_ENTER:
						proName.setText(proName.getText());
						break;
					default:
						EventQueue.invokeLater(new Runnable() {

							@Override
							public void run() {
								String txt = proName.getText();
								String cate = String.valueOf(cateComboBox.getSelectedItem());
								String whatToDo = "delete";
								String name = null;
								try {
									name = data.searchProduct(cate,txt,whatToDo);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								data.searchWeight(name);
								weightBox.setModel(new DefaultComboBoxModel(data.weights.toArray()));

								
								
							}
							
						});
				}
			}
		});
		

		
		
		final JRadioButton deleteAll = new JRadioButton("\u05DE\u05D7\u05D9\u05E7\u05EA \u05DE\u05D5\u05E6\u05E8");
		deleteAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		deleteAll.setBounds(49, 87, 121, 21);
		frame.getContentPane().add(deleteAll);
		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quantityLabel.setVisible(false);
				quantity.setVisible(false);
				
			}
		});
		


		final JRadioButton delete = new JRadioButton("\u05D4\u05D5\u05E8\u05D3\u05EA \u05DE\u05DC\u05D0\u05D9 \u05DC\u05DE\u05D5\u05E6\u05E8");
		delete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		delete.setBounds(181, 87, 138, 21);
		frame.getContentPane().add(delete);
		delete.setSelected(true);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quantityLabel.setVisible(true);
				quantity.setVisible(true);
				
				
			}
		});
		
		JLabel cateLabel = new JLabel("\u05E7\u05D8\u05D2\u05D5\u05E8\u05D9\u05D4:");
		cateLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		cateLabel.setBounds(155, 134, 88, 35);
		frame.getContentPane().add(cateLabel);
		
		JLabel productLabel = new JLabel("\u05DE\u05D5\u05E6\u05E8:");
		productLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		productLabel.setBounds(155, 183, 64, 35);
		frame.getContentPane().add(productLabel);
		
		JLabel weightLabel = new JLabel("\u05DE\u05E9\u05E7\u05DC:");
		weightLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		weightLabel.setBounds(155, 237, 83, 35);
		frame.getContentPane().add(weightLabel);
		

		

		
		
		
		
		JLabel addTitle = new JLabel("\u05DE\u05D7\u05E7 \u05DE\u05D4\u05DE\u05DC\u05D0\u05D9");
		addTitle.setFont(new Font("Tahoma", Font.BOLD, 27));
		addTitle.setBounds(93, 22, 202, 46);
		frame.getContentPane().add(addTitle);
		
		JButton delButton = new JButton("מחק");
		delButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = proName.getText();
				float weightP = Float.parseFloat((String) weightBox.getSelectedItem());
				if(delete.isSelected()) {
					int amount = Integer.parseInt(quantity.getText());
					data.delete(name, weightP, amount);
				}
				if(deleteAll.isSelected()) data.deleteAll(name, weightP);
				frame.dispose();
				
			}
		});
		delButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		delButton.setBounds(120, 338, 118, 35);
		frame.getContentPane().add(delButton);
		


		
		ButtonGroup bg = new ButtonGroup();
		bg.add(deleteAll);
		bg.add(delete);
	}




}

