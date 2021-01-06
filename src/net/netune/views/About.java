package net.netune.views;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;

public class About extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm;
	private JTextField txtHttpcreativecommonsorglicensesbyus;
	private JTextField txtWwwahasoftcom;
	
	public About() {
		setResizable(false);
		setAlwaysOnTop(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/net/netune/icons/E-mail.png")));
		setTitle("About");
		setSize(400,380);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel lblAuthors = new JLabel("Author: MiyukiNozomi");
		lblAuthors.setBounds(10, 48, 374, 25);
		getContentPane().add(lblAuthors);
		
		JLabel lblNetune = new JLabel("netune");
		lblNetune.setFont(new Font("Tahoma", Font.PLAIN, 34));
		lblNetune.setIcon(new ImageIcon(new ImageIcon(About.class.getResource("/net/netune/icons/Icon.png")).getImage().getScaledInstance(50,50,0)));
		lblNetune.setBounds(10, 289, 374, 57);
		getContentPane().add(lblNetune);
		
		JLabel lblStandardEdition = new JLabel("Current Version: Netune Standard Edition");
		lblStandardEdition.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStandardEdition.setBounds(10, 11, 374, 26);
		getContentPane().add(lblStandardEdition);
		
		JLabel lblVersionDate = new JLabel("Version Date: 5/1/2021");
		lblVersionDate.setBounds(10, 84, 374, 25);
		getContentPane().add(lblVersionDate);
		
		JLabel lblAssets = new JLabel("About the Assets");
		lblAssets.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAssets.setBounds(10, 114, 374, 26);
		getContentPane().add(lblAssets);
		
		JLabel lblTheCurrentIcon = new JLabel("The current icon pack is from:");
		lblTheCurrentIcon.setBounds(10, 152, 374, 14);
		getContentPane().add(lblTheCurrentIcon);
		
		txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm = new JTextField();
		txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm.setEditable(false);
		txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm.setText("http://www.small-icons.com/stock-icons/16x16-free-application-icons.htm");
		txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm.setBounds(10, 166, 374, 20);
		getContentPane().add(txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm);
		txtHttpwwwsmalliconscomstockiconsxfreeapplicationiconshtm.setColumns(10);
		
		JLabel lblLicensedUnderThe = new JLabel("Licensed under the:");
		lblLicensedUnderThe.setBounds(10, 197, 374, 14);
		getContentPane().add(lblLicensedUnderThe);
		
		txtHttpcreativecommonsorglicensesbyus = new JTextField();
		txtHttpcreativecommonsorglicensesbyus.setText("http://creativecommons.org/licenses/by/3.0/us/");
		txtHttpcreativecommonsorglicensesbyus.setEditable(false);
		txtHttpcreativecommonsorglicensesbyus.setColumns(10);
		txtHttpcreativecommonsorglicensesbyus.setBounds(10, 212, 374, 20);
		getContentPane().add(txtHttpcreativecommonsorglicensesbyus);
		
		txtWwwahasoftcom = new JTextField();
		txtWwwahasoftcom.setText("www.aha-soft.com");
		txtWwwahasoftcom.setEditable(false);
		txtWwwahasoftcom.setColumns(10);
		txtWwwahasoftcom.setBounds(10, 258, 374, 20);
		getContentPane().add(txtWwwahasoftcom);
		
		JLabel lblSmallIcons = new JLabel("Web-site small-icons.com belongs to Aha-Soft.");
		lblSmallIcons.setBounds(10, 243, 374, 14);
		getContentPane().add(lblSmallIcons);
	
		setVisible(true);
	}
}
