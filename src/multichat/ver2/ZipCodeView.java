package multichat.ver2;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class ZipCodeView extends JFrame implements ActionListener,FocusListener,MouseListener {
	//선언부
	String zdo = null;
	//물리적으로 떨어져 있는 db서버와 연결통로 만들기
	Connection 			con 	= null;
	//위에서 연결되면 쿼리문을 전달할 전령의 역할을 하는 인터페이스 객체 생성하기
	PreparedStatement 	pstmt 	= null;
	//조회된 결과를 화면에 처리해야 하므로 오라클에 커서를 조작하기 위해 ResultSet추가
	ResultSet 			rs 		= null;
	JPanel jp_north = new JPanel();//Div태그, span생각
	//insert here
	String zdos[] = {"전체","서울","경기","강원"};
	String zdos2[] = {"전체","부산","전남","대구"};
	Vector<String> vzdos = new Vector<>();//vzdos.size()==>0
	JComboBox jcb_zdo = new JComboBox(zdos);//West
	JComboBox jcb_zdo2 = null;//West
	JTextField jtf_search = new JTextField("동이름을 입력하세요.");//Center
	JButton jbtn_search = new JButton("조회");//East
	String cols[] = {"우편번호","주소"};
	String data[][] = new String[0][2];
	DefaultTableModel dtm_zipcode = new DefaultTableModel(data,cols);
	JTable jtb_zipcode = new JTable(dtm_zipcode);
	JTableHeader jth = jtb_zipcode.getTableHeader();
	JScrollPane jsp_zipcode = new JScrollPane(jtb_zipcode
			,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
			,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	String zdos3[] = null;
	DBConnectionMgr dbMgr = null;
	MemberShipView memberShipView = null;
	//MemberShip memberShip = null;
	//생성자

	public ZipCodeView(MemberShipView memberShipView) {
		if(dbMgr == null) dbMgr = new DBConnectionMgr(); 
		this.memberShipView = memberShipView;
	}

	//화면처리부
	public void initDisplay() {
		jtb_zipcode.requestFocus();
		jtf_search.addFocusListener(this);
		jtb_zipcode.addMouseListener(this);
		jbtn_search.addActionListener(this);
		jtf_search.addActionListener(this);
		jp_north.setLayout(new BorderLayout());
		/*	*/
		//vzdos.copyInto(zdos2);
		for(int x=0;x<zdos2.length;x++) {
			vzdos.add(zdos2[x]);
		}
		for(String s:vzdos) {
			System.out.println("s===>"+s);
		}
		//jcb_zdo2 = new JComboBox(zdos3);//West
		//jp_north.add("West",jcb_zdo2);
		jp_north.add("Center",jtf_search);
		jp_north.add("East",jbtn_search);
		this.add("North",jp_north);
		this.add("Center",jsp_zipcode);
		this.setTitle("우편번호 검색");
		this.setSize(500, 400);
		this.setVisible(true);
	}
	public List<Map<String, Object>> getMapList(String dong) {//db에서 list얻어오는 메서드
		StringBuilder sql = new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<>();
		sql.append("SELECT zipcode, address");
	    sql.append(" FROM zipcode_t");
	    sql.append(" WHERE dong LIKE ? || '%' ");
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dong);
			rs = pstmt.executeQuery();
			Map<String, Object> map =null; 
			
			while(rs.next()) {
				map = new HashMap<>();
				map.put("zipcode", rs.getInt("zipcode"));
				map.put("address", rs.getObject("address"));
				list.add(map);
			}
		}catch (SQLException se) {
			System.out.println(sql.toString());//출력된 sql을 토드에서 실행해볼것
		}
		catch (NullPointerException ne) {
			// TODO: handle exception
		}catch(Exception e) {
			
		}
		return list;
	}

	public void refreshData(String dong) {//리스트 맵을 화면에 구현하는 메서드
		if("".equals(dong)) {
			return;
		}
		List<Map<String, Object>> list = getMapList(dong);
		while(dtm_zipcode.getRowCount()>0) {
			dtm_zipcode.removeRow(0);
		}		
		for(int i = 0; i< list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Vector<Object> v = new Vector<>();
			v.add(0,map.get("zipcode"));
			v.add(1,map.get("address"));
			dtm_zipcode.addRow(v);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String command = e.getActionCommand();
		if(obj == jbtn_search || obj == jtf_search) {
			String userText =jtf_search.getText();
			refreshData(userText);
			jtf_search.setText("");
		}
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("더블클릭");
		if(e.getClickCount()==2) {
			int index = jtb_zipcode.getSelectedRow();
			int zipcode = (int)dtm_zipcode.getValueAt(index, 0);
			String address = (String)dtm_zipcode.getValueAt(index, 1);
			System.out.println(address);
			memberShipView.jtf_zipcode.setText(String.valueOf(zipcode));
			memberShipView.jtf_address.setText(address);
			this.dispose();
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}