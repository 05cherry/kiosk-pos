package com._team.kiosk;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com._team.DB.OrderEach;
import com._team.DB.Product;

public class ChangeOrderCheckPage extends JFrame {
	private static final long serialVersionUID = 2441257836333258380L;
	Container cp;
	
	public ChangeOrderCheckPage() {
		cp = getContentPane();
		cp.setLayout(null);

		// JPanel 생성하기
		JPanel topPane = new TopPane();
		JPanel centerPane = new CenterPane();
		JPanel bottomPane = new BottomPane();
		topPane.setBounds(0, 0, 664, 120);
		centerPane.setBounds(0, 120, 664, 526);
		bottomPane.setBounds(0, 646, 664, 526);
		
		// JPanel들을 컨테이너 위에 올리기
		cp.add(topPane);
		cp.add(centerPane);
		cp.add(bottomPane);
		
		setSize(664, 1172);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("주문 내역 확인");
	}

	class TopPane extends JPanel {
		private static final long serialVersionUID = 2506804283786039768L;

		// 주문 내역 확인 JLabel 넣을 JPanel
		public TopPane() {
			setLayout(null);
			
			JLabel title = new JLabel("주문 내역 확인");
			title.setBounds(200, 50, 200, 50);

			// JPanel 위에 올리기
			add(title);
			
			/**디자인 시작-------------------------------**/
			//주문 내역 확인 패널 배경색 지정
			setBackground(Color.white);

			//주문 내역 확인 레이블 배경색 지정
			title.setOpaque(true);
			title.setBackground(Color.white);
			
			//주문 내역 확인 레이블 폰트, 가운데 정렬 지정
			title.setFont(new Font("",Font.BOLD,25));
			Kiosk.setJLabelAlignmentCenter(title);
			/**디자인 끝----------------------------------**/
		}
	}

	class CenterPane extends JPanel {
		private static final long serialVersionUID = -7304465484556432560L;
		JLabel menu, customMenu, count, price, customPrice;

		public CenterPane() {
			setLayout(null);
			setVisible(true);
			setSize(664, 526);
			
			JLabel titleOrder, titleCount, titlePrice;
			JPanel menuOrderPane, menuOrderScroll, menuOrderTable;
			titleOrder = new JLabel("주문 내역");
			titleCount = new JLabel("수량");
			titlePrice = new JLabel("금액");
			menuOrderTable = new JPanel();
			menuOrderScroll = new JPanel();
			menuOrderPane = new JPanel();
			titleOrder.setBounds(0,0,132,44);
			titleCount.setBounds(350,0,98,44);
			titlePrice.setBounds(495,0,98,44);
			
			//레이아웃 설정
			menuOrderTable.setLayout(null);
			menuOrderPane.setLayout(null);
			menuOrderScroll.setLayout(new BorderLayout());
			
			//menuOrderTable에 들어갈 menuOrderEachPans 설정 및 tableHeight 설정
			int tableHeight = 0 ;
			ArrayList<MenuOrderEachPane> menuOrderEachPans = new ArrayList<MenuOrderEachPane>();
			for (int i = 0; i < Kiosk.orderEachs.size(); i++) {
				MenuOrderEachPane moep = new MenuOrderEachPane(Kiosk.orderEachs.get(i));
				moep.setBackground(Color.white);
				moep.setBounds(0,tableHeight,664,moep.getHeight());
				menuOrderEachPans.add(moep);
				menuOrderTable.add(menuOrderEachPans.get(i));
				tableHeight += moep.getHeight();
			}
			if (tableHeight < 450)
				tableHeight = 450;
		
			//menuOrderPane 크기 설정
			menuOrderPane.setBounds(0,45,664, 485);
			menuOrderScroll.setBounds(0,0,664, 485);
			menuOrderTable.setPreferredSize(new Dimension(0,tableHeight));

			//컴포넌트 추가
			menuOrderScroll.add(new JScrollPane(menuOrderTable));
			menuOrderPane.add(menuOrderScroll);
			add(titleOrder);
			add(titleCount);
			add(titlePrice);
			add(menuOrderPane);
			
			/**디자인 시작-------------------------------**/
			//주문 내역 패널 배경색 지정
			setBackground(Color.LIGHT_GRAY);
			
			//상단의 "주문 내역", "수량", "금액" 레이블 폰트, 정렬 지정
			titleOrder.setFont(new Font("",Font.BOLD,15));
			titleCount.setFont(new Font("",Font.BOLD,15));
			titlePrice.setFont(new Font("",Font.BOLD,15));
			Kiosk.setJLabelAlignmentCenter(titleOrder);
			Kiosk.setJLabelAlignmentCenter(titleCount);
			Kiosk.setJLabelAlignmentCenter(titlePrice);
			
			//메뉴 뜨는 컴포넌트 배경색 지정
			menuOrderTable.setBackground(Color.white);
			/**디자인 끝----------------------------------**/
		}

