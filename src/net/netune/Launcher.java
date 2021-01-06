package net.netune;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Toolkit;

public class Launcher extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	private boolean alreadyStarted;
	
	public Launcher() {
		super("Netune");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Launcher.class.getResource("/net/netune/icons/Icon.png")));
		setResizable(false);
		setSize(500,137);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblOhNo = new JLabel();
		lblOhNo.setText("Select your project Folder");
		lblOhNo.setBounds(10, 17, 398, 16);
		contentPane.add(lblOhNo);
		
		JTextField textField = new JTextField();
		textField.setBounds(6, 45, 442, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Launch");
		btnNewButton.setBounds(394, 76, 94, 26);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("...");
		button.setBounds(458, 45, 30, 20);
		contentPane.add(button);
		
		JLabel lblNoLastWorkspaces = new JLabel("no last workspaces");
		if(new File("lastproject.txt").exists()){
			try{
				lblNoLastWorkspaces.setText(new String(Files.readAllBytes(Paths.get("lastproject.txt"))));
			}catch(Exception e){
				e.printStackTrace();
				lblNoLastWorkspaces.setText("no last workspaces");
			}
		}
		lblNoLastWorkspaces.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblNoLastWorkspaces.setForeground(Color.BLUE);
		lblNoLastWorkspaces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField.setText(lblNoLastWorkspaces.getText());
			}
		});
		lblNoLastWorkspaces.setBounds(10, 66, 398, 16);
		contentPane.add(lblNoLastWorkspaces);

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(chooser.showOpenDialog(Launcher.this) == JFileChooser.APPROVE_OPTION){
					textField.setText(chooser.getSelectedFile().getPath());
				}
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(alreadyStarted)
					return;
		
				if(!new File(textField.getText()).exists())
					if(!new File(textField.getText()).mkdir())
						return;
				
				if(!new File(textField.getText()).isDirectory())
					return;
				
				alreadyStarted = true;
				
				try{
					Files.write(Paths.get("lastproject.txt"),textField.getText().getBytes());
				}catch(Exception er){
					er.printStackTrace();
					JOptionPane.showMessageDialog(Launcher.this,"Sorry, unable to save workspace location.");
				}
				
				new Netune(textField.getText(),getIconImage());
				setVisible(false);
			}
		});
		
		setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new Launcher();
	}
}