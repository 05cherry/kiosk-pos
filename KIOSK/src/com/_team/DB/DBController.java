package com._team.DB;

import java.sql.*;
import java.util.*;

import com._team.kiosk.Main;

public class DBController extends Thread {

	PreparedStatement ptmt;
	Connection conn;
	ResultSet res;

	public DBController() {
		openDb();
	}

	public void run() {
		try {
			while (true) {
				sleep(1000);
				// POS창이 꺼졌는지 확인하고 꺼졌으면 DB연결을 해제
				// TODO 주문 확인
				if (Main.win.isShowing())
					;
//					System.out.println("POS alive");
				else {
					System.out.println("POS dead");
					closeDb();
					System.exit(MAX_PRIORITY);
					break;
				}
			}
		} catch (InterruptedException e) {
		}
	}

	public void openDb() {
		try {
			// #1. DB연결
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://34.132.6.28:3306/cafe2022", "root", "1234");
			System.out.println("디비 연결 성공");
		} catch (SQLException ex) {
			System.out.println("디비 연결 실패");
			System.out.println("SQLException : " + ex.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void closeDb() {

		try {
			if (ptmt != null)
				ptmt.close(); // DB닫아!
			if (conn != null)
				conn.close(); // 연결해제해!
			System.out.println("디비 닫기 성공");

		} catch (Exception e) {
			System.out.println("디비 닫기 실패");
		}
	}

	public Vector<Vector<String>> searchData(String strSQL) {
		Vector<Vector<String>> rowData2 = new Vector<Vector<String>>();
		try {
			ptmt = conn.prepareStatement(strSQL);// , ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE
			res = ptmt.executeQuery();

			Vector<String> tempRow;
			ResultSetMetaData md = res.getMetaData();
			int column = md.getColumnCount();
			while (res.next()) {
				tempRow = new Vector<>();
				for (int i = 1; i <= column; i++) {
					tempRow.addElement(res.getString(i));
				}
				rowData2.add(tempRow);
			}
			res.close();
			return rowData2;
		} catch (SQLException e1) {
			System.out.println("SQLException : " + e1.getMessage());
			return null;
		} catch (NumberFormatException e2) {
			System.out.println("SQLException : " + e2.getMessage());
			return null;
		}
	}// searchData

	public void deleteData(String selectedData, String tableName) {
		try {
			// sql 질의어 실행
			ptmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE code = ?;");
			ptmt.setInt(1, Integer.parseInt(selectedData));
			ptmt.executeUpdate();
			// 삽입 잘 되었는지 확인
			System.out.println("데이터 삭제 완료");
		} catch (SQLException e1) {
			System.out.println("SQLException : " + e1.getMessage());
		} catch (NumberFormatException e2) {
			System.out.println("SQLException : " + e2.getMessage());
		}
	}// deleteData
	
	public void delete(String strSQL) {//DELETE FROM TABLE WHERE ? = ?"
		try {
			// sql 질의어 실행
			ptmt = conn.prepareStatement(strSQL); 
			ptmt.executeUpdate();
			// 삽입 잘 되었는지 확인
			System.out.println("데이터 삭제 완료");
		} catch (SQLException e1) {
			System.out.println("SQLException : " + e1.getMessage());
		} catch (NumberFormatException e2) {
			System.out.println("SQLException : " + e2.getMessage());
		}
	}// deleteData

	public void addData(LinkedHashMap<String, Object> inputData, String strSQL) {
		try {
			ptmt = conn.prepareStatement(strSQL);
			Iterator<String> keys = inputData.keySet().iterator();
			for (int i = 1; i <= inputData.size(); i++) {
				String key = keys.next();

				// instanceof 자료형 잘 보기
				if (inputData.get(key) instanceof Integer) {
					ptmt.setInt(i, (int) inputData.get(key));
				} else if (inputData.get(key) instanceof String) {
					ptmt.setString(i, (String) inputData.get(key));
				} else if (inputData.get(key) instanceof Timestamp) {
					ptmt.setTimestamp(i, (Timestamp) inputData.get(key));
				} else if (inputData.get(key) instanceof Boolean) {
					ptmt.setBoolean(i, (Boolean) inputData.get(key));
				}
			}
			ptmt.executeUpdate();
			System.out.println("데이터 삽입 완료");

		} catch (SQLException e) {
			System.out.println("데이터 삽입 실패");
			System.out.println("SQLException : " + e.getMessage());
		}
	}

	public String getMaxNum(String tableName) {
		String maxCode = null;
		try {
			ptmt = conn.prepareStatement("SELECT MAX(code) FROM " + tableName + ";");

			res = ptmt.executeQuery();
			res.next(); // ResultSet은 포인트는 0을 가르키는데 1부터 시작함 그래서 next()로 커서를 옮겨줘야함
			maxCode = String.valueOf(Integer.parseInt(res.getString(1)) + 1);
		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		} catch (NumberFormatException e3) {
			System.out.println("SQLException : " + e3.getMessage());
		}
		return maxCode;
	}

	public Vector<Vector<String>> select(String strSQL) {
		Vector<Vector<String>> rowData = new Vector<Vector<String>>();
		// Vector<String> tempRow = new Vector<>();
		try {
			// Statement 객체 생성
			ptmt = conn.prepareStatement(strSQL);
			ResultSet rs = ptmt.executeQuery();
			Vector<String> tempRow;

			int columnCount = rs.getMetaData().getColumnCount();

			// 레코드 가져오기
			while (rs.next()) {
				tempRow = new Vector<>();
				for (int i = 0; i < columnCount; i++) {
					tempRow.add(rs.getString(i + 1));
				}
				rowData.add(tempRow);
			}

			// 삽입 잘 되었는지 확인
			System.out.println("테이블에 데이터 로드 성공");
			rs.close();
		} catch (SQLException ex) {
			System.out.println("데이터 로드 실패");
			System.out.println("SQLException : " + ex.getMessage());
		}
		return rowData;
	}// searchData

	private Vector<Vector<String>> selectTable(String tableName) {
		String sql = "SELECT * FROM " + tableName + ";";
		Vector<Vector<String>> table = select(sql);
		return table;
	}

	public ArrayList<Cash> selectCashs() {
		ArrayList<Cash> cashs = new ArrayList<Cash>();
		Vector<Vector<String>> table = selectCashTable();

		// customer 불러오기
		for (int i = 0; i < table.size(); i++) {
			cashs.add(new Cash(table.get(i)));
		}
		return cashs;
	}

	public ArrayList<Category> selectCategorys() {
		ArrayList<Category> categorys = new ArrayList<Category>();
		Vector<Vector<String>> table = selectCategoryTable();

		// Category 불러오기
		for (int i = 0; i < table.size(); i++) {
			categorys.add(new Category(table.get(i)));
		}
		return categorys;
	}

	public ArrayList<Customer> selectCustomers() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		Vector<Vector<String>> table = selectCustomerTable();

		// customer 불러오기
		for (int i = 0; i < table.size(); i++) {
			customers.add(new Customer(table.get(i)));
		}
		return customers;
	}

	public ArrayList<Material> selectMaterials() {
		ArrayList<Material> materials = new ArrayList<Material>();
		Vector<Vector<String>> table = selectMaterialTable();

		// Material 불러오기
		for (int i = 0; i < table.size(); i++) {
			materials.add(new Material(table.get(i)));
		}
		return materials;
	}

	public ArrayList<OrderEach> selectOrderEachs() {
		ArrayList<OrderEach> orderEachs = new ArrayList<OrderEach>();
		Vector<Vector<String>> table = selectOrderEachTable();

		// OrderEach 불러오기
		for (int i = 0; i < table.size(); i++) {
			orderEachs.add(new OrderEach(table.get(i)));
		}
		return orderEachs;
	}

	public ArrayList<OrderMain> selectOrderMains() {
		ArrayList<OrderMain> orderMains = new ArrayList<OrderMain>();
		Vector<Vector<String>> table = selectOrderMainTable();

		// OrderMain 불러오기
		for (int i = 0; i < table.size(); i++) {
			orderMains.add(new OrderMain(table.get(i)));
		}
		return orderMains;
	}

	public ArrayList<Product> selectProducts() {
		ArrayList<Product> products = new ArrayList<Product>();
		Vector<Vector<String>> table = selectProductTable();

		// Product 불러오기
		for (int i = 0; i < table.size(); i++) {
			products.add(new Product(table.get(i)));
		}
		return products;
	}

	public ArrayList<ReceiptNumber> selectReceiptNumbers() {
		ArrayList<ReceiptNumber> receiptNumbers = new ArrayList<ReceiptNumber>();
		Vector<Vector<String>> table = selectReceiptNumberTable();

		// ReceiptNumber 불러오기
		for (int i = 0; i < table.size(); i++) {
			receiptNumbers.add(new ReceiptNumber(table.get(i)));
		}
		return receiptNumbers;
	}

	public Vector<Vector<String>> selectCashTable() {
		return selectTable("cash");
	}

	public Vector<Vector<String>> selectCategoryTable() {
		return selectTable("category");
	}

	public Vector<Vector<String>> selectCustomerTable() {
		return selectTable("customer");
	}

	public Vector<Vector<String>> selectMaterialTable() {
		return selectTable("material");
	}

	public Vector<Vector<String>> selectOrderEachTable() {
		return selectTable("order_each");
	}

	public Vector<Vector<String>> selectOrderMainTable() {
		return selectTable("order_main");
	}

	public Vector<Vector<String>> selectProductTable() {
		return selectTable("product");
	}

	public Vector<Vector<String>> selectReceiptNumberTable() {
		return selectTable("receipt_Number");
	}

	public static Object parseData(Object data, String type) {
		switch (type) {
		case "String":
			data = data.toString();
			break;
		case "int": 
			data = Integer.parseInt(data.toString());
			break;
		case "boolean":
			data = data.toString().equals("1") ? true : false;
			break;
		case "Date":
			data = java.sql.Date.valueOf(data.toString());
			break;
		case "Timestamp":
			data = java.sql.Timestamp.valueOf(data.toString());
			break;
		default:
			System.out.println("데이터 타입 에러");
			break;
		}
		return data;
	}

}