package com._team.DB;

import com._team.pos.Main;
import com._team.pos.POS;

import java.sql.*;
import java.util.*;

public class DBController extends Thread {
	public final int WAITING = -1;
	public final int INSERT = 0;
	public final int UPDATE = 1;
//	public final int SELECT = 2; //메소드를 만들지 않아서 사용 불가능
	public final int DELETE = 3;
	public int stateCash;
	public int stateCategory;
	public int stateCustomer;
	public int stateMaterial;
	public int stateOrderEachs;
	public int stateOrderMain;
	public int stateProduct;
	public int stateReceiptNumber;
	public Cash cash;
	public Category category;
	public Customer customer;
	public Material material;
	public ArrayList<OrderEach> orderEachs;
	public OrderMain orderMain;
	public Product product;
	public ReceiptNumber receiptNumber;
	public ArrayList<ReceiptNumber> previousReceiptNumbers;

	PreparedStatement ptmt;
	Connection conn;
	ResultSet res;
	
	public DBController() {
		openDb();
		stateCash = WAITING;
		stateCategory = WAITING;
		stateCustomer = WAITING;
		stateMaterial = WAITING;
		stateOrderEachs = WAITING;
		stateOrderMain = WAITING;
		stateProduct = WAITING;
		stateReceiptNumber = WAITING;
		orderEachs = new ArrayList<OrderEach>();
		previousReceiptNumbers = new ArrayList<ReceiptNumber>();
	}

