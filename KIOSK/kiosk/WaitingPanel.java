package com._team.kiosk;

import java.awt.*;
import javax.swing.*;

class WaitingPanel extends JFrame {
	private static final long serialVersionUID = -9196898779796490185L;
	private Container cp;
	private JLabel titleLabel;
	// 결제 대기중 글자 뜰 레이블
	private JLabel timerLabel;
	// __초 남음 글자 뜰 레이블
	private JLabel imgLabel;
	// 눈알 이미지 넣을 레이블

	WaitingPanel() {
		cp = getContentPane();
		setLayout(null);

		setTitle("결제 대기");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(102, 258, 460, 656);

		titleLabel = new JLabel("결제 대기 중...");
		new Thread() {
			// 결제 대기중에 1분동안 시간 뜨게 설정
			public void run() {
				// 타이머를 60초로 설정
				int timer = 5;
				try {
					while (timer > 0) {
						Thread.sleep(1000);
						timer--;
						setTimerLabel(timer + "초 남음");
						// timer+"초 남음" 매개변수를 setTimerLabel() 메소드한테 넘김
					}
					// 타이머 시간 끝나면 창 꺼지고 결제 완료 창인 CompletePayment 클래스로 넘어가게 설정
					dispose();
					new Complete();
				} catch (Exception e) {
				}
			};
		}.start();
		timerLabel = new JLabel();
		imgLabel = new JLabel(new ImageIcon("images/eyes.png"));
		
		titleLabel.setBounds(0, 99, 460, 41);
		timerLabel.setBounds(0, 168, 460, 36);
		imgLabel.setBounds(0, 298, 460, 141);

		add(titleLabel);
		add(timerLabel);
		add(imgLabel);

		setVisible(true);
		
		/**디자인 시작-------------------------------**/
		//전체 배경색 지정
		cp.setBackground(Color.WHITE);
		
		//"결제 대기중...", "타이머 레이블 폰트, 폰트 색상, 정렬 지정
		titleLabel.setFont(new Font("", Font.BOLD, 40));
		timerLabel.setFont(new Font("", Font.BOLD, 30));
		timerLabel.setForeground(Color.gray);
		Kiosk.setJLabelAlignmentCenter(titleLabel);
		Kiosk.setJLabelAlignmentCenter(timerLabel);
		
		//이미지 레이블 정렬 지정
		Kiosk.setJLabelAlignmentCenter(imgLabel);
		/**디자인 끝----------------------------------**/
	}

	public void setTimerLabel(String str) {
		// timer+"초 남음"를 문자열 str로 받아오기
		timerLabel.setText(str);
		// timerLabel에 문자열로 받아온 str을 띄우기
		// setText는 기존에 있는 내용을 지우고 새롭게 세팅해주는 메소드
	}
}