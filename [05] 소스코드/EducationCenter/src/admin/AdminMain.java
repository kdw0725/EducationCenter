package admin;

import java.util.Scanner;

import covid.CovidMain;

public class AdminMain {
	
	public void showMain() {
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("====================");
			System.out.println("1. 교사관리");
			System.out.println("2. 학생 관리");
			System.out.println("3. 기초 정보 관리");
			System.out.println("4. 개설 과정 관리");
			System.out.println("5. 출결 관리");
			System.out.println("6. 코로나 특별관리");
			System.out.println("0. 뒤로 가기");
			System.out.println("====================");
			System.out.print("번호 입력 : ");
			String input = sc.nextLine();
			if(input.equals("0")) {
				break;
			} else if(input.equals("1")) {
				// 교사 관리
				AdminManageTeacher adminManageTeacher = new AdminManageTeacher();
				adminManageTeacher.showMain();
			} else if(input.equals("2")) {
				// 학생 관리
				AdminStudent adminStudent = new AdminStudent();
				adminStudent.showMenu();
			} else if(input.equals("3")) {
				// 기초 정보 관리
				BasicControl basicControl = new BasicControl();
				basicControl.basicDataMenu();
			} else if(input.equals("4")) {
				// 개설 과정 관리
				OpenCourseControl openCourseControl = new OpenCourseControl();
				openCourseControl.manageocrs();
			} else if(input.equals("5")) {
				// 출결관리
				Admin_Attend adminAttend = new Admin_Attend();
				adminAttend.AttendManagementMenu();
			} else if(input.equals("6")) {
				// 코로나 특별관리
				CovidMain covidMain = new CovidMain();
				covidMain.showMain();
			} else {
				System.out.println("잘못된 번호입니다.");
				continue;			
			}
			
		}
		
		
		
	}
	
	

}
