package net.netune.editors.dlang;

import java.awt.Color;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class DEditorKit extends StyledEditorKit {
	private static final long serialVersionUID = 1L;

	private final int TAB_SIZE = 36;
	
	public DEditorKit() {
		
	}
	
	@Override
	public ViewFactory getViewFactory() {
		return new EditorViewFactory();
	}
	
	@Override
	public String getContentType() {
		return "text/xml";
	}

	@Override
	public Document createDefaultDocument() {
		StyleContext styleContext = new StyleContext();
		Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);

		Style cwStyle = styleContext.addStyle("ConstantWidth", null);
		Style comment = styleContext.addStyle("Comment", null);
		Style quotes = styleContext.addStyle("Quotes", null);
		
		StyleConstants.setForeground(cwStyle, Color.BLUE);
		StyleConstants.setForeground(comment, Color.GRAY);
		StyleConstants.setForeground(quotes, new Color(0,100,0));
		
		return new DHightlight(defaultStyle, quotes, comment, cwStyle);
	}

	public class EditorParagraphView extends ParagraphView {

		public EditorParagraphView(Element elem) {
			super(elem);
		}

		public float nextTabStop(float x, int tabOffset) {
			TabSet tabs = getTabSet();
			
			if (tabs == null) {
				return (float) (getTabBase() + (((int) x / TAB_SIZE + 1) * TAB_SIZE));
			}

			return super.nextTabStop(x, tabOffset);
		}
	}
	
	public class EditorViewFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new EditorParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            return new LabelView(elem);
        }
    }
}
