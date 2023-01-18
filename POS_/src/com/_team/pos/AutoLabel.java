package com._team.pos;

import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class AutoLabel extends JLabel {
	private static final long serialVersionUID = -6916227294535288969L;
	public AutoLabel(String text) {
		setText(text);
        setHorizontalAlignment(SwingConstants.CENTER);
        addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			@Override
			public void componentResized(ComponentEvent e) {
                while(true) {
                    Font before = getFont();
                    Font after = new Font(before.getName(), before.getStyle(), before.getSize()+1);
                    setFont(after);
                    if(getPreferredSize().getWidth()>getWidth() || getPreferredSize().getHeight()>getHeight()) {
                        setFont(before);
                        break;
                    }//end if
                }//end while
			}//end componentResized
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
    }//end main
 
}
