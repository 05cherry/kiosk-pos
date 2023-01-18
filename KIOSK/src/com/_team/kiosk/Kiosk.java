package com._team.kiosk;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;

import javax.swing.*;

import com._team.DB.*;

public class Kiosk extends JFrame {
	private static final long serialVersionUID = 8649056310872915283L;
	public static ArrayList<Category> categorys;
	public static ArrayList<Customer> customers;
	public static ArrayList<OrderEach> orderEachs;
	public static ArrayList<Product> products;
	public static ArrayList<ReceiptNumber> receiptNumbers;
	public static Vector<Vector<String>> rowData;
	public static OrderMain orderMain;

	//상단의 "KIOSK" 레이블
	private JLabel kiosk;
	//카테고리별 메뉴 탭팬
	private JTabbedPane pane;
	//하단의 장바구니와 결제 레이블, 버튼 담을 패널
	public Bottom bottom;

	//레이블 가운데 정렬 메소드
	public static void setJLabelAlignmentCenter(JLabel jl) {
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jl.setVerticalAlignment(SwingConstants.CENTER);
	}

	//버튼 가운데 정렬 메소드
	public static void setJButtonAlignmentCenter(JButton jb) {
		jb.setHorizontalAlignment(SwingConstants.CENTER);
		jb.setVerticalAlignment(SwingConstants.CENTER);
	}

	public Kiosk() {
		// KIOSK에서 사용할 데이터를 DB에서 불러온다.
		updateDB();
		orderMain = new OrderMain();
		orderMain.setCode(Integer.parseInt(Main.dbc.getMaxNum("order_main")));
		orderMain.setCustomerCode(-1); // 연결된 회원이 없을 때 -1값으로 설정
		orderMain.setDateTime(new Timestamp(System.currentTimeMillis()));
		orderMain.setUsePoint(0);

		orderEachs = new ArrayList<OrderEach>();

		Container cp;
		cp = getContentPane();
		cp.setLayout(null);

		// 하단 패널 생성
		bottom = new Bottom();

		//"키오스크" 레이블
		kiosk = new JLabel("KIOSK");

		// 탭팬 생성
		pane = menuTabbedPane();

		// 컴포넌트 위치 설정
		kiosk.setBounds(281, 36, 100, 30);
		pane.setBounds(0, 101, 664, 805);
		bottom.setBounds(0, 906, 664, 266);

		cp.add(kiosk);
		cp.add(pane);
		cp.add(bottom);
		// 탭팬을 컨텐츠팬에 부착

		/**디자인 시작-------------------------------**/
		//전체 배경색
		cp.setBackground(Color.white);
		
		//키오스크 레이블
		kiosk.setFont(new Font("", Font.BOLD, 25));
		Kiosk.setJLabelAlignmentCenter(kiosk);
		/**디자인 끝----------------------------------**/
		
	}

	public void kioskUpdate() {
		// KIOSK에서 사용할 데이터를 DB에서 불러온다.
		updateDB();
		orderMain = new OrderMain();
		orderMain.setCode(Integer.parseInt(Main.dbc.getMaxNum("order_main")));
		orderMain.setCustomerCode(-1); // 연결된 회원이 없을 때 -1값으로 설정
		orderMain.setDateTime(new Timestamp(System.currentTimeMillis()));
		orderMain.setUsePoint(0);

		orderEachs = new ArrayList<OrderEach>();
		
		//bottom 초기화
		bottom.updateBottom();
	}

	public static void updateDB() {
		updateCategorys();
		updateCustomers();
		updateProducts();
		updateReceiptNumbers();
	}

	public static void updateCategorys() {
		categorys = Main.dbc.selectCategorys();
	}

	public static void updateCustomers() {
		customers = Main.dbc.selectCustomers();
	}

	public static void updateProducts() {
		products = Main.dbc.selectProducts();
	}

	public static void updateReceiptNumbers() {
		receiptNumbers = Main.dbc.selectReceiptNumbers();
	}

	// 메뉴 탭팬 생성하고 5개의 탭을 생성하여 부착
	private JTabbedPane menuTabbedPane() {
		// 탭팬 객체 생성
		JTabbedPane pane = new JTabbedPane();
		
		products = Main.dbc.selectProducts();

		//각 탭에 들어갈 카테고리들 ArrayList로 만들기
		ArrayList<Menu> coffeeMenus = new ArrayList<Menu>();
		ArrayList<Menu> coldbrewMenus = new ArrayList<Menu>();
		ArrayList<Menu> teaMenus = new ArrayList<Menu>();
		ArrayList<Menu> teavariationMenus = new ArrayList<Menu>();
		ArrayList<Menu> fruitteaMenus = new ArrayList<Menu>();

		for (int i = 0; i < products.size(); i++) {
			switch (products.get(i).getCategory()) {
			case 2000:
				coffeeMenus.add(new Menu(products.get(i)));
				break;
			case 2001:
				coldbrewMenus.add(new Menu(products.get(i)));
				break;
			case 2002:
				teaMenus.add(new Menu(products.get(i)));
				break;
			case 2003:
				teavariationMenus.add(new Menu(products.get(i)));
				break;
			case 2004:
				fruitteaMenus.add(new Menu(products.get(i)));
				break;
			default:
				System.out.println("카테고리가 없음");
				break;
			}
		}

		// coffee 탭
		pane.addTab("COFFEE", new MenuPanel(coffeeMenus));
		// cold brew 탭
		pane.addTab("COLD BREW", new MenuPanel(coldbrewMenus));
		// tea 탭
		pane.addTab("TEA", new MenuPanel(teaMenus));
		// tea variation 탭
		pane.addTab("TEA VARIATION", new MenuPanel(teavariationMenus));
		// fruit tea 탭
		pane.addTab("FRUIT TEA", new MenuPanel(fruitteaMenus));

		return pane;
	}

