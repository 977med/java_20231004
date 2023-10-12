package com.ch01;

import java.net.*;
import java.io.*;
import java.util.*;


public class MultiChatServer extends Thread{
	MultiChatServerView serverView = null;
	ServerSocket serverSocket = null;
	Socket socket = null;
	List<ChatServerThread> clientThread;//클라이언트 스레드를 담을 ArrayList
	//클라이언트가 접속했을때 리스트에 add되어 관리된다.
	List<String> nickName = new ArrayList<>();
	//스레드마다 닉네임을 관리하기위해 리스트를 하나 더 만듦
	
	MultiChatServer(){
		clientThread = new ArrayList<>();
		Collections.synchronizedList(clientThread);
		serverView = new MultiChatServerView();
	}

	public void run() {
		int port = 5000;
		try {
			serverSocket = new ServerSocket(port);
			//System.out.println("서버가 준비되었습니다.");
			//서버가 준비됨
			while(true) {
				socket = serverSocket.accept();
				//서버에 도달한 클라이언트에 소켓배정
				
				ChatServerThread thread = new ChatServerThread(this);
				//스레드 하나 생성하고 소켓 넘겨줌
				thread.start();
				//스레드 시작
			}
		}catch (Exception e) {
			
		}
	}


	public static void main(String[] args) {
		new MultiChatServer().start();
	}

}
class ChatServerThread extends Thread{
	MultiChatServer mcs = null;
	Socket socket = null;	
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	
	
	ChatServerThread(MultiChatServer mcs){
		this.mcs = mcs;
		this.socket = mcs.socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());	
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mcs.clientThread.add(this);
		String nickName = socket.getInetAddress()+"";
		System.out.println(nickName);
		mcs.nickName.add(nickName);
		broadcast(nickName + "님이 입장하셨습니다.");
	}
	public void broadcast(String msg) {//모든 사용자에게 메시지를 보냄
		try {
			String id = "";
			for(int i = 0; i < mcs.clientThread.size(); i++) {
				if(this == mcs.clientThread.get(i)) {
					id = mcs.nickName.get(i);
				}//들어온 입력이 어느 사용자에게서 온 것인지 식별
			}
			for(int i = 0; i < mcs.clientThread.size(); i++) {
				mcs.clientThread.get(i).oos.writeObject("["+id+"]"+msg);
			}//List를 순회하면서 스레드마다 oos에 write 해준다.
			mcs.serverView.jta_display.append(msg+"\n");
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void run() {
		
		while(true) {//protocol | 닉네임 | 내용
			try {
				String msg = ois.readObject().toString();
				int protocol = 0;
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer(msg,"|");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				
					case Protocol.ROOM_IN: {
						st.nextToken();
						broadcast(st.nextToken());
						//System.out.println(this);
					}
					case Protocol.ROOM_OUT: {
						for(int i = 0; i < mcs.clientThread.size(); i++) {
							if(this == mcs.clientThread.get(i)) {
								//System.out.println(st.nextToken()+"님이 나가셨습니다.");
								broadcast(st.nextToken()+"님이 나가셨습니다.");
								mcs.clientThread.remove(i);
								mcs.nickName.remove(i);
								try {
									oos.flush();
									oos.close();
									ois.close();
									socket.close();
								} catch (Exception e) {
									
								}
								break;
							}
							//System.out.println(mcs.clientThread.size());
						}
					}
					case Protocol.SET_ID: {
						for(int i = 0; i < mcs.clientThread.size(); i++) {
							if(this == mcs.clientThread.get(i)) {
								mcs.nickName.set(i, st.nextToken());
								broadcast(mcs.nickName.get(i)+"로 닉네임이 설정되었습니다.");
							}
						}
					}
					case Protocol.MESSAGE: {
						st.nextToken();
						broadcast(st.nextToken());				
					}
					default:{
						
					}
				}

			} catch (Exception e) {
				
				
			}
		}
	}
}
