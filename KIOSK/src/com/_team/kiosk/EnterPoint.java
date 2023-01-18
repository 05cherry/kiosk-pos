package com._team.kiosk;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com._team.DB.Customer;

public class EnterPoint extends JFrame {
	private static final long serialVersionUID = -6920514121342713219L;
	Container cp;
	JLabel phoneNumberLabel;
	
	private void addPhoneNumber(String num) {
		int phoneNumberLen;
		// 입력된 숫자의 길이에 따라 중간에 하이픈을 추가하여주거나 더 이상의 입력을 막아준다.

		phoneNumberLabel.setText(phoneNumberLabel.getText() + num);
		phoneNumberLen = phoneNumberLabel.getText().length();
		
		if (phoneNumberLen == 3) {
			phoneNumberLabel.setText(phoneNumberLabel.getText() + "-");
		} else if (phoneNumberLen == 8) {
			phoneNumberLabel.setText(phoneNumberLabel.getText() + "-");
		} else if (phoneNumberLen == 13) {
			//회원정보가 있을 시 회원코드 저장
			int customerCode = -1;
			for(Customer c : Kiosk.customers) {
				if(phoneNumberLabel.getText().equals(c.getPhone())) {
					customerCode = c.getCode();
					break;
				}
			}
			if(customerCode == -1){//회원정보가 없을 시 회원정보 생성 후 회원코드 저장
				customerCode = Integer.parseInt(Main.dbc.getMaxNum("customer"));
				int point = 0;
				String strSQL = "INSERT INTO customer VALUES (?,?,?)";
				// LinkedHashMap으로 sql에 넣을 값 전달(반드시 순서 정해져있어야함)
				LinkedHashMap<String, Object> inputData = new LinkedHashMap<>();
				inputData.put("code", (Integer) customerCode);
				inputData.put("phone", (String) phoneNumberLabel.getText());
				inputData.put("point", (Integer) point);
				Main.dbc.addData(inputData, strSQL);
				//TODO 나중에 DBController에서 자동으로 확인하고 update하게 해주기 지금처럼 작동시 작업이 완료될 떄까지 딜레이가 생김-------------------
				Kiosk.updateCustomers();
				//--------------------------------------------------------------------------------------------------------------
				JOptionPane.showMessageDialog(this, phoneNumberLabel.getText() + "으로 신규 회원가입되었습니다.", "신규 회원 가입", JOptionPane.WARNING_MESSAGE);
			}
			//회원코드 연결
			Kiosk.orderMain.setCustomerCode(customerCode);
			//0.5초 대기 후 다음 페이지
			new Thread() {
				public void run() {
					try {
						sleep(500);
						new UsePoint();
						dispose();
					} catch (Exception e) {
					}
				}
			}.start();
		}
	}