	// 메뉴 클래스에 들어갈 메뉴 버튼, 이름 레이블, 가격 레이블 정의
	class Menu extends JPanel {
		private static final long serialVersionUID = 8201621651029015325L;
		JButton btnMenu;
		JLabel menuName;
		JLabel menuPrice;

		// 메뉴 클래스의 생성자 정의
		Menu(Product product) {
			String menu = product.getName();
			Integer c_Price = product.getPrice();
			String imgSrc = product.getImgSrc();

			setLayout(null);
			setVisible(true);
			setSize(143, 220);

			ImageIcon menuImg = new ImageIcon(imgSrc);
			// ImageIcon 객체에서 Image 추출
			Image img = menuImg.getImage();
			// 추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
			Image updateImg = img.getScaledInstance(146, 143, Image.SCALE_FAST);
			// 새로운 Image 객체로 ImageIcon 객체 생성
			ImageIcon updateMenuImg = new ImageIcon(updateImg);

			menuName = new JLabel(menu);
			menuPrice = new JLabel(c_Price.toString());
			btnMenu = new JButton(updateMenuImg);

			// 컴포넌트 위치 설정
			menuName.setBounds(0, 159, 143, 13);
			menuPrice.setBounds(0, 187, 143, 13);
			btnMenu.setBounds(0, 0, 143, 140);
			menuName.setHorizontalAlignment(SwingConstants.CENTER);
			menuPrice.setHorizontalAlignment(SwingConstants.CENTER);

			add(menuName);
			add(menuPrice);
			add(btnMenu);

			// 패널 클릭시 KioskMenu로 가게 됨
			btnMenu.addActionListener((e) -> {
				new KioskMenu(product);
			});
			addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					new KioskMenu(product);
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			/**디자인 시작-------------------------------**/
			/**디자인 없음-------------------------------**/
			/**디자인 끝----------------------------------**/
		}
	}

	// 메뉴 패널
	class MenuPanel extends JPanel {
		private static final long serialVersionUID = -2558859561934912063L;

		MenuPanel(ArrayList<Menu> menus) {
			setLayout(new GridLayout(0, 3));
			setVisible(true);

			// 메뉴가 JFrame 위에 올라감
			for (Menu m : menus)
				add(m);
			
			/**디자인 시작-------------------------------**/
			/**디자인 없음-------------------------------**/
			/**디자인 끝----------------------------------**/
		}
	}

	// 하단 패널
	class Bottom extends JPanel {
		private static final long serialVersionUID = 6466491633733122284L;
		private JPanel orderListPanel;
		private JLabel countLabel;
		private JLabel chargeLabel;

		public Bottom() {
			setLayout(null);

			orderListPanel = new JPanel();
			countLabel = new JLabel();
			chargeLabel = new JLabel();
			JButton delete = new JButton("전체 삭제");
			JButton pay = new JButton("결제");

			orderListPanel.setLayout(null);
			orderListPanel.setBounds(24, 16, 428, 225);
			countLabel.setBounds(476, 20, 104, 18);
			chargeLabel.setBounds(476, 49, 116, 27);
			delete.setBounds(478, 101, 159, 34);
			pay.setBounds(478, 148, 159, 93);

			updateBottom();

			add(orderListPanel);
			add(countLabel);
			add(chargeLabel);
			add(delete);
			add(pay);

			// JButton 전체 삭제, 결제
			delete.addActionListener((e) -> {
				orderEachs.clear();
				updateBottom();
			});

			pay.addActionListener((e) -> {
				String count = countLabel.getText().replaceAll("[^0-9]", "");
				if (!(Integer.parseInt(count) == 0)) {
					new ChoicePanel();
				}
			});
			
			/**디자인 시작-------------------------------**/
			// 버튼 디자인
			delete.setBackground(Color.gray);
			delete.setForeground(Color.white);
			pay.setBackground(Color.red);
			pay.setForeground(Color.white);
			/**디자인 끝----------------------------------**/
		}

