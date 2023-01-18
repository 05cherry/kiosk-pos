package com._team.pos;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com._team.DB.*;

public class StockPanel extends DefaultRightPanel {
	private static final long serialVersionUID = -4608647147048032968L;
	String strSQL = "SELECT * FROM material WHERE code = ? ;";
	JTable stockTable;
	Vector<Vector<String>> rowData;

	StockPanel() {
		setPanel();
		// -----------------------------------top-------------------------------------
		String[] strOption = { "재고 코드", "재고명" };
		JComboBox<String> searchOption = new JComboBox<String>(strOption);

		JTextField searchJTF = new JTextField(50);
		JButton btnSearch = new JButton("검색");

		searchOption.setBounds(56, 45, 154, 29);
		searchJTF.setBounds(225, 45, 347, 29);
		btnSearch.setBounds(573, 45, 59, 29);

		btnSearch.addActionListener((e) -> {
			String inputData = searchJTF.getText();
			Object seletedCBX = searchOption.getSelectedItem();
			if (inputData.isEmpty()) {
				JOptionPane.showMessageDialog(this, "검색할 내용을 입력해주세요", "재고 검색 오류", JOptionPane.WARNING_MESSAGE);
				loadTable();
				return;
			}
			if (seletedCBX.equals("재고 코드")) {
				strSQL = "SELECT * FROM material WHERE code LIKE '%" + inputData + "%' ;";
			} else if (seletedCBX.equals("재고명")) {
				strSQL = "SELECT * FROM material WHERE name LIKE '%" + inputData + "%' ;";
			}
			rowData.clear();
			rowData.addAll(Main.dbc.searchData(strSQL));

			updateTable();
		});

		addComponents(topPanel, searchOption, searchJTF, btnSearch);

		// -----------------------------------table-----------------------------------
		Vector<String> title = Material.getVectorColumnName();
		rowData = Main.dbc.selectMaterialTable();

		DefaultTableModel tmodel = new DefaultTableModel(rowData, title) {
			//더블클릭 방지
	         public boolean isCellEditable(int i, int c){
	                return false;
	          }
	      };
		stockTable = new JTable(tmodel);
		stockTable.getTableHeader().setReorderingAllowed(false);
		stockTable.getTableHeader().setResizingAllowed(false);
		updateTable();
		JScrollPane stockScrollPane = new JScrollPane(stockTable);

		stockScrollPane.setBounds(40, 26, 847, 415);
		stockScrollPane.setViewportView(stockTable);
		stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// -----------------------------------table-----------------------------------
		JButton btnChange, btnAdd, btnDelete;
		btnAdd = new JButton("등록");
		btnChange = new JButton("수정");
		btnDelete = new JButton("삭제");
		btnChange.setBounds(113, 457, 59, 29);
		btnAdd.setBounds(39, 457, 59, 29);
		btnDelete.setBounds(187, 457, 59, 29);
		// TODO create actionListener
		DefaultRightPanel.addComponents(centerPanel, stockScrollPane);
		DefaultRightPanel.addComponents(centerPanel, btnChange, btnAdd, btnDelete);// stockPanel
		DefaultRightPanel.addComponents(this, topPanel, centerPanel);

		// LISTENER
		// [등록]
		btnAdd.addActionListener((e) -> {
			// ------------------------------- 재고 추가창 UI 생성
			// -------------------------------//
			ArrayList<Attr> addStockAttr = new ArrayList<Attr>();
			String givenCode = Main.dbc.getMaxNum("material");
			addStockAttr.add(new Attr("재고 코드", givenCode, MiniWindow.JLABEL));
			addStockAttr.add(new Attr("재 고 명", "", MiniWindow.JTEXTFIELD));
			addStockAttr.add(new Attr("재고 가격", "", MiniWindow.JTEXTFIELD));
			addStockAttr.add(new Attr("유통 기한", "", MiniWindow.JTEXTFIELD));
			addStockAttr.add(new Attr("재고 수량", "", MiniWindow.JTEXTFIELD));

			// addStockPanel
			MiniWindow addStockPanel = new MiniWindow("재고 등록", "등록", "취소", addStockAttr);
			addStockPanel.setBounds(251 + (949 - addStockPanel.getWidth()) / 2,
					112 + (499 - addStockPanel.getHeight()) / 2, addStockPanel.getWidth(), addStockPanel.getHeight());
			JButton btnLeft = addStockPanel.getBtnLeft();
			// ------------------------------- 재고 추가창 DB연결 -------------------------------//
			btnLeft.addActionListener((e2) -> {
				// LinkedHashMap으로 component컬렉션 만듬(MiniWindow에서 컴포넌트 생성)
				LinkedHashMap<String, Component> comp = addStockPanel.getComp();
				// LinkedHashMap으로 sql에 넣을 값 전달(반드시 순서 정해져있어야함)

				String stockName=((JTextField)comp.get("재 고 명")).getText();
				String stockPrice=((JTextField) comp.get("재고 가격")).getText();
				String stockValidDate=((JTextField) comp.get("유통 기한")).getText();
				String stockNum=((JTextField) comp.get("재고 수량")).getText();

				if(stockName.equals("")||stockPrice.equals("")||stockValidDate.equals("")||stockNum.equals("")) {
					JOptionPane.showMessageDialog(this, "입력 정보를 다 넣어주세요", "입력 정보 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!addStockPanel.isNumeric(stockPrice)) {
					JOptionPane.showMessageDialog(this, "숫자만 입력해주세요", "재고 가격 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!addStockPanel.checkDate(stockValidDate)) {
					JOptionPane.showMessageDialog(this, "날짜 형식에 맞지 않습니다 (yyyy-mm-dd)", "유통 기한 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!addStockPanel.isNumeric(stockNum)) {
					JOptionPane.showMessageDialog(this, "숫자만 입력해주세요", "재고 수량 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}

				//INSERT Material
				Material material = new Material(Integer.parseInt(givenCode), stockName, Integer.parseInt(stockPrice), java.sql.Date.valueOf(stockValidDate), Integer.parseInt(stockNum));
				Main.dbc.material = material;
				Main.dbc.stateMaterial = Main.dbc.INSERT;

				addStockPanel.dispose();
			});
		});
		// [수정]
		btnChange.addActionListener((e) -> {
			int row = stockTable.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "수정할 재고를 선택해주세요", "선택 수정 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// JTEXTFIELD의 값을 가져와서 값을 넣어야함.-----------------------------------
			String[] data = new String[stockTable.getColumnCount()];

			for (int i = 0; i < stockTable.getColumnCount(); i++) {
				data[i] = (String) stockTable.getValueAt(row, i);
			}
			// ------------------------------- 재고 수정창 UI 생성-------------------------------//
			ArrayList<Attr> chgStockAttr = new ArrayList<Attr>();
			chgStockAttr.add(new Attr("재고 코드", data[0], MiniWindow.JLABEL));
			chgStockAttr.add(new Attr("재 고 명", data[1], MiniWindow.JTEXTFIELD));
			chgStockAttr.add(new Attr("재고 가격", data[2], MiniWindow.JTEXTFIELD));
			chgStockAttr.add(new Attr("유통 기한", data[3], MiniWindow.JTEXTFIELD));
			chgStockAttr.add(new Attr("재고 수량", data[4], MiniWindow.JTEXTFIELD));

			MiniWindow chgStockPanel = new MiniWindow("재고 수정", "등록", "취소", chgStockAttr);
			chgStockPanel.setBounds(251 + (949 - chgStockPanel.getWidth()) / 2,
					(112 + 611 - chgStockPanel.getHeight()) / 2, chgStockPanel.getWidth(), chgStockPanel.getHeight());
			JButton btnLeft = chgStockPanel.getBtnLeft();

			// ------------------------------- 재고 수정창 DB연결 -------------------------------//
			btnLeft.addActionListener((e2) -> {
				// LinkedHashMap으로 component컬렉션 만듬(MiniWindow에서 컴포넌트 생성)
				LinkedHashMap<String, Component> comp = chgStockPanel.getComp();

				String stockName=((JTextField)comp.get("재 고 명")).getText();
				String stockPrice=((JTextField) comp.get("재고 가격")).getText();
				String stockValidDate=((JTextField) comp.get("유통 기한")).getText();
				String stockNum=((JTextField) comp.get("재고 수량")).getText();

				if(stockName.equals("")||stockPrice.equals("")||stockValidDate.equals("")||stockNum.equals("")) {
					JOptionPane.showMessageDialog(this, "입력 정보를 다 넣어주세요", "입력 정보 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!chgStockPanel.isNumeric(stockPrice)) {
					JOptionPane.showMessageDialog(this, "숫자만 입력해주세요", "재고 가격 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!chgStockPanel.checkDate(stockValidDate)) {
					JOptionPane.showMessageDialog(this, "날짜 형식에 맞지 않습니다 (yyyy-mm-dd)", "유통 기한 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!chgStockPanel.isNumeric(stockNum)) {
					JOptionPane.showMessageDialog(this, "숫자만 입력해주세요", "재고 수량 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}

				//UPDATE Material
				Material material = new Material(Integer.parseInt(((JLabel) comp.get("재고 코드")).getText()), stockName, Integer.parseInt(stockPrice), java.sql.Date.valueOf(stockValidDate), Integer.parseInt(stockNum));
				Main.dbc.material = material;
				Main.dbc.stateMaterial = Main.dbc.UPDATE;

				chgStockPanel.dispose();
			});
		});

		btnDelete.addActionListener((e) -> {
			// 선택된 열이 몇번째 열인지 정수형으로 가져온다.
			int row = stockTable.getSelectedRow();
			// 선택된 열의 index[0]의 값 즉 재품코드값을 가져온다.
			String selectedData = stockTable.getValueAt(row, 0).toString();
			// 만약 테이블에서 재고를 선택하지않았으면 팝업창이 뜨게 한다.
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "삭제할 재고를 선택해주세요", "선택 삭제 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// showConfirmDialog메서드로 팝업창에서 사용자가 선택한 경우를 정수형으로 반환해주고 choice 변수로 받는다.
			int choice = JOptionPane.showConfirmDialog(this, "선택한 재고를 삭제 하시겠습니까?", "선택 삭제",
					JOptionPane.OK_CANCEL_OPTION);
			// OK == 0, cancel == 2, X(팝업 종료) == -1
			if (choice == 0) {
				Material material = null;
				for(Material m : POS.materials) {
					if(String.valueOf(m.getCode()).equals(selectedData)) {
						material = m;
						break;
					}
				}
				//DELETE Material
				Main.dbc.material = material;
				Main.dbc.stateMaterial = Main.dbc.DELETE;
			} else
				return;
		});

		/**디자인 시작-------------------------------**/
		//검색 버튼 배경색, 폰트색 지정
		btnSearch = POS.buttonSetColor(btnSearch);

		//"재고 코드", "재고명", 입력받는 값, 검색 버튼 폰트 지정
		setFonts(new Font("", Font.BOLD, 12), searchOption, searchJTF, btnSearch);

		//"재고 코드"와 "재고명" 입력받는 JTextField 정렬 지정
		searchJTF.setHorizontalAlignment(SwingConstants.RIGHT);

		//등록, 수정, 삭제 버튼 배경색, 폰트색 지정
		btnAdd = POS.buttonSetColor(btnAdd);
		btnChange = POS.buttonSetColor(btnChange);
		btnDelete = POS.buttonSetColor(btnDelete);
		/**디자인 끝----------------------------------**/
	}

	// 데이터베이스를 새로 불러옴
	public void loadTable() {
		rowData.clear();
		rowData.addAll(Main.dbc.selectMaterialTable());
		updateTable();
	}

	// rowdata를 업데이트함
	private void updateTable() {
		stockTable.updateUI();
	}
}
