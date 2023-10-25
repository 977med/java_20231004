package multichat.ver2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatClient implements ActionListener {
	JFrame jf = new JFrame();
	JPanel jp_center = new JPanel();
	JTextArea jta_display = null;
	JScrollPane jsp_display = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JTextField jtf_user   = new JTextField();
	Font f = new Font("Thoma",Font.BOLD,14);
	JPanel jp_east   = new JPanel();
	JButton jbtn_exit 	= new JButton("나가기");
	//////////////////////////////////////////위는view//////////////
	Socket socket = null;
	ObjectOutputStream oos = null;
	String nickName;
	LogIn li = null;//Login 화면객체
	
	MultiChatClient(){
		try {
			li = new LogIn(this);
			socket = new Socket("127.0.0.1",5000);
			System.out.println("서버접속 성공");
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			ClientReceiverThread thread = new ClientReceiverThread(this);
			thread.start();
			
		} catch (Exception e) {
			
		}
	}
	public void initDisplay() {//채팅view
		System.out.println("initDisplay호출성공1");
		jta_display = new JTextArea();
		jsp_display = new JScrollPane(jta_display);
		jta_display.setOpaque(false);
		jf.setResizable(false);
		jtf_user.addActionListener(this);//여기서 this는 자기자신 클래스를 가리킴.-BaseBallGame:내안에 actionPerformed
		jbtn_exit.addActionListener(this);
		jp_east.setLayout(new GridLayout(4,1));
		jp_east.add(jbtn_exit);
		jta_display.setFont(f);
		jp_center.setLayout(new BorderLayout(0,10));
		jp_center.add("Center",jsp_display);
		jp_center.add("South",jtf_user);
		jta_display.setLineWrap(true);
		jf.setLayout(new BorderLayout(10,50));
		jf.add("Center",jp_center);
		jf.add("East",jp_east);
		jf.setTitle("MultiChatClient ver.1");
		jf.setSize(400, 600);
		jf.setVisible(true);
		jta_display.setText("채팅방에 입장했습니다.\n");
	}
	@Override
	public void actionPerformed(ActionEvent e) {//뷰 입력은 여기서 처리
		Object obj = e.getSource();
		
		if(obj == jtf_user) {
			String userMessage = jtf_user.getText();
			System.out.println(userMessage);
			try {
				jtf_user.setText("");
				oos.writeObject(Protocol.MESSAGE+"|"+"user"+"|"+userMessage);
				System.out.println("oos보냄");
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}else if(obj == jbtn_exit) {
			try {
				oos.writeObject(Protocol.ROOM_OUT+"|"+"user"+"|"+"나갑니다.");
				jf.dispose();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				socket.close();		
				oos.flush();
				oos.close();
			} catch (Exception e2) {
			}
		}
	}
	public static void main(String[] args) {
		new MultiChatClient();
	}
	
}
class ClientReceiverThread extends Thread{//클라이언트 스레드
	MultiChatClient mcc = null;
	Socket socket =null;
	ObjectInputStream ois = null;
	
	ClientReceiverThread(MultiChatClient mcc){
		this.mcc = mcc;
		this.socket = mcc.socket;
	}
	public void run() {
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			while(ois != null){
				String message = ois.readObject().toString();//무한대기상태......
				if("LOGIN_ADMIT********".equals(message)) {
					mcc.initDisplay();
					mcc.li.dispose();//Frame하나만 끄는 메서드!!!! 여러개의 Frame을 조작할 수 있다.
				}else if("USED_ID********".equals(message)) {
					System.out.println("client: 사용중 아이디");
					JOptionPane.showMessageDialog(mcc.li.msv, "아이디가 사용중입니다", "알림", 2);
				}else if("AVAILABLE_ID********".equals(message)) {
					System.out.println("client: 사용가능 아이디");
					JOptionPane.showMessageDialog(mcc.li.msv, "사용가능한 아이디입니다", "알림", 2);
				}else if("WRONG_ID********".equals(message)) {
					JOptionPane.showMessageDialog(mcc.li, "아이디 비밀번호를 확인하세요", "알림", 2);
				}else if("NO_ID********".equals(message)) {
					JOptionPane.showMessageDialog(mcc.li, "가입된 아이디가 없습니다", "알림", 2);
				}else {
					System.out.println("여기 들어오니");
					mcc.jta_display.append(message+"\n");		
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				ois.close();
				socket.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}finally{
				
			}
		}
	}
}
