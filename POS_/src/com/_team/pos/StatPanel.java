package com._team.pos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com._team.DB.OrderMain;

public class StatPanel extends DefaultRightPanel {

	private static final long serialVersionUID = -3297835361878465037L;
	private Vector<Vector<String>> rowData = Main.dbc.selectOrderMainTable();
	private ArrayList<StaticData> staticDataList;  // 통계 데이터 객체를 담은 ArrayList
	private LocalDate firstDate, lastDate; 
	private JTable statTable;
	private ArrayList<ColumnElement> list;
	private GraphPanel graphPanel;
  // 내부 클래스 통계데이터 : 날짜와, 그날의 매출, 주문 건수가 담겨있음
	private class StaticData {
		private LocalDate date = null;
		private int sum = 0;
		private int numOfOrders = 0;

		private LocalDate getDate() {
			return date;
		}

		private int getSum() {
			return sum;
		}

		private int getNumOfOrders() {
			return numOfOrders;
		}
	}

	// Constructor
	StatPanel() {
		setPanel();

		// topPanel
		AutoLabel statTitle;
		statTitle = new AutoLabel("매출 통계");
		statTitle.setBounds(325, 36, 347, 41);
		topPanel.add(statTitle);
    // 테이블에 올릴 column명들을 벡터객체에 담음
		Vector<String> title = new Vector<>();
		title.add("매출 날짜");
		title.add("매출 합계");
		title.add("주문 건수");
    // 테이블에 올릴 통계 데이터 생성
		staticDataList = new ArrayList<>();
    // 한달 간의 기간을 정하는 메서드 호출
		setPeriod();
    // 테이블 위의 벡터 데이터
		rowData = new Vector<Vector<String>>();
    // 오른쪽 그래프에 올릴 요일 별 ArrayList
		list = new ArrayList<ColumnElement>();
    // 요일별로 매출 측정하는 메서드 발동
		calcDaySales();
    // 테이블 값들 셋팅
		setRowData();
    // 테이블을 행단위롤 핸들링하기 위해 DefaultTableModel을 모델로 데이터와 컬럼값을 넣어줌
		DefaultTableModel tmodel = new DefaultTableModel(rowData, title){
      public boolean isCellEditable(int i, int c) {return false;}
    };
		statTable = new JTable(tmodel);
    // 컬럼명 클릭해서 위치 바꾸는거 금지
    statTable.getTableHeader().setReorderingAllowed(false);
    // 컬럼의 경계선을 마우스로 드래그하면 늘어나거나 줄어든느거 방지
    statTable.getTableHeader().setResizingAllowed(false);
    // 자동으로 행들의 값을 정렬해주는 메서드
		statTable.setAutoCreateRowSorter(true);
    JScrollPane statScrollPane = new JScrollPane(statTable);
		statScrollPane.setViewportView(statTable);
		statScrollPane.setBounds(41, 121, 428, 367);
		addComponents(centerPanel, statScrollPane);


		AutoLabel graphTitle = new AutoLabel("요일별 매출량");
		AutoLabel subTitle = new AutoLabel(firstDate.plusDays(1) + " - " + lastDate.minusDays(1));
		graphTitle.setBounds(580, 121, 250, 35);
		subTitle.setBounds(580, 161, 250, 20);

		graphPanel = new GraphPanel(list);
		graphPanel.setBounds(489, 185, 435, 300);
		addComponents(centerPanel, graphPanel, graphTitle, subTitle);

		addComponents(this, topPanel, centerPanel, bottomPanel);

		/**디자인 시작-------------------------------**/
		statTitle.setFont(new Font("", Font.PLAIN, 40));
		/**디자인 끝----------------------------------**/
	}// Constructor
  // 한달 데이터 중 양 끝의 날짜를 구하는 메서드
	private void getEndPointDate() {
		firstDate = LocalDate.now().minusMonths(1).minusDays(1);
		lastDate = LocalDate.now();
	}

  // 한달간의 주문건을 계산하는 메서드
	private void setPeriod() {
    // 주문건을 담을 객체 생성
		ArrayList<OrderMain> aMonthData = new ArrayList<OrderMain>();
		getEndPointDate();
    // 주문건들을 가져와서 한달안의 값에 포함되는 날짜의 주문건이면 aMonthData에 주문건을 담음
		for (OrderMain om : POS.orderMains) {
			LocalDate orderDate = om.getDate();
			if (orderDate.isAfter(firstDate) && orderDate.isBefore(lastDate)) {
				aMonthData.add(om);
			}
		}
		setStaticData(aMonthData);
	}

