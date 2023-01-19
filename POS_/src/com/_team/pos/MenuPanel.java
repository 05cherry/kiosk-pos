package com._team.pos;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com._team.DB.Customer;
import com._team.DB.Product;

public class MenuPanel extends DefaultRightPanel {
	private static final long serialVersionUID = 3924123086486276257L;
	String strSQL = "SELECT * FROM product WHERE code = ? ;";
	JTable menuTable;
	Vector<String> tempRow = new Vector<>();
	Vector<Vector<String>> rowData;
	// 검색된 값이 숫자인지 문자인지 확인하기 위한 변수

	MenuPanel() {
		setPanel();
		// -----------------------------------top-------------------------------------
		String[] strOption = { "상품 코드", "상품명" };
		JComboBox<String> searchOption = new JComboBox<String>(strOption);

		JTextField searchJTF = new JTextField(50);
		JButton btnSearch = new JButton("검색");

		searchOption.setBounds(56, 45, 154, 29);
		searchJTF.setBounds(225, 45, 347, 29);
		btnSearch.setBounds(573, 45, 59, 29);

		// [검색] 리스너
		btnSearch.addActionListener((e) -> {
			String inputData = searchJTF.getText();
			Object seletedCBX = searchOption.getSelectedItem();
			if (inputData.isEmpty()) {
				JOptionPane.showMessageDialog(this, "검색할 내용을 입력해주세요", "상품 검색 오류", JOptionPane.WARNING_MESSAGE);
				loadTable();
				return;
			}

			if (seletedCBX.equals("상품 코드")) {
				strSQL = "SELECT * FROM product WHERE code LIKE '%" + inputData + "%' ;";
			} else if (seletedCBX.equals("상품명")) {
				strSQL = "SELECT * FROM product WHERE name LIKE '%" + inputData + "%' ;";
			}
			rowData.clear();
			rowData.addAll(Main.dbc.searchData(strSQL));
			updateTable();
		});
		addComponents(topPanel, searchOption, searchJTF, btnSearch);

		// -----------------------------------table-----------------------------------
		Vector<String> title = Product.getVectorColumnName();
		rowData = Main.dbc.selectProductTable();

		DefaultTableModel tmodel = new DefaultTableModel(rowData, title) {
			//더블클릭 방지
	         public boolean isCellEditable(int i, int c){
	                return false;
	          }
	      };
		menuTable = new JTable(tmodel);
		menuTable.getTableHeader().setReorderingAllowed(false);
		menuTable.getTableHeader().setResizingAllowed(false);
		updateTable();
		JScrollPane stockScrollPane = new JScrollPane(menuTable);

		stockScrollPane.setBounds(40, 26, 847, 415);
		stockScrollPane.setViewportView(menuTable);
		menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// -----------------------------------table-----------------------------------

		JButton btnChange, btnAdd, btnDelete;
		btnAdd = new JButton("등록");
		btnChange = new JButton("수정");
		btnDelete = new JButton("삭제");
		btnChange.setBounds(113, 457, 59, 29);
		btnAdd.setBounds(39, 457, 59, 29);
		btnDelete.setBounds(187, 457, 59, 29);

		addComponents(centerPanel, btnChange, btnAdd, btnDelete, stockScrollPane);
		addComponents(this, topPanel, centerPanel);

		// LISTENER
		// [등록]
		btnAdd.addActionListener((e) -> {
			// ------------------------------- 등록 UI 생성 -------------------------------//
			String givenCode = Main.dbc.getMaxNum("product");
			ArrayList<Attr> addMenuAttr = new ArrayList<Attr>();
			addMenuAttr.add(new Attr("상품 코드", givenCode, MiniWindow.JLABEL));
			addMenuAttr.add(new Attr("상 품 명", "", MiniWindow.JTEXTFIELD));
			addMenuAttr.add(new Attr("카테 고리", "Coffee ColdBrew Tea TeaVariation FruitTea Coffee", MiniWindow.JCOMBOBOX));
			addMenuAttr.add(new Attr("상품 가격", "", MiniWindow.JTEXTFIELD));
			addMenuAttr.add(new Attr("상품 이미지", "", MiniWindow.IMAGEICON));
			addMenuAttr.add(new Attr("품    절", "", MiniWindow.YES_OR_NO));

			MiniWindow addMenuPanel = new MiniWindow("상품 등록", "등록", "취소", addMenuAttr);
			addMenuPanel.setBounds(251 + (949 - addMenuPanel.getWidth()) / 2,
					112 + (499 - addMenuPanel.getHeight()) / 2, addMenuPanel.getWidth(), addMenuPanel.getHeight());
			JButton btnLeft = addMenuPanel.getBtnLeft();

			// ------------------------------- 등록 DB 연결 -------------------------------//
			btnLeft.addActionListener((e2) -> {
				strSQL = "INSERT INTO product VALUES (?,?,?,?,?,?)";
				// LinkedHashMap으로 component컬렉션 만듬(MiniWindow에서 컴포넌트 생성)
				LinkedHashMap<String, Component> comp = addMenuPanel.getComp();

				//TODO 유효성 검사 정규표현식으로 하기!!
				String menuName=((JTextField)comp.get("상 품 명")).getText();
				String menuPrice=((JTextField) comp.get("상품 가격")).getText();
				String menuCategory = ((JComboBox<String>) comp.get("카테 고리")).getSelectedItem().toString(); // key: 카테 고리
				String menuImgSrc = addMenuPanel.getImgSrcPath();

				if(menuName.equals("")||menuPrice.equals("")|| menuCategory.equals("") || menuImgSrc.equals("")) {
					JOptionPane.showMessageDialog(this, "입력 정보를 다 넣어주세요", "입력 정보 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!addMenuPanel.isNumeric(menuPrice)) {
					JOptionPane.showMessageDialog(this, "숫자만 입력해주세요", "상품 가격 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//INSERT Product
				Product product = new Product(Integer.parseInt(givenCode),menuName,(Integer) convertCategoryCode(menuCategory),Integer.parseInt(menuPrice),menuImgSrc,(Boolean) (((JToggleButton) comp.get("품    절")).isSelected()));
				Main.dbc.product = product;
				Main.dbc.stateProduct = Main.dbc.INSERT;
				
				addMenuPanel.copyImagesFile();
				addMenuPanel.dispose();
			});
		});
		// [수정]
		btnChange.addActionListener((e) -> {

			int row = menuTable.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "수정할 상품을 선택해주세요", "선택 수정 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// JTEXTFIELD의 값을 가져와서 값을 넣어야함.-----------------------------------
			String[] data = new String[menuTable.getColumnCount()];

			for (int i = 0; i < menuTable.getColumnCount(); i++) {
				data[i] = (String) menuTable.getValueAt(row, i);
			}
			// ------------------------------- 상품 수정창 UI 생성-------------------------------//
			ArrayList<Attr> chgMenuAttr = new ArrayList<Attr>();
			chgMenuAttr.add(new Attr("상품 코드", data[0], MiniWindow.JLABEL));
			chgMenuAttr.add(new Attr("상 품 명", data[1], MiniWindow.JTEXTFIELD));
			chgMenuAttr.add(new Attr("카테 고리", "Coffee ColdBrew Tea TeaVariation FruitTea "+data[2], MiniWindow.JCOMBOBOX));
			chgMenuAttr.add(new Attr("상품 가격", data[3], MiniWindow.JTEXTFIELD));
			chgMenuAttr.add(new Attr("상품 이미지", data[4], MiniWindow.IMAGEICON));
			chgMenuAttr.add(new Attr("품    절", data[5], MiniWindow.YES_OR_NO));

			MiniWindow chgMenuPanel = new MiniWindow("상품 수정", "수정", "취소", chgMenuAttr);
			chgMenuPanel.setBounds(251 + (949 - chgMenuPanel.getWidth()) / 2,
					112 + (499 - chgMenuPanel.getHeight()) / 2, chgMenuPanel.getWidth(), chgMenuPanel.getHeight());
			JButton btnLeft = chgMenuPanel.getBtnLeft();

			// ------------------------------- 상품 수정창 DB연결 -------------------------------//
			btnLeft.addActionListener((e2) -> {
				// LinkedHashMap으로 component컬렉션 만듬(MiniWindow에서 컴포넌트 생성)
				LinkedHashMap<String, Component> comp = chgMenuPanel.getComp();

				String menuCode = ((JLabel) comp.get("상품 코드")).getText();
				String menuName=((JTextField)comp.get("상 품 명")).getText();
				String menuPrice=((JTextField) comp.get("상품 가격")).getText();
				String menuCategory = ((JComboBox<String>) comp.get("카테 고리")).getSelectedItem().toString();
				String menuImgSrc = chgMenuPanel.getImgSrcPath();
				Boolean menuSoldOut = ((JToggleButton) comp.get("품    절")).isSelected();

				if(menuName.equals("")||menuPrice.equals("")) {
					JOptionPane.showMessageDialog(this, "입력 정보를 다 넣어주세요", "입력 정보 오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (!chgMenuPanel.isNumeric(menuPrice)) {
					JOptionPane.showMessageDialog(this, "숫자만 입력해주세요", "상품 가격 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//UPDATE Product
				Product product = new Product(Integer.parseInt(menuCode),menuName,(Integer) convertCategoryCode(menuCategory),Integer.parseInt(menuPrice),menuImgSrc, menuSoldOut);
				Main.dbc.product = product;
				Main.dbc.stateProduct = Main.dbc.UPDATE;
				
				chgMenuPanel.copyImagesFile();
				chgMenuPanel.dispose();
			});
		});

		btnDelete.addActionListener((e) -> {
			int row = menuTable.getSelectedRow();
			// 선택된 열의 index[0]의 값 즉 재품코드값을 가져온다.
			String selectedData = menuTable.getValueAt(row, 0).toString();
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "삭제할 상품을 선택해주세요", "선택 삭제 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int choice = JOptionPane.showConfirmDialog(this, "선택한 상품을 삭제 하시겠습니까?", "선택 삭제",
					JOptionPane.OK_CANCEL_OPTION);
			// OK == 0, cancel == 2, X(팝업 종료) == -1
			if (choice == 0) {
				Product product = null;
				for(Product p : POS.products) {
					if(String.valueOf(p.getCode()).equals(selectedData)) {
						product = p;
						break;
					}
				}
				//DELETE Product
				Main.dbc.product = product;
				Main.dbc.stateProduct = Main.dbc.DELETE;
			} else {
				return;
			}
		});
		
		/**디자인 시작-------------------------------**/
		//검색 버튼 배경색, 폰트색 지정
		btnSearch = POS.buttonSetColor(btnSearch);
		
		//레이블들 폰트 지정
		setFonts(new Font("", Font.BOLD, 12), searchOption, searchJTF, btnSearch);
		
		//상품코드, 상품명 검색하는 JTextField 정렬 지정
		searchJTF.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//하단의 등록, 수정, 삭제 버튼 배경색, 폰트색 지정
		btnAdd = POS.buttonSetColor(btnAdd);
		btnChange = POS.buttonSetColor(btnChange);
		btnDelete = POS.buttonSetColor(btnDelete);
		/**디자인 끝----------------------------------**/
	}

	private int convertCategoryCode(String category) {
		int code;
		if (category.equals("Coffee"))
			code = 2000;
		else if (category.equals("ColdBrew"))
			code = 2001;
		else if (category.equals("Tea"))
			code = 2002;
		else if (category.equals("TeaVariation"))
			code = 2003;
		else if (category.equals("FruitTea"))
			code = 2004;
		else
			code = 0;
		return code;
	}
	private String convertCategoryCode(int category) {
		String code;
		if (category == 2000)
			code = "Coffee";
		else if (category == 2001)
			code = "ColdBrew";
		else if (category == 2002)
			code = "Tea";
		else if (category == 2003)
			code = "TeaVariation";
		else if (category == 2004)
			code = "FruitTea";
		else
			code = null;
		return code;
	}

	// 데이터베이스를 새로 불러옴
	public void loadTable() {
		rowData.clear();
		rowData.addAll(Main.dbc.selectProductTable());
		updateTable();
	}

	//rowdata를 업데이트
	private void updateTable() {
		for (int i = 0; i < rowData.size(); i++) {
			int category = Integer.parseInt(rowData.get(i).get(2));
			String soldOut = rowData.get(i).get(5).equals("0") ? "재고 있음" : "재고 없음";
			rowData.get(i).set(2, convertCategoryCode(category));
			rowData.get(i).set(5, soldOut);
		}

		menuTable.updateUI();
	}
}
