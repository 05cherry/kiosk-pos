package com._team.pos;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import com._team.DB.*;

public class OrderPanel extends JPanel {
	private static final long serialVersionUID = 7035637818073704321L;
	private ArrayList<JPanel> miniOrderPanels;
	private int height;

	OrderPanel() {
		miniOrderPanels = new ArrayList<JPanel>();
		height = 499;
//		updateOrderPanel();
		setLayout(null);
	}

	public void updateOrderPanel() {
		// receiptNumbers 업데이트
		POS.updateReceiptNumbers();

		// 주문을 받아와 주문 갯수만큼 miniOrderPanels 생성
		miniOrderPanels.clear();
		for (int i = 0; i < POS.receiptNumbers.size(); i++) {
			miniOrderPanels.add(new MiniOrderPanel(POS.receiptNumbers.get(i)));
			miniOrderPanels.get(i).setBounds(((i % 3) + 1) * 288 - 248, ((i / 3) + 1) * 366 - 320, 226, 339);
		}

		int panelHeight = ((((POS.receiptNumbers.size() - 1) / 3) + 1) * 366 + 30);
		if (height < panelHeight)
			height = panelHeight;
		setPreferredSize(new Dimension(0, height));

		this.removeAll();
		DefaultRightPanel.addJPanels(this, miniOrderPanels);
		this.updateUI();
	}
	class MiniOrderPanel extends JPanel {
		private static final long serialVersionUID = 7409050644008111866L;
		JPanel titlePanel, menuPanel, orderInfo;
		JLabel orderNumLabel, orderTimeLabel, orderPackingLabel, orderClientPhoneNumberLabel;
		private String orderNum, orderTime, orderClientPhoneNumber;
		private OrderMain orderMain = null;
		private Customer customer = null;

