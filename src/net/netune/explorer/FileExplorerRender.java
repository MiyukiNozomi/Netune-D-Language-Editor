package net.netune.explorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class FileExplorerRender implements TreeCellRenderer {
	
	public static final ImageIcon 
	folder = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/Folder.png")),
	file = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/File.png")),
	source = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/Source.png")),
	executable = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/Executable.png")),
	text = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/Text.png")),
	object = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/Object.png")),
	script = new ImageIcon(FileExplorerRender.class.getResource("/net/netune/icons/Script.png"));
	
	//public static final Border selectedBorder = BorderFactory.createMatteBorder(1,5,1,1,Color.RED);
	
	public static final Border spacing = BorderFactory.createEmptyBorder(4,4,4,4);
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		JLabel result = new JLabel();
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		if(node.getUserObject() instanceof String){
			result.setText((String) node.getUserObject());
			result.setIcon(folder);
		}else{
			result.setText(((File) node.getUserObject()).getName());
			File f = ((File) node.getUserObject());
			
			if(f.isDirectory())
				result.setIcon(folder);
			else
				result.setIcon(new ImageIcon(getImageForExt(f.getName())));
		}
			
		if(selected){
			result.setForeground(Color.BLUE);
		}else{
			result.setForeground(Color.BLACK);
			result.setBackground(Color.WHITE);
		}
	
		result.setBorder(spacing);
		
		return result;
	}

	public static Image getImageForExt(String name){
		if (name.endsWith(".d"))
			return source.getImage();
		else if (name.endsWith(".exe") || name.endsWith(".json"))
			return executable.getImage();
		else if(name.endsWith(".obj"))
			return object.getImage();
		else if(name.endsWith(".txt") || name.endsWith(".png") || name.endsWith(".jpg"))
			return text.getImage();
		else if(name.endsWith(".bat") || name.endsWith(".cmd") || name.endsWith(".vbs"))
			return script.getImage();
		else
			return file.getImage();
	}
} 
