package com.ch01;

public class BaseBallGameLogic {
	int randomNum = (int)(Math.random()*899)+100;

	public void ranCom() {
		randomNum = (int)(Math.random()*899)+100;
	}
	//사용자가 입력한 값을 판정하는 메소드를 구현해 봅시다.
	
	public String account(String user) {
		if(user.length()!=3) {
			return "세자리 숫자를 입력하세요.";
		}
		int[] numArr = new int[3];
		int[] inputArr = new int [3];		
		try {
			int userInput = Integer.valueOf(user);
			
		} catch (NumberFormatException e) {
			return "숫자만 입력하세요.";
		}
		int ranNum = randomNum;
		int userInput = Integer.valueOf(user);
		for(int i = 0; i < 3; i++) {
			numArr[2-i] = ranNum%10;
			inputArr[2-i] = userInput%10;
			ranNum /=10;
			userInput /=10;
		}
		
		int[] answer = new int[2];
		
		for(int i = 0; i < 3; i++) {//스트라이크, 볼, 아웃 카운트 계산
			int strike = 0;
			int ball = 0;
			for(int j = 0; j < 3; j++) {
				if(numArr[i] == inputArr[j]) {
					if(i == j) {
						strike++;//인덱스가 같고 값이 같으면 strike++						
					}else {
						ball++;
					}
				}
			}
			if(strike == 1) {//해당 자리수가 스트라이크인지 볼인지 판단
				answer[0]++;
			}else if(ball > 0) {
				answer[1]++;
			}
			if(answer[0] == 3) {
				return "정답입니다";
			}
		}return answer[0]+"스 "+answer[1]+"볼";
	}
		//사용자가 jtf_user에 입력한 숫자는 보기에는 숫자 처럼 보여도 알맹이는 문자열로 
		//인식을 합니다. 그래서 형전환을 한 후 my[]배열에 담아야 함니다.
		//문자열 "256"을 숫자로 담을 변수 선언
		//strike와ball을 지역변수로 해야 하는건 매 회차 마다 값이 변해야 하기 때문이다.

	//나가기 버튼이나 나가기 메뉴 아이템을 선택(클릭)했을때 호출되는 메소드 구현
	public void exit() {
		System.exit(1);
	}
	public void initialMent(BaseBallGameView bgv) {
		bgv.jta_display.setText("야구 게임을 시작합니다.\n" + "세자리 숫자를 입력해주세요.\n");
	}
}
