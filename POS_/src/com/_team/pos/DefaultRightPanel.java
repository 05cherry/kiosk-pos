package com._team.pos;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class DefaultRightPanel extends JPanel {
	private static final long serialVersionUID = 7513815899393242911L;
	JPanel topPanel, centerPanel, bottomPanel;

	public DefaultRightPanel() {
		setBounds(0, 0, 949, 704);
		setLayout(null);
		setBackground(Color.WHITE);
	}

	public void setPanel() {
		topPanel = new JPanel();
		centerPanel = new JPanel();
		bottomPanel = new JPanel();
		topPanel.setLayout(null);
		centerPanel.setLayout(null);
		bottomPanel.setLayout(null);
		topPanel.setBounds(0, 0, 949, 112);
		centerPanel.setBounds(0, 112, 949, 499);
		bottomPanel.setBounds(0, 611, 949, 93);
		
		/**디자인 시작-------------------------------**/
		topPanel.setBackground(new Color(255,255,255));
		centerPanel.setBackground(new Color(222,222,222));
		bottomPanel.setBackground(new Color(255,255,255));
		/**디자인 끝----------------------------------**/
	}

	public static void setFonts(Font f,Component ... cmp) {
		for (int i = 0; i < cmp.length; i++) {
			cmp[i].setFont(f);
		}
	}
	
	public static void addComponents(JPanel jp, Component ... comp) {
		for (int i = 0; i < comp.length; i++) {
			jp.add(comp[i]);
		}
	}
	public static void addComponents(JPanel jp, ArrayList<Component> comp) {
		for (int i = 0; i < comp.size(); i++) {
			jp.add(comp.get(i));
		}
	}
	public static void addJPanels(JPanel jp, ArrayList<JPanel> jps) {
		for (int i = 0; i < jps.size(); i++) {
			jp.add(jps.get(i));
		}
	}
}