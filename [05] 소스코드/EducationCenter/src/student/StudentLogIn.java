package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


/**
 * 
 * @author 장정우, 김동욱
 * 학생 로그인 관련 클래스
 */
public class StudentLogIn {
	
	/**
	 * 로그인 폼 및 결과 반환 메소드
	 * @return studentBasic
	 */
	public StudentBasic logIn() {
		
		while (true) {
			
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			Scanner scan = new Scanner(System.in);
			
			
			System.out.print("아이디 : ");
			String id = scan.nextLine();
			
			System.out.print("비밀번호 : ");
			String pw = scan.nextLine();
			
			try {
				// 교육생 로그인 프로시저 호출 -> 교육생 계정정보를 반환하는 sql문 호출
//				String sql = "{ call PROC_STUDENT_LOGIN(?, ?, ?, ?) }";
				String sql = "SELECT SEQ, NAME, ID, SSN, TEL, ACCOUNT, REGDATE, DELFLAG FROM TBL_STUDENT WHERE ID = ? AND SUBSTR(SSN, 8, 7) = ?";
				
						
				conn = util.open();
				stat = conn.prepareStatement(sql);
				
				stat.setString(1, id);
				stat.setString(2, pw);
				
				rs = stat.executeQuery();
				
				if(rs.next()) {
					StudentBasic student = new StudentBasic(
							rs.getInt("SEQ"), 
							rs.getString("NAME"), 
							rs.getString("ID"), 
							rs.getString("SSN"), 
							rs.getString("TEL"), 
							rs.getString("ACCOUNT"), 
							rs.getString("REGDATE"), 
							rs.getString("DELFLAG"));
					return student;
				} else {
					System.out.println("아이디 및 비밀번호가 일치하지 않습니다.");
					System.out.println("뒤로 가시려면 0번을, 다시 정보를 입력하시려면 아무 키나 입력해주세요.");
					String input = scan.nextLine();
					if (!input.equals("0")) {
						stat.close();
						conn.close();
						//logIn();
						continue;
					}
				}
				
				
				
				//아이디, 비밀번호 받고 정확한지(1) 아닌지(0) 값을 받음
//				stat.setString(1, id);
//				stat.setString(2, pw);
//				stat.registerOutParameter(3, OracleTypes.NUMBER);
//				stat.registerOutParameter(4, OracleTypes.NUMBER);
				
//				stat.executeQuery();
				
//				if (stat.getInt(3) == 1) {
//					//정확하면(1) 로그인 성공
//					System.out.println("로그인 완료되었습니다.");
//					
//					
//					
//					Student_Main sm = new Student_Main();
//					sm.student_main(stat.getInt(4));
//					
//				} else {
//					//정확하지 않으면(0) 로그인 실패
//					System.out.println("로그인 실패");
//				}
				
				stat.close();
				conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		
		
		return null;
	}
	
	
	

}
