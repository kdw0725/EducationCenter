package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 학생의 출결을 담당하는 클래스
 * @author 장정우
 *
 */
public class StudentCheckAttend {
	
	//페이징 배열
	List<Paging> list = new ArrayList<Paging>();
	
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
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		Calendar c = Calendar.getInstance();
		
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
				
				if (num == 1) { //출근 && 퇴근
					
					String sql = "{ call PROC_STUDENT_ATTEND_INOUT(?, ?) }";
					conn = util.open();
					conn.setAutoCommit(false);
					stat = conn.prepareCall(sql);
					
					stat.setInt(1, stuseq);
					stat.registerOutParameter(2, OracleTypes.NUMBER);
					
					stat.executeQuery();
					
					if (stat.getInt(2) == 0) {
						
						System.out.printf("\n현재 시간 : %tF %tT\r\n", c, c);
			            System.out.print("> 출근 하시겠습니까?(y/n) : ");
			            String status = scan.nextLine();
			            
			            if (status.toLowerCase().equals("y")) {
						
			            	System.out.println("출근을 완료했습니다.");
			            	conn.commit();
						
			            } else if (status.toLowerCase().equals("n")) {
			            	
			            	System.out.println("출결 기록을 중지합니다.");
			            	conn.rollback();
			            	continue;
			            	
			            }
					
					} else if (stat.getInt(2) == 1) {
						
						System.out.printf("\n현재 시간 : %tF %tT\r\n", c, c);
						System.out.print("> 퇴근 하시겠습니까?(y/n) : ");
			            String status = scan.nextLine();
			            
			            if (status.toLowerCase().equals("y")) {
						
			            	System.out.println("퇴근을 완료했습니다.");
			            	conn.commit();
						
			            } else if (status.toLowerCase().equals("n")) {
			            	
			            	System.out.println("출결 기록을 중지합니다.");
			            	conn.rollback();
			            	continue;
			            	
			            }
					
					}
					stat.close();
					conn.close();				
					
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
				
				//객체에 출결값 담기
				Paging p = new Paging();
				
				p.setName(rs.getString("name"));
				p.setDays(rs.getString("days"));
				p.setIntime(rs.getString("intime"));
				p.setOuttime(rs.getString("outtime"));
				p.setStatus(rs.getString("status"));
				p.setNote(rs.getString("note"));
				
				//객체 배열에 담기
				list.add(p);
				
			}
			
			//전체 개수
			int totalCount = list.size();
			//페이지 당 개수
			int countList = 10;
			//페이지 수 (나머지 처리는 밑에서)
			int totalPage = totalCount / countList;
			//배열 카운트 숫자
			int countNum = 0;
			//한 페이지에 몇개 담았는지 카운트
			int number = 0;
			
			//마지막 페이지에 10칸이 다 안차도 페이지 부여
			if (totalCount % countList > 0) {
				totalPage++;
			}
			
			
			//페이지 수 만큼
			for (int i=0; i<totalPage; i++) {
				
				//한 페이지에 10개씩
				for (int j=0; j<countList; j++) {
					
					System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\n"
							, list.get(countNum).getName()
							, list.get(countNum).getDays()
							, list.get(countNum).getIntime()
							, list.get(countNum).getOuttime()
							, list.get(countNum).getStatus()
							, list.get(countNum).getNote());
					
					//배열 카운트 증가
					countNum++;
					//한 페이지에 몇개 담았는지 카운트
					number++;
					
					//마지막에 전부 다 담았으면 break
					//break 안하면 index에러남
					if (countNum == totalCount) {
						break;
					}
					
				}
				
				System.out.println("=========================================================");
				
				System.out.println("1. 이전 페이지\t2. 다음 페이지");
				System.out.println("3. 종료");
				System.out.println("===============================");
				System.out.print("번호 : ");
				String num = scan.nextLine();
				System.out.println();
				
				//이전 페이지가 처음 페이지일때
				if (num.equals("1") && i == 0) {
					
					System.out.println("이전 페이지가 없습니다.\n");
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
					countNum = countNum - 10;
					number = 0;
					i--;
				
				//마지막 페이지에서 이전 페이지로 갈 때
				} else if (num.equals("1") && countNum == totalCount) {
					
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
					countNum = countNum - 10 - number;
					number = 0;
					i = i-2;
				
				//기본 이전 페이지
				} else if (num.equals("1")) {
					
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
					countNum = countNum - 20;
					number = 0;
					i = i-2;
				
				//마지막 페이지에서 다음 페이지로 갈 때	
				} else if (num.equals("2") && countNum == totalCount){
					
					System.out.println("다음 페이지가 없습니다.\n");
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
					countNum = countNum - number;
					number = 0;
					i--;
				
				//기본 다음 페이지
				} else if (num.equals("2")) {
					
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
					number = 0;
					continue;
				
				//종료
				} else if (num.equals("3")) {
					
					break;
					
				} else { //그 외 번호 입력 시
					
					System.out.println("없는 번호입니다.\n");
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");
					countNum = countNum - 10;
					number = 0;
					i--;
					
				}			
				
			}
			
			countNum = 1;
			
			System.out.println("=========================================================");
			System.out.println();
			
			list.clear();
			
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
