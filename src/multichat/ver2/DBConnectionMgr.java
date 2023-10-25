package multichat.ver2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnectionMgr {
	Connection con = null;//oracle과 연결을 하기 위한 통로를 만드는 클래스
	PreparedStatement pstmt = null;//Java의 
	ResultSet rs = null;//오라클에 커서를 내리면서 정보를 받아올 수 있도록 도와주는 클래스
	//DB관리자 정보를 상수로 저장하는 변수 
	public static final String _DRIVER 	= "oracle.jdbc.driver.OracleDriver";
	public static final String _URL 	= "jdbc:oracle:thin:@127.0.0.1:1521:SID";
	public static final String _USER 	= "user";
	public static final String _PW 		= "password";
	public Connection getConnection()//getConnection 메서드로 
	{
		//예외처리시 catch문은 멀티블록이 가능함, 단 하위에서 상위클래스방향으로 처리
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");//데이터베이스와 연결하는 클래스(데이터베이스 드라이버)
			con = DriverManager.getConnection(_URL,_USER,_PW);//url, user, pw를 받아서 Connection객체에 저장
		}catch (ClassNotFoundException e) {
			System.out.println("ojdbc.jar를 설정하지 않았다. 그래서 클래스를 못찾음");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	public static void freeConnection(ResultSet rs, PreparedStatement pstmt, Connection con){
		try {
			if(rs !=null) rs.close();
			if(pstmt !=null) pstmt.close();
			if(con !=null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void freeConnection(PreparedStatement pstmt, Connection con){
		try {
			if(pstmt !=null) pstmt.close();
			if(con !=null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void freeConnection(ResultSet rs, CallableStatement cstmt, Connection con){
		try {
			if(rs !=null) rs.close();
			if(cstmt !=null) cstmt.close();
			if(con !=null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void freeConnection(CallableStatement cstmt, Connection con){
		try {
			if(cstmt !=null) cstmt.close();
			if(con !=null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}