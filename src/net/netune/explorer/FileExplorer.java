package net.netune.explorer;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileExplorer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTree document;
	private DefaultMutableTreeNode node;
	private File file;
	private FileExplorerRender render;
	
	public FileExplorer(String target) {
		setLayout(new BorderLayout());
		
		render = new FileExplorerRender();
		file = new File(target);
		
		node = new DefaultMutableTreeNode("Source Explorer");
		list(node,file);
		document = new JTree(node);
		document.setCellRenderer(render);
		
		document.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					new FileExplorerPopup(document,FileExplorer.this,render).show(document,e.getX(),e.getY());
				}
			}
		});
		
		add(document);
	}
	
	public void requestRefresh(){
		remove(document);
		
		node = new DefaultMutableTreeNode("Source Explorer");
		list(node,file);
		document = new JTree(node);
		document.setCellRenderer(render);
		
		document.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					new FileExplorerPopup(document,FileExplorer.this,render).show(document,e.getX(),e.getY());
				}
			}
		});
		add(document);
		
		repaint();
		document.repaint();
	}

	private void list(DefaultMutableTreeNode node, File f) {
		if (!f.isDirectory()) {
			node.add(new DefaultMutableTreeNode(f));
		} else {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
			node.add(child);
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++)
				list(child, files[i]);
		}
	}

	public JTree getDocument() {
		return document;
	}
}
