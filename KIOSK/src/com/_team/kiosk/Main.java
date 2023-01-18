package com._team.kiosk;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.JFrame;
import com._team.DB.*;

public class Main {
	public static Kiosk win;
	public static DBController dbc;
	public static void main(String[] args) {
		dbc = new DBController();
		//KIOSK
		win = new Kiosk();
        win.setSize(664, 1210);
        win.setVisible(true);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //dbc();
		dbc.run();
		
	}
	public static void abc() {
		System.out.println("start");
		for(int i = 0; i<1000; i++) {
			String strSQL;
			LinkedHashMap<String, Object> inputData = new LinkedHashMap<>();
			
			//orderMain init
			OrderMain orderMain = new OrderMain();
			orderMain.setCode(Integer.parseInt(Main.dbc.getMaxNum("order_main")));
			// 회원 랜덤 연결
			int rand;
			rand = (int)(Math.random()*Kiosk.customers.size());
			
			orderMain.setCustomerCode(Kiosk.customers.get(rand).getCode()); 
			
			String month, day, hour, min, sec;
			DecimalFormat dec = new DecimalFormat("00");
			month = dec.format((int)(Math.random()*2)==1?11:12);
			day = dec.format((int)(Math.random()*29)+1);
			hour = dec.format((int)(Math.random()*24));
			min = dec.format((int)(Math.random()*60));
			sec = dec.format((int)(Math.random()*60));
			String time = "2022-"+month+"-"+day+" "+hour+":"+min+":"+sec+".0"; // 형식을 지켜야 함
			System.out.println(i +"    "+time);
			java.sql.Timestamp t = java.sql.Timestamp.valueOf(time);
			orderMain.setDateTime(t);
			
			orderMain.setPayAmount(0);
			
			orderMain.setUsePoint(0);

			rand =(int)(Math.random()*2);
			orderMain.setPayType(rand == 1 ? "현금": "카드"); 
			
			rand =(int)(Math.random()*2);
			orderMain.setTakeOut(rand == 1 ? true : false);
			
			//ordeEachs init
			ArrayList<OrderEach> orderEachs = new ArrayList<OrderEach>();
			rand = (int)(Math.random()*3)+1;
			for(int j = 0;j<rand;j++) {
				OrderEach orderEach = new OrderEach();
				Product product = win.products.get((int)(Math.random()*win.products.size()));
				orderEach = new OrderEach();
				orderEach.setOrder_main_code(orderMain.getCode());
				orderEach.setProduct_code(product.getCode());
				orderEach.setEachPrice(product.getPrice());
				
				orderEach.setEachNum((int)(Math.random()*4)+1);
				
				orderEach.setSize( (int)(Math.random()*2)==1 ? true:false);
				if(orderEach.isSize()) orderEach.setEachPrice(orderEach.getEachPrice()+500);
				
				orderEach.setHotOrIce((int)(Math.random()*2)==1 ? true:false);
				if(orderEach.isHotOrIce()) orderEach.setEachPrice(orderEach.getEachPrice()+500);
				
				int option = (int)(Math.random()*6);
				orderEach.setAlmondSyrup(false);
				orderEach.setCream(false);
				orderEach.setHazelSyrup(false);
				orderEach.setShot(false);
				orderEach.setVanillaSyrup(false);
				if(option == 0) orderEach.setAlmondSyrup(true);
				else if(option == 1) orderEach.setCream(true);
				else if(option == 2) orderEach.setHazelSyrup(true);
				else if(option == 3) orderEach.setShot(true);
				else if(option == 4) orderEach.setVanillaSyrup(true);
				if(option !=6) orderEach.setEachPrice(orderEach.getEachPrice()+500);
				
				orderEachs.add(orderEach);
				orderMain.setPayAmount(orderMain.getPayAmount()+orderEach.getEachPrice()*orderEach.getEachNum());
			}
			
			int usePoint = (int)(Math.random()*orderMain.getPayAmount()*0.1);
			orderMain.setUsePoint(usePoint);
			
			// DB 데이터 추가
			// order_main
			inputData.clear();
			strSQL = "INSERT INTO order_main VALUES (?,?,?,?,?,?,?)";
			inputData.put("code", (Integer) orderMain.getCode());
			inputData.put("customerCode", (Integer) orderMain.getCustomerCode());
			inputData.put("dateTime", (Timestamp) orderMain.getDateTime());
			inputData.put("payAmount", (Integer) orderMain.getPayAmount());
			inputData.put("usePoint", (Integer) orderMain.getUsePoint());
			inputData.put("payType", (String) orderMain.getPayType());
			inputData.put("takeOut", (boolean) orderMain.isTakeOut());
			Main.dbc.addData(inputData, strSQL);
			
			// order_each
			strSQL = "INSERT INTO order_each VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			for (OrderEach oe : orderEachs) {
				inputData.clear();
				inputData.put("order_main_code", (Integer) oe.getOrder_main_code());
				inputData.put("product_code", (Integer) oe.getProduct_code());
				inputData.put("eachPrice", (Integer) oe.getEachPrice());
				inputData.put("eachNum", (Integer) oe.getEachNum());
				inputData.put("size", (boolean) oe.isSize());
				inputData.put("shot", (boolean) oe.isShot());
				inputData.put("hotOrIce", (boolean) oe.isHotOrIce());
				inputData.put("cream", (boolean) oe.isCream());
				inputData.put("hazelSyrup", (boolean) oe.isHazelSyrup());
				inputData.put("almondSyrup", (boolean) oe.isAlmondSyrup());
				inputData.put("vanillaSyrup", (boolean) oe.isVanillaSyrup());
				Main.dbc.addData(inputData, strSQL);
			}
		}
	}
}
