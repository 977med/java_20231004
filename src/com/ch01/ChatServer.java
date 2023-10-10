package com.ch01;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		int port  = 7000;
		
		try {
			serverSocket = new ServerSocket(port);//서버소켓을 포트번호에 바인딩
			System.out.println("서버가 준비되었습니다.");
			
			socket = serverSocket.accept();//서버에 접속한 클라이언트소켓을 통신용소켓과 연결
			
			Sender sender = new Sender(socket);
			Receiver receiver  = new Receiver(socket);
			
			sender.start();
			receiver.start();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
class Sender extends Thread {
	Socket socket;
	DataOutputStream out;
	String name;
	
	Sender(Socket socket){
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());//socket의 output스트림을 얻어와 생성자로 넣어엮음
			name = "["+socket.getInetAddress()+":"+socket.getPort()+"]";
		} catch (Exception e) {
			
		}
	}
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while(out!=null) {//어떤 조건에서 DataOutputStream이 null상태가 되지?
			try {
				out.writeUTF(name+scanner.nextLine());

			} catch (IOException e) {
				
			}
		}
	}
}
class Receiver extends Thread{
	Socket socket;
	DataInputStream in;
	
	Receiver(Socket socket){
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			
		}
	}
	public void run() {
		try {
			while(in!=null) {
				String message = in.readUTF();
				System.out.println(message);
				if("quit".equalsIgnoreCase(message)) {
					break;
				}
			}
		} catch (IOException e) {

		}
	}
}