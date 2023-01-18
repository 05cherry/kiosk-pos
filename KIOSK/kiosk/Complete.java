package com._team.kiosk;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com._team.DB.*;

class Complete extends JFrame {
	private static final long serialVersionUID = 9047409665544293066L;

	Container cp;

	ImageIcon checkImg;
	// 체크 표시 이미지
	JLabel check;
	// 체크 표시 이미지 넣을 레이블
	JLabel paymentComplete;
	// 결제가 완료되었습니다 글자 넣을 레이블

	JLabel orderLabel;
	// 주문 번호 글자 넣을 레이블

	JButton print;
	// 영수증 인쇄 버튼
	JButton goBack;

	Complete() {
		// DB와 Kiosk클래스의 객체에 데이터 추가
		// 주문번호 설정
		int receiptNumber = 0;
		for (ReceiptNumber rn : Kiosk.receiptNumbers) {
			if (rn.getReceiptNumber() > receiptNumber)
				receiptNumber = rn.getReceiptNumber();
		}
		receiptNumber++;
		// 적립포인트 설정
		int savingPoint = 0;
		if (Kiosk.orderMain.getCustomerCode() != -1) {
			for (Customer c : Kiosk.customers) {
				if (c.getCode() == Kiosk.orderMain.getCustomerCode()) {
					savingPoint = c.getPoint() + (int) (Kiosk.orderMain.getPayAmount() * 0.1)
							- Kiosk.orderMain.getUsePoint();
				}
			}
		}

		// TODO 나중에 DBController에서 자동으로 확인하고 update하게 해주기 지금처럼 작동시 작업이 완료될 떄까지 딜레이가
		// 생김-------------------
		// DB 데이터 추가
		String strSQL;
		LinkedHashMap<String, Object> inputData = new LinkedHashMap<>();

		// customer
		if (Kiosk.orderMain.getCustomerCode() != -1) {
			strSQL = "UPDATE customer SET point = ? WHERE code = ?;";
			inputData.clear();
			inputData.put("point", (Integer) savingPoint);
			inputData.put("code", (Integer) Kiosk.orderMain.getCustomerCode());
			Main.dbc.addData(inputData, strSQL);
		}

		// order_main
		inputData.clear();
		strSQL = "INSERT INTO order_main VALUES (?,?,?,?,?,?,?)";
//		strSQL = "UPDATE order_main SET customerCode = ?, dateTime = ?, payAmount = ?, usePoint = ?, payType = ?, takeOut = ? WHERE code = ?;";
		inputData.put("code", (Integer) Kiosk.orderMain.getCode());
		inputData.put("customerCode", (Integer) Kiosk.orderMain.getCustomerCode());
		inputData.put("dateTime", (Timestamp) Kiosk.orderMain.getDateTime());
		inputData.put("payAmount", (Integer) Kiosk.orderMain.getPayAmount());
		inputData.put("usePoint", (Integer) Kiosk.orderMain.getUsePoint());
		inputData.put("payType", (String) Kiosk.orderMain.getPayType());
		inputData.put("takeOut", (boolean) Kiosk.orderMain.isTakeOut());
		Main.dbc.addData(inputData, strSQL);

		// receipt_Number
		inputData.clear();
		strSQL = "INSERT INTO receipt_Number VALUES (?,?)";
		inputData.put("orderCode", (Integer) Kiosk.orderMain.getCode());
		inputData.put("receiptNumber", (Integer) receiptNumber);
		Main.dbc.addData(inputData, strSQL);

		// order_each
		strSQL = "INSERT INTO order_each VALUES (?,?,?,?,?,?,?,?,?,?,?)";
//		strSQL = "UPDATE order_each SET product_code = ?, eachPrice = ? , eachNum = ? , size = ? , shot = ? , hotOrIce = ? , cream = ? , hazelSyrup = ? , almondSyrup = ? , vanillaSyrup = ? WHERE order_main_code = ?;";
		for (OrderEach oe : Kiosk.orderEachs) {
			inputData.clear();
			inputData.put("order_main_code", (Integer) oe.getOrder_main_code());
			inputData.put("product_code", (Integer) oe.getProduct_code());
			inputData.put("eachPrice", (Integer) oe.getEachPrice());
			inputData.put("eachNum", (Integer) oe.getEachNum());
			inputData.put("size", (boolean) oe.isSize());
			inputData.put("shot", (boolean) oe.isShot());
			inputData.put("hotOrIce", (boolean) oe.isHotOrIce());
			inputData.put("cream", (boolean) oe.isCream());
			inputData.put("hazelSyrup", (boolean) oe.isHazelSyrup());
			inputData.put("almondSyrup", (boolean) oe.isAlmondSyrup());
			inputData.put("vanillaSyrup", (boolean) oe.isVanillaSyrup());
			Main.dbc.addData(inputData, strSQL);
		}
		// --------------------------------------------------------------------------------------------------------------

		cp = getContentPane();
		setLayout(null);
		setBounds(0, 0, 664, 1172);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("주문 완료");

		checkImg = new ImageIcon("images/img.png");
		Image img = checkImg.getImage();
		// 추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
		Image updateImg = img.getScaledInstance(40, 40, Image.SCALE_FAST);
		// 새로운 Image 객체로 ImageIcon 객체 생성
		ImageIcon updateCheckImg = new ImageIcon(updateImg);
		check = new JLabel(updateCheckImg);

		paymentComplete = new AutoLabel("결제가 완료되었습니다.");
		check.setBounds(309, 167, 46, 46);

		paymentComplete.setBounds(0, 254, 664, 31);

		cp.add(check);
		cp.add(paymentComplete);
		add(check);
		add(paymentComplete);

		orderLabel = new JLabel("<html><body><center>주문 번호<br> <br>" + receiptNumber + "</center></body></html>");
		orderLabel.setBounds(104, 339, 455, 193);

		add(orderLabel);

		JLabel pointLabel = new AutoLabel("적립 포인트");
		
		// 적립된 포인트를 뜨게 만들기
		JLabel savingPointLabel = new JLabel(String.valueOf(savingPoint + "P"));
		pointLabel.setBounds(200, 568, 100, 26);
		savingPointLabel.setBounds(450, 568, 200, 26);
		add(pointLabel);
		add(savingPointLabel);

		// 처음으로 버튼
		print = new JButton("영수증 인쇄");
		goBack = new JButton("처음으로");

		print.setBounds(145, 765, 378, 77);
		goBack.setBounds(145, 859, 378, 77);

		add(print);
		add(goBack);

		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Main.win.kioskUpdate();
			}
		});

		// 처음으로 버튼을 클릭하면 키오스크 창이 뜨는 액션 리스너
		goBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Main.win.kioskUpdate();
			}
		});
		setVisible(true);
		
		/**디자인 시작-------------------------------**/
		//전체 배경색 지정
		cp.setBackground(Color.white);
		
		//"결제가 완료되었습니다." 폰트 지정
		paymentComplete.setFont(new Font("", Font.PLAIN, 25));
		
		//"주문번호", 주문번호 네모 라인, 폰트, 정렬 지정
		orderLabel.setBorder(new LineBorder(Color.gray));
		orderLabel.setFont(new Font("", Font.BOLD, 30));
		Kiosk.setJLabelAlignmentCenter(orderLabel);
		
		//"적립 포인트" 레이블 폰트 색상 지정
		pointLabel.setForeground(Color.gray);
		
		//영수증 인쇄 버튼 폰트, 라인 색상, 배경색 지정
		print.setFont(new Font("", Font.BOLD, 25));
		print.setBorder(new LineBorder(Color.black));
		print.setBackground(Color.lightGray);
		
		//처음으로 버튼 폰트, 라인 색상, 배경색 지정
		goBack.setFont(new Font("", Font.BOLD, 25));
		goBack.setBorder(new LineBorder(Color.white));
		goBack.setBackground(Color.red);
		/**디자인 끝----------------------------------**/
	}
}