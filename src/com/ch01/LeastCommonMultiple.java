package com.ch01;

public class LeastCommonMultiple {
	//유클리드 호제법
	//두 수를 나누어 나머지가 0이면 작은 수가 최대공약수
	//만약 나누어떨어지지 않으면 작은 수를 나머지로 나눈다.
	//나머지가 0이 나올때까지 위 과정을 반복한다.

    public int gcf(int a, int b){//최소공배수를 구하는 메서드
        int r = 0;
        if(a > b){
            r = a%b;
            if(r == 0){
                return b;
            }else{
                return gcf(b,r);
            }
        }else{
            r = b%a;
            if(r == 0){
                return a;
            }else{
                return gcf(a,r);
            }
        }
    }
	public static void main(String[] args) {
		//최소공배수는 두 수를 곱한 후에 최대공약수로 나누면 나온다.
		int[] arr = {2,6,8,4,7,12};
		LeastCommonMultiple lcm = new LeastCommonMultiple();
		if(arr.length == 1){
            System.out.println(arr[0]);
        }
        int g = lcm.gcf(arr[0], arr[1]);
        int answer = arr[0]*arr[1]/g;
            
        for(int i = 2; i < arr.length; i++){
            g = lcm.gcf(answer, arr[i]);
            answer = answer*arr[i]/g;
        }
        System.out.println(answer);
	}

}