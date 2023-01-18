package com._team.pos;
import com._team.DB.DBController;

public class Main {
	public static POS win;
	public static DBController dbc;
	public static void main(String[] args) {
		dbc= new DBController();
		//POS
		win = new POS();
		win.setTitle("POS");
		win.setSize(1215, 745);
		win.setVisible(true);
		win.setDefaultCloseOperation(POS.DISPOSE_ON_CLOSE);
		dbc.run();
	}
}