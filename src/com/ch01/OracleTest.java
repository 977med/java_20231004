package com.ch01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleTest {
	DBConnectionMgr dbMgr = new DBConnectionMgr();//커넥션 풀을 관리하는 클래스
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public OracleTest() {
	
	}
	public List<Map<String,Object>> getIdPw(String id, String pw){
		List<Map<String,Object>> dList = null;//리스트안에 해시맵을 넣는 구조로 고객리스트 관리
		StringBuilder sql = new StringBuilder();//sql이라는 이름의 Stringbuilder생성
		try {
			sql.append("SELECT name, id, pw FROM customer WHERE id = \'"+id+"\'and pw = \'"+ pw+"\'");//SELECT문을 string형태로 sql에 append
			con = dbMgr.getConnection();//Connection객체를 받아온다.
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			dList = new ArrayList<>();
			Map<String,Object> rmap =  null;
			while(rs.next()) {//rs.next()를 하면 테이블의 커서를 로우단위로 하나씩 내리고 성공하면 true를 반환한다.
				rmap = new HashMap<>();//왜 조회한 값이 거꾸로 정렬?
				rmap.put("name", rs.getString("name"));
				rmap.put("id", rs.getString("id"));
				rmap.put("pw", rs.getString("pw"));
				dList.add(rmap);
			}
		} catch (SQLException se) {
			System.out.println(se.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return dList;
	}
	public static void main(String[] args) {
//		OracleTest ot = new OracleTest();//오라클 테스트 객체생성
//		List<Map<String,Object>> dList = ot.getIdPw("977med", "tiger");
//		for(int i=0;i<dList.size();i++) {
//			Map<String,Object> map = dList.get(0);
//			System.out.println(map);
//		}
	}
}