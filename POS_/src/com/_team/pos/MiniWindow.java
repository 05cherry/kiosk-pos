package com._team.pos;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/* 작은 창을 만드는 클래스입니다. */
class MiniWindow extends JFrame {
	//
	private static final long serialVersionUID = -683222729602835421L;
	// Attr객체를 만들 때 컴포넌트를 표시하는 final객체
	public static final int JLABEL = 0;
	public static final int JTEXTFIELD = 1;
	public static final int JBUTTON = 2;
	public static final int IMAGEICON = 3;
	public static final int YES_OR_NO = 4;
	public static final int JCOMBOBOX = 5;
	
	private int width, height;
	private LinkedHashMap<String, Component> comp = new LinkedHashMap<>();
	private JButton btnLeft;
	private JButton btnRight;
	public String imgSrc = "";
	private File file;
	private File originFile;
	
	// Constructor
	MiniWindow(String title, String btnLeftText, String btnRightText, ArrayList<Attr> attr) {
	/* title: 작은 창의 맨위에 타이틀로 쓸 문구, 왼쪽버튼(등록, 수정), 오른쪽버튼(취소), 가운데 패널에 들어가는 컴포넌트 정보 객체*/
		// 기본셋팅
		width = 353;
		height = 210 + attr.size() * 42;
		setLayout(null);
		setSize(width, height);
		setVisible(true);
		
		// Title
		JLabel titleLabel = new JLabel(title);
		titleLabel.setBounds(111, 21, 130, 30);
		add(titleLabel);

		// attribute
		for (int i = 0; i < attr.size(); i++) {
			JPanel attrPanel = addattr(attr.get(i).getName(), attr.get(i).getValue(), attr.get(i).getCompType());
			attrPanel.setBounds(15, 80 + (42 * i), 353, 30);
			add(attrPanel);
		}

		// Button
		btnLeft = new JButton(btnLeftText);
		JButton btnRight = new JButton(btnRightText);

		btnLeft.setBounds(110, 110 + (attr.size() * 42), 60, 30);
		btnRight.setBounds(198, 110 + (attr.size() * 42), 60, 30);

		add(btnLeft);
		add(btnRight);

		// addActionListener
		btnRight.addActionListener((e) -> {
			dispose();
		});
		
		/**디자인 시작-------------------------------**/
		//"상품 수정" 레이블 폰트, 정렬 지정
		titleLabel.setFont(new Font("", Font.BOLD, 28));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//"등록/수정", "취소" 버튼 색상, 폰트 지정
		btnLeft = POS.buttonSetColor(btnLeft);
		btnRight = POS.buttonSetColor(btnRight);
		btnLeft.setFont(new Font("", Font.BOLD, 10));
		btnRight.setFont(new Font("", Font.BOLD, 10));
		/**디자인 끝----------------------------------**/
	}

	
	private JPanel addattr(String name, String value, int compType) {
		if (compType == JLABEL) {
			return getAttrJLabel(name, value);
		} else if (compType == JTEXTFIELD) {
			return getAttrJTextField(name, value);
		} else if (compType == IMAGEICON) {
			return getAttrImageIcon(name, value);
		} else if (compType == YES_OR_NO) {
			return getAttrYesOrNo(name, value);
		} else if (compType == JCOMBOBOX) {
			return getAttrJComboBox(name, value);
		}
		return null;
	}

	private JPanel getAttrJLabel(String name, String value) {
		JPanel jp = new JPanel();
		JLabel jl1 = new JLabel(name);
		JLabel jl2 = new JLabel(value);

		jp.setLayout(null);

		jp.setSize(302, 30);
		;
		jl1.setBounds(0, 5, 106, 20);
		jl2.setBounds(107, 0, 195, 30);

		jl1.setFont(new Font("", Font.PLAIN, 20));
		jl2.setFont(new Font("", Font.PLAIN, 20));
		
		jp.add(jl1);
		jp.add(jl2);
		comp.put(jl1.getText(), jl2);
		return jp;
	}

	private JPanel getAttrJTextField(String name, String value) {
		JPanel jp = new JPanel();
		JLabel jl = new JLabel(name);
		JTextField jtf = new JTextField(value);

		jp.setLayout(null);

		jp.setSize(302, 30);
		jl.setBounds(0, 5, 106, 20);
		jtf.setBounds(107, 0, 195, 30);

		jl.setFont(new Font("", Font.PLAIN, 20));
		jtf.setFont(new Font("", Font.PLAIN, 20));

		jp.add(jl);
		jp.add(jtf);
		
		comp.put(name, jtf);
		return jp;
	}

