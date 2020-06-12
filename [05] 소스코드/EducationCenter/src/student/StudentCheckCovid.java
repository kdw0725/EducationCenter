package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 
 * @author 장정우
 * 학생의 온도를 출력해주는 클래스
 */
public class StudentCheckCovid {
	
	/**
	 * 학생의 체온을 출력해주는 메소드
	 * @param 로그인된 학생의 계정정보
	 */
	public void checkCovid(StudentBasic student) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//체온 조회 프로시저 호출
			String sql = "{ call PROC_FINDCOVIDSTU(?, ?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, student.getSeq());
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			System.out.println("======================체온 조회======================");
	        System.out.println("[이름]\t[날짜]\t\t[오전]\t[오후]");
			
			while (rs.next()) {
				
				printTemp(rs);
				
			}
			
			System.out.println("=====================================================");
			
			rs.close();
			stat.close();
			
			//재사용 불가 -> 다시 받아오기
			
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, student.getSeq());
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
				String num = scan.nextLine();
				
				if (num.equals("1")) { //기간 입력
					
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
						        
						        System.out.println("======================체온 조회======================");
						        System.out.println("[이름]\t[날짜]\t\t[오전]\t[오후]");
						        
						        while (rs.next()) {
						        	
						        	Date days = dateFormat.parse(rs.getString("days"));
						        	
						        	//입력받은 기간과 체온조회의 날짜를 비교
						        	if (days.getTime() > start_date.getTime() && days.getTime() < end_date.getTime()) {
						        		printTemp(rs);
						        		
						        		
						        	}
						        	
						        }
			
							  //위의 유효성검사 불일치 시
							} catch (java.text.ParseException e){
								System.out.println("잘못된 날짜 형식입니다.");
							}
						
						
							System.out.println("=====================================================");
						
						} catch (Exception e) {
							e.printStackTrace();
						}				
					
				} else if (num.equals("0")) { //뒤로가기
					
					break;
					
				} else { //그 외 번호 입력 시
					
					System.out.println("잘못된 번호입니다.");
					continue;
					
				}
				
			}
			
			rs.close();
			stat.close();
			conn.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

	private void printTemp(ResultSet rs) throws Exception{
		System.out.printf("%s\t%s\t%s\t%s\n"
				, rs.getString("name")
				, rs.getString("days").substring(0, 10).trim()
				, rs.getString("amtemp") == null ? "  -" : rs.getString("amtemp") 
				, rs.getString("pmtemp") == null ? "  -" : rs.getString("pmtemp"));
	}

}
