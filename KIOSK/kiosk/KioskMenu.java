package com._team.kiosk;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com._team.DB.*;

public class KioskMenu extends JFrame {
	private static final long serialVersionUID = 5813647090542250483L;
	private Product product;
	private OrderEach orderEach;

	public KioskMenu(Product product) {
		this.product = product;
		orderEach = new OrderEach();
		orderEach.setOrder_main_code(Kiosk.orderMain.getCode());
		orderEach.setProduct_code(product.getCode());
		orderEach.setEachPrice(product.getPrice());
		orderEach.setEachNum(1);
		orderEach.setSize(false);
		orderEach.setHotOrIce(false);
		setAddInit();

		Container cp = getContentPane();
		setTitle(product.getName());
		setVisible(true);
		setLayout(null);
		setBounds(44, 100, 576, 969);

		JPanel choosePane = new ChoosePane();
		JPanel sizePane = new SizePane();
		JPanel HotOrIcePane = new HotOrIcePane();
		JPanel addPane = new AddPane();
		JPanel okPane = new OkPane();

		// 각 패널 컴포넌트 위치 설정
		choosePane.setBounds(0, 0, 576, 188);
		sizePane.setBounds(0, 188, 576, 152);
		HotOrIcePane.setBounds(0, 340, 576, 152);
		addPane.setBounds(0, 492, 576, 300);
		okPane.setBounds(0, 792, 576, 177);
		
		// 컨테이너 위에 올리기
		cp.add(choosePane);
		cp.add(sizePane);
		cp.add(HotOrIcePane);
		cp.add(addPane);
		cp.add(okPane);
		
		/**디자인 시작-------------------------------**/
		//전체 배경색 지정
		setBackground(Color.white);
		
		//각 패널 배경색 지정
		choosePane.setBackground(Color.white);
		sizePane.setBackground(Color.white);
		HotOrIcePane.setBackground(Color.white);
		addPane.setBackground(Color.white);
		okPane.setBackground(Color.white);
		/**디자인 끝----------------------------------**/
	}

	private void setAddInit() {
		orderEach.setShot(false);
		orderEach.setCream(false);
		orderEach.setHazelSyrup(false);
		orderEach.setAlmondSyrup(false);
		orderEach.setVanillaSyrup(false);
	}

	private void setBoundsButton(int w, int h, int i, Component cmp) {
		cmp.setBounds(w + (125 * (i % 4 + 1) - 125), h + (118 * (i / 4 + 1) - 118), 114, 111);
	}

	/**선택한 메뉴**/
	class ChoosePane extends JPanel {
		private static final long serialVersionUID = 6454489502991928949L;

		public ChoosePane() {
			setLayout(null);
			ImageIcon img = new ImageIcon(product.getImgSrc());
			// img의 크기 조절
			img = new ImageIcon(img.getImage().getScaledInstance(127, 124, Image.SCALE_FAST));

			JButton menuBtn = new JButton(img);
			JLabel menuName = new JLabel(product.getName());
			JLabel menuPrice = new JLabel(product.getPrice() + "원");

			// 버튼 테두리 색 변경
			menuBtn.setBorder(new LineBorder(Color.white));

			menuBtn.setBounds(30, 33, 127, 124);
			menuName.setBounds(183, 38, 393, 26);
			menuPrice.setBounds(183, 86, 393, 26);

			menuName.setFont(new Font("", Font.BOLD, 20));
			menuPrice.setFont(new Font("", Font.BOLD, 20));

			// JPanel 위에 올리기
			add(menuBtn);
			add(menuName);
			add(menuPrice);
			
			/**디자인 시작-------------------------------**/
			//메뉴 이름 폰트 지정
			menuName.setFont(new Font("", Font.BOLD, 20));
			
			//메뉴 가격 폰트 지정
			menuPrice.setFont(new Font("", Font.BOLD, 20));
			/**디자인 끝----------------------------------**/
		}
	}

	/**사이즈 설정**/
	class SizePane extends JPanel {
		private static final long serialVersionUID = 3831703052671250902L;

		SizePane() {
			setLayout(null);
			
			JLabel size = new JLabel("사이즈");
			// 토글 버튼의 기능 : 하나를 선택하면 나머지는 모두 해제되는 기능
			JToggleButton regular = new JToggleButton("<html><body><center>Regular<br><br>0원</center></body></html>");
			JToggleButton large = new JToggleButton("<html><body><center>Large<br><br>+500원</center></body></html>");
			ButtonGroup bg = new ButtonGroup();
			
			// 버튼 그룹으로 토글 버튼 묶기
			bg.add(regular);
			bg.add(large);
			
			size.setBounds(30,10,100,20);
			setBoundsButton(30,40,0,regular);
			setBoundsButton(30,40,1,large);
			
			// JPanel 위에 올리기
			add(size);
			add(regular);
			add(large);
			
			//사이즈 선택시 orderEach 업데이트
			regular.setSelected(true);
			regular.addActionListener((e)->{
				orderEach.setSize(false);
			});
			large.addActionListener((e)->{
				orderEach.setSize(true);
			});
			
			/**디자인 시작-------------------------------**/
			//사이즈 버튼 색상 지정
			regular.setBackground(Color.gray);
			regular.setForeground(Color.white);
			large.setBackground(Color.gray);
			large.setForeground(Color.white);
			/**디자인 끝----------------------------------**/
		}
	}
	
	/**핫/아이스 설정**/
	class HotOrIcePane extends JPanel {
		private static final long serialVersionUID = -5705600524161532332L;

