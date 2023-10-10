package com.ch01;
import java.net.*;
import java.io.*;


public class ChatClient {

	public static void main(String[] args) {
		try {
			String serverIp = "127.0.0.1";
			
			Socket socket = new Socket(serverIp, 7000);//서버는 소켓을 생성할 때 서버ip와 포트번호를 생성자에
			//소켓이 생성되는 시점에 바로 서버에 연결을 시도?
			System.out.println("서버에 연결되었습니다.");
			
			Sender sender = new Sender(socket);
			Receiver receiver = new Receiver(socket);
			
			sender.start();
			receiver.start();
			}catch (ConnectException ce) {
				ce.printStackTrace();
			}catch (IOException ie) {
				ie.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}

	}

}
