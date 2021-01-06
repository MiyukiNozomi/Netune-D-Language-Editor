package net.netune.editors.dlang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

import net.netune.Netune;
import net.netune.editors.TextLineNumber;

public class DEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextPane document;
	private JTextField detail;
	private TextLineNumber lineNumbers;
	private File file;

	public DEditor() {
		document = new JTextPane();
		document.setFont(new Font("Consolas", Font.PLAIN, 16));
		document.setCaretColor(Color.BLACK);
		document.setEditorKit(new net.netune.editors.dlang.DEditorKit());

		AbstractDocument d = ((AbstractDocument) document.getDocument());
		d.setDocumentFilter(new TabSpacingFilter());

		document.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent arg0) {
				try {
					int caretPos = document.getCaretPosition();
					int rowNum = (caretPos == 0) ? 1 : 0;
					for (int offset = caretPos; offset > 0;) {
						offset = Utilities.getRowStart(document, offset) - 1;
						rowNum++;
					}

					int offset = Utilities.getRowStart(document, caretPos);
					int colNum = caretPos - offset + 1;

					int totalLineCount = 1;
					
					Matcher m = Pattern.compile("\r\n|\r|\n").matcher(document.getText());
					
					while(m.find())
						totalLineCount++;
					
					detail.setText("Line " + rowNum + " of " + totalLineCount + " Col " + colNum);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		document.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN
				   && e.getKeyCode() != KeyEvent.VK_LEFT && e.getKeyCode() != KeyEvent.VK_RIGHT){
					Netune.setTitleAt(DEditor.this, "*" + file.getName());
				}
			}
		});
		
		lineNumbers = new TextLineNumber(document);
		lineNumbers.setBackground(Color.blue);
		lineNumbers.setForeground(Color.WHITE);
		
		setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(document);
		scroll.setRowHeaderView(lineNumbers);
		add(scroll, BorderLayout.CENTER);

		detail = new JTextField();
		detail.setEditable(false);
		add(detail, BorderLayout.SOUTH);
		
		doStrokes();
	}

	public void openFile(File f) {
		try {
			this.file = f;
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String total = "", line = "";

			while ((line = reader.readLine()) != null) {
				total += line + "\n";
			}

			reader.close();
			document.setText(total);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void saveFile() {
		try {
			if (file == null)
				return;
			Files.write(Paths.get(file.getPath()), document.getText().getBytes());
			detail.setText("Saved!");
			Netune.setTitleAt(DEditor.this, file.getName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public class TabSpacingFilter extends DocumentFilter {
		public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
			if ("\n".equals(str))
				str = addWhiteSpace(fb.getDocument(), offs);

			super.insertString(fb, offs, str, a);
		}

		public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
				throws BadLocationException {
			if ("\n".equals(str))
				str = addWhiteSpace(fb.getDocument(), offs);

			super.replace(fb, offs, length, str, a);
		}

		private String addWhiteSpace(Document doc, int offset) throws BadLocationException {
			StringBuilder whiteSpace = new StringBuilder("\n");
			Element root = doc.getDefaultRootElement();
			int line = root.getElementIndex(offset);
			int length = doc.getLength();

			for (int i = root.getElement(line).getStartOffset(); i < length; i++) {
				String temp = doc.getText(i, 1);

				if (temp.equals(" ") || temp.equals("\t")) {
					whiteSpace.append(temp);
				} else
					break;
			}

			return whiteSpace.toString();
		}
	}
	
	private void doStrokes(){

		KeyStroke save = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

		document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(save, "saveKeyStroke");
		document.getActionMap().put("saveKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (file != null)
					saveFile();
			}
		});
		
		KeyStroke autoFormat = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK + Event.SHIFT_MASK);

		document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(autoFormat, "autoFormatStroke");
		document.getActionMap().put("autoFormatStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String result = document.getText();
				
				//fixing the "){" to ") {"
				result = result.replace("){",") {");
				
				//fixing the while/if/for(
				result = result.replace("while(","while (");
				result = result.replace("if(","if (");
				result = result.replace("for(","for (");
				
				//keeping the { in the same line.
				result = result.replace("\n{"," {");
				
				//fixing the "}else{"
				result = result.replace("}else", "} else");
				result = result.replace("else{","else {");
				
				document.setText(result);
			}
		});
	}	
	
	public TextLineNumber getLineNumbers() {
		return lineNumbers;
	}

	public File getFile() {
		return file;
	}
}