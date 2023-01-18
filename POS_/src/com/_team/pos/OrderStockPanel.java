package com._team.pos;

import java.awt.*;
import javax.swing.*;

public class OrderStockPanel extends DefaultRightPanel {
	private static final long serialVersionUID = -2992446941735671847L;
	private JPanel topCenterPanel;
	private JPanel scp;
	public OrderPanel orderPanel;
	public MenuPanel menuPanel;
	public StockPanel stockPanel;

	OrderStockPanel() {
		// setPanel
		setPanel();

		// cardLayout
		topCenterPanel = new JPanel();
		topCenterPanel.setLayout(new CardLayout());
		topCenterPanel.setSize(949, 611);

		// topCenterPanel
		orderPanel = new OrderPanel();
		scp = new JPanel();
		scp.setBounds(0, 0, 949, 499);
		// orderPanel.setBounds(0, 0, 949, 611);
		menuPanel = new MenuPanel();
		menuPanel.setBounds(0, 0, 949, 611);
		stockPanel = new StockPanel();
		stockPanel.setBounds(0, 0, 949, 611);

		topPanel.setVisible(false);
		scp.setLayout(new BorderLayout());
		scp.add( new JScrollPane(orderPanel));
		
		// bottomPanel
		RoundedButton btnOrder, btnMenu, btnStock; 
		btnOrder = new RoundedButton("주문");
		btnMenu = new RoundedButton("메뉴");
		btnStock = new RoundedButton("재고");
		btnOrder.setBounds(673, 25, 78, 50);
		btnMenu.setBounds(763, 25, 78, 50);
		btnStock.setBounds(854, 25, 78, 50);
		
		addComponents(bottomPanel, btnOrder, btnMenu, btnStock);
		addComponents(this, topCenterPanel, bottomPanel);
		
		//cardLayoput
		topCenterPanel.add(scp, "Order");
		topCenterPanel.add(stockPanel, "Stock");
		topCenterPanel.add(menuPanel, "Menu");
		((CardLayout) topCenterPanel.getLayout()).show(topCenterPanel, "Order");
		// event
		btnOrder.addActionListener((e) -> {
			((CardLayout) topCenterPanel.getLayout()).show(topCenterPanel, "Order");
		});
		btnStock.addActionListener((e) -> {
			((CardLayout) topCenterPanel.getLayout()).show(topCenterPanel, "Stock");
		});
		btnMenu.addActionListener((e) -> {
			((CardLayout) topCenterPanel.getLayout()).show(topCenterPanel, "Menu");
		});
	}
}


