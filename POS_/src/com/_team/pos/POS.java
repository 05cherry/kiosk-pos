package com._team.pos;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import com._team.DB.*;

public class POS extends JFrame {
	private static final long serialVersionUID = 2244705917575222603L;
	public static ArrayList<Cash> cashs;
	public static ArrayList<Category> categorys;
	public static ArrayList<Customer> customers;
	public static ArrayList<Material> materials;
	public static ArrayList<OrderEach> orderEachs;
	public static ArrayList<OrderMain> orderMains;
	public static ArrayList<Product> products;
	public static ArrayList<ReceiptNumber> receiptNumbers;
	public static Vector<Vector<String>> rowData;
	
	public JPanel leftPanel, rightPanel;
	public OrderStockPanel orderStockPanel;
	public ClientPanel clientPanel;
	public CalculatorPanel calculatorPanel;
	public StatPanel statPanel;
	POS() {
		//init
		updateDB();
		Container ct = getContentPane();
		ct.setLayout(null);
		// leftPanel
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 251, 704);
		leftPanel.setLayout(null);
		
		JButton btnHome, btnOrderStock, btnClient, btnCalculator, btnStat;
		btnHome = new JButton("POS");
		btnOrderStock = new JButton("주문/메뉴");
		btnClient = new JButton("고객관리");
		btnCalculator = new JButton("정산");
		btnStat = new JButton("통계");

		btnHome.setBounds(0, 0, 251, 112);
		btnOrderStock.setBounds(0, 112, 251, 66);
		btnClient.setBounds(0, 178, 251, 66);
		
		btnCalculator.setBounds(0, 244, 251, 66);
		btnStat.setBounds(0, 310, 251, 66);

		btnHome.addActionListener((e) -> {
			setRightPanel(orderStockPanel);
		});
		btnOrderStock.addActionListener((e) -> {
			setRightPanel(orderStockPanel);
		});
		btnClient.addActionListener((e) -> {
			setRightPanel(clientPanel);
		});
		btnCalculator.addActionListener((e) -> {
			setRightPanel(calculatorPanel);
		});
		btnStat.addActionListener((e) -> {
			setRightPanel(statPanel);
		});

		leftPanel.add(btnHome);
		leftPanel.add(btnOrderStock);
		leftPanel.add(btnClient);
		leftPanel.add(btnCalculator);
		leftPanel.add(btnStat);

		// rightPanel
		rightPanel = getDefaultRightPanel();
		rightPanel.setBounds(251, 0, 949, 704);

		// orderStockPanel
		orderStockPanel = new OrderStockPanel();

		// clientPanel
		clientPanel = new ClientPanel();

		// calculatorPanel
		calculatorPanel = new CalculatorPanel();

		// StatPanel
		statPanel = new StatPanel();

		
		setRightPanel(orderStockPanel);
		ct.add(leftPanel);
		ct.add(rightPanel);
		
		/**디자인 시작-------------------------------**/
		//포스 왼쪽 패널 배경색 지정
		leftPanel.setBackground(Color.white);
		
		//배경색, 폰트색 정의
		Color backgroundColor = new Color(153,153,153);
		Color foregroundColor = new Color(255,255,255);
		
		//POS, 주문/메뉴, 고객관리, 정산, 통계 버튼 배경색, 폰트색 지정
		btnHome.setBackground(backgroundColor);
		btnOrderStock.setBackground(backgroundColor);
		btnClient.setBackground(backgroundColor);
		btnCalculator.setBackground(backgroundColor);
		btnStat.setBackground(backgroundColor);
		btnHome.setForeground(foregroundColor);
		btnOrderStock.setForeground(foregroundColor);
		btnClient.setForeground(foregroundColor);
		btnCalculator.setForeground(foregroundColor);
		btnStat.setForeground(foregroundColor);
		
		//POS, 주문/메뉴, 고객관리, 정산, 통계 버튼   폰트 지정
		btnHome.setFont(new Font("", Font.BOLD, 20));
		btnOrderStock.setFont(new Font("", Font.BOLD, 20));
		btnClient.setFont(new Font("", Font.BOLD, 20));
		btnCalculator.setFont(new Font("", Font.BOLD, 20));
		btnStat.setFont(new Font("", Font.BOLD, 20));
		/**디자인 끝----------------------------------**/
	}
	
	public static JButton buttonSetColor(JButton jb) {
		jb.setBackground(new Color(051,051,051));
		jb.setForeground(new Color(255,255,255));
		return jb;
	}

	static public JPanel getDefaultRightPanel() {
		JPanel defaultRightPanel = new JPanel();
		defaultRightPanel.setBounds(0, 0, 949, 704);
		defaultRightPanel.setLayout(null);
		defaultRightPanel.setBackground(Color.WHITE);
		return defaultRightPanel;
	}

	private void setRightPanel(JPanel jp) {
		rightPanel.removeAll();
		rightPanel.add(jp);
		rightPanel.updateUI();
	}
	
	public static void updateDB() {
		updateCashs();
		updateCategorys();
		updateCustomers();
		updateMaterials();
		updateOrderEachs();
		updateOrderMains();
		updateProducts();
		updateReceiptNumbers();
	}
	public static void updateCashs() {
		cashs = Main.dbc.selectCashs();
	}
	public static void updateCategorys() {
		categorys = Main.dbc.selectCategorys();
	}
	public static void updateCustomers() {
		customers = Main.dbc.selectCustomers();
	}
	public static void updateMaterials() {
		materials = Main.dbc.selectMaterials();
	}
	public static void updateOrderEachs() {
		orderEachs = Main.dbc.selectOrderEachs();
	}
	public static void updateOrderMains() {
		orderMains = Main.dbc.selectOrderMains();
	}
	public static void updateProducts() {
		products = Main.dbc.selectProducts();
	}
	public static void updateReceiptNumbers() {
		receiptNumbers = Main.dbc.selectReceiptNumbers();
	}
}
