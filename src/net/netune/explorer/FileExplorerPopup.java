package net.netune.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import net.netune.Netune;
import net.netune.editors.dlang.DEditor;
import net.netune.editors.json.JSONEditor;
import net.netune.editors.text.TextEditor;
import net.netune.views.ImageVisualizer;

public class FileExplorerPopup extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public FileExplorerPopup(JTree tree, FileExplorer e, FileExplorerRender r) {

		JMenu mnNew = new JMenu("New");
		add(mnNew);

		JMenuItem mntmFolder = new JMenuItem("Folder");
		mntmFolder.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/Folder.png")));
		mntmFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(e, "New Folder", "Folder Name");
				if (name != null) {
					if (name.isEmpty())
						return;
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();
						new File(file.getPath() + File.separator + name).mkdir();
						e.requestRefresh();
					}
				}
			}
		});
		mnNew.add(mntmFolder);

		JMenuItem mntmFile = new JMenuItem("File");
		mntmFile.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/File.png")));
		mntmFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(e, "New File", "File Name");
				if (name != null) {
					if (name.isEmpty())
						return;
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();
						try {
							new File(file.getPath() + File.separator + name).createNewFile();
							e.requestRefresh();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(e, "Unable to make a file. \n" + e1.toString());
							e1.printStackTrace();
						}
					}
				}
			}
		});
		mnNew.add(mntmFile);

		JMenuItem mntmDSourceFile = new JMenuItem("D Source File");
		mntmDSourceFile.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/Source.png")));
		mnNew.add(mntmDSourceFile);
		mntmDSourceFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(e, "New D Source File", "Name");
				if (name != null) {
					if (name.isEmpty())
						return;
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();
						try {
							if (!name.endsWith(".d"))
								new File(file.getPath() + File.separator + name + ".d").createNewFile();
							else
								new File(file.getPath() + File.separator + name).createNewFile();

							e.requestRefresh();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(e, "Unable to make a file. \n" + e1.toString());
							e1.printStackTrace();
						}
					}
				}
			}
		});

		JMenu mnShowIn = new JMenu("Show In");
		add(mnShowIn);

		JMenuItem mntmExplorer = new JMenuItem("System Explorer");
		mntmExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();
						if (file.isDirectory()) {
							Runtime.getRuntime().exec("explorer " + file.getPath());
						}
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Unable to open in the System Explorer.");
				}
			}
		});
		mntmExplorer.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/Explorer.png")));
		mnShowIn.add(mntmExplorer);

		JMenuItem mntmReload = new JMenuItem("Reload");
		mntmReload.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/Refresh.png")));
		mntmReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				e.requestRefresh();
			}
		});

		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/Delete.png")));
		mntmDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tree.getSelectionPath() != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();

						if (!file.isDirectory()) {
							if (JOptionPane.showConfirmDialog(null,
									"You Really Want to delete this file?") == JOptionPane.OK_OPTION) {
								file.delete();
								e.requestRefresh();
							}
						} else {
							JOptionPane.showMessageDialog(null, "Sorry, you can only delete Files.");
						}
					}
				}
			}
		});

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tree.getSelectionPath() != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();

						if (!file.isDirectory()) {
							if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
								Netune.addPane(file.getName(), new ImageVisualizer(file.getPath()),
										FileExplorerRender.getImageForExt(file.getName()));
							} else if (file.getName().endsWith(".json")) {
								JSONEditor editor = new JSONEditor();
								editor.openFile(file);

								Netune.addPane(file.getName(), editor,
										FileExplorerRender.getImageForExt(file.getName()));
							} else if (file.getName().endsWith(".d")) {
								DEditor editor = new DEditor();
								editor.openFile(file);

								Netune.addPane(file.getName(), editor,
										FileExplorerRender.getImageForExt(file.getName()));
							} else {
								TextEditor editor = new TextEditor();
								editor.openFile(file);

								Netune.addPane(file.getName(), editor,
										FileExplorerRender.getImageForExt(file.getName()));
							}
						}
					}
				}
			}
		});
		add(mntmOpen);

		JMenu mnOpenWith = new JMenu("Open With");
		add(mnOpenWith);

		JMenuItem mntmDEditor = new JMenuItem("D Editor");
		mntmDEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tree.getSelectionPath() != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();

						if (!file.isDirectory()) {
							DEditor editor = new DEditor();
							editor.openFile(file);

							Netune.addPane(file.getName(), editor, FileExplorerRender.getImageForExt(file.getName()));
						}
					}
				}
			}
		});
		mnOpenWith.add(mntmDEditor);

		JMenuItem mntmJsonEditor = new JMenuItem("JSON Editor");
		mntmJsonEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (tree.getSelectionPath() != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();

						if (!file.isDirectory()) {
							JSONEditor editor = new JSONEditor();
							editor.openFile(file);

							Netune.addPane(file.getName(), editor, FileExplorerRender.getImageForExt(file.getName()));
						}
					}
				}

			}
		});
		mnOpenWith.add(mntmJsonEditor);

		JMenuItem mntmNotepad = new JMenuItem("Notepad");
		mntmNotepad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (tree.getSelectionPath() != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File file = (File) node.getUserObject();

						if (!file.isDirectory()) {
							TextEditor editor = new TextEditor();
							editor.openFile(file);

							Netune.addPane(file.getName(), editor, FileExplorerRender.getImageForExt(file.getName()));
						}
					}
				}

			}
		});
		mnOpenWith.add(mntmNotepad);
		add(mntmDelete);
		add(mntmReload);
	}
}
