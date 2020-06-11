package main;

import java.util.Scanner;

import jdbc.DBUtil;
import student.StudentSignIn;

/**
 * 
 * @author 김동욱
 * 쌍용 교육센터 JDBC 프로젝트
 */
public class ProjectMain {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("교육센터 프로그램");
			System.out.println("1. 학생 로그인");
			System.out.println("2. 교사 로그인");
			System.out.println("3. 관리자 로그인");
			System.out.println("4. 회원가입");
			System.out.println("5. 프로그램 종료");
			System.out.print("입력 : ");
			String input = sc.nextLine();
			
			if (input.equals("1")) {
				// 학생 로그인
			} else if (input.equals("2")) {
				// 교사 로그인
			} else if (input.equals("3")) {
				// 관리자 로그인
			} else if (input.equals("4")) {
				StudentSignIn signIn = new StudentSignIn();
				signIn.signInInput();
			} else if (input.equals("5")) {
				System.out.println("프로그램을 종료합니다.");
				break;
			} else {
				System.out.println("올바른 번호를 입력해주세요.");
				System.out.println("계속 하시려면 엔터를 입력해주세요.");
				sc.nextLine();
			}
			
		}
		
		sc.close();
	}

}
