package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 
 * @author 장정우
 * 학생의 점수를 출력해주는 클래스
 */
public class StudentCheckScore {
	
	/**
	 * 학생이 수강하고 있는 모든 과목의 점수를 출력해주는 메소드
	 * @param 로그인된 학생 정보
	 */
	public void showScore(StudentBasic logInStudent) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//성적 확인 프로시저 호출
			String sql = "{ call PROC_STUDENT_SCORE_INFO(?, ?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, logInStudent.getSeq());
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			//성적 출력
			System.out.println("===================================================================================================과목별 성적===================================================================================================");
			System.out.println("[이름]\t[번호]\t[과목명]\t[과정기간]\t\t[교재명]\t\t\t\t\t[교사명]\t[출결배점]\t[필기배점]\t[실기배점]\t[출결점수]\t[필기점수]\t[실기점수]");
			
			while (rs.next()) {
				
				System.out.printf("%s\t%s\t%8s\t%s ~ %s\t%s\t%s\t\t   %s\t\t   %s\t\t   %s\t\t   %s\t\t   %s\t\t   %s\n"
									, rs.getString("name")
									, rs.getString("seq")
									, rs.getString("subject")
									, rs.getString("startdate")
									, rs.getString("enddate")
									, rs.getString("book")
									, rs.getString("teacher")
									, rs.getString("atpercent") == null ? " -" : rs.getString("atpercent")
									, rs.getString("wrpercent") == null ? " -" : rs.getString("wrpercent")
									, rs.getString("pepercent") == null ? " -" : rs.getString("pepercent")
									, rs.getString("atscore") == null ? " -" : rs.getString("atscore")
									, rs.getString("wrscore") == null ? " -" : rs.getString("wrscore")
									, rs.getString("pescore") == null ? " -" : rs.getString("pescore"));				
				
			}
			
			System.out.println("=================================================================================================================================================================================================================");
			
			rs.close();
			stat.close();
			conn.close();
			
			System.out.println("뒤로가려면 엔터를 입력하세요.");
			
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
