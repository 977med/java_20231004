package multichat.ver2;

import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LogIn extends JFrame implements ActionListener {
	MultiChatClient mcc = null;	
	JLabel jlb_id = new JLabel("아이디");
	JLabel jlb_pw = new JLabel("패스워드");
	Font jl_font = new Font("휴먼매직체", Font.BOLD, 17);
	JTextField jtf_id = new JTextField();
	JPasswordField jpf_pw = new JPasswordField();
	JButton jbtn_login = new JButton("로그인");
	JButton jbtn_join = new JButton("회원가입");
	MemberShipView msv = new MemberShipView(this);
	LogIn(){
		
	}
	LogIn(MultiChatClient mcc){
		this.mcc = mcc;
		initDisplayLogIn();

	}
	public void initDisplayLogIn() {
		
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
		jlb_id.setBounds(30, 200, 80, 40);
		jlb_id.setFont(jl_font);
		jtf_id.setBounds(110, 200, 185, 40);
		this.add(jlb_id);
		this.add(jtf_id);

		// pw 라인
		jlb_pw.setBounds(30, 240, 80, 40);
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
	public static void main(String[] args) {
		new LogIn().initDisplayLogIn();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String mem_id = null;
		String mem_pwd = null;
		if(obj == jbtn_login) {
			System.out.println("버튼 눌렸다.");
			mem_id = jtf_id.getText();
			mem_pwd = jpf_pw.getText();
			try {
				mcc.oos.writeObject(Protocol.LOGIN_REQUEST+"|"+mem_id+"|"+mem_pwd);	
				jtf_id.setText("");
				jpf_pw.setText("");
			} catch (Exception e2) {
				
			}
		}else if(obj == jbtn_join) {
			System.out.println("join버튼 눌림");
			msv.initDisplay();
		}
	}
}
