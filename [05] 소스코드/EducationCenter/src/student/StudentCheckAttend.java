package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 학생의 출결을 담당하는 클래스
 * @author 장정우
 *
 */
public class StudentCheckAttend {
	
	private int count = 0;
	
	/**
	 * 학생의 출결을 보여주는 메뉴
	 * @param 로그인된 학생의 계정정보
	 */
	public void studentAttendMain(StudentBasic student) { // 3. 출결 관리
		Scanner scan = new Scanner(System.in);
		
		Prompt p = new Prompt();	// 달력 보여주는 객체
		p.runPrompt();				// 달력 출력
		
		try {
			
			boolean loop = true;
			
			while (loop) {
				
				System.out.println("===============");
				System.out.println("1. 출결 조회");
				System.out.println("2. 출결 기록");
				System.out.println("0. 뒤로가기");
				System.out.println("===============");
				System.out.print("번호 : ");
				
				String num = scan.nextLine();
				
				if (num.equals("1")) { //출결 조회
					System.out.println(student.getSeq());
					seeAttend(student.getSeq());
					
				} else if (num.equals("2")) { //출결 기록
					
					checkAttend(student.getSeq());
					
				} else if (num.equals("0")) { //뒤로가기
					
					break;
					
				} else { //그 외 번호 입력 시
					
					System.out.println("잘못된 번호입니다.");
					continue;
					
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

//-------------------------------------------2. 출결 기록----------------------------------------------
	
	
	private void checkAttend(int stuseq) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		try {
			boolean loop = true;
			
			while (loop) {
				
				System.out.println("=========================");
				System.out.println("1. 출근/퇴근");
				System.out.println("0. 뒤로가기");
				System.out.println("=========================");
				System.out.print("번호 : ");
				int num = scan.nextInt();
				scan.skip("\r\n");
				
				if (num == 1) { //출근
					
					if (count == 0) {
						
						String sql = "{ call PROC_STUDENT_ATTEND_IN(?) }";
						conn = util.open();
						stat = conn.prepareCall(sql);				
						
						stat.setInt(1, stuseq);
						
						stat.executeQuery();
						
						System.out.println("\n\n\t출↗근↘\n\n");
						
						stat.close();
						conn.close();
						
						count++;
						
					} else if (count == 1) {					// 이 부분 처리해야함 -> 하루에 한번만 초기화 되도
						
						String sql = "{ call PROC_STUDENT_ATTEND_OUT(?) }";
						conn = util.open();
						stat = conn.prepareCall(sql);
						
						stat.setInt(1, stuseq);
						
						stat.executeQuery();
						
						System.out.println("\n\n\t퇴↗근↘\n\n");
						
						stat.close();
						conn.close();				
						
						count = 0;
					}
					
				
				} else if (num == 0) { //뒤로가기
					
					break;
					
				} else { //그 외 번호 입력 시
					
					System.out.println("잘못된 번호입니다.");
					continue;
					
				}
				
				
			}
		} catch (Exception e) {
			System.out.println("StudentCheckAttend.checkAttend()");
			e.printStackTrace();
		}
		
		
	}

	
	
	
//-------------------------------------------1. 출결 조회----------------------------------------------

	
	
	private void seeAttend(int stuseq) throws SQLException { //출결 조회
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		try {
			//출결 프로시저 호출
			String sql = "{ call PROC_STUDENT_ATTEND(?, ?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, stuseq);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			
			//전체조회
			System.out.println("========================출결 조회========================");
			System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
			
			while (rs.next()) {
				printAttend(rs);
			}
			
			System.out.println("=========================================================");
			System.out.println();
			
			rs.close();
			stat.close();
			
			//재사용 불가 -> 다시 받아오기
			
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, stuseq);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			//기간 선택해서 조회
			boolean loop = true;
			
			while (loop) {
				
				System.out.println("=========================");
				System.out.println("1. 기간 선택");
				System.out.println("0. 뒤로가기");	
				System.out.println("=========================");
				System.out.print("번호 : ");
				int num = scan.nextInt();
				scan.skip("\r\n");
				
				if (num == 1) { //기간 입력
					
					System.out.print("시작기간을 입력하세요('yy-MM-dd') : ");
					String start = scan.nextLine().trim();
					
					System.out.print("종료기간을 입력하세요('yy-MM-dd') : ");
					String end = scan.nextLine().trim();
					
					try {
					
					//문자열로 입력받은 기간을 Date 타입으로 변환
					SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
					
					//유효성 검사
					dateFormat.setLenient (false);

						try {                                            
						        Date start_date = dateFormat.parse(start);
						        Date end_date = dateFormat.parse(end);
						        
						        System.out.println("========================출결 조회========================");
						        System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
						        
						        while (rs.next()) {
						        	
						        	Date days = dateFormat.parse(rs.getString("days"));
						        	
						        	//입력받은 기간과 출결기록의 날짜를 비교
						        	if (days.getTime() >= start_date.getTime() && days.getTime() <= end_date.getTime()) {
						        		
						        		printAttend(rs);
						        		
						        	}
						        	
						        }
		
						  //위의 유효성검사 불일치 시
						} catch (java.text.ParseException e){
							System.out.println("잘못된 날짜 형식입니다.");
						}
					
					
					System.out.println("=========================================================");
					
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if (num == 0) { //뒤로가기
					
					break;
					
				} else { //그 외 번호 입력 시
					
					System.out.println("잘못된 번호입니다.");
					continue;
					
				}
				
			}
		} catch (Exception e) {
			System.out.println("StudentCheckAttend.seeAttend()");
			e.printStackTrace();
		}
		
		
		rs.close();
		stat.close();
		conn.close();		
		
	}

	private void printAttend(ResultSet rs) throws Exception{
		System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\n"
				, rs.getString("name")
				, rs.getString("days")
				, rs.getString("intime") == null ? "  -" : rs.getString("intime") 
				, rs.getString("outtime") == null ? "  -" : rs.getString("outtime")
				, rs.getString("status") == null ? " -" : rs.getString("status")
				, rs.getString("note") == null ? " -" : rs.getString("note"));
	}
}
