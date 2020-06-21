package covid;

import java.util.Scanner;

import admin.AdminCovid;
import admin.TeacherCovidControl;

/**
 * 
 * @author 김동욱
 * 코로나 메인을 담당하는 클래스
 */
public class CovidMain {
	Scanner scan = new Scanner(System.in);
	
	/**
	 * 코로나 메인
	 */
	public void showMain() {
		
		while (true) {
			System.out.println("====================");
			System.out.println("1. 교육생");
			System.out.println("2. 교사");
			System.out.println("3. 관리자");
			System.out.println("0. 뒤로가기");
			System.out.println("====================");
			System.out.print("입력 : ");
			String input = scan.nextLine();
			
			if(input.equals("0")) {
				break;
			} else if(input.equals("1")) {
				Covid19_student covidStudent = new Covid19_student();
				covidStudent.covid19stu();
			} else if(input.equals("2")) {
				// 교사 코로나
				TeacherCovidControl teacherCovidControl = new TeacherCovidControl();
				teacherCovidControl.teachercovid();
			} else if(input.equals("3")) {
				// 관리자 코로나
				AdminCovid adminCovid = new AdminCovid();
				adminCovid.admincovid();
			} else {
				System.out.println("올바른 번호를 입력해주세요!");
			}
			
		}
	}

}
