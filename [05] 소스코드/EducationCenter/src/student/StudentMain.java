package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 
 * @author 장정우
 * 학생 메인화면 클래스
 */
public class StudentMain {

	/**
	 * 학생 정보를 가져와 보여주는 메소드
	 * @param 로그인한 학생 계정
	 */
	public void showInfo (StudentBasic logInStudent) throws Exception{
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
			
			//학생 메인화면 호출
			String sql = "{ call PROC_STUDENT_MAIN(?, ?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setInt(1, logInStudent.getSeq());
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			
			if (rs.next()) {
				printInfo(rs);
			} else {
				throw new NullPointerException();
			}
			
			System.out.println("==========================================================");
			
			rs.close();
			stat.close();
			conn.close();
			
			
			
	}

	/**
	 * 학생 기본 메뉴를 출력해주는 메소드
	 * @param 로그인한 학생 계정
	 */
	public void showMenu(StudentBasic logInStudent) {
		//----------------------------------------번호 입력---------------------------------------------
		Scanner scan = new Scanner(System.in);
		boolean loop = true;
		
		while (loop) {
			System.out.println("====== M E N U =========");
			System.out.println("1. 성적 조회");
			System.out.println("2. 교사 평가 관리");
			System.out.println("3. 출결 관리");
			System.out.println("4. 체온 조회");
			System.out.println("5. 취업 현황 조회");
			System.out.println("0. 뒤로가기");
			System.out.println("=========================");
			System.out.print("번호 : ");
			
			String num = scan.nextLine();
			
			if (num.equals("1")) { //성적 조회
				StudentCheckScore studentCheckScore = new StudentCheckScore(); 
				studentCheckScore.showScore(logInStudent);
			} else if (num.equals("2")) { //교사 평가 관리  
				StudentScoreTeacher studentScoreTeacher = new StudentScoreTeacher();
				studentScoreTeacher.student_scoreteacher(logInStudent.getSeq());
				
			} else if (num.equals("3")) { //출결 관리
				StudentCheckAttend studentCheckAttend = new StudentCheckAttend();
				studentCheckAttend.studentAttendMain(logInStudent);
				
			} else if (num.equals("4")) { //체온 조회
				StudentCheckCovid studentCheckCovid = new StudentCheckCovid();
				studentCheckCovid.checkCovid(logInStudent);
//				Student_CheckCovid scc = new Student_CheckCovid();
//				scc.student_checkcovid(stuseq);
				
			} else if (num.equals("5")) { //취업 현황 조회
				Employment employment = new Employment();
				employment.employment();
				
			} else if (num.equals("0")) { //뒤로 가기
				break;
				
			} else { //그 외 번호 입력 시
				
				System.out.println("잘못된 번호입니다.");
				continue;
				
			}
		
		}		
	}

	// 학생 기본 정보
	private void printInfo(ResultSet rs) throws Exception{
		//타이틀 출력
		System.out.println("========================교육생 정보========================");
		
		System.out.printf("이름 : %s\n"
						+ "아이디 : %s\n"
						+ "주민번호 : %s\n"
						+ "전화번호 : %s\n"
						+ "계좌번호 : %s\n"
						+ "수강중인 과정 : %s\n"
						+ "과정 기간 : %s ~ %s\n"
						+ "강의실 : %s\n"
						+ "교육 타입 : %s\n"
							, rs.getString("name")
							, rs.getString("id")
							, rs.getString("ssn")
							, rs.getString("tel")
							, rs.getString("account")
							, rs.getString("coursename")
							, rs.getString("startdate")
							, rs.getString("enddate")
							, rs.getString("room")
							, rs.getString("type"));		
	}

}
