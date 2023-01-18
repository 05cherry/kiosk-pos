package com._team.kiosk;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

class ChoicePanel extends JFrame {
	Container cp;

	ChoicePanel() {
		cp = getContentPane();
		cp.setLayout(null);
		setTitle("매장/포장 주문선택");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(102, 286, 460, 635);

		ImageIcon hereImg = new ImageIcon("images\\here.jpg");
		ImageIcon toGoImg = new ImageIcon("images\\togo.png");

		/**매장 버튼에 들어갈 이미지 크기 조정**/
		// ImageIcon 객체에서 Image 추출
		Image img1 = hereImg.getImage();
		// 추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
		Image updatehere = img1.getScaledInstance(100, 70, Image.SCALE_FAST);
		// 새로운 Image 객체로 ImageIcon 객체 생성
		ImageIcon updateHereImg = new ImageIcon(updatehere);

		/**포장 버튼에 들어갈 이미지 크기 조정**/
		// ImageIcon 객체에서 Image 추출
		Image img2 = toGoImg.getImage();
		// 추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
		Image updatetoGo = img2.getScaledInstance(100, 70, Image.SCALE_FAST);
		// 새로운 Image 객체로 ImageIcon 객체 생성
		ImageIcon updatetoGoImg = new ImageIcon(updatetoGo);

		JButton here = new JButton("매장 주문", updateHereImg);
		JButton toGo = new JButton("포장 주문", updatetoGoImg);

		here.setBounds(0, 0, 460, 300);
		toGo.setBounds(0, 300, 460, 300);

		cp.add(here);
		cp.add(toGo);

		here.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Kiosk.orderMain.setTakeOut(false);
				new ChangeOrderCheckPage();
				dispose();
			}
		});

		toGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Kiosk.orderMain.setTakeOut(true);
				new ChangeOrderCheckPage();
				dispose();
			}
		});

		/** 디자인 시작------------------------------- **/
		//매장/포장 버튼 폰트 지정
		here.setFont(new Font("", Font.BOLD, 50));
		toGo.setFont(new Font("", Font.BOLD, 50));
		
		//매장/포장 버튼 색상 지정
		here.setBackground(Color.white);
		toGo.setBackground(Color.white);
		here.setForeground(Color.red);
		toGo.setForeground(Color.red);
		/** 디자인 끝---------------------------------- **/
	}
}