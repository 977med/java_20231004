package com.ch01;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class BaseBallEvent implements ActionListener {
	BaseBallGameView bgv;
	BaseBallGameLogic bgl = new BaseBallGameLogic();
	BaseBallEvent() {
		
	}
	BaseBallEvent(BaseBallGameView bg){
		bgv = bg;
	}
	////////jtf_user에 엔터를 쳤을 때 , jbtn_exit버튼을 클릭했을때 이벤트 지원하는 인터페이스가 ActionListener이다.
	//ActionListener는 반드시 actionPerformed를 재정의 해야 한다.
	//annotation- 부모가 가진 메소드를 재정의 하였다 는  의미임.
	//콜백메소드는 개발자가 호출할 수 없는 메소드로 시스템 레벨에서 필요할 때 자동으로 호출됨.
	//자바에 main메소드도 일종의 콜백 메소드 임.

	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed 호출 성공");
		String label = e.getActionCommand();
		System.out.println("너가 누른 버튼의 문자열은 "+label+ " 입니다.");
		Object obj = e.getSource();//이벤트소스의 주소번지를담아줌.
		//너 나가기 버튼이니?
		if("나가기".equals(label)) {
			bgl.exit();
		}
		//새게임을 누른거야?
		else if(obj == bgv.jbtn_new) {
			bgl.ranCom();
			bgv.jta_display.setText("");
			bgv.jta_display.setText("새로운 게임을 시작합니다.\n");
		}
		//이벤트가 발생한 이벤트 소스의 문자열을 비교하기
		else if(obj==bgv.jtf_user) {//JTextfield는 자동으로 엔터입력 후 텍스트를 인식한다.
			bgv.jta_display.append(bgv.jtf_user.getText()+"\n");
			bgv.jta_display.append(bgl.account(bgv.jtf_user.getText())+ "\n");
			bgv.jtf_user.setText("");
		}///////////입력하고 엔터 쳤을 때
		else if(obj==bgv.jbtn_dap) {
			bgv.jta_display.append("정답은 " + bgl.randomNum + "입니다.\n");
		}else if(obj == bgv.jbtn_clear) {
			bgl.initialMent(bgv);
		}
	}///////////////end of actionPerformed
}
