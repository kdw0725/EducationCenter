package student;

import java.util.Calendar;
/**
 * 달력 클래스
 * @author 장정우
 *
 */
public class MyCalendar { // 달력
	
	//월별 몇일까지 있는지
	private static int[] MAX_DAYS = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	//윤년일때
	private static int[] LEAP_MAX_DAYS = new int[] {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	
	/**
	 * 윤년 계산 메소드
	 * @param 연
	 * @return 윤년 여부
	 */
	public boolean isLeapYear(int year){
		
		if(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 월별 일수 계산
	 * @param 연
	 * @param 월
	 * @return 월별 최대 일
	 */
	public int getMaxDayOfMonth(int year, int month){
		
		if(isLeapYear(year)){
			return LEAP_MAX_DAYS[month -1];
		}else{
			return MAX_DAYS[month-1];
		}
		
	}
	
	/**
	 * 달력을 출력해주는 메소드 
	 * @param 연
	 * @param 월
	 */
	public void printCalendar(int year, int month){
		
		System.out.printf("<<< %4d년 %3d월 >>>>\n", year, month + 1);
		System.out.println(" 일   월   화   수   목   금   토");
		System.out.println("---------------------");
		
		// 1일이 무슨요일인지 받아오기
		int weekday = getWeekDay(year, month);
		
		// 요일간 공백 출력
		for(int i=0; i<weekday; i++){
			System.out.print("   ");
		}
		
		int maxDay = getMaxDayOfMonth(year, month);
		
		// 첫줄 출력
		int count = 7-weekday;
		int enter = count < 7 ? count : 0;
		
		for(int i=1; i<=count; i++){
			System.out.printf("%3d", i);
		}
		count++;
		System.out.println();
		
		// 둘째줄부터 출력
		for(int i=count; i<= maxDay; i++){
			System.out.printf("%3d", i);
			if(i % 7 == enter){ //줄 바꿈
				System.out.println();
			}
		}
		System.out.println();
	}

	
	// 입력받은 달의 1일이 무슨요일인지 찾음
	private int getWeekDay(int year, int month) {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, year);	// 년도 셋팅
		date.set(Calendar.MONTH, month);	// 월 셋팅
		date.set(Calendar.DATE, 1);	// 1일로 지정
		int weekday = date.get(Calendar.DAY_OF_WEEK) -1;	// 리턴값이 sun:1 ~ sat:7 로리턴 되므로 -1을 해줌
		return weekday;
	}

}
