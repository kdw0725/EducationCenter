package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

public class StudentScoreTeacher { 
	
	Connection conn = null;
	CallableStatement stat = null;
	ResultSet rs = null;
	DBUtil util = new DBUtil();
	Scanner scan = new Scanner(System.in);
	ArrayList<String> list = new ArrayList<String>();
	int result;
	
	public void student_scoreteacher(int stuseq) { // 2. 교사 평가 관리
		
		try {
			
			//수강 중인 과목 목록
			String sql = "{ call PROC_STUDENT_SCORE_TEACHER(?, ?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, stuseq);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			//수강중인 과목 전체 조회
			System.out.println("================================과목 목록================================");
			System.out.println("[교사명]\t[과정번호]\t  [과목명]\t  [과목시작]\t  [과목종료]\t[평점]");
			
			while (rs.next()) {
				
				//과목번호 배열에 담아놓기
				list.add(rs.getString("seq"));
				
				System.out.printf("%s\t%3s\t%8s\t%s\t%s\t%2s\n"
						, rs.getString("name")
						, rs.getString("seq")
						, rs.getString("subject")
						, rs.getString("startdate").substring(0, 10)
						, rs.getString("enddate").substring(0, 10)
						, rs.getString("score"));
				
			}
			
			System.out.println("=====================================================================");
			
			rs.close();
			stat.close();
			
//-----------------------------------------------점수 매기기-----------------------------------------------
			
			sql = "{ call PROC_STUDENT_GRADETEACHER(?, ?, ?, ?) }";
			
			stat = conn.prepareCall(sql);
			
			while (true) {
			
			System.out.print("번호를 입력하세요. : ");
			String num = scan.nextLine();
			
				if (list.contains(num)) { //배열에 담아놓은 과목번호가 맞으면
					
					System.out.print("점수를 입력하세요.(10점만점) : ");
					String score = scan.nextLine();
					
					if (Integer.parseInt(score) <= 10 && Integer.parseInt(score) >= 0) { //점수가 정확하면
						
						stat.setInt(1, stuseq);
						stat.setInt(2, Integer.parseInt(num));
						stat.setInt(3, Integer.parseInt(score));
						stat.registerOutParameter(4, OracleTypes.NUMBER);
						
						stat.executeQuery();
						
						if (stat.getInt(4) == 1) {
						
						System.out.println("점수가 입력 되었습니다.");
						
						} else { //아직 끝나지 않은 과목일 경우
							
							System.out.println("점수를 입력 할 수 없습니다.");
							System.out.println();
							continue;
							
						}
						
						stat.close();
						conn.close();
						
						//점수가 제대로 입력되면 탈출
						break;
						
					} else { //정확한 점수가 아닐 때
						
						System.out.println("올바른 점수가 아닙니다.");
						System.out.println();
						continue;
						
					}
					
				} else { //과목번호가 틀릴 때
					
					System.out.println("올바르지 않은 번호입니다.");
					System.out.println();
					
				}
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}


