package com.ch01;

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
	public static final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl11";
	public static final String user = "scott";
	public static final String pw = "tiger";
	public Connection getConnection()//getConnection 메서드로 
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");//데이터베이스와 연결하는 클래스(데이터베이스 드라이버)
			con = DriverManager.getConnection(url,user,pw);//url, user, pw를 받아서 Connection객체에 저장
		} catch (Exception e) {
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