		public void updateBottom() {
			// countLabel, chargeLabel
			int count = 0;
			orderMain.setPayAmount(0);
			for (OrderEach oe : orderEachs) {
				count += oe.getEachNum();
				orderMain.setPayAmount(orderMain.getPayAmount() + oe.getEachPrice() * oe.getEachNum());
			}

			// 스크롤 구현
			JPanel scp = new JPanel();
			OrderListPanel olp = new OrderListPanel();
			// TODO width height 조절해줘야 함.
			olp.setPreferredSize(new Dimension(0, ((6 / 3)) * 366 + 46));
			scp.setBounds(0, 0, 428, 225);
			scp.setLayout(new BorderLayout());
			scp.add(new JScrollPane(olp));

			orderListPanel.removeAll();
			orderListPanel.add(scp);
			orderListPanel.updateUI();
			countLabel.setText("총 " + String.valueOf(count) + "개 결제");
			chargeLabel.setText(orderMain.getPayAmount() + "원");
			
			/**디자인 시작-------------------------------**/
			countLabel.setFont(new Font("", Font.PLAIN, 17));
			chargeLabel.setFont(new Font("", Font.BOLD, 17));
			/**디자인 끝----------------------------------**/
		}

		class OrderListPanel extends JPanel {
			private static final long serialVersionUID = -268767000696415466L;

			OrderListPanel() {
				setLayout(null);
				setBounds(0, 0, 428, 225);
				int i = 0;
				for (OrderEach oe : orderEachs) {
					BottomOrderEachPanel boep = new BottomOrderEachPanel(oe);
					boep.setBounds(9, (-29 + 47 * (++i)), 391, 45);
					boep.setBackground(Color.white);
					add(boep);
				}
				
				/**디자인 시작-------------------------------**/
				//배경색
				setBackground(Color.white);
				/**디자인 끝----------------------------------**/
			}

			class BottomOrderEachPanel extends JPanel {
				private static final long serialVersionUID = -5710388633442141112L;

				BottomOrderEachPanel(OrderEach oe) {
					setLayout(null);
					JLabel name, addOption, count, price;
					JButton btnAdd, btnSub, btnDel;
					
					// 메뉴 이름 찾아오기
					String menuName = "";
					for (Product p : products) {
						if (p.getCode() == oe.getProduct_code()) {
							menuName = p.getName();
							break;
						}
					}
					name = new JLabel(menuName);
					// 옵션 이름 찾기
					String option = "";
					if (oe.isSize())
						option += "Large";
					else
						option += "Regular";
					if (oe.isHotOrIce())
						option += "/ICE";
					else
						option += "/HOT";
					if (oe.isShot())
						option += " + 샷 추가";
					if (oe.isCream())
						option += " + 크림 추가";
					if (oe.isHazelSyrup())
						option += " + 헤이즐넛시럽 추가";
					if (oe.isAlmondSyrup())
						option += " + 아몬드시럽 추가";
					if (oe.isVanillaSyrup())
						option += " + 바닐라시럽 추가";
					addOption = new JLabel(option);
					count = new JLabel(String.valueOf(oe.getEachNum()));
					price = new JLabel(String.valueOf(oe.getEachPrice() * oe.getEachNum()));

					// JButton
					ImageIcon imgAdd = new ImageIcon("images\\btnAdd.png");
					ImageIcon imgSub = new ImageIcon("images\\btnSub.png");
					ImageIcon imgDel = new ImageIcon("images\\btnDel.png");
					imgAdd = new ImageIcon(imgAdd.getImage().getScaledInstance(22, 22, Image.SCALE_FAST));
					imgSub = new ImageIcon(imgSub.getImage().getScaledInstance(22, 22, Image.SCALE_FAST));
					imgDel = new ImageIcon(imgDel.getImage().getScaledInstance(20, 20, Image.SCALE_FAST));
					btnAdd = new JButton(imgAdd);
					btnSub = new JButton(imgSub);
					btnDel = new JButton(imgDel);

					btnAdd.addActionListener((e) -> {
						oe.setEachNum(oe.getEachNum() + 1);
						updateBottom();
					});
					btnSub.addActionListener((e) -> {
						if (oe.getEachNum() == 1) {
							orderEachs.remove(oe);
							updateBottom();
							return;
						}
						oe.setEachNum(oe.getEachNum() - 1);
						updateBottom();
					});
					btnDel.addActionListener((e) -> {
						orderEachs.remove(oe);
						updateBottom();
						return;
					});

					// 컴포넌트 배치
					name.setBounds(9, 9, 200, 13);
					addOption.setBounds(9, 24, 200, 21);
					count.setBounds(245, 16, 6, 10);
					price.setBounds(285, 15, 75, 13);
					btnAdd.setBounds(214, 10, 22, 22);
					btnSub.setBounds(260, 10, 22, 22);
					btnDel.setBounds(360, 12, 20, 20);

					add(name);
					add(addOption);
					add(count);
					add(price);
					add(btnAdd);
					add(btnSub);
					add(btnDel);
				}
			}
		}
	}
}