	EnterPoint() {
		cp = getContentPane();
		setTitle("전화번호 입력");
		setLayout(null);
		setSize(460, 601);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
		phoneNumberLabel = new JLabel();
		
		// 포인트 숫자 입력받는 레이블
		JButton btn1 = new JButton("1");
		JButton btn2 = new JButton("2");
		JButton btn3 = new JButton("3");
		JButton btn4 = new JButton("4");
		JButton btn5 = new JButton("5");
		JButton btn6 = new JButton("6");
		JButton btn7 = new JButton("7");
		JButton btn8 = new JButton("8");
		JButton btn9 = new JButton("9");
		JButton btn010 = new JButton("010");
		JButton btn0 = new JButton("0");
		JButton btnDel = new JButton("DEL");
		JLabel close = new JLabel("X");
		
		cp.add(phoneNumberLabel);
		cp.add(btn1);
		cp.add(btn2);
		cp.add(btn3);
		cp.add(btn4);
		cp.add(btn5);
		cp.add(btn6);
		cp.add(btn7);
		cp.add(btn8);
		cp.add(btn9);
		cp.add(btn010);
		cp.add(btn0);
		cp.add(btnDel);
		cp.add(close);

		phoneNumberLabel.setBounds(77, 85, 304, 53);
		btn1.setBounds(100, 188, 69, 69);
		btn2.setBounds(188, 188, 69, 69);
		btn3.setBounds(276, 188, 69, 69);
		btn4.setBounds(100, 274, 69, 69);
		btn5.setBounds(188, 274, 69, 69);
		btn6.setBounds(276, 274, 69, 69);
		btn7.setBounds(100, 360, 69, 69);
		btn8.setBounds(188, 360, 69, 69);
		btn9.setBounds(276, 360, 69, 69);
		btn010.setBounds(100, 446, 69, 69);
		btn0.setBounds(188, 446, 69, 69);
		btnDel.setBounds(276, 446, 69, 69);
		close.setBounds(415, 15, 20, 20);

		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("1");
				// phoneNumberLabel JLabel에 "1" 글자가 작성
			}
		});

		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("2");
				// phoneNumberLabel JLabel에 "2" 글자가 작성
			}
		});

		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("3");
				// phoneNumberLabel JLabel에 "3" 글자가 작성
			}
		});
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("4");
				// phoneNumberLabel JLabel에 "4" 글자가 작성
			}
		});

		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("5");
				// phoneNumberLabel JLabel에 "5" 글자가 작성
			}
		});

		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("6");
				// phoneNumberLabel JLabel에 "6" 글자가 작성
			}
		});

		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("7");
				// phoneNumberLabel JLabel에 "7" 글자가 작성
			}
		});

		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("8");
				// phoneNumberLabel JLabel에 "8" 글자가 작성
			}
		});

		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("9");
				// phoneNumberLabel JLabel에 "9" 글자가 작성
			}
		});

		btn010.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 첫 숫자 입력일때만 입력가능
				if (phoneNumberLabel.getText().length() == 0) {
					addPhoneNumber("010");
				}
				// phoneNumberLabel JLabel에 "010" 글자가 작성
			}
		});

		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPhoneNumber("0");
				// phoneNumberLabel JLabel에 "0" 글자가 작성
			}
		});

		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pl = phoneNumberLabel.getText().length();
				phoneNumberLabel.setText(phoneNumberLabel.getText().substring(0, pl - 1));
				// substring은 문자열 자르기
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
		
		//전화번호 입력받는 숫자 폰트 지정
		phoneNumberLabel.setFont(new Font("", Font.BOLD, 45));
		
		//X 레이블 네모 라인, 정렬 지정 
		LineBorder lb = new LineBorder(Color.black, 1, true);
		close.setBorder(lb);
		Kiosk.setJLabelAlignmentCenter(close);
		
		//전화번호 버튼들 폰트 지정
		btn1.setFont(new Font("", Font.PLAIN, 50));
		btn2.setFont(new Font("", Font.PLAIN, 50));
		btn3.setFont(new Font("", Font.PLAIN, 50));
		btn4.setFont(new Font("", Font.PLAIN, 50));
		btn5.setFont(new Font("", Font.PLAIN, 50));
		btn6.setFont(new Font("", Font.PLAIN, 50));
		btn7.setFont(new Font("", Font.PLAIN, 50));
		btn8.setFont(new Font("", Font.PLAIN, 50));
		btn9.setFont(new Font("", Font.PLAIN, 50));
		btn010.setFont(new Font("", Font.PLAIN, 20));
		btn0.setFont(new Font("", Font.PLAIN, 50));
		btnDel.setFont(new Font("", Font.PLAIN, 17));
		close.setFont(new Font("", Font.BOLD, 15));
		
		//전화번호 버튼들 배경색 지정
		btn1.setBackground(Color.white);
		btn2.setBackground(Color.white);
		btn3.setBackground(Color.white);
		btn4.setBackground(Color.white);
		btn5.setBackground(Color.white);
		btn6.setBackground(Color.white);
		btn7.setBackground(Color.white);
		btn8.setBackground(Color.white);
		btn9.setBackground(Color.white);
		btn010.setBackground(Color.white);
		btn0.setBackground(Color.white);
		btnDel.setBackground(Color.white);
		/**디자인 끝----------------------------------**/
	}
}