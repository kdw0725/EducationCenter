package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

public class Support {
	
	 Scanner scan = new Scanner(System.in);


	public void supportmenu() {
		// 2-1-6. 국비지원
		System.out.println("국비지원 전체 보기입니다.");
		SeeSp();
		while(true) {
			System.out.println("=================================");

			System.out.println("국비지원 메뉴입니다.");
			System.out.println("=================================");
			System.out.println("1. 추가");
			System.out.println("2. 특정 학생 타입 보기");
			System.out.println("0. 뒤로가기");
			System.out.println("=================================");

			System.out.print("입력:");
			String answer = scan.nextLine();
			
			if(answer.equals("1")) {
				//추가하기
				addsupport();
			}else if(answer.equals("2")) {
				//특정 학생 타입 보기
				prseesp();
				
			}
			
			else if(answer.equals("0")) {
				break;
			} else {
				System.out.println("번호를 잘 못 입력하였습니다. 다시 입력해주세요.");
			}
			
			
			
			
		}
		
		
		
	}

	private void addsupport() {
		//국비지원 입력하기
		
		System.out.print("해당 학생의 번호를 눌러주세요. :");
		String appseq = scan.nextLine();
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		System.out.print("해당 학생의 타입을 눌러주세요. :");
		String type = scan.nextLine();
		if(type.equals("내일배움") || type.equals("취성패") ||type.equals("재직자")) {
			
			
		System.out.print("다음 지급일을 입력하세요. (yy-mm-01): ");
		String date = scan.nextLine();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-01");
		dateFormat.setLenient(false);
		
		
		try {
			Date date1 = dateFormat.parse(date);
			
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(date1);
			
			if(c2.getTimeInMillis() <= c1.getTimeInMillis()) {
			String date2 = String.format("%tF", c1);
			
			
			try {
				
				conn = util.open();
				
				String sql = "{ call proc_addsupport(?, ?, ?) }";
				
				stat = conn.prepareCall(sql);
				
				stat.setString(1, type);
				stat.setString(2, date2);
				stat.setString(3, appseq);
				
				
				
				
				
			} catch (Exception e) {
				System.out.println("입력에 실패하였습니다.");
			}
			} else {
	    	 System.out.println("날짜입력이 잘못되었습니다. 다시입력하여주십시오.");
	     }
			
			
			
		}catch (java.text.ParseException e){
      System.out.println("잘못된 날짜 입력입니다.");
     }
		} else if(type.equals("자비")) {
			//자비 입력
			String date = null;
try {
				
				conn = util.open();
				
				String sql = "{ call proc_addsupport(?, ?, ?) }";
				
				stat = conn.prepareCall(sql);
				
				stat.setString(1, type);
				stat.setString(2, date);
				stat.setString(3, appseq);
				
				
				
				
				
			} catch (Exception e) {
				System.out.println("입력에 실패하였습니다.");
			}
			
			
			
			
		} else {
			System.out.println("잘못된 타입 입력입니다.");
		}
		
		
		while(true) {
		
		System.out.println("1. 확정");
		System.out.println("2. 취소");
		
		System.out.print("입력 : ");
		String answer = scan.nextLine();
		
		
		if(answer.equals("1")) {
			

			try {
				stat.executeUpdate();
				stat.close();
				conn.close();
				System.out.println("입력 완료되었습니다.");
			} catch (SQLException e) {
				System.out.println("입력에 실패하였습니다.");			}
			
			
			
			
			break;
			
			
			
		} else if(answer.equals("2")) {
			
			System.out.println("취소하였습니다.");
			try {
				stat.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			break;
		} else {
			System.out.println("번호를 다시 입력하시오. ");
			
		}
		
	
		}
		
		
		
		
	}
//	private void updspport() {
//		// 국비지원 수정 프로시저
//		System.out.print("수정할 번호를 눌러주시오 : ");
//		String num = scan.nextLine();
//		
//		System.out.print("수정할 타입을 눌러주세요. :");
//		String type = scan.nextLine();
//		
//		System.out.print("수정할 지급일을 입력하세요. (yy-mm-dd): ");
//		String date = scan.nextLine();
//		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
//		dateFormat.setLenient(false);
//		
//		Connection conn = null;
//		CallableStatement stat = null;
//		DBUtil util = new DBUtil();
//		
//		
//		
//		
//		try {
//			Date date1 = dateFormat.parse(date);
//			
//			Calendar c1 = Calendar.getInstance();
//			c1.setTime(date1);
//			String date2 = String.format("%tF", c1);
//			
//			try {
//				
//				conn = util.open();
//				
//				String sql = "{ call proc_updspport(?, ?, ?) }";
//				
//				stat = conn.prepareCall(sql);
//				
//				stat.setString(1, num);
//				stat.setString(2, type);
//				stat.setString(3, date2);
//				
//				stat.executeUpdate();
//
//				
//				conn.close();
//				stat.close();
//				
//				
//				
//				
//				
//				System.out.println("수정완료되었습니다.");
//				
//				
//				
//			} catch (Exception e) {
//				System.out.println("수정에 실패하였습니다. ");
//			}
//			
//			
//			
//			
//			
//		 }catch (java.text.ParseException e){
//      System.out.println("잘못된 날짜 입력입니다.");
//     }
//		
//		
//		
//		
//	}

	private void prseesp() {
		// 특정 번호 지정 프로시저
		
		
		System.out.print("조회하고싶은 학생의 번호를 입력하여 주십시오. : ");
		String stunum = scan.nextLine();
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		
		try {
			conn = util.open();
			String sql = " { call proc_seesp(?, ?) } ";
			stat = conn.prepareCall(sql);
			
			
			

			
			stat.setString(1, stunum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			
			rs = (ResultSet)stat.getObject(2);
			
			
			
			
			System.out.println("[번호]\t[학생번호]\t[학생 이름]\t[주민번호]\t[계좌]\t[유형]\t[과정 시작일]\t[과정 종료일]\t[지급일]");
			
		if(rs.next()) {
			System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n"
					, rs.getString("supseq")
					, rs.getString("seq")
					, rs.getString("name")
					, rs.getString("ssn")
					, rs.getString("account")
					, rs.getString("type")
					, rs.getString("startdate").substring(0,10)
					, rs.getString("enddate").substring(0,10)
					, rs.getString("depdate") == null ? rs.getString("depdate") : rs.getString("depdate").substring(0,10)
);
			while(rs.next()) {
				
				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n"
						, rs.getString("supseq")
						, rs.getString("seq")
						, rs.getString("name")
						, rs.getString("ssn")
						, rs.getString("account")
						, rs.getString("type")
						, rs.getString("startdate").substring(0,10)
						, rs.getString("enddate").substring(0,10)
						, rs.getString("depdate") == null ? rs.getString("depdate") : rs.getString("depdate").substring(0,10)
);
				
				
			}//while
		} else {
			System.out.println("해당 학생의 번호가 존재하지 않습니다.");
		}
			rs.close();
			conn.close();
			stat.close();
				
				
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("학생의 번호가 존재하지않습니다.");
		}
		
		
		
		
		
		
		
		
		
		
		
	}//prseesp

	private void SeeSp() {
		//국비지원 보는 뷰
		Connection conn = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		
		ArrayList<String> list = new ArrayList<String>();		
		try {
			conn = util.open();

			String sql = "select * from vw_seesp";
			stat = conn.createStatement();

			System.out.println("[번호]\t[학생번호]\t[학생 이름]\t[주민번호]\t[계좌]\t[유형]\t[과정 시작일]\t[과정 종료일]\t[지급일]");
			rs = stat.executeQuery(sql);
			
			while(rs.next()) {
				
				 list.add(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s"
						, rs.getString("supseq")
						, rs.getString("seq")
						, rs.getString("name")
						, rs.getString("ssn")
						, rs.getString("account")
						, rs.getString("type")
						, rs.getString("startdate").substring(0,10)
						, rs.getString("enddate").substring(0,10)
						, rs.getString("depdate") == null ? rs.getString("depdate") : rs.getString("depdate").substring(0,10)));
				
				
			}//while
			
			
			
			Pagingfile.pageNonum(Pagingfile.save(list));
			rs.close();
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("오류가 발생했습니다.");
		}
		
		
		
		
		
		
		
		
		
	}
	
	
}//support