	private JPanel getAttrImageIcon(String name, String value) {
		JPanel jp = new JPanel();
		JLabel jl = new JLabel(name);
		JButton jb = new JButton("img");

		jp.setLayout(null);

		jp.setSize(302, 30);
		jl.setBounds(0, 5, 106, 20);
		jb.setBounds(189, 0, 30, 30);

		jl.setFont(new Font("", Font.PLAIN, 20));
		
		originFile = new File(value);
		file = new File(value);
		imgSrc = file.toString();
		
		jb.addActionListener((e) -> {

			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png", "jpg", "JPG", "jpeg");
			
			// 사진 파일만 가져오게
			fileChooser.setFileFilter(filter);
			
			// 파일오픈 다이얼로그 를 띄움
			int result= fileChooser.showSaveDialog(this);
			File selectedFile;
			
			if (result != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일 설정을 취소하셨습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			while(true) {
				selectedFile = fileChooser.getSelectedFile();
				if(selectedFile.canRead()) {
					break;
				}
				JOptionPane.showMessageDialog(null, "파일을 읽을  수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				break;
			}
			fileChooser.setApproveButtonText("선택");

			if (result == JFileChooser.APPROVE_OPTION) {
				file = new File("images\\" + selectedFile.getName());
				originFile = selectedFile;
				// 경로 출력
				imgSrc = file.toString();
				System.out.println(imgSrc);
			}
		});

		jp.add(jl);
		jp.add(jb);
		comp.put(name, jb);
		return jp;
	}

	private JPanel getAttrYesOrNo(String name, String value) {
		JPanel jp = new JPanel();
		JLabel jl = new JLabel(name);
		JToggleButton btnY = new JToggleButton("Y");
		JToggleButton btnN = new JToggleButton("N");
		ButtonGroup bg = new ButtonGroup();

		jp.setLayout(null);

		jp.setSize(302, 30);
		jl.setBounds(0, 5, 106, 20);
		btnY.setBackground(Color.gray);
		btnN.setBackground(Color.gray);
		btnY.setBounds(159, 0, 50, 30);
		btnN.setBounds(229, 0, 50, 30);
		
		if(value.equals("재고 있음"))
			btnN.setSelected(true);
		else
			btnY.setSelected(true);
			

		jl.setFont(new Font("", Font.PLAIN, 20));

		bg.add(btnY);
		bg.add(btnN);
		jp.add(jl);
		jp.add(btnY);
		jp.add(btnN);
		comp.put(name, btnY);
		return jp;
	}
	
	private JPanel getAttrJComboBox(String name, String value) {
		JPanel jp = new JPanel();
		JLabel jl = new JLabel(name);
		String[] values = value.split(" ");
		String[] option = new String[values.length-1];
		
		//values의 마지막 배열요소를 제외한 배열 option 생성
		//values의 마지막 배열요소는 setSelectedItem에 활용
	    for(int index = 0; index < values.length - 1; index++)
	    	option[index] = values[index];
	    
		JComboBox<String> jc= new JComboBox<String>(option);

		jp.setLayout(null);

		jp.setSize(302, 30);
		jl.setBounds(0, 5, 106, 20);
		jc.setBounds(107, 0, 195, 30);
		
		//옵션 선택
		jc.setSelectedItem(values[values.length-1]);

		jl.setFont(new Font("", Font.PLAIN, 20));

		jp.add(jl);
		jp.add(jc);
		comp.put(name, jc);
		return jp;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public JButton getBtnLeft() {
		return btnLeft;
	}

	public JButton getBtnRight() {
		return btnRight;
	}

	public LinkedHashMap<String, Component> getComp() {
		return comp;
	}
	
	public void copyImagesFile() {
		try {
			Files.copy(originFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}catch (IOException e1) {
			System.out.println("이미지버튼 오류");
			return;
		}
	}

	public String getImgSrcPath() {
		return imgSrc;
	}
	
	public boolean isNumeric(String str) {
		return Pattern.matches("^[0-9]*$", str);
	}

	// 재고의 날짜 확인 -올바른 입력값인지 확인
	public boolean checkDate(String str) {
		
		try {
			SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd"); // 검증할 날짜 포맷 설정
			dateFormatParser.setLenient(false); // false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생 - setLetient(false)엄격하게 검사
			dateFormatParser.parse(str); // 대상 값 포맷에 적용되는지 확인
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}