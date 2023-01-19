package com._team.pos;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com._team.DB.*;

public class CalculatorPanel extends DefaultRightPanel {
	private static final long serialVersionUID = -1400935350276332201L;
	private JTextField jtfResult;
	private JTextField jtf;
	private JLabel jl3;
	public void updateCash() {
		// 숫자 포맷 설정
		DecimalFormat decFormat1 = new DecimalFormat("###,###");
		// 현금 잔고 불러오기
		String cash = decFormat1.format(POS.cashs.get(0).getCurrentCash());
		jl3.setText(cash+"원");
		jtf.setText("");
	}
	public CalculatorPanel() {
		// set panel
		setPanel();
		
		// topPanel
		JLabel btnCalTitle;
		btnCalTitle = new JLabel("POS기 현금 관리");
		btnCalTitle.setBounds(300, 36, 347, 41);

		topPanel.add(btnCalTitle);
		// centerPanel
		JLabel jl1, jl2, jl4;
		RoundedButton jb1;
		jl1 = new JLabel();
		jl2 = new JLabel("현재 현금 잔액");
		jl3 = new JLabel("");
		jl4 = new JLabel("원");
		jtf = new JTextField("");
		jb1 = new RoundedButton("추가");
		updateCash();
		jl1.setBounds(99, 138, 376, 153);
		jl2.setBounds(160, 164, 253, 39);
		jl3.setBounds(191, 219, 191, 39);
		jtf.setBounds(99, 304, 244, 43);
		jl4.setBounds(353, 311, 30, 30);
		jb1.setBounds(401, 304, 74, 43);

		// jtf 문자 입력 이벤트
		jtf.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// 백스페이스 사용시 검사하지 않고 종료
				if (e.getKeyChar() == '')
					return;
				int jtfLen = jtf.getText().length();
				String pattern;
				String val = String.valueOf(e.getKeyChar());
				if (jtfLen == 1) { // 첫번째 문자일 때 숫자나 - 입력 가능
					pattern = "^[0-9]|-$";
				} else { // 두번째 문자부터 숫자만 입력가능
					pattern = "^[0-9]";
				}
				// 조건에 맞지 않을 시 입력한 텍스트를 삭제
				if (!Pattern.matches(pattern, val)) {
					jtf.setText(jtf.getText().substring(0, jtf.getText().length() - 1));
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		jb1.addActionListener((e) -> {
			// jll3에서 숫자만 추출
			String cashStr = jl3.getText().replaceAll("[^0-9]", "");
			int addCash = Integer.parseInt(cashStr) + Integer.parseInt(jtf.getText());
			// DB 업데이트
			Main.dbc.cash = new Cash(1234, addCash);
			Main.dbc.stateCash = Main.dbc.UPDATE;
		});

		// calcualtorPanel
		JPanel calPanel = new JPanel();
		calPanel.setLayout(null);
		calPanel.setBounds(582, 58, 332, 398);
		calPanel.setBackground(Color.WHITE);

		JButton btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, btnNum0, btnEqual,
				btnDel, btnAdd, btnSub, btnMul, btnDiv;

		btnNum1 = new JButton("1");
		btnNum2 = new JButton("2");
		btnNum3 = new JButton("3");
		btnNum4 = new JButton("4");
		btnNum5 = new JButton("5");
		btnNum6 = new JButton("6");
		btnNum7 = new JButton("7");
		btnNum8 = new JButton("8");
		btnNum9 = new JButton("9");
		btnNum0 = new JButton("0");
		btnEqual = new JButton("=");
		btnDel = new JButton("DEL");
		btnAdd = new JButton("+");
		btnSub = new JButton("-");
		btnMul = new JButton("*");
		btnDiv = new JButton("/");
		jtfResult = new JTextField("");
		
		btnNum1.setBounds(0, 71, 69, 69);
		btnNum2.setBounds(87, 71, 69, 69);
		btnNum3.setBounds(174, 71, 69, 69);
		btnNum4.setBounds(0, 156, 69, 69);
		btnNum5.setBounds(87, 156, 69, 69);
		btnNum6.setBounds(174, 156, 69, 69);
		btnNum7.setBounds(0, 241, 69, 69);
		btnNum8.setBounds(87, 241, 69, 69);
		btnNum9.setBounds(174, 241, 69, 69);
		btnNum0.setBounds(87, 326, 69, 69);
		btnEqual.setBounds(0, 326, 69, 69);
		btnDel.setBounds(174, 326, 69, 69);
		btnAdd.setBounds(264, 71, 69, 69);
		btnSub.setBounds(264, 156, 69, 69);
		btnMul.setBounds(264, 241, 69, 69);
		btnDiv.setBounds(264, 326, 69, 69);
		jtfResult.setBounds(0, 0, 332, 52);

		// addActionListeners
		btnNum1.addActionListener((e) -> {
			appendFormula("1");
		});
		btnNum2.addActionListener((e) -> {
			appendFormula("2");
		});
		btnNum3.addActionListener((e) -> {
			appendFormula("3");
		});
		btnNum4.addActionListener((e) -> {
			appendFormula("4");
		});
		btnNum5.addActionListener((e) -> {
			appendFormula("5");
		});
		btnNum6.addActionListener((e) -> {
			appendFormula("6");
		});
		btnNum7.addActionListener((e) -> {
			appendFormula("7");
		});
		btnNum8.addActionListener((e) -> {
			appendFormula("8");
		});
		btnNum9.addActionListener((e) -> {
			appendFormula("9");
		});
		btnNum0.addActionListener((e) -> {
			appendFormula("0");
		});
		btnEqual.addActionListener((e) -> {
			resultFormula();
		});
		btnDel.addActionListener((e) -> {
			deleteFormula();
		});
		btnAdd.addActionListener((e) -> {
			appendFormula("+");
		});
		btnSub.addActionListener((e) -> {
			appendFormula("-");
		});
		btnMul.addActionListener((e) -> {
			appendFormula("*");
		});
		btnDiv.addActionListener((e) -> {
			appendFormula("/");
		});

		addComponents(calPanel, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9,
				btnNum0, btnEqual, btnDel, btnAdd, btnSub, btnMul, btnDiv, jtfResult);

		// 밑에 깔리는 컴포넌트는 위에 표시되는 컴포넌트보다 나중에 추가해야 함.
		addComponents(centerPanel, jl2, jl3, jl4, jtf, jb1, calPanel, jl1);

		// bottomPanel

		// add panels
		addComponents(this, topPanel, centerPanel, bottomPanel);
		
		/**디자인 시작-------------------------------**/
		//POS기 현금 관리 레이블 폰트 지정
		btnCalTitle.setFont(new Font("", Font.PLAIN, 40));
		
		//"현재 현금 잔액", ___원 들어있는 네모 라인 지정, 배경색 지정
		jl1.setBorder(new LineBorder(Color.gray, 1, true));
		jl1.setOpaque(true);
		jl1.setBackground(Color.WHITE);
		
		//입력받는 JTextField 배경색, 정렬 지정
		jtf.setBackground(Color.WHITE);
		jtf.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//추가 버튼 배경색 지정
		jb1.setBackground(Color.LIGHT_GRAY);
		
		//레이블들 폰트 지정
		jl2.setFont(new Font("", Font.PLAIN, 38));
		jl3.setFont(new Font("", Font.PLAIN, 38));
		jl4.setFont(new Font("", Font.PLAIN, 29));
		jtf.setFont(new Font("", Font.PLAIN, 29));
		jb1.setFont(new Font("", Font.PLAIN, 20));
		
		//계산기 버튼 배경색 정의
		Color backgroundColor = new Color(59,59,59);
		
		//계산기 버튼 배경색 지정
		btnNum1.setBackground(backgroundColor);
		btnNum2.setBackground(backgroundColor);
		btnNum3.setBackground(backgroundColor);
		btnNum4.setBackground(backgroundColor);
		btnNum5.setBackground(backgroundColor);
		btnNum6.setBackground(backgroundColor);
		btnNum7.setBackground(backgroundColor);
		btnNum8.setBackground(backgroundColor);
		btnNum9.setBackground(backgroundColor);
		btnNum0.setBackground(backgroundColor);
		btnEqual.setBackground(backgroundColor);
		btnDel.setBackground(backgroundColor);
		btnAdd.setBackground(backgroundColor);
		btnSub.setBackground(backgroundColor);
		btnMul.setBackground(backgroundColor);
		btnDiv.setBackground(backgroundColor);
		
		//계산기 버튼 폰트색 지정
		btnNum1.setForeground(Color.white);
		btnNum2.setForeground(Color.white);
		btnNum3.setForeground(Color.white);
		btnNum4.setForeground(Color.white);
		btnNum5.setForeground(Color.white);
		btnNum6.setForeground(Color.white);
		btnNum7.setForeground(Color.white);
		btnNum8.setForeground(Color.white);
		btnNum9.setForeground(Color.white);
		btnNum0.setForeground(Color.white);
		btnEqual.setForeground(Color.white);
		btnDel.setForeground(Color.white);
		btnAdd.setForeground(Color.white);
		btnSub.setForeground(Color.white);
		btnMul.setForeground(Color.white);
		btnDiv.setForeground(Color.white);
		
		//계산기 버튼 폰트 지정
		setFonts(new Font("", Font.PLAIN, 29), btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8,
				btnNum9, btnNum0, btnEqual, btnAdd, btnSub, btnMul, btnDiv, jtfResult);
		btnDel.setFont(new Font("", Font.PLAIN, 17));
		
		//계산기 입력받은 값 정렬 지정
		jtfResult.setHorizontalAlignment(SwingConstants.RIGHT);
		/**디자인 끝----------------------------------**/
	}

	private void updateResult() {
		String result = "";
		for (int i = 0; i < formula.size(); i++)
			result += formula.get(i);
		result += sb.toString();
		jtfResult.setText(result);
	}

	ArrayList<String> formula = new ArrayList<>();
	ArrayList<String> postfix = new ArrayList<>();
	Stack<String> opStack = new Stack<>();
	StringBuilder sb = new StringBuilder();

	private void appendFormula(String f) {
		// 연산자가 들어왔을 때
		if (f.equals("+") || f.equals("-") || f.equals("*") || f.equals("/")) {
			// 이전에 들어온 수식이 없을 때
			if (formula.size() == 0) {
				// 이전에 입력된 숫자가 없을 때 연산자 입력 받지 않음
				if (sb.toString().equals("")) {
					return;
				} else {// 이전에 입력된 숫자가 있을 때 이전에 입력된 숫자 추가 후 연산자 추가
					formula.add(sb.toString());
					formula.add(f);
					sb.delete(0, sb.length());
					updateResult();
					return;
				}
			}
			// 이전에 들어온 완성된 수식이 있을 때
			if (!sb.toString().equals("")) {// 이전에 들어온 숫자가 있을 때 이전에 입력된 숫자 추가 후 연산자 추가
				formula.add(sb.toString());
				formula.add(f);
				sb.delete(0, sb.length());
				updateResult();
				return;
			} else {// 이전에 들어온 숫자가 없을 때 이전에 입력된 연산자 변경
				formula.set(formula.size() - 1, f);
				updateResult();
				return;
			}
		}
		// 연산자가 들어오지 않았을때 숫자 계속 추가
		sb.append(f);
		updateResult();
		return;
	}

	private void deleteFormula() {
		if (sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		} else if (formula.size() != 0) {
			formula.remove(formula.size() - 1);
		}
		updateResult();
	}

	private void resultFormula() {
		// 수식에 숫자만 들어와 있을 때 계산하지 않고 끝냄.
		if (formula.size() == 0)
			return;
		// 수식 마지막에 연산자가 입력되었을 시 계산하지 않고 끝냄.
		String lastFormula = formula.get(formula.size() - 1);
		if (sb.length() == 0 && (lastFormula.equals("+") || lastFormula.equals("-") || lastFormula.equals("*")
				|| lastFormula.equals("/")))
			return;
		// 수식 마지막에 들어온 숫자를 스택에 추가
		formula.add(sb.toString());
		sb.delete(0, sb.length());

		// 후위 표기법 준비
		for (int i = 0; i < formula.size(); i++) {
			String tmp = formula.get(i);
			if (tmp.equals("+") || tmp.equals("-") || tmp.equals("*") || tmp.equals("/")) {
				if (opStack.size() == 0) {
					opStack.push(tmp);
				} else {
					while (priorityCheck(tmp) <= priorityCheck(opStack.peek())) {
						postfix.add(opStack.pop());
						if (opStack.size() == 0)
							break;
					}
					opStack.push(tmp);
				}
			} else {
				postfix.add(tmp);
			}
		}
		// 남은 연산자 입력
		int opStackSize = opStack.size();
		for (int i = 0; i < opStackSize; i++) {
			postfix.add(opStack.pop());
		}
		// 후위 표기법 사용
		for (int i = 2; i < postfix.size(); i++) {
			double num1;
			double num2;
			switch (postfix.get(i)) {
			case "+":
				num1 = Double.parseDouble(postfix.get(i - 2));
				num2 = Double.parseDouble(postfix.get(i - 1));
				// 2개의 숫자의 1개의 연산자로 연산후 1개의 결과를 반환, ArrayList 공간 3개중 1개만 남김
				postfix.set(i, Double.toString((num1 + num2)));
				postfix.remove(--i);
				postfix.remove(--i);
				break;
			case "-":
				num1 = Double.parseDouble(postfix.get(i - 2));
				num2 = Double.parseDouble(postfix.get(i - 1));
				postfix.set(i, Double.toString((num1 - num2)));
				postfix.remove(--i);
				postfix.remove(--i);
				break;
			case "*":
				num1 = Double.parseDouble(postfix.get(i - 2));
				num2 = Double.parseDouble(postfix.get(i - 1));
				postfix.set(i, Double.toString((num1 * num2)));
				postfix.remove(--i);
				postfix.remove(--i);
				break;
			case "/":
				num1 = Double.parseDouble(postfix.get(i - 2));
				num2 = Double.parseDouble(postfix.get(i - 1));
				postfix.set(i, Double.toString((num1 / num2)));
				postfix.remove(--i);
				postfix.remove(--i);
				break;
			}
		}

		// 수식 초기화
		formula.clear();
		// 계산된 수식 입력
		DecimalFormat df = new DecimalFormat("#.###");
		appendFormula(df.format(Double.parseDouble(postfix.get(0))));
		// 계산된 수식 화면에 업데이트
		updateResult();
		// 스택 초기화
		postfix.clear();

	}

	public int priorityCheck(String op) {
		switch (op) {
		case ("+"):
			return 1;

		case ("-"):
			return 1;

		case ("*"):
			return 2;

		case ("/"):
			return 2;

		default:
			return -1;
		}
	}
}