		MiniOrderPanel(ReceiptNumber receiptNumber) {

			// MiniOrderPanel 클릭 리스너
			JLabel clickListenerLabel = new JLabel();
			clickListenerLabel.setBounds(0, 0, 226, 339);
			add(clickListenerLabel);

			// 주문 찾기
			for (OrderMain om : POS.orderMains) {
				if (om.getCode() == receiptNumber.getOrderCode()) {
					orderMain = om;
					break;
				}
			}
			// 회원 찾기
			for (Customer c : POS.customers) {
				if (c.getCode() == orderMain.getCustomerCode()) {
					customer = c;
					break;
				}
			}

			setLayout(null);

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			orderNum = "#" + receiptNumber.getReceiptNumber();
			orderTime = formatter.format(orderMain.getDateTime());
			orderClientPhoneNumber = customer.getCode() == -1 ? "" : customer.getPhone();

			// titlePanel
			titlePanel = new JPanel();
			titlePanel.setLayout(null);
			titlePanel.setBounds(0, 0, 226, 44);

			JPanel j = new JPanel();
			j.setBounds(0, 0, 226, 44);
			j.setOpaque(false);
			add(j);

			orderNumLabel = new JLabel(orderNum);
			orderTimeLabel = new JLabel(orderTime);
			orderNumLabel.setBounds(22, 11, 70, 20);
			orderTimeLabel.setBounds(99, 11, 56, 20);
			DefaultRightPanel.addComponents(titlePanel, orderNumLabel, orderTimeLabel);

			// menuPanel
			JPanel menuPanel = new JPanel();
			menuPanel.setBounds(0, 44, 226, 246);
			menuPanel.setLayout(null);
			menuPanel.setBackground(new Color(222, 222, 222));

			ArrayList<JPanel> miniMenuPanels = new ArrayList<JPanel>();
			// MiniMenuPanel들 만들기
			int lastY = 0;
			int index = 0;
			for (OrderEach oe : POS.orderEachs) {
				// orderEachs 찾기
				if (oe.getOrder_main_code() == orderMain.getCode()) {
					// MiniMenuPanel들 정보 찾기
					String menuName = "";
					String menuSize = "";
					String[] addOption;
					int menuCount;

					// menuName
					for (Product p : POS.products) {
						if (p.getCode() == oe.getProduct_code()) {
							menuName = p.getName();
							break;
						}
					}

					// menusize
					menuSize = oe.isSize() ? "Large" : "Regular";
					// addOption
					// option Count
					int optionCount = 1;
					if (oe.isAlmondSyrup())
						optionCount++;
					if (oe.isCream())
						optionCount++;
					if (oe.isHazelSyrup())
						optionCount++;
					if (oe.isShot())
						optionCount++;
					if (oe.isVanillaSyrup())
						optionCount++;
					addOption = new String[optionCount];
					optionCount = 0;
					if (oe.isHotOrIce())
						addOption[optionCount++] = "ICE";
					else
						addOption[optionCount++] = "HOT";
					;
					if (oe.isAlmondSyrup())
						addOption[optionCount++] = "아몬드 시럽";
					if (oe.isCream())
						addOption[optionCount++] = "휘핑크림";
					if (oe.isHazelSyrup())
						addOption[optionCount++] = "헤이즐넛 시럽";
					if (oe.isShot())
						addOption[optionCount++] = "샷 추가";
					if (oe.isVanillaSyrup())
						addOption[optionCount++] = "바닐라 시럽";

					// menuCount
					menuCount = oe.getEachNum();

					// create MiniMenuPanel
					miniMenuPanels.add(new MiniMenuPanel(menuName, menuSize, addOption, menuCount));

					// miniMenuPanel 위치 설정
					int height = 100 + 6 * (addOption.length);
					miniMenuPanels.get(index).setBounds(0, lastY, 226, height);
					lastY += height;
					index++;
				}
			}

			DefaultRightPanel.addJPanels(menuPanel, miniMenuPanels);

			// orderInfoPanel
			orderInfo = new JPanel();
			orderInfo.setLayout(null);
			orderInfo.setBounds(0, 289, 226, 50);

			orderPackingLabel = new JLabel(orderMain.isTakeOut() ? "포장" : "매장");
			orderClientPhoneNumberLabel = new JLabel(orderClientPhoneNumber);
			orderPackingLabel.setBounds(22, 15, 41, 20);
			orderClientPhoneNumberLabel.setBounds(76, 19, 136, 16);
			orderPackingLabel.setFont(new Font("", Font.BOLD, 20));
			orderClientPhoneNumberLabel.setFont(new Font("", Font.PLAIN, 16));

			DefaultRightPanel.addComponents(orderInfo, orderPackingLabel, orderClientPhoneNumberLabel);

			// this(OrderPanel)
			DefaultRightPanel.addComponents(this, titlePanel, menuPanel, orderInfo);

			// MiniOrderPanel 클릭 리스너
			clickListenerLabel.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					new MiddleMenuPanel(receiptNumber, customer, orderMain);
				}
			});
			/**디자인 시작-------------------------------**/
			//#1 12:10 뜨는 창 배경색 지정
			titlePanel.setBackground(Color.LIGHT_GRAY);
			
			//레이블 폰트 지정
			DefaultRightPanel.setFonts(new Font("", Font.PLAIN, 20), orderNumLabel, orderTimeLabel);
			
			//주문 받은 정보 창 배경색 지정
			orderInfo.setBackground(Color.LIGHT_GRAY);
			/**디자인 끝----------------------------------**/
		}
	}

	class MiniMenuPanel extends JPanel {
		private static final long serialVersionUID = -7009886099979139897L;
		JLabel menuNameLabel, menuSizePanel, menuCountLabel;
		ArrayList<Component> addOptionPanel;
		String menuName, menuSize, addOption[];
		int menuCount;

		MiniMenuPanel(String menuName, String menuSize, String addOption[], int menuCount) {
			// 메뉴 정보 입력
			this.menuName = menuName;
			this.menuSize = menuSize;
			this.addOption = addOption;
			this.menuCount = menuCount;

			setLayout(null);

			menuNameLabel = new JLabel(menuName);
			menuSizePanel = new JLabel(menuSize);
			menuCountLabel = new JLabel(String.valueOf(menuCount));

			// addOption
			int i = 0;
			if (addOption.length != 0) {
				addOptionPanel = new ArrayList<Component>();
				for (i = 0; i < addOption.length; i++) {
					addOptionPanel.add(new JLabel(addOption[i]));
					addOptionPanel.get(i).setBounds(42, 63 + 26 * i, 184, 16);
					addOptionPanel.get(i).setFont(new Font("", Font.PLAIN, 12));
				}
			}

			menuNameLabel.setBounds(22, 10, 121, 20);
			menuSizePanel.setBounds(40, 37, 186, 18);
			menuCountLabel.setBounds(143, 10, 83, 20);

			DefaultRightPanel.addComponents(this, addOptionPanel);
			DefaultRightPanel.addComponents(this, menuNameLabel, menuSizePanel, menuCountLabel);
			
			/**디자인 시작-------------------------------**/
			//주문내역 레이블 폰트 지정
			menuSizePanel.setFont(new Font("", Font.PLAIN, 12));
			DefaultRightPanel.setFonts(new Font("", Font.BOLD, 15), menuNameLabel, menuCountLabel);
			
			//배경색 지정
			setBackground(new Color(222, 222, 222));
			/**디자인 끝----------------------------------**/
		}
	}

	class MiddleMenuPanel extends JFrame {
		private static final long serialVersionUID = 2441257836333258380L;
		ArrayList<OrderEach> orderEachs;

		public MiddleMenuPanel(ReceiptNumber receiptNumber, Customer customer, OrderMain orderMain) {
			Container cp = getContentPane();
			setTitle("주문 내역 확인");
			cp.setLayout(null);
			setBounds(268, 30, 664, 688);
			setVisible(true);

			// JPanel 생성하기
			// top
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			String orderTime = formatter.format(orderMain.getDateTime());
			JLabel titleLabel = new AutoLabel("주문 내역 확인");
			JLabel menuInfoLabel = new AutoLabel(
					"#" + receiptNumber.getReceiptNumber() + "   " + orderTime + "	   포장   " + customer.getPhone());

			titleLabel.setBounds(0, 46, 664, 25);
			menuInfoLabel.setBounds(0, 87, 664, 25);

			add(titleLabel);
			add(menuInfoLabel);
			// center
			orderEachs = new ArrayList<OrderEach>();
			for (OrderEach oe : POS.orderEachs) {
				if (oe.getOrder_main_code() == orderMain.getCode())
					orderEachs.add(oe);
			}
			JPanel centerPane = new CenterPane();
			centerPane.setBounds(0, 120, 664, 450);
			cp.add(centerPane);
			//-----------------------------------------------------

			// bottom
			RoundedButton btnCancel = new RoundedButton("주문 취소");
			RoundedButton btnComplete = new RoundedButton("전달 완료");

			btnCancel.setBounds(31, 580, 293, 60);
			btnComplete.setBounds(339, 580, 293, 60);

			add(btnCancel);
			add(btnComplete);

			btnCancel.addActionListener((e) -> {
				Main.dbc.stateReceiptNumber = Main.dbc.DELETE;
				Main.dbc.stateOrderEachs = Main.dbc.DELETE;
				Main.dbc.stateOrderMain = Main.dbc.DELETE;
				Main.dbc.receiptNumber = receiptNumber;
				Main.dbc.orderEachs = orderEachs;
				Main.dbc.orderMain = orderMain;
				this.dispose();
			});
			btnComplete.addActionListener((e) -> {
				// customer 사용한 포인트만큼 차감 또는 추가
				if (orderMain.getCustomerCode() != -1) {
					int savingPoint = customer.getPoint() + (int) (orderMain.getPayAmount() * 0.1) - orderMain.getUsePoint();
					Main.dbc.customer = new Customer(customer.getCode(), customer.getPhone(), savingPoint);
					Main.dbc.stateCustomer = Main.dbc.UPDATE;
				}
				// cash 현금결제일시 잔고에 현금추가
				if (orderMain.getPayType().equals("현금")) {
					int currentCash = POS.cashs.get(0).getCurrentCash() + orderMain.getPayAmount() - orderMain.getUsePoint();
					Main.dbc.cash = new Cash(1234,currentCash);
					Main.dbc.stateCash = Main.dbc.UPDATE;
				}
				// TODO stat패널 업데이트
				Main.dbc.receiptNumber = receiptNumber;
				Main.dbc.stateReceiptNumber = Main.dbc.DELETE;
//				Main.win.statPanel.updateTime();
				this.dispose();
			});

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			/**디자인 시작-------------------------------**/
			//전체 배경색 지정
			cp.setBackground(new Color(244, 244, 244));
			
			//상단의 "주문 내역 확인", "#1 12:10 ..." 레이블 정렬 지정
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			menuInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
			/**디자인 끝----------------------------------**/
		}

		class CenterPane extends JPanel {
			private static final long serialVersionUID = -7304465484556432560L;
			JLabel menu, customMenu, count, price, customPrice;

			public CenterPane() {
				setLayout(null);
				setVisible(true);
				setSize(664, 526);
				// TODO TEST용 높이이므로 나중에 관려된 높이 모두 수정할 것
				setSize(664, 326);
				setBackground(new Color(210, 210, 210));

				JLabel titleOrder, titleCount, titlePrice;
				JPanel menuOrderPane, menuOrderScroll, menuOrderTable;
				titleOrder = new JLabel("주문 내역");
				titleCount = new JLabel("수량");
				titlePrice = new JLabel("금액");
				menuOrderTable = new JPanel();
				menuOrderScroll = new JPanel();
				menuOrderPane = new JPanel();
				titleOrder.setBounds(0, 0, 132, 44);
				titleCount.setBounds(350, 0, 98, 44);
				titlePrice.setBounds(495, 0, 98, 44);

				// 레이아웃 설정
				menuOrderTable.setLayout(null);
				menuOrderPane.setLayout(null);
				menuOrderScroll.setLayout(new BorderLayout());

				// menuOrderTable에 들어갈 menuOrderEachPans 설정 및 tableHeight 설정
				int tableHeight = 0;
				ArrayList<MenuOrderEachPane> menuOrderEachPans = new ArrayList<MenuOrderEachPane>();
				for (int i = 0; i < orderEachs.size(); i++) {
					MenuOrderEachPane moep = new MenuOrderEachPane(orderEachs.get(i));
					moep.setBackground(new Color(222, 222, 222));
					moep.setBounds(0, tableHeight, 664, moep.getHeight());

					menuOrderEachPans.add(moep);
					menuOrderTable.add(menuOrderEachPans.get(i));
					tableHeight += moep.getHeight();
				}
				if (tableHeight < 450)
					tableHeight = 450;

				// menuOrderPane 크기 설정
				menuOrderPane.setBounds(0, 45, 647, 405);
				menuOrderScroll.setBounds(0, 0, 647, 405);
				menuOrderTable.setPreferredSize(new Dimension(0, tableHeight));

				// 컴포넌트 추가
				menuOrderScroll.add(new JScrollPane(new JScrollPane(menuOrderTable)));
				menuOrderPane.add(menuOrderScroll);

				add(titleOrder);
				add(titleCount);
				add(titlePrice);
				add(menuOrderPane);
				
				/**디자인 시작-------------------------------**/
				//"주문 내역", "수량", "금액" 레이블 폰트, 정렬 지정
				titleOrder.setFont(new Font("",Font.BOLD,15));
				titleCount.setFont(new Font("",Font.BOLD,15));
				titlePrice.setFont(new Font("",Font.BOLD,15));
				titleOrder.setHorizontalAlignment(SwingConstants.CENTER);
				titleCount.setHorizontalAlignment(SwingConstants.CENTER);
				titlePrice.setHorizontalAlignment(SwingConstants.CENTER);
				
				//주문 테이블 배경색 지정
				menuOrderTable.setBackground(new Color(222, 222, 222));
				/**디자인 끝----------------------------------**/
			}

			class MenuOrderEachPane extends JPanel {
				private static final long serialVersionUID = -3406558597254434283L;
				private int height;

				MenuOrderEachPane(OrderEach orderEach) {
					setLayout(null);

					// top
					JLabel menuName, menuCount, menuPrice;
					menuName = new JLabel();
					menuCount = new JLabel();
					menuPrice = new JLabel();

					for (Product p : POS.products) {
						if (orderEach.getProduct_code() == p.getCode()) {
							menuName.setText(p.getName());
							break;
						}
					}
					menuCount.setText(String.valueOf(orderEach.getEachNum()));
					menuPrice.setText(String.valueOf(orderEach.getEachPrice() * orderEach.getEachNum()));

					menuName.setBounds(23, 20, 200, 20);
					menuCount.setBounds(380, 20, 37, 20);
					menuPrice.setBounds(450, 20, 112, 20);
					menuName.setHorizontalAlignment(SwingConstants.LEFT);
					menuCount.setHorizontalAlignment(SwingConstants.CENTER);
					menuPrice.setHorizontalAlignment(SwingConstants.RIGHT);

					add(menuName);
					add(menuCount);
					add(menuPrice);

					// size
					JLabel sizeTitle, size, sizePrice;
					sizeTitle = new JLabel("사이즈");
					size = new JLabel(orderEach.isSize() ? "Regular" : "Large");
					sizePrice = new JLabel(orderEach.isSize() ? "+500원" : "+0원");

					sizeTitle.setBounds(74, 58, 100, 16);
					size.setBounds(84, 88, 100, 16);
					sizePrice.setBounds(536, 88, 200, 16);

					add(sizeTitle);
					add(size);
					add(sizePrice);

					// hotOrIce
					JLabel hotOrIceTitle, hotOrIce, hotOrIcePrice;
					hotOrIceTitle = new JLabel("HOT OR ICE");
					hotOrIce = new JLabel(orderEach.isHotOrIce() ? "HOT" : "ICE");
					hotOrIcePrice = new JLabel(orderEach.isHotOrIce() ? "+500원" : "+0원");

					hotOrIceTitle.setBounds(74, 122, 100, 16);
					hotOrIce.setBounds(84, 152, 100, 16);
					hotOrIcePrice.setBounds(536, 152, 200, 16);

					add(hotOrIceTitle);
					add(hotOrIce);
					add(hotOrIcePrice);

					// add
					JLabel addTitle, add, addPrice;
					addTitle = new JLabel("추가");
					add = new JLabel("");
					if (orderEach.isShot())
						add.setText("샷 추가");
					if (orderEach.isCream())
						add.setText("크림 추가");
					if (orderEach.isHazelSyrup())
						add.setText("헤이즐넛시럽 추가");
					if (orderEach.isAlmondSyrup())
						add.setText("아몬드시럽 추가");
					if (orderEach.isVanillaSyrup())
						add.setText("바닐라시럽 추가");
					addPrice = new JLabel(!add.getText().equals("") ? "+500원" : "+0원");

					addTitle.setBounds(74, 184, 100, 16);
					add.setBounds(84, 214, 200, 16);
					addPrice.setBounds(536, 214, 200, 16);

					// add의 추가 여부와 height 설정
					if (add.getText().equals("")) {
						height = 188;
					} else {
						height = 240;
						add(addTitle);
						add(add);
						add(addPrice);
					}
				}

				public int getHeight() {
					return height;
				}
			}

		}

	}

}