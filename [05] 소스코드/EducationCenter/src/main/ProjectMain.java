package main;

import java.util.Scanner;

import apply.ApplyMain;
import student.StudentBasic;
import student.StudentLogIn;
import student.StudentMain;
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
			System.out.println("2. 수강신청");
			System.out.println("3. 교사 로그인");
			System.out.println("4. 관리자 로그인");
			System.out.println("5. 회원가입");
			System.out.println("0. 프로그램 종료");
			System.out.print("입력 : ");
			String input = sc.nextLine();
			
			if (input.equals("1")) {
				// 학생 로그인
				StudentLogIn login = new StudentLogIn();
				StudentBasic logInStudent = login.logIn();
				
				// 로그인 오류시 반복문으로 돌아가기
				if(logInStudent == null) {
					continue;
				}
				
				StudentMain studentMain = new StudentMain();
				try {
					studentMain.showInfo(logInStudent);
					studentMain.showMenu(logInStudent);
				} catch (NullPointerException e) {
					System.out.println("본 서비스는 개강 이후 이용이 가능합니다. 개강일이 지난 이후 이용해주세요!");
					System.out.println("뒤로 돌아가시려면 아무키나 눌러주시기 바랍니다.");
					sc.nextLine();
				} 
				
				catch (Exception e) {
					System.out.println("ProjectMain.main()");
					e.printStackTrace();
				}
				
			} else if(input.equals("2")) {
				// 수강신청
				StudentLogIn login = new StudentLogIn();
				StudentBasic logInStudent = login.logIn();
				if(logInStudent != null) {
					ApplyMain apply = new ApplyMain();
					if(apply.checkApply(logInStudent) == 0) {
						apply.showMain(logInStudent);
					} else {
						System.out.println("이미 수강중이거나 수강 예정인 과목이 존재합니다.");
					}
					System.out.println("계속하시려면 엔터를 입력해주세요.");
					sc.nextLine();
					
				}
				
			}
			else if (input.equals("3")) {
				// 교사 로그인
			} else if (input.equals("4")) {
				// 관리자 로그인
			} else if (input.equals("5")) {
				StudentSignIn signIn = new StudentSignIn();
				signIn.signInInput();
			} else if (input.equals("0")) {
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