		class MenuOrderEachPane extends JPanel {
			private static final long serialVersionUID = -3406558597254434283L;
			private int height;
			MenuOrderEachPane(OrderEach orderEach) {
				setLayout(null);
				
				//top
				JLabel menuName, menuCount, menuPrice;
				menuName = new JLabel();
				menuCount = new JLabel();
				menuPrice = new JLabel();
				
				for(Product p:Kiosk.products) {
					if(orderEach.getProduct_code() == p.getCode()) {
						menuName.setText(p.getName());
						break;
					}
				}
				menuCount.setText(String.valueOf(orderEach.getEachNum()));
				menuPrice.setText(String.valueOf(orderEach.getEachPrice()*orderEach.getEachNum())+"원");

				menuName.setBounds(23,20,200,20);
				menuCount.setBounds(380,20,37,20);
				menuPrice.setBounds(450,20,112,20);

				add(menuName);
				add(menuCount);
				add(menuPrice);
				
				//size
				JLabel sizeTitle, size, sizePrice;
				sizeTitle = new JLabel("└사이즈");
				size = new JLabel(orderEach.isSize() ? "Regular":"Large");
				sizePrice = new JLabel(orderEach.isSize() ? "+500원" :"+0원");

				sizeTitle.setBounds(74,58,100,16);
				size.setBounds(84,88,100,16);
				sizePrice.setBounds(536,88,200,16);
				
				add(sizeTitle);
				add(size);
				add(sizePrice);
				
				//hotOrIce
				JLabel hotOrIceTitle, hotOrIce, hotOrIcePrice;
				hotOrIceTitle = new JLabel("└HOT OR ICE");
				hotOrIce = new JLabel(orderEach.isHotOrIce() ? "ICE" : "HOT");
				hotOrIcePrice = new JLabel(orderEach.isHotOrIce() ? "+500원" :"+0원");
				
				hotOrIceTitle.setBounds(74,122,100,16);
				hotOrIce.setBounds(84,152,100,16);
				hotOrIcePrice.setBounds(536,152,200,16);
				
				add(hotOrIceTitle);
				add(hotOrIce);
				add(hotOrIcePrice);
				
				//add
				JLabel addTitle, add ,addPrice;
				addTitle = new JLabel("└추가");
				add = new JLabel("");
				if(orderEach.isShot()) add.setText("샷 추가");
				if(orderEach.isCream()) add.setText("휘핑 추가");
				if(orderEach.isHazelSyrup()) add.setText("헤이즐넛시럽 추가");
				if(orderEach.isAlmondSyrup()) add.setText("아몬드시럽 추가");
				if(orderEach.isVanillaSyrup()) add.setText("바닐라시럽 추가");
				addPrice = new JLabel(!add.getText().equals("") ? "+500원" :"+0원");
				
				addTitle.setBounds(74,184,100,16);
				add.setBounds(84,214,200,16);
				addPrice.setBounds(536,214,200,16);
				
				//add의 추가 여부와 height 설정
				if(add.getText().equals("")) {
					height = 178;
				}else {
					height = 240;
					add(addTitle);
					add(add);
					add(addPrice);
				}
				
				/**디자인 시작-------------------------------**/
				//메뉴 이름, 수량, 금액 레이블 폰트, 정렬 지정
				menuName.setFont(new Font("",Font.PLAIN,15));
				menuCount.setFont(new Font("",Font.PLAIN,15));
				menuPrice.setFont(new Font("",Font.PLAIN,15));
				menuName.setHorizontalAlignment(SwingConstants.LEFT);
				menuCount.setHorizontalAlignment(SwingConstants.CENTER);
				menuPrice.setHorizontalAlignment(SwingConstants.RIGHT);
				
				//사이즈 레이블들 폰트, 폰트 색상 지정
				sizeTitle.setFont(new Font("",Font.PLAIN,12));
				size.setFont(new Font("",Font.PLAIN,12));
				sizePrice.setFont(new Font("",Font.PLAIN,12));
				sizeTitle.setForeground(Color.gray);
				size.setForeground(Color.gray);
				sizePrice.setForeground(Color.gray);
				
				//핫/아이스 레이블들 폰트, 폰트 색상 지정
				hotOrIceTitle.setFont(new Font("",Font.PLAIN,12));
				hotOrIce.setFont(new Font("",Font.PLAIN,12));
				hotOrIcePrice.setFont(new Font("",Font.PLAIN,12));
				hotOrIceTitle.setForeground(Color.gray);
				hotOrIce.setForeground(Color.gray);
				hotOrIcePrice.setForeground(Color.gray);
				
				//샷, 휘핑, 헤이즐넛시럽, 아몬드시럽, 바닐라시럽 레이블들 폰트, 폰트 색상 지정
				addTitle.setFont(new Font("",Font.PLAIN,12));
				add.setFont(new Font("",Font.PLAIN,12));
				addPrice.setFont(new Font("",Font.PLAIN,12));
				addTitle.setForeground(Color.gray);
				add.setForeground(Color.gray);
				addPrice.setForeground(Color.gray);
				/**디자인 끝----------------------------------**/
			}
			public int getHeight() {
				return height;
			}
		}

	}

