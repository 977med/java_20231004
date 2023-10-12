package com.ch01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatServerView{
	
	JFrame jf = new JFrame();
	JMenuBar 	jmb 		= new JMenuBar();
	JMenu	 	jm_game 	= new JMenu("게임");
	JPanel jp_center = new JPanel();
	JTextArea jta_display = null;
	JScrollPane jsp_display = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JTextField jtf_user   = new JTextField();
	Font f = new Font("Thoma",Font.BOLD,14);
	JPanel jp_east   = new JPanel();
	
	MultiChatServerView() {
		initDisplay();
	}
	
	public void initDisplay() {
		
		jta_display = new JTextArea();
		jsp_display = new JScrollPane(jta_display);
		jta_display.setOpaque(false);
		jf.setResizable(false);
		jmb.add(jm_game);
		System.out.println("initDisplay 호출 성공");
		jp_east.setLayout(new GridLayout(4,1));
		jta_display.setFont(f);
		jta_display.setBackground(new Color(255,255,200));
		jta_display.setForeground(new Color(57,109,165));

		jf.setJMenuBar(jmb);
		jtf_user.setBackground(new Color(255,255,200));
		jp_center.setBackground(Color.green);
		jp_east.setBackground(Color.black);
		jp_center.setLayout(new BorderLayout(0,10));
		jp_center.add("Center",jsp_display);
		jp_center.add("South",jtf_user);
		jta_display.setLineWrap(true);
		jf.setLayout(new BorderLayout(10,20));
		jf.add("Center",jp_center);
		jf.add("East",jp_east);
		jf.setTitle("MultiChatServer ver.1");
		jf.setSize(400, 300);
		jf.setVisible(true);
		jta_display.setText("서버가 준비되었습니다.\n");
	}

}
