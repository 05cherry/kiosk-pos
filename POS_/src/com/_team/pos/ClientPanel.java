package com._team.pos;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.regex.Pattern;
// 스윙 라이브러리 사용
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
// DB패키지 안의 자바 클래스 사용
import com._team.DB.Customer;
import com._team.DB.OrderMain;
import com._team.DB.ReceiptNumber;

// 클래스 DefaultRightPanel 을 상속받음
public class ClientPanel extends DefaultRightPanel {
	// serialVersionUID?
	private static final long serialVersionUID = -6007466316954014319L;
	private JTable customersTable;
	private Vector<String> title = new Vector<String>();
	private Vector<Vector<String>> rowData = new Vector<Vector<String>>();
	private JScrollPane clientScrollPane;

	ClientPanel() {
		// 전체 패널 세팅
		setPanel();
		// 고객 테이블 세팅 & 업데이트
		initCustomersTable();
		// topPanel
		// 콤보박스 옵션값 설정
		String[] strOption = { "고객 코드", "전화 번호", "포인트" };
		JComboBox<String> searchOpction = new JComboBox<String>(strOption);

		JTextField serachJTF = new JTextField();
		JButton btnSearch = new JButton("검색");

		// 컴포넌트 배치
		searchOpction.setBounds(56, 45, 154, 29);
		serachJTF.setBounds(225, 45, 347, 29);
		btnSearch.setBounds(573, 45, 59, 29);
		// 버튼 눌리면 벌어지는 액선리스너 익명클래스로 작성
		btnSearch.addActionListener((e) -> {
			String inputData = serachJTF.getText();
			Object seletedCBX = searchOpction.getSelectedItem();
			// String strSQL;
			String strSQL = null;
			if (inputData.isEmpty()) {
				loadTable();
				JOptionPane.showMessageDialog(this, "검색할 내용을 입력해주세요", "고객 검색 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// 기본 검색 쿼리
			strSQL = "SELECT * FROM customer";
			// '고객 코드'
			if (seletedCBX.equals("고객 코드")) {
				if (Pattern.matches("^[0-9]*$", inputData))
					strSQL += " WHERE code LIKE '%" + inputData + "%' ;";
				else
					JOptionPane.showMessageDialog(this, "고객 코드만 입력해주세요", "고객 검색 오류", JOptionPane.WARNING_MESSAGE);
				// '전화 번호'
			} else if (seletedCBX.equals("전화 번호")) {
				if (Pattern.matches("^\\d{1,11}$", inputData))
					strSQL += " WHERE replace(phone,'-','') LIKE '%" + inputData + "%' ;";
				else
					JOptionPane.showMessageDialog(this, "전화 번호만 입력해주세요", "고객 검색 오류", JOptionPane.WARNING_MESSAGE);
				// '포인트'
			} else if (seletedCBX.equals("포인트")) {
				if (Pattern.matches("^[0-9]*$", inputData))
					strSQL += " WHERE point = " + inputData + " ;";
				else
					JOptionPane.showMessageDialog(this, "포인트는 숫자만 입력해주세요", "고객 검색 오류", JOptionPane.WARNING_MESSAGE);
			}
			// 테이블 데이터 클리어 > 다시 검색 결과 붙이기
			rowData.clear();
			rowData.addAll(Main.dbc.searchData(strSQL));

			if (rowData.isEmpty()) {
				JOptionPane.showMessageDialog(this, "검색된 데이터가 없습니다.", "고객 검색 오류", JOptionPane.WARNING_MESSAGE);
				loadTable();
			}
			// 검색 끝나면 테이블 업데이트
			updateTable();
		});

		addComponents(topPanel, searchOpction, serachJTF, btnSearch);

		// centerPanel
		// 고객 정보 설정창
		JPanel clientDataUpdate;
		clientDataUpdate = new ClientDataUpdate() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Object obj = ae.getSource();
				// customer 찾기
				Customer customer = null;
				for (Customer c : POS.customers) {
					if (clientCode.equals(String.valueOf(c.getCode()))) {
						customer = c;
					}
				}
				if (obj.equals(btnDelete)) {
					// 삭제 여부 조사
					int choice = JOptionPane.showConfirmDialog(this, "선택한 고객 정보를 삭제 하시겠습니까?", "선택 삭제",
							JOptionPane.OK_CANCEL_OPTION);
					// OK == 0, cancel == 2, X(팝업 종료) == -1
					if (choice == 0) {
						// OrderMain
						for (OrderMain om : POS.orderMains) {
							// receiptNumbers의 주문번호가 외래키로 연결된 오더메인 코드와 같으면
							for (ReceiptNumber rn : POS.receiptNumbers) {
								if (om.getCode() == rn.getOrderCode()) {
									// TODO 스택으로 만들기
									// DELETE ReceiptNumber
									Main.dbc.receiptNumber = rn;
									Main.dbc.stateCustomer = Main.dbc.DELETE;
								}
							}
						}
						Main.dbc.customer = customer;
						Main.dbc.stateCustomer = Main.dbc.DELETE;
					} else
						return;
				} else if (obj.equals(btnUpdate)) {
					customer.setPhone(phoneNumJTF.getText().toString());
					customer.setPoint(Integer.parseInt(pointJTF.getText()));
					Main.dbc.customer = customer;
					Main.dbc.stateCustomer = Main.dbc.UPDATE;
				}
			}
		};
		// 테이블 내 데이터가 선택되었을 때
		customersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ae) {
				int row = customersTable.getSelectedRow();
				// 데이터를 문자열로 가져오
				String[] data = new String[customersTable.getColumnCount()];

				for (int i = 0; i < customersTable.getColumnCount(); i++) {
					data[i] = (String) customersTable.getValueAt(row, i);
				}
				((ClientDataUpdate) clientDataUpdate).setCustomerInfo(data);
			}

		});

		addComponents(centerPanel, clientScrollPane, clientDataUpdate);
		addComponents(this, topPanel, centerPanel, bottomPanel);

		/** 디자인 시작------------------------------- **/
		// 검색 버튼 배경색, 폰트색 지정
		btnSearch = POS.buttonSetColor(btnSearch);

		// 레이블들 폰트 지정
		setFonts(new Font("", Font.BOLD, 12), searchOpction, serachJTF, btnSearch);
		serachJTF.setHorizontalAlignment(SwingConstants.RIGHT);
		/** 디자인 끝---------------------------------- **/
	}

	private void initCustomersTable() {
		// 컬럼명 불러오기
		title = Customer.getVectorColumnName();

		// 테이블에 들어갈 rowData 불러오기
		rowData.addAll(Main.dbc.selectCustomerTable());
		rowData.remove(0);

		// DefaultTableModel 생성
		DefaultTableModel tmodel = new DefaultTableModel(rowData, title);
		// 테이블에 데이터 삽입
		customersTable = new JTable(tmodel);

		// 테이블 설정 & 업데이트
		customersTable.getTableHeader().setReorderingAllowed(false);
		customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customersTable.updateUI();

		// 스크롤에 세팅항 테이블 적용
		clientScrollPane = new JScrollPane(customersTable);
		// 스크롤 설정
		clientScrollPane.setBounds(56, 90, 478, 382);
		clientScrollPane.setViewportView(customersTable);
	}

	// 디비에서 전체 데이터를 다시 가져옴
	public void loadTable() {
		rowData.clear();
		rowData.addAll(Main.dbc.selectCustomerTable());
		updateTable();
	}

	// rowdata를 업데이트하고 테이블만 업데이트
	private void updateTable() {
		// 코드가 -1인 값을 제거
		for (Vector<String> v : rowData) {
			if (v.get(0).equals("-1")) {
				rowData.remove(v);
				break;
			}
		}
		customersTable.updateUI();
	}
}

