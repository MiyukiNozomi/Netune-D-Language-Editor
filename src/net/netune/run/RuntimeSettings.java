package net.netune.run;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class RuntimeSettings extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final Image ICON = new ImageIcon(RuntimeSettings.class.getResource("/net/netune/icons/Toolbox.png")).getImage();
	
	private JTextField textField;
	
	public RuntimeSettings() {
		super("Runtime Settings");
		setAlwaysOnTop(true);
		setResizable(false);
		setSize(365,286);
		setLocationRelativeTo(null);
		setIconImage(RuntimeSettings.ICON);
		getContentPane().setLayout(null);
		
		JLabel lblTargetPath = new JLabel("Target Path");
		lblTargetPath.setBounds(10, 11, 87, 14);
		getContentPane().add(lblTargetPath);
		
		textField = new JTextField(NetuneRuntime.location);
		textField.setBounds(10, 25, 339, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblBatchCode = new JLabel("Batch Code");
		lblBatchCode.setBounds(10, 56, 75, 14);
		getContentPane().add(lblBatchCode);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 74, 339, 144);
		getContentPane().add(scrollPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setText(NetuneRuntime.exec_batch);
		scrollPane.setViewportView(textPane);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				NetuneRuntime.location = textField.getText();
				NetuneRuntime.exec_batch = textPane.getText();
				
				setVisible(false);
				
				try{
					Files.write(Paths.get("last_config.txt"),textPane.getText().getBytes());
				}catch(Exception e){
					JOptionPane.showMessageDialog(null,"Unable to save ");
					e.printStackTrace();
				}
			}
		});
		btnSave.setBounds(260, 229, 89, 23);
		getContentPane().add(btnSave);
		
		setVisible(true);
	}
}
