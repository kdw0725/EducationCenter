package main;

import java.util.Scanner;

import com.github.lalyos.jfiglet.FigletFont;

import admin.AdminLogIn;
import admin.AdminMain;
import apply.ApplyMain;
import student.StudentBasic;
import student.StudentLogIn;
import student.StudentMain;
import student.StudentSignIn;
import teacher.TeacherMain;

/**
 * 
 * @author 김동욱
 * 쌍용 교육센터 JDBC 프로젝트
 */
public class ProjectMain {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			mainIcon();
			figlet();
			System.out.println("===== M A I N ========");
			System.out.println("1. 학생 로그인");
			System.out.println("2. 수강신청");
			System.out.println("3. 교사 로그인");
			System.out.println("4. 관리자 로그인");
			System.out.println("5. 회원가입");
			System.out.println("0. 프로그램 종료");
			System.out.println("======================");
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
					// 로그인시 로그인된 객체를 결과로 받아옴
					studentMain.showInfo(logInStudent);
					// 그 결과를 통하여 메뉴 출력하기
					studentMain.showMenu(logInStudent);
				} catch (NullPointerException e) {	// 개강이 되지 않는 경우 처리
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
					// 수강신청이 존재하는지 확인
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
				TeacherMain teacherMain = new TeacherMain();
				teacherMain.teacherLogin();
			} else if (input.equals("4")) {
				// 관리자 로그인
				AdminLogIn logIn = new AdminLogIn();
				int logInResult = logIn.logIn();
				if(logInResult == 0) {
					System.out.println("아이디와 비빌번호를 확인하여주세요.");
				} else if(logInResult == 1) {
					System.out.println("관리자로 로그인이 되었습니다.");
					AdminMain adminMain = new AdminMain();
					adminMain.showMain();
				}
				
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

	// Chick Center 글씨 출력
	private static void figlet() {
		try {
			String menu =  FigletFont.convertOneLine(FigletFont.class.getResourceAsStream("/flf/puffy.flf"), "  CHICK CENTER");
			System.out.println(menu);
		} catch (Exception e) {
			System.out.println("ProjectMain.figlet()");
			e.printStackTrace();
		}
	}

	// 병아리 아이콘 출력
	private static void mainIcon() {
		
		System.out.println("                                                                                \r\n" + 
	               "                                                                                \r\n" + 
	               "                                     iv5SPKS1v:.                                \r\n" + 
	               "                                  SQQKvi:..:ivSgQD1.                            \r\n" + 
	               "                                RBJ              .sQBP.                         \r\n" + 
	               "                              vQr                    sBB.                       \r\n" + 
	               "                             JBr                       YBs                      \r\n" + 
	               "                            .ZBQEPgZBi                   QY                     \r\n" + 
	               "                          iBBY.     QB                   :Q                     \r\n" + 
	               "                         BBi  ..... .B..rs5Q:             Bi                    \r\n" + 
	               "                  PQDgPBBX  ...:..  :BgLYY:SB.            B.                    \r\n" + 
	               "                  BQ:.:Bs ....... :BBBB5    IB  :jEQBr   2B                     \r\n" + 
	               "                   IBuQg ......:. ZBBBBBi .. PQgd1: BBB:.B.                     \r\n" + 
	               "                     gB. ........ :BBBBE..... .   . 1B QBr                      \r\n" + 
	               "                     7B ...:......  :r.  .......... LB                          \r\n" + 
	               "                     DB ......:.:...   .....:...... DB                          \r\n" + 
	               "                     gB .................:.:.:...:. vBi                         \r\n" + 
	               "                     LB ..............:.....:...:... :RQZULi:..                 \r\n" + 
	               "                      Br ..............:.:...:.:.:.... .:7sjjBg                 \r\n" + 
	               "                      XQ  ......:.................:......   RB                  \r\n" + 
	               "                       QB. ........:...:.....:........... .BB                   \r\n" + 
	               "                        PBr  .........:...:............ .2B1                    \r\n" + 
	               "                          BBv.   ...................  .uBg                      \r\n" + 
	               "                     rr  7BuIQQSL:...           ..:7XgBd                        \r\n" + 
	               "                     rgBBM    .75ZDRDDddqPqPqPbDgMZqr .Mu                      \r\n" + 
	               "                                     ...::i::...       BB      **                 \r\n" + 
	               "                                                      r7                        \r\n" + 
	               "                                                                                \r\n" + 
	               "                                                                                \r\n" + 
	               "                                                                                ");
		
		
		
	}

}
