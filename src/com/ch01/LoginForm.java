package com.ch01;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JFrame implements ActionListener {
	/////////////////////////////////////////////////////
	/* 선언부 */
	/////////////////////////////////////////////////////
	String nickName="";
	String imgPath="D:\\workspace_java\\dev_java\\src\\image\\";//이미지 파일이 있는 경로
	JLabel jlb_id = new JLabel("아이디");
	JLabel jlb_pw = new JLabel("패스워드");

	Font jl_font = new Font("휴먼매직체", Font.BOLD, 17);
	JTextField jtf_id = new JTextField("test");
	JPasswordField jpf_pw = new JPasswordField("123");

	JButton jbtn_login = new JButton(
			new ImageIcon(imgPath+"login.png"));
	JButton jbtn_join = new JButton(
			new ImageIcon(imgPath+"confirm.png"));

	// JPanel에 쓰일 이미지아이콘
	ImageIcon ig = new ImageIcon(imgPath+"main.PNG");

	/////////////////////////////////////////////////////
	/* 생성자 */
	/////////////////////////////////////////////////////
	public LoginForm(){
		initDisplay();
	}

	/////////////////////////////////////////////////////
	/* jpanal 오버라이드 */
	/////////////////////////////////////////////////////

	/* 배경이미지 */
	class mypanal extends JPanel {
		public void paintComponent(Graphics g) {
			g.drawImage(ig.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponents(g);
		}
	}

	/////////////////////////////////////////////////////
	/* 화면처리 */
	/////////////////////////////////////////////////////
	public void initDisplay() {
		setContentPane(new mypanal());
		
		/* 버튼과 텍스트필드 구성 */
		jbtn_join.addActionListener(this);
		jbtn_login.addActionListener(this);
		this.setLayout(null);
		this.setTitle("자바채팅 ver.1");
		this.setSize(350, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(800, 250);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// id 라인
		jlb_id.setBounds(45, 200, 80, 40);
		jlb_id.setFont(jl_font);
		jtf_id.setBounds(110, 200, 185, 40);
		this.add(jlb_id);
		this.add(jtf_id);

		// pw 라인
		jlb_pw.setBounds(45, 240, 80, 40);
		jlb_pw.setFont(jl_font);
		jpf_pw.setBounds(110, 240, 185, 40);
		this.add(jlb_pw);
		this.add(jpf_pw);

		// 로그인 버튼 라인
		jbtn_login.setBounds(175, 285, 120, 40);
		this.add(jbtn_login);

		// 회원가입 버튼 라인
		jbtn_join.setBounds(45, 285, 120, 40);
		this.add(jbtn_join);		
	}

	public static void main(String[] args) throws Exception {
		new LoginForm();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == jbtn_join) {
			//new memberJoin();
		} 
		else if (obj == jbtn_login) {
			if ( "".equals(jtf_id.getText()) || "".equals(jpf_pw.getText()) ){
				JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
				System.out.println("1번 메시지");
				return;
			}
			try {
				String mem_id = jtf_id.getText();
				String mem_pwd = jpf_pw.getText();
				if (mem_id.length()==0) {
					JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
					System.out.println("2번 메시지");
					return;
				}else {
					OracleTest ot = new OracleTest();
					List<Map<String, Object>> listMap = ot.getIdPw(mem_id, mem_pwd);
					Map<String, Object> map = listMap.get(0);
					String dbId = (String)map.get("id");
					System.out.println(dbId);
					String dbPw = (String)map.get("pw");
					System.out.println(dbPw);
					String dbName = (String)map.get("name");
					if(mem_id.equals(dbId) && mem_pwd.equals(dbPw)) {
						JOptionPane.showMessageDialog(this, dbName + "님의 접속을 환영합니다.");
						this.setVisible(false);
						jtf_id.setText("");
						jpf_pw.setText("");
						new MultiChatClient();
						//new WaitingRoom(this);	
					}else{
						JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);		
						System.out.println("3번 메시지");
					}
				}
			} catch (Exception ae) { 
				ae.printStackTrace();
			}
		}
	}
}