	public void run() {
		try {
			while (true) {
				sleep(100);
				// TODO 하루가 지날 때 마다 스탯패널 갱신
				//DB INSERT, UPDATE, SELECT, DELETE 사항 체크 후 실행
				if(stateCash != WAITING) {
					doCRUDCash();
					POS.updateCashs();
					Main.win.calculatorPanel.updateCash();
				}
				if(stateCategory != WAITING) {
					doCRUDCategory();
				}
				if(stateMaterial != WAITING) {
					doCRUDMaterial();
					POS.updateMaterials();
					Main.win.orderStockPanel.stockPanel.loadTable();
				}
				if(stateProduct != WAITING) {
					doCRUDProduct();
					POS.updateProducts();
					Main.win.orderStockPanel.menuPanel.loadTable();
				}
				// 순서 중요!! ReceiptNumber > OrderEachs > OrderMain > Customer
				if(stateReceiptNumber != WAITING) {
					doCRUDReceiptNumber();
				}
				if(stateOrderEachs != WAITING) {
					doCRUDOrderEachs();
				}
				if(stateOrderMain != WAITING) {
					doCRUDOrderMain();
				}
				if(stateCustomer != WAITING) {
					doCRUDCustomer();
					POS.updateCustomers();
					Main.win.clientPanel.loadTable();
				}
				
				//주문 변경되면 오더패널 갱신
				//주문이 변경되었는지 확인
				if (isUpdateOrderPanel()) {
					//오더패널 갱신
					previousReceiptNumbers = POS.receiptNumbers;
					POS.updateOrderMains();
					POS.updateOrderEachs();
					POS.updateCustomers();
					Main.win.orderStockPanel.orderPanel.updateOrderPanel();
					Main.win.clientPanel.loadTable();
					Main.win.statPanel.updateTime();
					System.out.println("UPDATE OrderPanel");
				} /*
					 * else { System.out.println("WAITING OrderPanel WAITING"); }
					 */
					
				//POS창 꺼졌으면 DBController 종료
				if (Main.win.isShowing());
//					System.out.println("POS alive");
				else {
					System.out.println("POS dead");
					closeDb();
					System.exit(MAX_PRIORITY);
					break;
				}
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	private boolean isUpdateOrderPanel() {
		boolean doUpdate = true;
		POS.updateReceiptNumbers();
		if(POS.receiptNumbers.size() == previousReceiptNumbers.size()) {
			doUpdate = false;
			for(int i =0;i<POS.receiptNumbers.size();i++) {
				if(previousReceiptNumbers.get(i).getOrderCode() != POS.receiptNumbers.get(i).getOrderCode() || previousReceiptNumbers.get(i).getReceiptNumber() != POS.receiptNumbers.get(i).getReceiptNumber()) {
					doUpdate = true;
					break;
				}
			}
		}
		return doUpdate;
	}

	public void openDb() {
		try {
			// #1. DB연결
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://javacafe2022.ciuztmb4byzu.ap-northeast-2.rds.amazonaws.com:3306/cafe2022", "root", "12345678");
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
				}else if (inputData.get(key) instanceof java.sql.Date) {
		               ptmt.setDate(i, (java.sql.Date) inputData.get(key));
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
//			System.out.println("테이블에 데이터 로드 성공");
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
	//---------------------------------------------------------------------------------------------------CRUD
	private boolean DBExecuteUpdate(String strSQL) {
		try {
			// sql 질의어 실행
			ptmt = conn.prepareStatement(strSQL); 
			ptmt.executeUpdate();
//			System.out.println("DBExecuteUpdate");
			return true;
		} catch (SQLException e1) {
			System.out.println("SQLException : " + e1.getMessage());
			return false;
		} catch (NumberFormatException e2) {
			System.out.println("SQLException : " + e2.getMessage());
			return false;
		}
	}
	
	//INSERT
	private boolean insertCash(Cash cash) {
		String strSQL = "INSERT INTO cash VALUES ("+String.valueOf(cash.getCode())+","+String.valueOf(cash.getCurrentCash())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertCategory(Category category) {
		String strSQL = "INSERT INTO category VALUES ("+String.valueOf(category.getCode())+","+String.valueOf(category.getName())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertCustomer(Customer customer) {
		String strSQL = "INSERT INTO customer VALUES ("+String.valueOf(customer.getCode())+","+String.valueOf(customer.getPhone())+","+String.valueOf(customer.getPoint())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertMaterial(Material material) {
		String strSQL = "INSERT INTO material VALUES ("+String.valueOf(material.getCode())+",'"+String.valueOf(material.getName())+"',"+String.valueOf(material.getPrice())+",'"+String.valueOf(material.getOutOfDate())+"',"+String.valueOf(material.getNum())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertOrderEach(OrderEach orderEach) {
		String strSQL = "INSERT INTO order_each VALUES ("+String.valueOf(orderEach.getOrder_main_code())+","+String.valueOf(orderEach.getProduct_code())+","+String.valueOf(orderEach.getEachPrice())+","+String.valueOf(orderEach.getEachNum())+","+parseBooleanToNumber(orderEach.isSize())+","+parseBooleanToNumber(orderEach.isShot())+","+parseBooleanToNumber(orderEach.isHotOrIce())+","+parseBooleanToNumber(orderEach.isCream())+","+parseBooleanToNumber(orderEach.isHazelSyrup())+","+parseBooleanToNumber(orderEach.isAlmondSyrup())+","+parseBooleanToNumber(orderEach.isVanillaSyrup())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertOrderMain(OrderMain orderMain) {
		String strSQL = "INSERT INTO order_main VALUES ("+String.valueOf(orderMain.getCode())+","+String.valueOf(orderMain.getCustomerCode())+","+String.valueOf(orderMain.getDateTime())+","+String.valueOf(orderMain.getPayAmount())+","+String.valueOf(orderMain.getUsePoint())+","+String.valueOf(orderMain.getPayType())+","+parseBooleanToNumber(orderMain.isTakeOut())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertProduct(Product product) {
		String strSQL = "INSERT INTO product VALUES ("+String.valueOf(product.getCode())+",'"+String.valueOf(product.getName())+"',"+String.valueOf(product.getCategory())+","+String.valueOf(product.getPrice())+",'"+String.valueOf(product.getImgSrc()).replace("\\", "\\\\")+"',"+parseBooleanToNumber(product.isSoldOut())+")";
		return DBExecuteUpdate(strSQL);
	}
	private boolean insertReceiptNumber(ReceiptNumber receiptNumber) {
		String strSQL = "INSERT INTO receipt_Number VALUES ("+String.valueOf(receiptNumber.getOrderCode())+","+String.valueOf(receiptNumber.getReceiptNumber())+")";
		return DBExecuteUpdate(strSQL);
	}
	private String parseBooleanToNumber(boolean b) {
		String str = "";
		if (b)
			str = "1";
		else
			str = "0";
		return str;
	}
	
	//UPDATE---------------------------------------------------------------------------------------------------
	private boolean updateCash(Cash cash) {
		String strSQL = "UPDATE cash SET currentCash =  "+String.valueOf(cash.getCurrentCash())+" WHERE code = "+String.valueOf(cash.getCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateCategory(Category category) {
		String strSQL = "UPDATE category SET name = "+String.valueOf(category.getName())+" WHERE code = "+String.valueOf(category.getCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateCustomer(Customer customer) {
		String strSQL = "UPDATE customer SET phone = '"+String.valueOf(customer.getPhone())+"', point = "+String.valueOf(customer.getPoint())+" WHERE code = "+String.valueOf(customer.getCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateMaterial(Material material) {
		String strSQL = "UPDATE material SET name = '"+String.valueOf(material.getName())+"', price = "+String.valueOf(material.getPrice())+", outOfDate = '"+String.valueOf(material.getOutOfDate())+"', num = "+String.valueOf(material.getNum())+" WHERE code = "+String.valueOf(material.getCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateOrderEach(OrderEach orderEach) {
		String strSQL = "UPDATE order_each SET product_code = "+String.valueOf(orderEach.getProduct_code())+", eachPrice = "+String.valueOf(orderEach.getEachPrice())+", eachNum = "+String.valueOf(orderEach.getEachNum())+", size = "+parseBooleanToNumber(orderEach.isSize())+", shot = "+parseBooleanToNumber(orderEach.isShot())+", hotOrIce = "+parseBooleanToNumber(orderEach.isHotOrIce())+", cream = "+parseBooleanToNumber(orderEach.isCream())+", hazelSyrup = "+parseBooleanToNumber(orderEach.isHazelSyrup())+", almondSyrup = "+parseBooleanToNumber(orderEach.isAlmondSyrup())+", vanillaSyrup = "+parseBooleanToNumber(orderEach.isVanillaSyrup())+" WHERE order_main_code = "+orderEach.getOrder_main_code()+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateOrderMain(OrderMain orderMain) {
		String strSQL = "UPDATE order_main SET customerCode = "+String.valueOf(orderMain.getCustomerCode())+", dateTime = "+String.valueOf(orderMain.getDateTime())+", payAmount = "+String.valueOf(orderMain.getPayAmount())+", usePoint = "+String.valueOf(orderMain.getUsePoint())+", payType = "+String.valueOf(orderMain.getPayType())+", takeOut = "+parseBooleanToNumber(orderMain.isTakeOut())+" WHERE code = "+String.valueOf(orderMain.getCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateProduct(Product product) {
		String strSQL = "UPDATE product SET name = '"+String.valueOf(product.getName())+"', category = "+String.valueOf(product.getCategory())+", price = "+String.valueOf(product.getPrice())+", imgSrc = '"+String.valueOf(product.getImgSrc()).replace("\\", "\\\\")+"', soldOut = "+parseBooleanToNumber(product.isSoldOut())+" WHERE code = "+String.valueOf(product.getCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	private boolean updateReceiptNumber(ReceiptNumber receiptNumber) {
		String strSQL = "UPDATE receipt_Number SET receiptNumber = "+String.valueOf(receiptNumber.getReceiptNumber())+" WHERE order_main_code "+String.valueOf(receiptNumber.getOrderCode())+";";
		return DBExecuteUpdate(strSQL);
	}
	
	//SELECT searchData를 사용바람--------------------------------------------------------------------------------
	//PreparedStatement.executeQuery를 사용
	
	//DELETE---------------------------------------------------------------------------------------------------
	private boolean deleteCash(Cash cash) {
		String strSQL = "DELETE FROM cash WHERE code = "+cash.getCode();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteCategory(Category category) {
		String strSQL = "DELETE FROM category WHERE code = "+category.getCode();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteCustomer(Customer customer) {
		String strSQL = "DELETE FROM customer WHERE code = "+customer.getCode();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteMaterial(Material material) {
		String strSQL = "DELETE FROM material WHERE code = "+material.getCode();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteOrderEach(OrderEach orderEach) {
		//같은 order_main_code를 가지고 있는 order_each 레코드 모두 삭제
		String strSQL = "DELETE FROM order_each WHERE order_main_code = "+orderEach.getOrder_main_code();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteOrderMain(OrderMain orderMain) {
		String strSQL = "DELETE FROM order_main WHERE code = "+orderMain.getCode();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteProduct(Product product) {
		String strSQL = "DELETE FROM product WHERE code = "+product.getCode();
		return DBExecuteUpdate(strSQL);
	}
	private boolean deleteReceiptNumber(ReceiptNumber receiptNumber) {
		String strSQL = "DELETE FROM receipt_Number WHERE order_main_code = "+receiptNumber.getOrderCode();
		return DBExecuteUpdate(strSQL);
	}
	//---------------------------------------------------------------------------------------------------
	private boolean doCRUDCash() {
		boolean success = true;
		if (stateCash == INSERT) success = insertCash(cash);
		else if (stateCash == UPDATE) success = updateCash(cash);
//		else if (stateCash == SELECT) success = selectCash(cash);
		else if (stateCash == DELETE) success = deleteCash(cash);
		else success = false;
		stateCash = WAITING;
		System.out.println("CRUDCash");
		return success;
	}
	private boolean doCRUDCategory() {
		boolean success = true;
		if (stateCategory == INSERT) success = insertCategory(category);
		else if (stateCategory == UPDATE) success = updateCategory(category);
//		else if (stateCategory == SELECT) success = selectCategory(category);
		else if (stateCategory == DELETE) success = deleteCategory(category);
		else success = false;
		stateCategory = WAITING;
		System.out.println("CRUDCategory");
		return success;
	}
	private boolean doCRUDCustomer() {
		boolean success = true;
		if (stateCustomer == INSERT) success = insertCustomer(customer);
		else if (stateCustomer == UPDATE) success = updateCustomer(customer);
//		else if (stateCustomer == SELECT) success = selectCustomer(customer);
		else if (stateCustomer == DELETE) success = deleteCustomer(customer);
		else success = false;
		stateCustomer = WAITING;
		System.out.println("CRUDCustomer");
		return success;
	}
	private boolean doCRUDMaterial() {
		boolean success = true;
		if (stateMaterial == INSERT) success = insertMaterial(material);
		else if (stateMaterial == UPDATE) success = updateMaterial(material);
//		else if (stateMaterial == SELECT) success = selectMaterial(material);
		else if (stateMaterial == DELETE) success = deleteMaterial(material);
		else success = false;
		stateMaterial = WAITING;
		System.out.println("CRUDMaterial");
		return success;
	}
	private boolean doCRUDOrderEachs() {
		boolean success = true;
		for(OrderEach orderEach : orderEachs) {
			if (stateOrderEachs == INSERT) success = insertOrderEach(orderEach);
			else if (stateOrderEachs == UPDATE) success = updateOrderEach(orderEach);
//			else if (stateOrderEach == SELECT) success = selectOrderEach(orderEach);
			else if (stateOrderEachs == DELETE) success = deleteOrderEach(orderEach);
			else success = false;
		}
		stateOrderEachs = WAITING;
		System.out.println("CRUDOrderEachs");
		return success;
	}
	private boolean doCRUDOrderMain() {
		boolean success = true;
		if (stateOrderMain == INSERT) success = insertOrderMain(orderMain);
		else if (stateOrderMain == UPDATE) success = updateOrderMain(orderMain);
//		else if (stateOrderMain == SELECT) success = selectOrderMain(orderMain);
		else if (stateOrderMain == DELETE) success = deleteOrderMain(orderMain);
		else success = false;
		stateOrderMain = WAITING;
		System.out.println("CRUDOrderMain");
		return success;
	}
	private boolean doCRUDProduct() {
		boolean success = true;
		if (stateProduct == INSERT) success = insertProduct(product);
		else if (stateProduct == UPDATE) success = updateProduct(product);
//		else if (stateProduct == SELECT) success = selectProduct(product);
		else if (stateProduct == DELETE) success = deleteProduct(product);
		else success = false;
		stateProduct = WAITING;
		System.out.println("CRUDProduct");
		return success;
	}
	private boolean doCRUDReceiptNumber() {
		boolean success = true;
		if (stateReceiptNumber == INSERT) success = insertReceiptNumber(receiptNumber);
		else if (stateReceiptNumber == UPDATE) success = updateReceiptNumber(receiptNumber);
//		else if (stateReceiptNumber == SELECT) success = selectReceiptNumber(receiptNumber);
		else if (stateReceiptNumber == DELETE) success = deleteReceiptNumber(receiptNumber);
		else success = false;
		stateReceiptNumber = WAITING;
		System.out.println("CRUDReceiptNumber");
		return success;
	}
	//---------------------------------------------------------------------------------------------------CRUD
}