	class BottomPane extends JPanel {
		private static final long serialVersionUID = 4568580328231944301L;
		private static JLabel totalPriceTitle, totalCount, totalPrice, totalBackground, orderPriceTitle, orderPrice, usePointTitle, usePoint;
		private static JButton cardPay, cashPay, point;
		
		BottomPane() {
			setLayout(null);
			setVisible(true);
			
			totalPriceTitle = new JLabel();
			totalCount = new JLabel();
			totalPrice = new JLabel();
			
			orderPriceTitle = new JLabel();
			orderPrice = new JLabel();
			usePointTitle = new JLabel();
			usePoint = new JLabel();

			updateLabel();
			
			point = new JButton("포인트");
			cardPay = new JButton("카드 결제");
			cashPay = new JButton("현금 결제");
			
			totalBackground = new JLabel("");
			
			point.setBounds(31, 0, 601, 69);
			
			totalPriceTitle.setBounds(54,117,200,31);
			totalCount.setBounds(324,117,117,31);
			totalPrice.setBounds(450,117,162,31);
			totalBackground.setBounds(31,95,601,75);

			orderPriceTitle.setBounds(54,194,150,17);
			orderPrice.setBounds(338,194,274,17);
			usePointTitle.setBounds(54,232,150,17);
			usePoint.setBounds(376,232,236,17);
			
			cardPay.setBounds(31, 287, 293, 209);
			cashPay.setBounds(339, 287, 293, 209);
			
			// JPanel 위에 올리기
			add(point);
			add(totalPriceTitle);
			add(totalCount);
			add(totalPrice);
			add(totalBackground);
			add(orderPriceTitle);
			add(orderPrice);
			add(usePointTitle);
			add(usePoint);
			add(cardPay);
			add(cashPay);

			point.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new EnterPoint();
				}
			});

			cardPay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Kiosk.orderMain.setPayType("카드");
					new WaitingPanel();
					dispose();
				}
			});

			cashPay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Kiosk.orderMain.setPayType("현금");
					new WaitingPanel();
					dispose();
				}
			});
			
			/**디자인 시작-------------------------------**/
			//전체 배경색 지정
			setBackground(Color.white);
			
			//결제 수량과 금액 컴포넌트 네모 라인, 배경색 지정
			totalBackground.setOpaque(true);
			totalBackground.setBackground(Color.white);
			LineBorder lb = new LineBorder(Color.black, 1, true);
			totalBackground.setBorder(lb);
			
			//"결제 금액", 결제 수량, 결제 금액 레이블 폰트, 정렬 지정
			totalPriceTitle.setFont(new Font("",Font.BOLD,35));
			totalCount.setFont(new Font("",Font.BOLD,35));
			totalPrice.setFont(new Font("",Font.BOLD,35));
			totalPriceTitle.setHorizontalAlignment(SwingConstants.LEFT);
			totalCount.setHorizontalAlignment(SwingConstants.RIGHT);
			totalPrice.setHorizontalAlignment(SwingConstants.RIGHT);
			
			//포인트 버튼 배경색, 폰트 색상, 폰트 지정
			point.setBackground(Color.red);
			point.setForeground(Color.white);
			point.setFont(new Font("",Font.BOLD,35));
			
			//"주문 금액", 주문 금액, "포인트 사용", 사용한 포인트 레이블 폰트, 정렬, 폰트 색상 지정
			orderPriceTitle.setFont(new Font("",Font.BOLD,15));
			orderPrice.setFont(new Font("",Font.BOLD,15));
			usePointTitle.setFont(new Font("",Font.BOLD,15));
			usePoint.setFont(new Font("",Font.BOLD,15));
			orderPriceTitle.setHorizontalAlignment(SwingConstants.LEFT);
			orderPrice.setHorizontalAlignment(SwingConstants.RIGHT);
			usePointTitle.setHorizontalAlignment(SwingConstants.LEFT);
			usePoint.setHorizontalAlignment(SwingConstants.RIGHT);
			orderPriceTitle.setForeground(Color.gray);
			orderPrice.setForeground(Color.gray);
			usePointTitle.setForeground(Color.gray);
			usePoint.setForeground(Color.gray);
			
			//카드 결제/ 현금 결제 버튼 폰트, 배경색, 폰트 색상 지정
			cardPay.setFont(new Font("",Font.BOLD,35));
			cashPay.setFont(new Font("",Font.BOLD,35));
			cardPay.setBackground(Color.gray);
			cardPay.setForeground(Color.white);
			cashPay.setBackground(Color.gray);
			cashPay.setForeground(Color.white);
			
			//버튼 정렬 지정
			Kiosk.setJButtonAlignmentCenter(point);
			Kiosk.setJButtonAlignmentCenter(cardPay);
			Kiosk.setJButtonAlignmentCenter(cashPay);
			/**디자인 끝----------------------------------**/
		}
		public static void updateLabel() {
			// 주문 개수 구하기
			int count = 0;
			for (OrderEach oe : Kiosk.orderEachs)
				count += oe.getEachNum();

			totalPriceTitle.setText("결제 금액");
			totalCount.setText(count + "개");
			totalPrice.setText((Kiosk.orderMain.getPayAmount() - Kiosk.orderMain.getUsePoint()) + "원");

			orderPriceTitle.setText("주문 금액");
			orderPrice.setText(Kiosk.orderMain.getPayAmount() + "원");
			usePointTitle.setText("포인트 사용");
			usePoint.setText("-" + Kiosk.orderMain.getUsePoint() + "P");
		}
	}
}