package apply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import course.OpenExpectedCourse;
import jdbc.DBUtil;
import student.StudentBasic;

/**
 * 수강신청을 담당하는 클래스
 * @author 김동욱
 *
 */
public class ApplyMain {
	
	
	public void showMain(StudentBasic studentBasic) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner sc = new Scanner(System.in);
		while(true) {
			try {
				// 개설과정 정보를 담고있는 ArrayList
				List<OpenExpectedCourse> list = new ArrayList<OpenExpectedCourse>();
				conn = util.open();
				String sql = "SELECT OCRSSEQ, CRSSEQ, PERIOD, COURSE, CRSDELFLAG, OCRSDELFLAG, CNT, MAXIMUM, STARTDATE, ENDDATE FROM VW_OPEN_EXPECTED_COURSE";
				
				stat = conn.prepareStatement(sql);
				
				rs = stat.executeQuery();
				
				System.out.println("============================================현재 오픈 예정인 과정============================================");
				System.out.println("[번호]\t[과정명]\t\t\t[신청 수]\t[최대 인원]\t[기간]");
				while(rs.next()) {
					
					OpenExpectedCourse openExpectedCourse = new OpenExpectedCourse(
							rs.getInt("CRSSEQ"), 
							rs.getString("COURSE"), 
							rs.getInt("PERIOD"), 
							rs.getString("CRSDELFLAG").equals("Y") ? true : false, 
							rs.getInt("OCRSSEQ"), 
							rs.getString("STARTDATE"), 
							rs.getString("ENDDATE"), 
							rs.getString("OCRSDELFLAG").equals("Y") ? true : false,
							rs.getInt("CNT"),
							rs.getInt("MAXIMUM")
							);
					list.add(openExpectedCourse);
							
				}
				
				for (int i = 0; i < list.size(); i++) {
					OpenExpectedCourse openExpectedCourse = list.get(i);
					
					System.out.printf("%s\t%-20s\t%s\t%s\t%s ~ %s\t\n",
							(i + 1),
							openExpectedCourse.getName().length() > 20 ? openExpectedCourse.getName().substring(0, 20)+"..." : openExpectedCourse.getName(),
							openExpectedCourse.getCnt(),
							openExpectedCourse.getMax(),
							openExpectedCourse.getStartdate().substring(0, 10),
							openExpectedCourse.getEnddate().substring(0, 10)
							);
				}
				System.out.println("======================================================================================================");
				stat.close();
				conn.close();
				
				System.out.print("자세히 볼 과정의 번호를 입력해주세요. 뒤로 가시려면 0번을 입력해주세요.");
				String courseNum = sc.nextLine();
				if(courseNum.matches("^[0-9]*$")) {
					if(courseNum.equals("0")) {
						break;
					} else {
						int cNum = Integer.parseInt(courseNum);
						if(cNum > list.size()) {
							System.out.println("올바른 번호를 입력해주세요!");
							System.out.println();
						} else {
							while(true) {
								courseDetail(list.get(cNum-1));
								System.out.println("수강신청을 원하시면 1번을, 뒤로가기를 원하시면 0번을 입력해주세요.");
								String applyNum = sc.nextLine();
								if(applyNum.matches("^[0-9]*$")) {
									if(applyNum.equals("0")) {
										break;
									} else if(applyNum.equals("1")) {
										studentApplyDo(list.get(cNum-1), studentBasic);
										System.out.println("계속 하시려면 엔터를 입력해주세요!");
										sc.nextLine();
										break;
									} else {
										System.out.println("올바른 숫자를 입력해주세요.");
									}
								}
							}
							
						}
					}
				} else {
					System.out.println("올바른 번호를 입력해주세요!");
				}
				 
				
			} catch (Exception e) {
				System.out.println("ApplyMain.showMain()");
				e.printStackTrace();
			}
		}
		
	}


	public int checkApply(StudentBasic studentBasic) {
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		
		try {
			conn = util.open();
			String sql = "SELECT COUNT(*) AS CNT FROM TBL_APPLY AP INNER JOIN TBL_OPEN_COURSE OCRS ON OCRS.SEQ = AP.OCRSSEQ  WHERE AP.STUSEQ = ? AND SYSDATE <= ENDDATE";
			
			stat = conn.prepareStatement(sql);
			stat.setInt(1, studentBasic.getSeq());
			
			rs = stat.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("CNT");
			}
			
			conn.close();
		} catch (Exception e) {
			System.out.println("ApplyMain.checkApply()");
			e.printStackTrace();
		}
		
		return -1;
	}


	// 수강신청 메소드
	private void studentApplyDo(OpenExpectedCourse openExpectedCourse, StudentBasic studentBasic) {
		
		Connection conn = null;
		PreparedStatement stat = null;
		DBUtil util = new DBUtil();
		
		try {
			conn = util.open();
			String sql = "INSERT INTO TBL_APPLY(SEQ, STATUS, STUSEQ, OCRSSEQ, DELFLAG) VALUES(APPLY_SEQ.NEXTVAL, DEFAULT, ?, ?, DEFAULT)";
			
			stat = conn.prepareStatement(sql);
			stat.setInt(1, studentBasic.getSeq());
			stat.setInt(2, openExpectedCourse.getSeq());
			
			stat.executeUpdate();
			
			stat.close();
			conn.close();
			System.out.printf("%s 과정에 수강신청이 완료되었습니다. 개설 예정 일은 %s입니다.\n", openExpectedCourse.getName(), openExpectedCourse.getStartdate().substring(0, 10));
		
		} catch (Exception e) {
			System.out.println("ApplyMain.studentApplyDo()");
			e.printStackTrace();
		}
	}


	// 과정의 상세 정보를 보여주는 메소드
	private void courseDetail(OpenExpectedCourse openExpectedCourse) {
		
		System.out.print("과정명           : ");
		System.out.println(openExpectedCourse.getName());
		System.out.print("과정 시작일     : ");
		System.out.println(openExpectedCourse.getStartdate().substring(0, 10));
		System.out.print("과정 종료일     : ");
		System.out.println(openExpectedCourse.getEnddate().substring(0, 10));
		System.out.print("현재 등록 인원 : ");
		System.out.println(openExpectedCourse.getCnt());
		System.out.print("수강 가능 인원 : ");
		System.out.println(openExpectedCourse.getMax());
		System.out.print("강의 과목       : ");
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		try {
			conn = util.open();
			String sql = "SELECT NAME FROM VW_SUBJECT_BY_COURSE WHERE OCRSSEQ = ?";
			stat = conn.prepareStatement(sql);
			stat.setInt(1, openExpectedCourse.getSeq());
			
			rs = stat.executeQuery();
			
			while(rs.next()) {
				System.out.print(rs.getString("NAME") + ", ");
			}
			stat.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("ApplyMain.courseDetail()");
			e.printStackTrace();
		}
		System.out.println();
		
		
		
		
	}
	
	
}
