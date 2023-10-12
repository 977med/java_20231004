package com.ch01;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatClientView implements ActionListener{
	
	JFrame jf = new JFrame();
	JMenuBar 	jmb 		= new JMenuBar();
	JMenu	 	jm_game 	= new JMenu("게임");
	JMenuItem   jmi_exit 	= new JMenuItem("나가기");
	JMenu	 	jm_info 	= new JMenu("도움말");
	JMenuItem   jmi_detail 	= new JMenuItem("야구숫자게임이란?");
	JMenuItem   jmi_create 	= new JMenuItem("만든사람들");
	JPanel jp_center = new JPanel();
	JTextArea jta_display = null;
	JScrollPane jsp_display = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JTextField jtf_user   = new JTextField();
	Font f = new Font("Thoma",Font.BOLD,14);
	JPanel jp_east   = new JPanel();
	JButton jbtn_exit 	= new JButton("나가기");
	
	MultiChatClientView(){
		initDisplay();
	}
	
	public void initDisplay() {
		
		jta_display = new JTextArea();
		jsp_display = new JScrollPane(jta_display);
		jta_display.setOpaque(false);
		jf.setResizable(false);
		jm_game.add(jmi_exit);
		jm_info.add(jmi_detail);
		jm_info.add(jmi_create);
		jmb.add(jm_game);
		jmb.add(jm_info);
		System.out.println("initDisplay 호출 성공");
		jtf_user.addActionListener(this);//여기서 this는 자기자신 클래스를 가리킴.-BaseBallGame:내안에 actionPerformed
		jbtn_exit.addActionListener(this);
		jmi_exit.addActionListener(this);
		jbtn_exit.setBackground(new Color(54,54,54));
		jbtn_exit.setForeground(new Color(212,212,212));
		jp_east.setLayout(new GridLayout(4,1));
		jp_east.add(jbtn_exit);
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
		jf.setTitle("MultiChatClient ver.1");
		jf.setSize(400, 300);
		jf.setVisible(true);
		jta_display.setText("채팅방에 입장했습니다.\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new MultiChatClientView();
	}

}