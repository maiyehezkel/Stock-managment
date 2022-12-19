import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AddStock {
	private JFrame frame;
	public static JTextField proName;
	private JTextField weight;
	private JTextField quantity;
	private JTextField category;
	
	public AddStock(final MyDB data) {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 360, 435);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		


		final JComboBox weightBox = new JComboBox();
		weightBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		weightBox.setEditable(true);
		weightBox.setBounds(130, 233, 96, 27);
		frame.getContentPane().add(weightBox);
		
		category = new JTextField();
		category.setBounds(130, 127, 96, 29);
		frame.getContentPane().add(category);
		category.setColumns(10);
		category.setVisible(false);
		
		weight = new JTextField();
		weight.setBounds(130, 233, 96, 27);
		frame.getContentPane().add(weight);
		weight.setColumns(10);
		weight.setVisible(false);
		
		final JComboBox cateComboBox = new JComboBox();
		cateComboBox.setModel(new DefaultComboBoxModel(MyDB.categories.toArray()));
		cateComboBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		cateComboBox.setEditable(true);
		cateComboBox.setBounds(130, 127, 96, 29);
		frame.getContentPane().add(cateComboBox);
		
		final JCheckBox newCategory = new JCheckBox("קטגוריה חדשה");
		newCategory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		newCategory.setHorizontalAlignment(SwingConstants.CENTER);
		newCategory.setBounds(10, 127, 110, 35);
		frame.getContentPane().add(newCategory);
		newCategory.setEnabled(false);
		newCategory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(newCategory.isSelected()) {
					cateComboBox.setVisible(false);
					category.setVisible(true);
				}
				else {
					cateComboBox.setVisible(true);
					category.setVisible(false);
				}
				
			}
		});
		
		final JRadioButton addExist = new JRadioButton("\u05D4\u05D5\u05E1\u05E4\u05EA \u05DE\u05D5\u05E6\u05E8 \u05E7\u05D9\u05D9\u05DD");
		addExist.setSelected(true);
		addExist.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addExist.setBounds(49, 77, 121, 21);
		frame.getContentPane().add(addExist);
		addExist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				newCategory.setEnabled(false);
				weightBox.setVisible(true);
				weight.setVisible(false);
			}
		});

		
		final JRadioButton addNew = new JRadioButton("\u05D4\u05D5\u05E1\u05E4\u05EA \u05DE\u05D5\u05E6\u05E8 \u05D7\u05D3\u05E9");
		addNew.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addNew.setHorizontalAlignment(SwingConstants.CENTER);
		addNew.setBounds(170, 77, 130, 21);
		frame.getContentPane().add(addNew);
		addNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				weightBox.setVisible(false);
				weight.setVisible(true);
				newCategory.setEnabled(true);

				
			}
		});

		
		JLabel cateLabel = new JLabel("\u05E7\u05D8\u05D2\u05D5\u05E8\u05D9\u05D4:");
		cateLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cateLabel.setBounds(240, 124, 88, 35);
		frame.getContentPane().add(cateLabel);
		
		JLabel productLabel = new JLabel("\u05DE\u05D5\u05E6\u05E8:");
		productLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		productLabel.setBounds(240, 173, 64, 35);
		frame.getContentPane().add(productLabel);
		
		JLabel weightLabel = new JLabel("\u05DE\u05E9\u05E7\u05DC:");
		weightLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		weightLabel.setBounds(240, 227, 50, 35);
		frame.getContentPane().add(weightLabel);
		
		JLabel quantityLabel = new JLabel("\u05DB\u05DE\u05D5\u05EA:");
		quantityLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		quantityLabel.setBounds(240, 281, 50, 35);
		frame.getContentPane().add(quantityLabel);
		

		

		
		proName = new JTextField();
		proName.setBounds(130, 179, 96, 27);
		frame.getContentPane().add(proName);
		proName.setColumns(10);
		proName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(addExist.isSelected()) {
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
									String whatToDo = "add";
									String txt = proName.getText();
									String cate = String.valueOf(cateComboBox.getSelectedItem());
									String name = null;
									try {
										name = data.searchProduct(cate,txt,whatToDo);
									} catch (SQLException e) {
										e.printStackTrace();
									}
									data.searchWeight(name);
									weightBox.setModel(new DefaultComboBoxModel(data.weights.toArray()));
									
								}
								
							});
					}
				}
			}
		});

		
		quantity = new JTextField();
		quantity.setBounds(130, 287, 96, 27);
		frame.getContentPane().add(quantity);
		quantity.setColumns(10);
		
		JLabel addTitle = new JLabel("\u05D4\u05D5\u05E1\u05E4\u05D4 \u05DC\u05DE\u05DC\u05D0\u05D9");
		addTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		addTitle.setBounds(73, 15, 220, 46);
		frame.getContentPane().add(addTitle);
		
		JButton addButton = new JButton("\u05D4\u05D5\u05E1\u05E3");
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = proName.getText();
				int amount = Integer.parseInt(quantity.getText());
				if(addNew.isSelected()) {
					String cate;
					if(newCategory.isSelected()) {
						 cate = "'"+category.getText()+"'";
					}
					else {
						 cate = "'"+(String) cateComboBox.getSelectedItem()+"'";
					}
					float weightP = Float.parseFloat(weight.getText());
					data.add(cate, name, weightP, amount);
				}
				if(addExist.isSelected()) {
					float weightP = Float.parseFloat((String) weightBox.getSelectedItem());
					String what = null;
					data.update(name, weightP, amount, what);
				}
				frame.dispose();
			}
		});
		addButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		addButton.setBounds(110, 338, 118, 35);
		frame.getContentPane().add(addButton);
		

		
		ButtonGroup bg = new ButtonGroup();
		bg.add(addNew);
		bg.add(addExist);
	}

	public static void main(String[] args,final MyDB data) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddStock window = new AddStock(data);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}


}
