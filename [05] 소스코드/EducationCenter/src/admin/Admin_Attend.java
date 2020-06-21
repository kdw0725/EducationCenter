package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import jdbc.DBUtil;

/**
 * 
 * @author 신수진
 * 관리자 출결 관련 메소드
 */
public class Admin_Attend {
	
	 Connection conn = null;
	 Statement stat = null;
	 CallableStatement callStat = null;
	 ResultSet rs = null;
	 DBUtil util = new DBUtil();
	 Scanner scan = new Scanner(System.in);
	 List<String> stuAtList = new ArrayList<String>();
	 List<String> teaAtList = new ArrayList<String>();
	 List<String> admAtList = new ArrayList<String>();
	
	 String admseq = "1";
	
	public void main(String[] args) {
		
		AttendManagementMenu();		//3-5. 출결관리
		
	}
	
	/**
	 * 관리자 출결 관리 메뉴
	 */
	public void AttendManagementMenu() {
		//출결관리 메뉴
		while (true) {
			System.out.println("====================");
			System.out.println("1. 관리자");
			System.out.println("2. 교사");
			System.out.println("3. 학생");
			System.out.println("0. 뒤로가기");
			System.out.println("====================");
			System.out.print("번호 입력 : ");
			String num = scan.nextLine();
			System.out.println();
			
			if (num.equals("1")) {
				//관리자
				AttendAdmin();
			} else if (num.equals("2")) {
				//교사
				AttendTeacher();
			} else if (num.equals("3")) {
				//학생
				AttendStudent();
			} else if (num.equals("0")) {
				//뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
	}//AttendManagementMenu

	/**
	 * 학생 출결 조회
	 */
	private void AttendStudent() {
		//학생 출결 조회

		stuAtList.clear();
		
		System.out.println("====================================================");
		MyCalendar();
		System.out.println();
		System.out.println("====================================================");
		System.out.println("조회할 기간 입력");
		System.out.print("시작 날짜(yyyy-mm-dd) : ");
		String startDate = scan.nextLine();
		System.out.print("종료 날짜(yyyy-mm-dd) : ");
		String endDate = scan.nextLine();
		
		try {
			
			conn = util.open();
			stat = conn.createStatement();
			String sql = String.format("select * from vw_studentAttend "
					+ "where days >= '%s' and days <= '%s' order by days, seq", startDate, endDate);
			rs = stat.executeQuery(sql);
			
			while(rs.next()) {
				stuAtList.add(String.format("%s,%s,%s,%s,%s"
						, rs.getString("seq") + "/" +rs.getString("name")
						, rs.getString("days")
						, rs.getString("intime")
						, rs.getString("outtime")
						, rs.getString("status")));
			}
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("[학생번호/이름]\t  [날짜]\t[입실]\t[퇴실]\t[상태]");
		Pagingfile.page(Pagingfile.save(stuAtList));
		
		System.out.println("====================================================");
		System.out.println("이전 페이지로 돌아가겠습니다.");
		System.out.println("계속하시려면 엔터를 눌러주세요.");
		scan.nextLine();
		
	}//AttendStudent

	private void AttendTeacher() {
		//교사 출결 조회

		teaAtList.clear();
		
		System.out.println("====================================================");
		MyCalendar();
		System.out.println();
		System.out.println("====================================================");
		System.out.println("조회할 기간 입력");
		System.out.print("시작 날짜(yyyy-mm-dd) : ");
		String startDate = scan.nextLine();
		System.out.print("종료 날짜(yyyy-mm-dd) : ");
		String endDate = scan.nextLine();
		
		try {
			
			conn = util.open();
			stat = conn.createStatement();
			String sql = String.format("select * from vw_teacherAttend "
					+ "where days >= '%s' and days <= '%s' order by days, seq", startDate, endDate);
			rs = stat.executeQuery(sql);
			
			while(rs.next()) {
				teaAtList.add(String.format("%s,%s,%s,%s,%s"
						, rs.getString("seq") + "/" +rs.getString("name")
						, rs.getString("days")
						, rs.getString("intime")
						, rs.getString("outtime")
						, rs.getString("status")));
			}
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("[교사번호/이름]\t  [날짜]\t[출근]\t[퇴근]\t[상태]");
		Pagingfile.page(Pagingfile.save(teaAtList));
		
		System.out.println("====================================================");
		System.out.println("이전 페이지로 돌아가겠습니다.");
		System.out.println("계속하시려면 엔터를 눌러주세요.");
		scan.nextLine();
		
	}//AttendTeacher

	private void AttendAdmin() {
		//관리자 출결 조회 + 기록
		while (true) {
			System.out.println("==========================");
			System.out.println("1. 출결 조회");
			System.out.println("2. 출결 기록");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String num = scan.nextLine();
			System.out.println();
			
			if (num.equals("1")) {
				//출결 조회
				ShowAttendAdmin();
			} else if (num.equals("2")) {
				//출결 기록
				CheckAttendAdmin();
			} else if (num.equals("0")) {
				//뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
	}//AttendAdmin
	
	private void CheckAttendAdmin() {
		//출결 기록
		try {
			
			conn = util.open();
			stat = conn.createStatement();
			String sql = String.format("select count(*) cnt from tbl_attend_admin where admseq = %s "
					+ "and to_char(days, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd')", admseq);
			rs = stat.executeQuery(sql);
			int count = 0;
			
			if (rs.next()) {
				count = Integer.parseInt(rs.getString("cnt"));
			}
			stat.close();
			
			if (count == 0) {
				//출근
				
				Calendar now = Calendar.getInstance();
				System.out.printf("현재 시간 : %tF %tT\r\n", now, now);
				System.out.print("> 출근 하시겠습니까?(y/n)");
				String status = scan.nextLine();
				
				if (status.toLowerCase().equals("y")) {
					
					sql = "{ call proc_InAttendAdmin(?) }";

					callStat = conn.prepareCall(sql);
					
					callStat.setString(1, admseq);
					
					callStat.executeUpdate();
					
					System.out.println("출근을 완료했습니다.");
					
					callStat.close();
					
				} else if (status.toLowerCase().equals("n")) {
					System.out.println("출결 기록을 중지합니다.");
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
				}
				System.out.println("이전 페이지로 돌아가겠습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
				
			} else if (count > 0) {
				//퇴근
				
				Calendar now = Calendar.getInstance();
				System.out.printf("현재 시간 : %tF %tT\r\n", now, now);
				System.out.print("> 퇴근 하시겠습니까?(y/n)");
				String status = scan.nextLine();
				
				if (status.toLowerCase().equals("y")) {
					
					sql = "{ call proc_OutAttendAdmin(?) }";
					
					callStat = conn.prepareCall(sql);
					
					callStat.setString(1, admseq);
					
					callStat.executeUpdate();
					
					System.out.println("퇴근을 완료했습니다.");
					
					callStat.close();
					
				} else if (status.toLowerCase().equals("n")) {
					System.out.println("출결 기록을 중지합니다.");
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
				}
				System.out.println("이전 페이지로 돌아가겠습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
				
			} else {
				System.out.println("sql error");
			}
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//CheckAttendAdmin

	private void ShowAttendAdmin() {
		//출결 조회
		
		admAtList.clear();
		
		System.out.println("====================================================");
		MyCalendar();
		System.out.println();
		System.out.println("====================================================");
		System.out.println("조회할 기간 입력");
		System.out.print("시작 날짜(yyyy-mm-dd) : ");
		String startDate = scan.nextLine();
		System.out.print("종료 날짜(yyyy-mm-dd) : ");
		String endDate = scan.nextLine();
		
		try {
			
			conn = util.open();
			stat = conn.createStatement();
			String sql = String.format("select * from vw_adminAttend "
					+ "where days >= '%s' and days <= '%s' order by days, seq", startDate, endDate);
			rs = stat.executeQuery(sql);
			
			while(rs.next()) {
				admAtList.add(String.format("%s,%s,%s,%s,%s"
						, rs.getString("seq") + "/" +rs.getString("id")
						, rs.getString("days")
						, rs.getString("intime")
						, rs.getString("outtime")
						, rs.getString("status")));
			}
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("[관리자번호/ID]\t  [날짜]\t[출근]\t[퇴근]\t[상태]");
		Pagingfile.page(Pagingfile.save(admAtList));
		
		System.out.println("====================================================");
		System.out.println("이전 페이지로 돌아가겠습니다.");
		System.out.println("계속하시려면 엔터를 눌러주세요.");
		scan.nextLine();
	}//ShowAttendAdmin

	private void MyCalendar() {
		
		Calendar now = Calendar.getInstance();
		
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH) + 1;    
    int START_DAY_OF_WEEK = 0;
    int END_DAY = 0;
    
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    
    start.set(year, month - 1, 1);
    end.set(year, month, 1);
    end.add(Calendar.DATE, -1);
    
    START_DAY_OF_WEEK = start.get(Calendar.DAY_OF_WEEK);
    END_DAY = end.get(Calendar.DATE);
    
    System.out.println(year+"년 "+month+"월\n"
        + "일\t월\t화\t수\t목\t금\t토");
    
    for(int q = 1 ; q < START_DAY_OF_WEEK ; q++) {
      System.out.print("\t");
    }
    
    int cnt = START_DAY_OF_WEEK - 1;
    for(int q = 1 ; q <= END_DAY ; q++) {
      System.out.print(q+"\t");
      cnt ++;
      if(cnt == 7) {
        cnt = 0;
        System.out.println("\n");
      }
    }
	}
	
}