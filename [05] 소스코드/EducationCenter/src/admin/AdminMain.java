package admin;

import java.util.Scanner;

public class AdminMain {
	
	public void showMain() {
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("1. 교사관리");
			System.out.println("2. 학생 관리");
			System.out.println("3. 기초 정보 관리");
			System.out.println("4. 개설 과정 관리");
			System.out.println("0. 뒤로 가기");
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
			} else if(input.equals("3")) {
				// 기초 정보 관리
			} else if(input.equals("4")) {
				// 개설 과정 관리
			} else {
				System.out.println("잘못된 번호입니다.");
				continue;			
			}
			
		}
		
		
		
	}
	
	

}
