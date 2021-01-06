package net.netune.views;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageVisualizer extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ImageVisualizer(String path) {
		setLayout(new BorderLayout());
		
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(path));
		
		add(new JScrollPane(label));
	}
}
