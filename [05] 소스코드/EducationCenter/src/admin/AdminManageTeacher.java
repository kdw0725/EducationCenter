package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Scanner;

import jdbc.DBUtil;

public class AdminManageTeacher {
	
	public void showMain() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("===============");
			System.out.println("1. 교사 등록");
			System.out.println("2. 교사 정보 조회");
			System.out.println("0. 뒤로가기");
			System.out.println("===============");
			System.out.print("입력 : ");
			String input = sc.nextLine();
			
			if(input.equals("0")) {
				break;
			} else if(input.equals("1")) {
				// 교사 등록
				addTeacher();
			} else if(input.equals("2")) {
				// 교사 정보 조회
				TeacherInfo teacherInfo = new TeacherInfo();
				teacherInfo.showMain();
			} else {
				System.out.println("잘못된 번호입니다.");
				continue;
			}
			
		}
		
	}


	private void addTeacher() {
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {
			System.out.println("==============================");
			System.out.print("교사 이름: ");
			String name = scan.nextLine();

			System.out.print("교사 아이디: ");
			String id = scan.nextLine();

			System.out.print("교사 주민등록번호: ");
			String ssn = scan.nextLine();

			System.out.print("교사 전화번호: ");
			String tel = scan.nextLine();
			
			System.out.println("==============================");

			System.out.println();

			System.out.print("> "+ name + " 교사 정보를 등록하시겠습니까?(y/n)");
			String insert = scan.nextLine();
			
			if (insert.toLowerCase().equals("y")) {

				String sql = "{ call proc_AddTeacher(?,?,?,?) }";
				
				conn = util.open();
				stat = conn.prepareCall(sql);
				
				stat.setString(1, name);
				stat.setString(2, id);
				stat.setString(3, ssn);
				stat.setString(4, tel);
				
				stat.executeUpdate();
				
//				System.out.println("Success: Insert tbl_teacher");
				System.out.println("등록을 완료하였습니다.");
				
				stat.close();
				conn.close();
				
			} else if (insert.toLowerCase().equals("n")) {
				System.out.println("등록을 중지합니다.");
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}
			System.out.println("이전 페이지로 돌아가겠습니다.");
			System.out.println("계속하시려면 엔터를 눌러주세요.");
			scan.nextLine();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_teacher");
		}
		
	}

}
