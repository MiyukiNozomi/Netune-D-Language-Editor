package net.netune;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import net.netune.editors.dlang.DEditor;
import net.netune.explorer.FileExplorer;
import net.netune.explorer.FileExplorerPopup;
import net.netune.run.NetuneRuntime;
import net.netune.run.RuntimeSettings;
import net.netune.views.About;

public class Netune extends JFrame {
	private static final long serialVersionUID = 1L;

	private static JTabbedPane editorsPane;

	private JPanel contentPane;

	public Netune(String workspace, Image image) {
		super("Netune - " + workspace);
		setIconImage(image);
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		NetuneRuntime.location = workspace;

		if (new File("last_config.txt").exists()) {
			try {
				NetuneRuntime.exec_batch = new String(Files.readAllBytes(Paths.get("last_config.txt")));
			} catch (Exception e) {
				NetuneRuntime.exec_batch = "title You should edit this at the Settings button. or File > Runtime Settings\ndub build\npause > nul";
				e.printStackTrace();
			}
		} else
			NetuneRuntime.exec_batch = "title You should edit this at the Settings button. or File > Runtime Settings\npause > nul";

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmCompilerConfiguration = new JMenuItem("Runtime Settings");
		mntmCompilerConfiguration.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Application.png")));
		mntmCompilerConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new RuntimeSettings();
			}
		});
		mnFile.add(mntmCompilerConfiguration);
		
		JMenuItem mntmNewWindow = new JMenuItem("New Window");
		mntmNewWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Netune netune = new Netune(workspace, image);
				netune.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				netune.setTitle("Netune (New Window) - " + workspace);
			}
		});
		mntmNewWindow.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Icon.png")));
		mnFile.add(mntmNewWindow);
		
		JMenuItem mntmChangeProject = new JMenuItem("Change Project");
		mntmChangeProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Launcher();
			}
		});
		mntmChangeProject.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Refresh.png")));
		mnFile.add(mntmChangeProject);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmCompileAndRun = new JMenuItem("Compile And Run");
		mntmCompileAndRun.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Play.png")));
		mntmCompileAndRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NetuneRuntime.execute();
			}
		});
		mnEdit.add(mntmCompileAndRun);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new About();
			}
		});
		mntmAbout.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/E-mail.png")));
		mnEdit.add(mntmAbout);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);

		JTabbedPane projectExplorer = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(projectExplorer);

		FileExplorer fileExplorer = new FileExplorer(workspace);
		fileExplorer.getDocument().setEditable(true);
		projectExplorer.addTab("Project Explorer", null, new JScrollPane(fileExplorer), null);

		editorsPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(editorsPane);

		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		Dimension SPACING = new Dimension(4, 16);

		JButton label = new JButton("");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new RuntimeSettings();
			}
		});

		JButton label_1_1 = new JButton("");
		label_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (editorsPane.getSelectedComponent() != null) {
					if (editorsPane.getSelectedComponent() instanceof DEditor) {
						((DEditor) editorsPane.getSelectedComponent()).saveFile();
					}
				}
			}
		});
		label_1_1.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Save.png")));
		toolBar.add(label_1_1);

		toolBar.addSeparator(SPACING);
		label.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Application.png")));
		toolBar.add(label);

		toolBar.addSeparator(SPACING);

		JButton label_1 = new JButton("");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				NetuneRuntime.execute();
			}
		});
		label_1.setIcon(new ImageIcon(Netune.class.getResource("/net/netune/icons/Play.png")));
		toolBar.add(label_1);

		/*
		 * JPanel panel_1 = new JPanel() { private static final long
		 * serialVersionUID = 1L;
		 * 
		 * private int[] p; BufferedImage frame;
		 * 
		 * @Override public void paint(Graphics g) { if (frame == null) { frame
		 * = new BufferedImage(getSize().width, getSize().height,
		 * BufferedImage.TYPE_INT_RGB);
		 * 
		 * p = ((DataBufferInt) frame.getRaster().getDataBuffer()).getData();
		 * 
		 * for (int y = 0; y < p.length; y++) { for (int x = 0; x < p.length;
		 * x++) { p[y] = 0xff00ff * x + 0x00ff00 * y; } } }
		 * 
		 * g.drawImage(frame, 0, 0, getSize().width, getSize().height, null); }
		 * };
		 */
		setVisible(true);
	}

	public static void setTitleAt(Component id, String title) {
		TabComponent com = (TabComponent) editorsPane.getTabComponentAt(editorsPane.indexOfComponent(id));
		com.titleLbl.setText(title);
		com.repaint();
	}

	public static void addPane(String name, java.awt.Component e, Image icon) {
		editorsPane.addTab(name, e);
		if (icon != null)
			editorsPane.setTabComponentAt(editorsPane.indexOfComponent(e),
					new TabComponent(editorsPane, e, name, icon));
		else
			editorsPane.setTabComponentAt(editorsPane.indexOfComponent(e),
					new TabComponent(editorsPane, e, name, icon));
	}

	public static class TabComponent extends JPanel {
		private static final long serialVersionUID = 1L;

		public JLabel titleLbl;
		public JLabel closeButton;

		public TabComponent(JTabbedPane tabbedPane, Component panel, String title, Image icon) {
			setOpaque(false);
			setLayout(new BorderLayout());
			titleLbl = new JLabel(title);
			titleLbl.setIcon(new ImageIcon(icon.getScaledInstance(16, 16, 0)));
			titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			add(titleLbl, BorderLayout.WEST);
			closeButton = new JLabel();
			closeButton.setIcon(new ImageIcon(FileExplorerPopup.class.getResource("/net/netune/icons/Delete.png")));
			closeButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() != MouseEvent.BUTTON1)
						return;
					tabbedPane.remove(panel);
				}
			});
			add(closeButton, BorderLayout.EAST);
		}

	}
}