		HotOrIcePane() {
			setLayout(null);
			
			JLabel size = new JLabel("Hot or Ice");

			// 토글 버튼의 기능 : 하나를 선택하면 나머지는 모두 해제되는 기능
			JToggleButton hot = new JToggleButton("<html><body><center>HOT<br><br>0원</center></body></html>");
			JToggleButton ice = new JToggleButton("<html><body><center>ICE<br><br>+500원</center></body></html>");
			ButtonGroup bg = new ButtonGroup();
			
			// 버튼 그룹으로 토글 버튼 묶기
			bg.add(hot);
			bg.add(ice);
			
			size.setBounds(30,10,100,20);
			setBoundsButton(30,40,0,hot);
			setBoundsButton(30,40,1,ice);
			
			// JPanel 위에 올리기
			add(size);
			add(hot);
			add(ice);
			
			//사이즈 선택시 orderEach 업데이트
			hot.setSelected(true);
			hot.addActionListener((e)->{
				orderEach.setHotOrIce(false);
			});
			ice.addActionListener((e)->{
				orderEach.setHotOrIce(true);
			});
			
			/**디자인 시작-------------------------------**/
			//핫/아이스 버튼 색상 지정
			hot.setBackground(Color.gray);
			hot.setForeground(Color.white);
			ice.setBackground(Color.gray);
			ice.setForeground(Color.white);
			/**디자인 끝----------------------------------**/
		}
	}
	
	/**샷/휘핑/시럽 추가 설정**/
	class AddPane extends JPanel {
		private static final long serialVersionUID = -2507747132524282881L;

		public AddPane() {
			setLayout(null);
			
			JLabel add = new JLabel("추가");
			JToggleButton addShot = new JToggleButton("<html><body><center>샷<br><br>+500원</center></body></html>");
			JToggleButton addCream = new JToggleButton("<html><body><center>휘핑<br><br>+500원</center></body></html>");
			JToggleButton addHazel = new JToggleButton("<html><body><center>헤이즐넛시럽<br><br>+500원</center></body></html>");
			JToggleButton addAlmond = new JToggleButton("<html><body><center>아몬드시럽<br><br>+500원</center></body></html>");
			JToggleButton addVanilla = new JToggleButton("<html><body><center>바닐라시럽<br><br>+500원</center></body></html>");
			ButtonGroup addBg = new ButtonGroup();
			// 그룹으로 묶어 관리하기 위한 addBg 객체 생성
			
			add.setBounds(30,18,38,20);
			setBoundsButton(30,55,0,addShot);
			setBoundsButton(30,55,1,addCream);
			setBoundsButton(30,55,2,addHazel);
			setBoundsButton(30,55,3,addAlmond);
			setBoundsButton(30,55,4,addVanilla);
			
			add(add);
			// 버튼 그룹으로 토글 버튼 묶기
			addBg.add(addShot);
			addBg.add(addCream);
			addBg.add(addHazel);
			addBg.add(addAlmond);
			addBg.add(addVanilla);
			
			// JPanel 위에 올리기
			add(addShot);
			add(addCream);
			add(addHazel);
			add(addAlmond);
			add(addVanilla);
			
			//추가 선택시 orderEach 업데이트
			addShot.addActionListener((e)->{
				setAddInit();
				orderEach.setShot(true);
			});
			addCream.addActionListener((e)->{
				setAddInit();
				orderEach.setCream(true);
			});
			addHazel.addActionListener((e)->{
				setAddInit();
				orderEach.setHazelSyrup(true);
			});
			addAlmond.addActionListener((e)->{
				setAddInit();
				orderEach.setAlmondSyrup(true);
			});
			addVanilla.addActionListener((e)->{
				setAddInit();
				orderEach.setVanillaSyrup(true);
			});
			
			/**디자인 시작-------------------------------**/
			//샷, 휘핑, 헤이즐넛시럽, 아몬드시럽, 바닐라시럽 버튼 색상 지정
			addShot.setBackground(Color.gray);
			addShot.setForeground(Color.white);
			addCream.setBackground(Color.gray);
			addCream.setForeground(Color.white);
			addHazel.setBackground(Color.gray);
			addHazel.setForeground(Color.white);
			addAlmond.setBackground(Color.gray);
			addAlmond.setForeground(Color.white);
			addVanilla.setBackground(Color.gray);
			addVanilla.setForeground(Color.white);
			/**디자인 끝----------------------------------**/
		}
	}

	class OkPane extends JPanel {
		private static final long serialVersionUID = 4629126491258123281L;

		public OkPane() {
			setLayout(null);
			JButton okay = new JButton("선택 완료");
			okay.setBounds(60, 26, 456, 87);
			add(okay);

			// 선택 완료 버튼을 눌렀을 때 발생하는 actionListener
			okay.addActionListener((e) -> {
				// 주문 상품 추가
				// 가격 설정
				int price = orderEach.getEachPrice();
				if (orderEach.isSize())
					price += 500;
				if (orderEach.isHotOrIce())
					price += 500;
				if (orderEach.isAlmondSyrup() || orderEach.isCream() || orderEach.isHazelSyrup() || orderEach.isShot()
						|| orderEach.isVanillaSyrup())
					price += 500;
				orderEach.setEachPrice(price);
				Kiosk.orderEachs.add(orderEach);
				Main.win.bottom.updateBottom();
				dispose();
			});

			/**디자인 시작-------------------------------**/
			//선택 완료 버튼 색상 지정
			okay.setBackground(Color.red);
			okay.setForeground(Color.white);
			/**디자인 끝----------------------------------**/
		}
	}

}