	// staticDataList를 완성하는 메서드
	private void setStaticData(ArrayList<OrderMain> aMonthData) {
		StaticData data;
    // aMonthData를 순회하면서 같은 날짜의 주문건끼리 합쳐 객체로 만듬/ 주문건 중 필요한 데이터만 가져옴
		for (int i = 0; i < aMonthData.size(); i++) {
			data = new StaticData();
			data.date = aMonthData.get(i).getDate();
			data.sum = aMonthData.get(i).getPayAmount();
			data.numOfOrders++;
      // 순회하는 객체의 뒤에 위치한 값 중에 같은 날짜의 주문건이 있으면 더하고
      // 그 주문건을 미리 없애서 두번 더하는 일이 없도록 한다.
			for (int k = i + 1; k < aMonthData.size(); k++) {
				if (aMonthData.get(k).getDate().isEqual(data.date)) {
					data.sum += aMonthData.get(k).getPayAmount();
					data.numOfOrders++;
					aMonthData.remove(k--);
				}
			}
      // 계산한 날짜의 데이터를 ArrayList에 넣어줌
			staticDataList.add(data);
		}
	}
  // staticDataList에 있는 객체들을 꺼내어 인스턴스의 값들을 가져와 벡터 객체에 넣어주고
  // 그것을 다시 테이블의 rowData 벡터에 넣어줌
	private void setRowData() {
		Vector<String> aLineData;
		for (StaticData sd : staticDataList) {
			aLineData = new Vector<String>();
			aLineData.addElement(sd.getDate().toString());
			aLineData.addElement(String.valueOf(sd.getSum()));
			aLineData.addElement(String.valueOf(sd.getNumOfOrders()));
			rowData.add(aLineData);
		}
	}
  // 요일별로 매출량을 계산하는 메서드
	private void calcDaySales() {
		String[] days = { "월 ", "화", "수", "목", "금", "토", "일" };
		for (String day : days) {
      // 날짜변수와 매출량을 담을 정수변수만 다루는 객체 ColumnElement
			list.add(new ColumnElement(day, 0));
		}

		for (StaticData data : staticDataList) {
      // getDayOfWeek: 요일을 가져옴
			DayOfWeek day = data.getDate().getDayOfWeek();
			int value = data.getSum();
			switch (day) {
			case MONDAY:
				list.get(0).addValue(value);
				break;
			case TUESDAY:
				list.get(1).addValue(value);
				break;
			case WEDNESDAY:
				list.get(2).addValue(value);
				break;
			case THURSDAY:
				list.get(3).addValue(value);
				break;
			case FRIDAY:
				list.get(4).addValue(value);
				break;
			case SATURDAY:
				list.get(5).addValue(value);
				break;
			case SUNDAY:
				list.get(6).addValue(value);
				break;
			}
		} // for문
	}

	public void updateTime() {

		POS.updateOrderEachs();
		POS.updateReceiptNumbers();
		POS.updateOrderMains();

		staticDataList.clear();
		setPeriod();
		rowData.clear();
		list.clear();
		calcDaySales();
		setRowData();
		statTable.updateUI();
		System.out.println("clear");

		graphPanel.reSet(list);
		graphPanel.repaint();
	}
}



// 그래프 패널 클래스
class GraphPanel extends JPanel {
	private static final long serialVersionUID = -8336681642988362054L;
  // 십만 단위로 그래프 수치화 상수
	private static final double HUNDREDTHOUSAND = 0.00001;
  // 날짜 변수와 매출량을 담은 ColumnElement객체 ArrayList
	private ArrayList<ColumnElement> list;
	// 생성될 때 list값을 매개변수로 받음
	public GraphPanel(ArrayList<ColumnElement> list) {
		this.list = list;
	}
  // 그래픽을 그리는 메서드
	public void paint(Graphics g) {
    //좌표(x, y)에 크기(width, height)만큼 사각형을 그린다.
    // size 435, 367
		g.clearRect(0, 0, getWidth(), getHeight());
    // 좌표(50, 250)에서 좌표(350, 250)까지 직선을 그린다. (가로선)
		g.drawLine(50, 250, 350, 250);
    // drawString: 캔버스 위에 글자를 새김, x,y값
		g.drawString("월", 60, 270);
		g.drawString("화", 105, 270);
		g.drawString("수", 150, 270);
		g.drawString("목", 198, 270);
		g.drawString("금", 240, 270);
		g.drawString("토", 285, 270);
		g.drawString("일", 330, 270);
		g.drawString("(단위: 십만원)", 360, 270);
		for (int cnt = 1; cnt < 11; cnt++) {

			g.drawString(cnt * 10 + "만", 15, 255 - 20 * cnt);
			g.drawLine(50, 250 - 20 * cnt, 350, 250 - 20 * cnt);

		}
    // 좌표(50, 20)에서 좌표(50, 250)까지 직선을 그린다. (세로선)
		g.drawLine(50, 20, 50, 250);
		g.setColor(Color.darkGray);

		int value;
    // ColumnElement객체 ArrayList를 순회
		for(int i = 0; i < list.size(); i++) {
      // 매출량을 그래프에 올리기 위해 계산한 후 정수화
			value = (int)(list.get(i).getValue() * HUNDREDTHOUSAND);
      // 해당 날자의 막대선 그리기
			if(value > 0)
        // fillRect: 사각형 채우기,
        // 좌표(60 + 45*i, 250 - value * 2)에 크기(10,  value * 2)만큼 사각형을 그린다.
				g.fillRect(60 + 45*i, 250 - value * 2, 10, value * 2);
		}
	}

	protected void reSet(ArrayList<ColumnElement> list) {
		this.list = list;
	}

}
