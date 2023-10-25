package com.ch01;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class MultiChatClient implements ActionListener{

	
	JFrame jf = new JFrame();
	JMenuBar 	jmb 		= new JMenuBar();
	JMenu	 	jm_game 	= new JMenu("게임");
	//JMenuItem   jmi_setId 	= new JMenuItem("ID설정");
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
	//JButton jbtn_setId 	= new JButton("ID설정");
		
	
	public void initDisplay() {
		System.out.println("initDisplay호출성공");
		jta_display = new JTextArea();
		jsp_display = new JScrollPane(jta_display);
		jta_display.setOpaque(false);
		jf.setResizable(false);
		jm_game.add(jmi_exit);
		//jm_game.add(jmi_setId);
		jm_info.add(jmi_detail);
		jm_info.add(jmi_create);
		jmb.add(jm_game);
		jmb.add(jm_info);
		jtf_user.addActionListener(this);//여기서 this는 자기자신 클래스를 가리킴.-BaseBallGame:내안에 actionPerformed
		jbtn_exit.addActionListener(this);
		jmi_exit.addActionListener(this);
		//jbtn_setId.addActionListener(this);
		//jmi_setId.addActionListener(this);
		jbtn_exit.setBackground(new Color(54,54,54));
		jbtn_exit.setForeground(new Color(212,212,212));
		jp_east.setLayout(new GridLayout(4,1));
		jp_east.add(jbtn_exit);
		//jp_east.add(jbtn_setId);
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
		jta_display.setText("@@+ID를 입력하시면 닉네임이 설정됩니다.\n");
	}
////////////////////////////// 위는 view //////////////////////
	Socket socket = null;
	ObjectOutputStream  oos = null;
	String nickName;
	
	MultiChatClient(){
		
		initDisplay();
		try {
			socket = new Socket("172.16.2.180",5000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			//System.out.println("서버에 접속했습니다.");
			
			ClientReceiver receiver = new ClientReceiver(socket, this);
			//System.out.println("receiver 생성자");
			receiver.start();
			oos.writeObject(Protocol.ROOM_IN+"|"+nickName+"|"+"서버 입장");
			
			
			
		} catch (Exception e) {	
			
		}	
	}
	
	public void setId(String nickName) {
		this.nickName = nickName;
		try {
			oos.writeObject(Protocol.SET_ID+"|"+nickName.toString()+"|"+"닉네임 설정");			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String label = e.getActionCommand();
		Object obj = e.getSource();
		
		if("나가기".equals(label)) {
			try {
				//System.out.println("나가기 버튼 눌림");
				oos.writeObject(Protocol.ROOM_OUT+"|"+nickName+"|"+"");				
			} catch (Exception e2) {
				// TODO: handle exception
			}
			System.exit(0);
			try {
				socket.close();		
				oos.flush();
				oos.close();
			} catch (Exception e2) {
			}
		}else if(obj == jtf_user) {//메시지 입력
			String message = jtf_user.getText();
			//System.out.println(message+"이렇게 들어옴");
			jtf_user.setText("");
			if("@@".equals(message.substring(0,2))) {//아이디 설정방법
				//System.out.println("if문 진입");
				nickName = message.substring(2);
				//System.out.println(nickName);
				setId(nickName);
			}else {
				try {
					oos.writeObject(Protocol.MESSAGE+"|"+nickName+"|"+message.toString());
				} catch (Exception e2) {
					// TODO: handle exception
				}				
			}
		}
	}
	
	public static void main(String[] args) {
		new MultiChatClient();
	}
}

class ClientReceiver extends Thread{//서버로부터 받아서 화면에 출력만 해주면 됨
	Socket socket = null;
	MultiChatClient mcc = null;
	ObjectInputStream ois = null;
	ClientReceiver(Socket socket, MultiChatClient mcc){
		this.mcc = mcc;
		this.socket = socket;
	}
	public void run() {
		//System.out.println("진입");
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			
			while(true) {
				//System.out.println("여기까지 들어오니?");
				String msg = ois.readObject().toString();
				
				mcc.jta_display.append(msg+"\n");
				System.out.println(msg);	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				ois.close();
				socket.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