// 고객 정보 설정창
class ClientDataUpdate extends JPanel implements ActionListener {
	private static final long serialVersionUID = -2364144317109677973L;
	String clientCode = "";
	JTextField phoneNumJTF, pointJTF;
	JButton btnDelete, btnUpdate;
	JLabel clientCodeLabel;

	ClientDataUpdate() {
		setLayout(null);
		setBounds(573, 90, 353, 382);

		JLabel titleLabel, phoneNumLabel, pointLabel;

		titleLabel = new JLabel("고객 정보 수정");
		clientCodeLabel = new JLabel("고객 코드  :");
		phoneNumLabel = new JLabel("전화 번호  :");
		pointLabel = new JLabel("포  인  트  :");
		phoneNumJTF = new JTextField();
		pointJTF = new JTextField();
		btnDelete = new RoundedButton("삭제");
		btnUpdate = new RoundedButton("수정");

		phoneNumJTF.setHorizontalAlignment(SwingConstants.RIGHT);
		pointJTF.setHorizontalAlignment(SwingConstants.RIGHT);

		titleLabel.setBounds(88, 21, 206, 30);
		clientCodeLabel.setBounds(25, 84, 302, 20);
		phoneNumLabel.setBounds(25, 148, 106, 20);
		pointLabel.setBounds(25, 216, 106, 20);
		phoneNumJTF.setBounds(131, 144, 195, 29);
		pointJTF.setBounds(131, 211, 195, 29);
		btnDelete.setBounds(95, 318, 79, 29);
		btnUpdate.setBounds(178, 318, 79, 29);

		DefaultRightPanel.addComponents(this, titleLabel, clientCodeLabel, phoneNumLabel, pointLabel, phoneNumJTF,
				pointJTF, btnDelete, btnUpdate);
		// 현재 객체에서 리스너 설정
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);

		/** 디자인 시작------------------------------- **/
		// 고객 정보 수정창 배경색 지정
		setBackground(Color.white);

		// "고객 정보 수정" 레이블 폰트 지정
		titleLabel.setFont(new Font("", Font.BOLD, 25));

		// 고객코드, 전화번호, 포인트 등 레이블들 폰트 지정
		DefaultRightPanel.setFonts(new Font("", Font.PLAIN, 20), clientCodeLabel, phoneNumLabel, pointLabel,
				phoneNumJTF, pointJTF, btnDelete, btnUpdate);
		/** 디자인 끝---------------------------------- **/
	}

	// 컴포넌트에 있는 값들을 고객 문자열 배열로 받아오는 메서드
	// 외부에서 쓰면 컴포넌트가 현재 객체 안에 생성되어 있어서 바로 받아올 수 없기 때문에
	void setCustomerInfo(String[] data) {
		clientCode = data[0];
		clientCodeLabel.setText("고객 코드  :                         " + clientCode);
		phoneNumJTF.setText(data[1]);
		pointJTF.setText(data[2]);
	}

	// 액션 리스너의 메서드
	@Override
	public void actionPerformed(ActionEvent ae) {

	}
}
