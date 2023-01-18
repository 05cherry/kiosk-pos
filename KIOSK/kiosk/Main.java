package com._team.kiosk;
import javax.swing.JFrame;
import com._team.DB.DBController;

public class Main {
	public static Kiosk win;
	public static DBController dbc;
	public static void main(String[] args) {
		dbc = new DBController();
		//KIOSK
		win = new Kiosk();
        win.setSize(664, 1172);
        win.setVisible(true);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dbc.run();
	}
}