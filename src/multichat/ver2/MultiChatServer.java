package multichat.ver2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class MultiChatServer implements Runnable {
	List<ChatServerThread> clientThread = null; //스레드 관리용 리스트
	List<Map<String, Object>> list = null;//테이블 받아오는 리스트맵
	ServerSocket serverSocket = null;//서버소켓관리
	Socket clientSocket = null;
	Connection con = null;//커넥션
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DBConnectionMgr dbMgr = null;//커넥션 매니져
	UserDTO userDTO = null;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		dbMgr = new DBConnectionMgr();//DBConnection객체생성
		clientThread = new Vector<>();//스레드 관리용 Vector
		Collections.synchronizedList(clientThread);//스레드 동기화
		//printIdPw();//db연동 테스트
		try {
			System.out.println("서버소켓 생성");
			serverSocket = new ServerSocket(5000);
			while(true) {
				clientSocket = serverSocket.accept();
				System.out.println("클라이언트 접속");
				ChatServerThread thread = new ChatServerThread(this);
				thread.start();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {//종료 시 자원 반환
			try {
				serverSocket.close();
				rs.close();
				pstmt.close();
				con.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public List<Map<String, Object>> getUserInfo(String id) {//DB에서 정보 얻어오는 메서드
		StringBuilder sql = new StringBuilder();
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> cmap = null;
		sql.append("SELECT name, id, pw FROM user_info WHERE id = ?");
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				cmap = new HashMap<>();
				cmap.put("name", rs.getString("name"));
				cmap.put("id", rs.getString("id"));
				cmap.put("pw", rs.getString("pw"));
				list.add(cmap);
			}
			if(cmap == null) {
				return null;
			}
		} catch (Exception e) {
		}
		return list;
	}
	public boolean idDuplicationCheck(String id) {//중복이 있으면 true반환
		boolean check = true;
		if(getUserInfo(id) == null) {
			check = false;//id가 DB에 없으면 false 반환
		}
		return check;
	}
	
	public void joinDB(UserDTO userDTO) {//DTO를 받아서 INSERT하는 메서드
		boolean check = idDuplicationCheck(userDTO.getId());
		if(check) {
			System.out.println("이아이디는 중복됨");
			return;//id중복이면 메서드 종료
		}
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO user_info(id, pw, nickname, name,gender,zipcode,address)");
		sql.append(" VALUES (?,?,?,?,?,?,?)");
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, userDTO.getId());
			pstmt.setString(2, userDTO.getPw());
			pstmt.setString(3, userDTO.getNickname());
			pstmt.setString(4, userDTO.getName());
			pstmt.setString(5, userDTO.getGender());
			pstmt.setString(6, userDTO.getZipcode());
			pstmt.setString(7, userDTO.getAddress());
			pstmt.executeUpdate();
			System.out.println(sql);
		} catch (Exception e) {
		}
		System.out.println("회원가입 성공");
		
	}
	
	public void printIdPw() {//db에서 받아온 자료 콘솔 출력
		List<Map<String, Object>> list = getUserInfo("977med");
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	public void broadCast(String message) {//모든 클라이언트에게 메시지 보내기
		for(ChatServerThread cst : clientThread) {
			try {
				cst.oos.writeObject(message);		
				System.out.println(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public static void main(String[] args) {
		MultiChatServer mcs = new MultiChatServer();
		Thread thread = new Thread(mcs);
		thread.start();
	}
	
}
class ChatServerThread extends Thread {
	MultiChatServer mcs = null;
	Socket clientSocket = null;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	String nickName;
	UserDTO userDTO = null;
	
	ChatServerThread(MultiChatServer mcs){
		this.clientSocket = mcs.clientSocket;
		this.mcs = mcs;
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}	
	public void run() {//프로토콜 | 닉네임 | 메시지 
		System.out.println("스레드 시작");
		mcs.clientThread.add(this);
		try {
			boolean isStop = false;
			while(!isStop) {
				String message = (String)ois.readObject();
				System.out.println(message);
				StringTokenizer st = null;
				int protocol = 0;
				if(ois != null) {
					st = new StringTokenizer(message, "|");
					protocol = Integer.parseInt(st.nextToken());
					String id = st.nextToken();
					String msg = st.nextToken();
					
					switch(protocol) {
					
					case Protocol.MESSAGE: {
						mcs.broadCast(nickName+":"+msg);
						System.out.println("메시지 발송");
						break;
					}
					case Protocol.ROOM_OUT:{
						mcs.broadCast(nickName+"님이 나가셨습니다.");
						mcs.clientThread.remove(this);
						break;
					}
					case Protocol.LOGIN_REQUEST:{
						List<Map<String, Object>> list = mcs.getUserInfo(id);
						Map<String, Object> umap = null;
						int cnt = 0;
						System.out.println("DB에서 받아옴"+list);
						if(list != null) {
							umap = list.get(0);
							cnt = list.size();
						}
						if(cnt == 1) {
							String dbId = (String)umap.get("id");
							String dbPw = (String)umap.get("pw");
							if(id.equals(dbId) && msg.equals(dbPw)) {
								nickName = (String)umap.get("id");
								oos.writeObject("LOGIN_ADMIT********");
								mcs.broadCast(nickName+"님이 입장하셨습니다.");
							}else{
								System.out.println("비밀번호가 일치하지 않습니다.");
								oos.writeObject("WRONG_ID********");
							}
						}else if(cnt == 0) {
							System.out.println("가입된 아이디가 없습니다.");
							oos.writeObject("NO_ID********");
						}
						System.out.println(cnt);
						break;
					}
					case Protocol.JOIN:{
						System.out.println("회원가입 할래!");
						userDTO = new UserDTO();
						StringTokenizer userst = new StringTokenizer(msg, "%");
						
						userDTO.setId(userst.nextToken());
						userDTO.setPw(userst.nextToken());
						userDTO.setNickname(userst.nextToken());
						userDTO.setName(userst.nextToken());
						userDTO.setGender(userst.nextToken());
						userDTO.setZipcode(userst.nextToken());
						userDTO.setAddress(userst.nextToken());
						
			        	System.out.println(userDTO.getId());	  
			        	mcs.joinDB(userDTO);
						break;
					}
					case Protocol.ID_CHECK:{
						System.out.println("ID 체크하자");
						boolean check = mcs.idDuplicationCheck(msg);
						try {
							if(check) {
								oos.writeObject("USED_ID********");
								System.out.println("이 id는 이미 사용중");
							}else {
								oos.writeObject("AVAILABLE_ID********");
								System.out.println("이 id는 사용가능");
							}							
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					}
				}
			}		
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				oos.close();
				ois.close();
				clientSocket.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
	}
}
