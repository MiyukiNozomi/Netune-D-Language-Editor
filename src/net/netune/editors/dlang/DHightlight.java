package net.netune.editors.dlang;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class DHightlight extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	private Style defaultStyle,cwStyle,commentStyle,quotes;

	public DHightlight(Style defaultStyle,Style quotes,Style commentStyle,Style cwStyle) {
		this.defaultStyle = defaultStyle;
		this.cwStyle = cwStyle;
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
		final List<Word> list = processWords(text);

		setCharacterAttributes(0, text.length(), defaultStyle, true);
		for (Word word : list) {
			int p0 = word.position;
			setCharacterAttributes(p0, word.word.length(), cwStyle, true);
		}
		
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

	private List<Word> processWords(String content) {
		content += " ";
		List<Word> hiliteWords = new ArrayList<Word>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();

		for (int index = 0; index < data.length; index++) {
			char ch = data[index];
			if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')) {
				lastWhitespacePosition = index;
				if (word.length() > 0) {
					if (isReservedWord(word)) {
						hiliteWords.add(new Word(word,
								(lastWhitespacePosition - word.length())));
					}
					word = "";
				}
			} else {
				word += ch;
			}
		}
		return hiliteWords;
	}

	private boolean isReservedWord(String word) {
		return (word
				.matches("false|true|__FILE__|__FILE_FULL_PATH__|__MODULE__|__LINE__|__FUNCTION__|__PRETTY_FUNCTION__|__gshared|__traits|__vector|__parameters|import|this|null|synchronized|in|new|inout|version|super|out|template|throw|try|typeid|typeof|ubyte|ucent|uint|ulong|union|unittest|ushort|void|wchar|while|with|lazy|long|macro|mixin|module|nothrow|override|package|pragma|pure|real|ref|return|scope|shared|short|static|struct|switch|abstract|alias|private|protected|public|align|asm|assert|auto|body|bool|break|byte|case|cast|catch|cdouble|cent|cfloat|char|class|const|continue|creal|dchar|debug|default|delegate|delete|deprecated|do|double|else|enum|export|extern|final|finally|float|for|foreach|foreach_reverse|function|goto|idouble|if|ifloat|immutable|int|interface|invariant|ireal|is"));
	}

	private class Word {

		public int position;
		public String word;

		public Word(String word, int position) {
			this.position = position;
			this.word = word;
		}
	}
}
