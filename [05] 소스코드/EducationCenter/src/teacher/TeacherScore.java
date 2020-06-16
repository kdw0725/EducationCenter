package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

public class TeacherScore {

	Scanner scan = new Scanner(System.in);

	int Tnum = 1;

	public void teacher_scorecheck(int Tnum) { // 4. 성적 관리
		try {

			boolean loop = true;

			while (loop) {

				System.out.println("==================================================");
				System.out.println("성적관리 페이지에 들어오셨습니다.");
				System.out.println("1. 성적 입력");
				System.out.println("2. 성적 확인/출력");
				System.out.println("0. 뒤로가기");
				System.out.println("==================================================");
				System.out.print("번호 : ");

				String num = scan.nextLine();

				if (num.equals("1")) { // 성적 입력

					teacherScoreIn(Tnum);

				} else if (num.equals("2")) { // 성적/확인 출력

					teacher_socreout(Tnum);

				} else if (num.equals("0")) { // 뒤로가기

					break;

				} else { // 그 외 번호 입력 시

					System.out.println("잘못된 번호입니다.");
					continue;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void teacher_socreout(int Tnum) { // 4-2 페이지

		try {

			boolean loop = true;

			while (loop) {

				System.out.println("==================================================");
				System.out.println("시험점수 확인/출력 페이지입니다.");
				System.out.println("1. 학생별 점수 확인");
				System.out.println("2. 과목별 성적확인");
				System.out.println("3. 전체 교육생 성적 등록 여부확인");
				System.out.println("0. 뒤로가기");
				System.out.println("==================================================");
				System.out.print("번호 : ");

				int num = scan.nextInt();
				scan.skip("\r\n");
				Teacher teacher = new Teacher();
				if (num == 1) { // 학생별 점수 확인

					teacherScoreOut(Tnum);

				} else if (num == 2) { // 과목별 성적 확인

					t015();
				} else if (num == 3) { // 전체 교육생 성적 등록 여부 확인

					teacher.t016();
				} else if (num == 0) { // 뒤로가기

					break;

				} else { // 그 외 번호 입력 시

					System.out.println("잘못된 번호입니다.");
					continue;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void t015() {
		/*
		 * 15-1. 과목 목록 출력시 과목번호, 과정명, 과정기간(시작 년월일, 끝 년월일), 강의실, 과목명, 과목기간(시작 년월일, 끝
		 * 년월일), 교재명, 출결, 필기, 실기 배점, 성적 등록 여부 등이 출력
		 * 
		 * 15-2. 특정 과목을 과목번호로 선택시 교육생 정보(이름, 전화번호, 수료 또는 중도탈락) 및 성적이 출결, 필기, 실기 점수로
		 * 구분되어서 출력되어야 한다.
		 */

		// 과목별 성적확인 4-2-2

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("====================================================================================================================");
			System.out.println("과목 목록입니다. 선택하실 과목번호를 입력하세요.");
			System.out.println();

			System.out.println("[과목번호]\t\t[과정명]\t\t[과정기간]\t\t[강의실]\t\t[과목명]\t[과목기간]\t\t[교재명]"
					+ "\t\t\t\t[출결배점]\t[필기배점]\t[실기배점]\t[성적등록여부]\n");

			String sql = "select distinct num,CouName,Course,Clr,SubName,SubPer,BookName,AttSco,"
					+ "WriSco,PerforSco,ScoReg from vw_SeeSubjectStu  order by num";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			List<String> list = new ArrayList<String>();
			Pagingfile file = new Pagingfile();

			while (rs.next()) {
				list.add(String.format("%s.,%s,%s,%2s,%25s,%10s,%s,%s,%2s,%2s,%2s", rs.getString("num"),
						rs.getString("CouName"), rs.getString("Course"), rs.getString("Clr"), rs.getString("SubName"),
						rs.getString("SubPer"), rs.getString("BookName"), rs.getString("AttSco"),
						rs.getString("WriSco"), rs.getString("PerforSco"), rs.getString("ScoReg")));

			}
			file.pageNonum(file.save(list));

			stat1.close();

			sql = "{ call proc_SubStuSco(?,?) }";

			stat = conn.prepareCall(sql);
			System.out.println("==========================================================================================================================================================================");
			System.out.print("과목번호 입력:");
			String Subnum = scan.nextLine();

			stat.setString(1, Subnum); // 과목번호

			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println("==========================================================================================================================================");
			System.out.println("학생 정보");
			System.out.println("===========================================================================================================================================");
			System.out.println("[번호]\t[과목명]\t[과목기간]\t[학생번호]\t[학생이름]\t[학생전화번호]\t\t[학생상태]\t" + "[필기점수]\t[실기점수]\t[출결점수]\n");

			List<String> list2 = new ArrayList<String>();
			Pagingfile file2 = new Pagingfile();

			while (rs.next()) {

				list2.add(String.format("%s,%2s,%10s,%2s,%12s,%20s,%20s,%10s,%10s,%10s", rs.getString("num"), rs.getString("SubName"),
						rs.getString("SubTerm"), rs.getString("StuNum"), rs.getString("StudentName"),
						rs.getString("StudentTel"), rs.getString("subAndstu"), rs.getString("WriteSco"),
						rs.getString("PerforSco"), rs.getString("AttSco")));

				// 페이징하기

			}
			file2.pageNonum(file2.save(list2));

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("정보를 불러오지 못했습니다.");
		}

	}// t015

	private static void teacherScoreOut(int Tnum) {

		// 학생별 점수 확인 4-2-1
		// 특정 인원(학생)의 성적 출력

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();

			System.out.println("==============================================================================================================================");
			System.out.println("강의한 과정 목록입니다.과정번호를 입력하세요. ");
			System.out.println();
			System.out.println("[번호]\t[교사이름]\t[과정번호]\t[과정명]\t\t\t\t\t\t[과정기간]\t[과정기간]\n");

			String sql = "select distinct num,TeacherName,CourseNum,CourseName,"
					+ "CoursePer,CourseDate from vw_TeaCourse where TeacherNum =" + Tnum + "order by CourseNum";
			// 1번 교사가 강의한 과정목록 출력

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t%-10s\t%-10s\t%-35s\t%-10s\t%-10s\n", rs.getString("num"),
						rs.getString("TeacherName"), rs.getString("CourseNum"), rs.getString("CourseName"),
						rs.getString("CoursePer"), rs.getString("CourseDate"));

			}
			System.out.println("==============================================================================================================================");
			stat1.close();
			rs.close();

			System.out.println();
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();
			System.out.println();
			System.out.println("==================================================================");
			System.out.println("과목 목록입니다. 선택하실 과목번호를 입력하세요.");
			System.out.println();
			System.out.println("[과목번호]\t[과목기간]\t\t\t[과목명]\n");
			sql = "select distinct OSubNum,SubPer,SubName from vw_SeeSubjectStu where TeaNum=" + Tnum
					+ " and CourseNum =" + courseNum + " order by OSubNum";
			// 1번 교사가 강의한 courseNum 번의 강의에 개설된 과목을 듣는 학생 출력하기

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t\t%s\t\t%s\n", rs.getString("OSubNum"), rs.getString("SubPer"),
						rs.getString("SubName"));

			}
			stat1.close();

			System.out.println("==================================================================");
			System.out.print("과목번호 선택:");
			String Subnum = scan.nextLine();

			System.out.println("==========================================================");
			System.out.println("학생 수강신청번호 목록입니다. 선택하실 번호를 입력하세요.");
			System.out.println();
			System.out.println("[학생이름]\t[학생번호]\n");

			stat1 = conn.createStatement();

			sql = "select StuNum,StudentName from vw_ScoStudent where TeaNum=" + Tnum + " and OSubNum = " + Subnum
					+ " order by OSubNum";

			rs = stat1.executeQuery(sql);

			while (rs.next()) {

				System.out.printf("%s.\t%13s\n", rs.getString("StuNum"), rs.getString("StudentName"));
			}

			stat1.close();

			System.out.println("==========================================================");
			System.out.print("학생의 수강신청번호 선택:");
			String Stunum = scan.nextLine();

			sql = "{ call proc_Score_Out(?,?,?) }";

			stat = conn.prepareCall(sql);

			stat.setString(1, Subnum); // 과목번호
			stat.setString(2, Stunum); // 학생번호

			stat.registerOutParameter(3, OracleTypes.CURSOR);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(3);

			System.out.println();
			System.out.println("======================");
			System.out.println("학생 성적 입력 확인");
			System.out.println("======================");
			System.out.println("[번호]\t[학생이름]\t[과목명]\t[필기점수]\t[실기점수]\t[출결점수]\n");

			while (rs.next()) {

				System.out.printf("%s\t%1s\t%14s\t%13s\t%13s\t%13s\n", rs.getString("num"), rs.getString("StuName"),
						rs.getString("SubName"), rs.getString("WritrSco"), rs.getString("PerforSco"),
						rs.getString("AttSco"));

			}

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("성적을 출력하지 못했습니다. 다시 시도해주세요.");
		}

	}// teacherScoreOut
	
	

	private static void teacherScoreIn(int Tnum) {
		/*
		 * 교사는 자신이 강의를 마친 과목의 목록 중에서 특정 과목을 선택하면, 교육생 정보가 출력되고, 특정 교육생 정보를 선택하면, 해당 교육생의
		 * 시험 점수를 입력할 수 있어야 한다. 이때, 출결, 필기, 실기 점수를 구분해서 입력할 수 있어야 한다.
		 */
		// 성적입력 4-1

		// 1번->5번->90번 ->점수입력 꼭 이렇게 누르기!!

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		CallableStatement stat3 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("==============================================================================================================================");
			System.out.println("강의한 과정 목록입니다.과정번호를 입력하세요. ");
			System.out.println("[번호]\t[교사이름]\t[과정번호]\t[과정명]\t\t\t\t\t\t[과정기간]\t[과정기간]\n");

			String sql = "select distinct num,TeacherName,CourseNum,CourseName,"
					+ "CoursePer,CourseDate from vw_TeaCourse where TeacherNum =" + Tnum + "order by CourseNum";
			// 1번 교사가 강의한 과정목록 출력

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t%-10s\t%-10s\t%-35s\t%-10s\t%-10s\n", rs.getString("num"),
						rs.getString("TeacherName"), rs.getString("CourseNum"), rs.getString("CourseName"),
						rs.getString("CoursePer"), rs.getString("CourseDate"));

			}

			stat1.close();
			rs.close();

			System.out.println("==============================================================================================================================");
			
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();
			System.out.println("========================================================");
			System.out.println("과목 목록입니다. 선택하실 과목번호를 입력하세요.");
			System.out.println("[과목번호]\t[과목기간]\t\t\t[과목명]\n");
			sql = "select distinct OSubNum,SubPer,SubName from vw_SeeSubjectStu where TeaNum=" + Tnum
					+ " and CourseNum =" + courseNum + " order by OSubNum";
			// 1번 교사가 강의한 courseNum 번의 강의에 개설된 과목을 듣는 학생 출력하기

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t\t%s\t\t%s\n", rs.getString("OSubNum"), rs.getString("SubPer"),
						rs.getString("SubName"));

			}
			stat1.close();
			System.out.println("========================================================");
			System.out.print("과목번호 선택:");
			String Subnum = scan.nextLine();
			
			System.out.println("==========================================================");

			System.out.println("학생 수강신청번호 목록입니다. 선택하실 번호를 입력하세요.");
			System.out.println("[학생이름]\t[학생번호]\n");

			stat1 = conn.createStatement();

			sql = "select StuNum,StudentName from vw_ScoStudent where TeaNum=" + Tnum + " and OSubNum = " + Subnum
					+ " order by OSubNum";

			rs = stat1.executeQuery(sql);

			while (rs.next()) {

				System.out.printf("%s.\t%13s\n", rs.getString("StuNum"), rs.getString("StudentName"));
			}

			stat1.close();
			System.out.println("==========================================================");
			System.out.print("학생의 수강신청번호 선택:");
			String Stunum = scan.nextLine();
			System.out.println("======================");
			System.out.print("필기성적 점수 입력:");
			String Writnum = scan.nextLine();
			
			System.out.print("실기성적 점수 입력:");
			String Perfornum = scan.nextLine();
			System.out.println("======================");
			
			sql = "{ call proc_FailStuSco(?,?,?,?,?) }";

			stat = conn.prepareCall(sql);

			stat.setString(1, Subnum); // 개설과목 번호
			stat.setString(2, Stunum); // 학생 번호
			stat.setString(3, Writnum); // 과목번호
			stat.setString(4, Perfornum); // 학생번호
			stat.registerOutParameter(5, OracleTypes.NUMBER);

			stat.executeQuery();

			rs = stat.executeQuery(sql);

			if (stat.getInt(5) == 0) {

				System.out.println("성적 입력불가.중도포기한 수강생입니다.");

			}

			System.out.println();

			rs.close();
			stat.close();

			sql = "{ call PROC_STUDENT_ATTEND_AUTO(?,?) }";

			stat3 = conn.prepareCall(sql);
			// System.out.println(Stunum);
			// System.out.println(Subnum);
			stat3.setString(1, Stunum);
			stat3.setString(2, Subnum);

			// ===============

			// stat2.executeQuery(sql);

			stat3.executeUpdate();

			System.out.println("완료");

			rs.close();
			stat3.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// t019

}// class
