package net.netune.editors.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class JSONHightlight extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	private Style defaultStyle,commentStyle,quotes;

	public JSONHightlight(Style defaultStyle,Style quotes,Style commentStyle) {
		this.defaultStyle = defaultStyle;
		this.commentStyle = commentStyle;
		this.quotes = quotes;
	}

	public void insertString(int offset, String str, AttributeSet a)
			throws BadLocationException {
		super.insertString(offset, str, a);
		refresh();
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		refresh();
	}
	
	private void refresh() throws BadLocationException {
		String text = getText(0, getLength());
		
		setCharacterAttributes(0, getLength(), defaultStyle,true);
		
		Pattern pattern = Pattern.compile("\\/\\/.*");
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			setCharacterAttributes(matcher.start(), matcher.end()
					- matcher.start(), commentStyle, false);
		}
		
		pattern = Pattern.compile("\\/\\*.*?\\*\\/", Pattern.DOTALL);
		matcher = pattern.matcher(text);

		while (matcher.find()) {
			setCharacterAttributes(matcher.start(), matcher.end()
					- matcher.start(), commentStyle, false);
		}
		
		pattern = Pattern.compile("\"([^\"]*)\"");
		matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			setCharacterAttributes(matcher.start(), matcher.end()
					- matcher.start(), quotes, false);
		}
	}
}
