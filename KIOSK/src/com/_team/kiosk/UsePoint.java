package com._team.kiosk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com._team.DB.Customer;

public class UsePoint extends JFrame {
	private static final long serialVersionUID = -6922400062452340479L;
	Container cp;
	JLabel backgroundLabel, pointLabel, usePointLabelTitle, savingPointLabelTitle, usingPointLabelLabelTitle, savingPointLabelLabel,
			usingPointLabelLabel;
	int savingPoint, usingPoint;

	private void addpointLabel(String num) {
		if(pointLabel.getText().equals("0")) {
			pointLabel.setText(num);
		}else {
			pointLabel.setText(pointLabel.getText() + num);
		}
	}

	UsePoint() {
		cp = getContentPane();

		setBounds(0, 0, 460, 656);
		setLayout(null);

		savingPoint = (int)(Kiosk.orderMain.getPayAmount()*0.1);
		usingPoint = savingPoint;
		for(Customer c : Kiosk.customers) {
			if(c.getCode() == Kiosk.orderMain.getCustomerCode()) {
				usingPoint += c.getPoint();
			}
		}
		
		backgroundLabel = new JLabel();
		usePointLabelTitle = new JLabel("포인트를 사용하시겠습니까?");
		savingPointLabelTitle = new JLabel("적립 예정 포인트");
		savingPointLabelLabel = new JLabel(savingPoint+"p");
		usingPointLabelLabelTitle = new JLabel("사용 가능 포인트");
		usingPointLabelLabel = new JLabel(usingPoint+"p");
		pointLabel = new JLabel("0");

		// 버튼 레이블
		JButton btn1 = new JButton("1");
		JButton btn2 = new JButton("2");
		JButton btn3 = new JButton("3");
		JButton btn4 = new JButton("4");
		JButton btn5 = new JButton("5");
		JButton btn6 = new JButton("6");
		JButton btn7 = new JButton("7");
		JButton btn8 = new JButton("8");
		JButton btn9 = new JButton("9");
		JButton btnEnter = new JButton("ENTER");
		JButton btn0 = new JButton("0");
		JButton btnDel = new JButton("DEL");
		JLabel close = new JLabel("X");

		usePointLabelTitle.setBounds(0, 39, 460, 18);
		savingPointLabelTitle.setBounds(0, 99, 460, 18);
		savingPointLabelLabel.setBounds(0, 125, 460, 18);
		usingPointLabelLabelTitle.setBounds(0, 154, 460, 18);
		usingPointLabelLabel.setBounds(0, 177, 460, 18);
		pointLabel.setBounds(0, 238, 460, 26);
		backgroundLabel.setBounds(50,92,380,115);
		btn1.setBounds(107, 289, 69, 69);
		btn2.setBounds(195, 289, 69, 69);
		btn3.setBounds(283, 289, 69, 69);
		btn4.setBounds(107, 375, 69, 69);
		btn5.setBounds(195, 375, 69, 69);
		btn6.setBounds(283, 375, 69, 69);
		btn7.setBounds(107, 461, 69, 69);
		btn8.setBounds(195, 461, 69, 69);
		btn9.setBounds(283, 461, 69, 69);
		btnEnter.setBounds(107, 547, 69, 69);
		btn0.setBounds(195, 547, 69, 69);
		btnDel.setBounds(283, 547, 69, 69);
		close.setBounds(415, 15, 20, 20);
		
		cp.add(usePointLabelTitle);
		cp.add(savingPointLabelTitle);
		cp.add(usingPointLabelLabelTitle);
		cp.add(savingPointLabelLabel);
		cp.add(usingPointLabelLabel);
		cp.add(pointLabel);
		cp.add(backgroundLabel);
		cp.add(btn1);
		cp.add(btn2);
		cp.add(btn3);
		cp.add(btn4);
		cp.add(btn5);
		cp.add(btn6);
		cp.add(btn7);
		cp.add(btn8);
		cp.add(btn9);
		cp.add(btnEnter);
		cp.add(btn0);
		cp.add(btnDel);
		cp.add(close);

		setVisible(true);

		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("1");
				// pointLabel에 JLabel에 "1" 글자가 작성
			}
		});

		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("2");
				// pointLabel에 JLabel에 "2" 글자가 작성
			}
		});

		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("3");
				// pointLabel에 JLabel에 "3" 글자가 작성
			}
		});
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("4");
				// pointLabel에 JLabel에 "4" 글자가 작성
			}
		});

		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("5");
				// pointLabel에 JLabel에 "5" 글자가 작성
			}
		});

		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("6");
				// pointLabel에 JLabel에 "6" 글자가 작성
			}
		});

		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("7");
				// pointLabel에 JLabel에 "7" 글자가 작성
			}
		});

		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("8");
				// pointLabel에 JLabel에 "8" 글자가 작성
			}
		});

		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("9");
				// pointLabel에 JLabel에 "9" 글자가 작성
			}
		});

		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpointLabel("0");
				// pointLabel에 JLabel에 "0" 글자가 작성
			}
		});

		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pl = pointLabel.getText().length();
				// substring은 문자열 자르기
				if(pl==1) {
					pointLabel.setText("0");
				}else {
					pointLabel.setText(pointLabel.getText().substring(0, pl - 1));
				}
			}
		});

		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Integer.parseInt(pointLabel.getText().toString())>usingPoint) {
					JOptionPane.showMessageDialog(cp, "사용가능한 포인트가 부족합니다.", "포인트 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(Integer.parseInt(pointLabel.getText().toString())>Kiosk.orderMain.getPayAmount()) {
					JOptionPane.showMessageDialog(cp, "사용가능한 포인트 범위를 벗어났습니다.", "포인트 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				dispose();
				Kiosk.orderMain.setUsePoint(Integer.parseInt(pointLabel.getText().toString()));
				ChangeOrderCheckPage.BottomPane.updateLabel();
				// TODO 주문 내역 확인 페이지로 넘어가기
			}
		});

		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
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
		//전체 배경색 지정
		cp.setBackground(Color.white);
		
		//X 레이블 네모 라인, 정렬 지정 / 상단의 네모 라인 지정
		LineBorder lb = new LineBorder(Color.black, 1, true);
		close.setBorder(lb);
		Kiosk.setJLabelAlignmentCenter(close);
		backgroundLabel.setBorder(lb);
		
		//"포인트를 사용하시겠습니까?", "적립 예정 포인트", 적립 예정 포인트, "사용 가능 포인트", 사용 가능 포인트 폰트, 폰트 색상, 정렬 지정
		usePointLabelTitle.setFont(new Font("", Font.BOLD, 10));
		savingPointLabelTitle.setFont(new Font("", Font.BOLD, 10));
		usingPointLabelLabelTitle.setFont(new Font("", Font.BOLD, 10));
		savingPointLabelLabel.setFont(new Font("", Font.BOLD, 10));
		usingPointLabelLabel.setFont(new Font("", Font.BOLD, 10));
		savingPointLabelTitle.setForeground(Color.gray);
		usingPointLabelLabelTitle.setForeground(Color.gray);
		Kiosk.setJLabelAlignmentCenter(usePointLabelTitle);
		Kiosk.setJLabelAlignmentCenter(savingPointLabelTitle);
		Kiosk.setJLabelAlignmentCenter(usingPointLabelLabelTitle);
		Kiosk.setJLabelAlignmentCenter(savingPointLabelLabel);
		Kiosk.setJLabelAlignmentCenter(usingPointLabelLabel);
		
		//입력 받을 포인트 숫자 폰트, 정렬 지정
		pointLabel.setFont(new Font("", Font.BOLD, 30));
		Kiosk.setJLabelAlignmentCenter(pointLabel);
		
		//버튼 폰트 지정
		btn1.setFont(new Font("", Font.PLAIN, 50));
		btn2.setFont(new Font("", Font.PLAIN, 50));
		btn3.setFont(new Font("", Font.PLAIN, 50));
		btn4.setFont(new Font("", Font.PLAIN, 50));
		btn5.setFont(new Font("", Font.PLAIN, 50));
		btn6.setFont(new Font("", Font.PLAIN, 50));
		btn7.setFont(new Font("", Font.PLAIN, 50));
		btn8.setFont(new Font("", Font.PLAIN, 50));
		btn9.setFont(new Font("", Font.PLAIN, 50));
		btnEnter.setFont(new Font("", Font.PLAIN, 10));
		btn0.setFont(new Font("", Font.PLAIN, 50));
		btnDel.setFont(new Font("", Font.PLAIN, 17));
		close.setFont(new Font("", Font.BOLD, 15));
		
		//버튼 배경색 지정
		btn1.setBackground(Color.white);
		btn2.setBackground(Color.white);
		btn3.setBackground(Color.white);
		btn4.setBackground(Color.white);
		btn5.setBackground(Color.white);
		btn6.setBackground(Color.white);
		btn7.setBackground(Color.white);
		btn8.setBackground(Color.white);
		btn9.setBackground(Color.white);
		btnEnter.setBackground(Color.white);
		btn0.setBackground(Color.white);
		btnDel.setBackground(Color.white);
		/**디자인 끝----------------------------------**/
	}
}
