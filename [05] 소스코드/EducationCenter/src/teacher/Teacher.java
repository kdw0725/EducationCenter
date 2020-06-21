package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 
 * @author 정희수
 * 교사 상태를 알려주는 클래스
 */
public class Teacher {
	
	/**
	 * 모든 출결 조회는 근태 상황을 구분할 수 있어야 한다.(정상, 지각, 조퇴, 외출, 병가, 기타) 
	 */
	public void t023() {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			
			System.out.println("=========================================================================================");
			System.out.println("출결 조회 근태 상황 ");
			System.out.println("[학생번호]\t[학생이름]\t[날짜]\t\t[입실시간]\t[퇴실시간]\t[학생상태]\t[공결]\n");

			String sql = "select distinct StuNum,StuName,Days,InTime,OutTime,StuCondition,StuStatus from vw_AllAtt order by StuNum";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				String result = String.format("%s.,%s,%s,%s,%s,%s,%s", rs.getString("StuNum"), rs.getString("StuName"),
						rs.getString("Days").substring(0, 10), rs.getString("InTime"), rs.getString("OutTime"),
						rs.getString("StuCondition"), rs.getString("StuStatus"));
				list.add(result);
			}
			System.out.println("========================================================================================");
			Pagingfile file = new Pagingfile();
			file.pageNonum(file.save(list));
			stat1.close();
			rs.close();
			conn.close();
			System.out.println("출력을 완료하였습니다. 계속하시려면 엔터를 입력해주세요.");
			scan.nextLine();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// t023

	/**
	 * 특정(특정 과정, 특정 인원) 출결 현황을 조회할 수 있어야 한다.
	 * @param 교사 번호
	 */
	public void t022(int Tnum) {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("=================================================================================================================================");
			System.out.println("강의한 과정 목록입니다.과정번호를 입력하세요. ");
			System.out.println("[번호]\t[교사이름]\t[과정번호]\t[과정명]\t\t\t\t\t\t[과정기간]\t[과정기간]\n");

			String sql = "select distinct TeacherNum,TeacherName,CourseNum,CourseName,"
					+ "CoursePer,CourseDate from vw_TeaCourse where TeacherNum =" + Tnum + "order by CourseNum";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t%-10s\t%-10s\t%-35s\t%-10s\t%-10s\n", rs.getString("TeacherNum"),
						rs.getString("TeacherName"), rs.getString("CourseNum"), rs.getString("CourseName"),
						rs.getString("CoursePer"), rs.getString("CourseDate"));

			}

			stat1.close();
			rs.close();
			System.out.println("=================================================================================================================================");
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();

			System.out.println("[과정번호]\t[학생번호]\t[학생이름]\t[날짜]\t\t[입실시간]\t[퇴실시간]\t[교사번호]\t[학생상태]\t[상태]\n");

			sql = "select distinct CourseNum,StuNum,StudentName,day,InTime,"
					+ "OutTime,Tecnum,StuCondition,Status from vw_CouStudent where TeacherNum =" + Tnum
					+ " and CourseNum = " + courseNum;

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(String.format("%s.,%15s,%15s,%5s,%10s,%10s,%10s,%10s,%10s", rs.getString("CourseNum"),
						rs.getString("StuNum"), rs.getString("StudentName"), rs.getString("day").substring(0, 10),
						rs.getString("InTime"), rs.getString("OutTime"), rs.getString("Tecnum"),
						rs.getString("StuCondition"), rs.getString("Status")));

			}
			
			Pagingfile file = new Pagingfile();
			
			file.pageNonum(file.save(list));
			
			System.out.println("================================================================================================");
			System.out.println("출결 조회할 학생의 번호를 입력하세요.");
			System.out.print("학생번호 입력:");
			String StuNum = scan.nextLine();

			System.out.println("[학생번호]\t[학생이름]\t[날짜]\t\t[입실시간]\t[퇴실시간]\t[학생상태]\t[상태]\n");

			sql = "select distinct StuNum,StudentName,day,InTime,"
					+ "OutTime,StuCondition,Status from vw_CouStudent where TeacherNum =" + Tnum + " and CourseNum = "
					+ courseNum + " and StuNum = " + StuNum + "order by day";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);
			
			
			
			while (rs.next()) {
				System.out.printf("%s.\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("StuNum"),
						rs.getString("StudentName"), rs.getString("day").substring(0, 10), rs.getString("InTime"),
						rs.getString("OutTime"), rs.getString("StuCondition"), rs.getString("Status"));
				
			}
			
			
			System.out.println("================================================================================================");
			rs.close();
			conn.close();
			// 페이징 하기
			System.out.println("출력을 완료하였습니다. 계속하시려면 엔터를 입력해주세요.");
			scan.nextLine();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// t022

	/**
	 * 출결 현황을 기간별(년, 월, 일) 조회할 수 있어야 한다.
	 * @param 교사 번호
	 * @throws Exception
	 */
	public void t021(int Tnum) throws Exception {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		conn = util.open();
		System.out.println("=================================================================================================================================");
		System.out.println("강의한 과정 목록입니다.과정번호를 입력하세요. ");
		System.out.println("[번호]\t[교사이름]\t[과정번호]\t[과정명]\t\t\t\t\t\t[과정기간]\t[과정기간]\n");

		String sql = "select distinct TeacherNum,TeacherName,CourseNum,CourseName,"
				+ "CoursePer,CourseDate from vw_TeaCourse where TeacherNum =" + Tnum + "order by CourseNum";
		// 1번 교사가 강의한 과정목록 출력

		stat1 = conn.createStatement();

		rs = stat1.executeQuery(sql);

		while (rs.next()) {
			System.out.printf("%s.\t%-10s\t%-10s\t%-35s\t%-10s\t%-10s\n",
//		list.add(String.format("%st%-10s%-10s\t%-35s\t%-10s\t%-10s\n",
					rs.getString("TeacherNum"), rs.getString("TeacherName"), rs.getString("CourseNum"),
					rs.getString("CourseName"), rs.getString("CoursePer"), rs.getString("CourseDate"));

		}

		stat1.close();
		rs.close();
		System.out.println("=================================================================================================================================");
		System.out.print("과정번호 선택:");
		String courseNum = scan.nextLine();

		// 프로시저 호출
		sql = "{ call PROC_TEACHER_STUATTEND(?, ?) }";

		conn = util.open();
		stat = conn.prepareCall(sql);

		stat.setString(1, courseNum);
		stat.registerOutParameter(2, OracleTypes.CURSOR);
		stat.executeQuery();
		rs = (ResultSet) stat.getObject(2);

		// 전체조회
		System.out.println("========================출결 조회========================");
		System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");

		List<String> list = new ArrayList<String>();
		while (rs.next()) {
			String result = String.format("%s,%s,%s,%s,%s,%s", rs.getString("name"), rs.getString("days"),
					rs.getString("intime"), rs.getString("outtime"), rs.getString("status"), rs.getString("note"));
			list.add(result);

		}
		Pagingfile file = new Pagingfile();
		file.pageNonum(file.save(list));

		System.out.println("=========================================================");
		System.out.println();

		rs.close();
		stat.close();

		// 재사용 불가 -> 다시 받아오기
		System.out.println("=======================");
		stat = conn.prepareCall(sql);

		stat.setString(1, courseNum);
		stat.registerOutParameter(2, OracleTypes.CURSOR);

		stat.executeQuery();

		rs = (ResultSet) stat.getObject(2);

		// 기간 선택해서 조회
		boolean loop = true;

		while (loop) {

			System.out.println("=========================");
			System.out.println("1. 기간 선택");
			System.out.println("0. 뒤로가기");
			System.out.println("=========================");
			System.out.print("번호 : ");
			int num = scan.nextInt();
			scan.skip("\r\n");

			if (num == 1) { // 기간 입력

				System.out.print("시작기간을 입력하세요('yy-MM-dd') : ");
				String start = scan.nextLine().trim();

				System.out.print("종료기간을 입력하세요('yy-MM-dd') : ");
				String end = scan.nextLine().trim();

				try {

					// 문자열로 입력받은 기간을 Date 타입으로 변환
					SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");

					// 유효성 검사
					dateFormat.setLenient(false);

					try {
						Date start_date = dateFormat.parse(start);
						Date end_date = dateFormat.parse(end);

						System.out.println("========================출결 조회========================");
						System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[상태]\t[기타]");

						while (rs.next()) {

							Date days = dateFormat.parse(rs.getString("days"));

							// 입력받은 기간과 출결기록의 날짜를 비교
							if (days.getTime() >= start_date.getTime() && days.getTime() <= end_date.getTime()) {

								System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("name"),
										rs.getString("days"), rs.getString("intime"), rs.getString("outtime"),
										rs.getString("status"), rs.getString("note"));

							}

						}

						// 위의 유효성검사 불일치 시
					} catch (java.text.ParseException e) {
						System.out.println("잘못된 날짜 형식입니다.");
					}

					System.out.println("=========================================================");

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (num == 0) { // 뒤로가기

				break;

			} else { // 그 외 번호 입력 시

				System.out.println("잘못된 번호입니다.");
				continue;

			}

		}

		rs.close();
		stat.close();
		conn.close();

	}// t021

	/**
	 * 교사가 강의한 과정에 한해 선택하는 경우 모든 교육생의 출결을 조회할 수 있어야 한다.
	 * @param 교사번호
	 */
	public void t020(int Tnum) {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("=================================================================================================================================");
			System.out.println("강의한 과정 목록입니다.과정번호를 입력하세요. ");
			System.out.println("[번호]\t[교사이름]\t[과정번호]\t[과정명]\t\t\t\t\t\t[과정기간]\t[과정기간]\n");

			String sql = "select distinct TeacherNum,TeacherName,CourseNum,CourseName,"
					+ "CoursePer,CourseDate from vw_TeaCourse where TeacherNum =" + Tnum + " order by CourseNum";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t%-10s\t%-10s\t%-35s\t%-10s\t%-10s\n",

						rs.getString("TeacherNum"), rs.getString("TeacherName"), rs.getString("CourseNum"),
						rs.getString("CourseName"), rs.getString("CoursePer"), rs.getString("CourseDate"));

			}

			stat1.close();
			rs.close();
			System.out.println("=================================================================================================================================");
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();

			sql = "select distinct CourseNum,StuNum,StudentName,day,InTime,"
					+ "OutTime,Tecnum,StuCondition,Status from vw_CouStudent where TeacherNum =" + Tnum
					+ " and CourseNum = " + courseNum;

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			List<String> list = new ArrayList<String>();
			System.out.println("=================================================================================================================================");
			System.out.println("[과정번호]\t[학생번호]\t[학생이름]\t[날짜]\t[입실시간]\t[퇴실시간]\t[교사번호]\t[학생상태]\t[근태]\n");

			while (rs.next()) {
//				System.out.printf("%s.\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n",
				list.add(String.format("%s.,%10s,%10s,%10s,%10s,%10s,%10s,%10s,%10s", rs.getString("CourseNum"),
						rs.getString("StuNum"), rs.getString("StudentName"), rs.getString("day").substring(0, 10),
						rs.getString("InTime"), rs.getString("OutTime"), rs.getString("Tecnum"),
						rs.getString("StuCondition"), rs.getString("Status")));

			}

			Pagingfile file = new Pagingfile();
			file.pageNonum(file.save(list));

			System.out.println();
			rs.close();
			conn.close();
			
			
			System.out.println("출력을 완료하였습니다. 계속하시려면 엔터를 입력해주세요.");
			scan.nextLine();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// t020

	/**
	 * 중도 탈락 처리된 교육생의 성적인 경우 중도탈락 이후 날짜의 성적은 입력하지 않는다.
	 * @param 교사 번호
	 */
	public void t019(int Tnum) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		CallableStatement stat3 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("===============================================================================");
			System.out.println("강의한 과정 목록입니다.과정번호를 입력하세요. ");
			System.out.println("[번호]\t[교사이름]\t[과정번호]\t[과정명]\t[과정기간]\t[과정시작일]\t[과정종료일]\n");

			String sql = "select distinct num,TeacherName,CourseNum,CourseName,"
					+ "CoursePer,CourseDate from vw_TeaCourse where TeacherNum =" + Tnum + "order by CourseNum";
			// 1번 교사가 강의한 과정목록 출력

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n", rs.getString("num"),
						rs.getString("TeacherName"), rs.getString("CourseNum"), rs.getString("CourseName"),
						rs.getString("CoursePer"), rs.getString("CourseDate"));

			}

			stat1.close();
			rs.close();
			System.out.println("===============================================================================");
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();

			System.out.println("과목 목록입니다. 선택하실 과목번호를 입력하세요.");
			System.out.println("[과목번호]\t[과목기간]\t[과목명]\n");
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
			System.out.println("===============================================================================");
			System.out.print("과목번호 선택:");
			String Subnum = scan.nextLine();

			System.out.println("학생 수강신청번호 목록입니다. 선택하실 번호를 입력하세요.");
			System.out.println("[학생이름]\t[학생번호]\n");

			stat1 = conn.createStatement();

			sql = "select StuNum,StudentName from vw_ScoStudent where TeaNum=" + Tnum + " and OSubNum = " + Subnum
					+ " order by OSubNum";

			rs = stat1.executeQuery(sql);

			while (rs.next()) {

				System.out.printf("%s\t%s\n", rs.getString("StuNum"), rs.getString("StudentName"));
			}

			stat1.close();
			System.out.println("===============================================================================");
			System.out.print("학생의 수강신청번호 선택:");
			String Stunum = scan.nextLine();

			System.out.print("필기성적 점수 입력:");
			String Writnum = scan.nextLine();

			System.out.print("실기성적 점수 입력:");
			String Perfornum = scan.nextLine();

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

	/**
	 * 중도 탈락인 경우 중도탈락 날짜가 출력되도록 한다.
	 */
	public void t018() {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("==========================================");
			System.out.println("중도탈락한 학생 목록과 중도탈락 일 ");
			System.out.println("[번호]\t[학생이름]\t\t[중도탈락일]\n");

			String sql = "select distinct num,StudentName,StudentFail from vw_FailStuDate order by num";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			while (rs.next()) {
				System.out.printf("%s.\t\t%s\t\t%s\n", rs.getString("num"), rs.getString("StudentName"),
						rs.getString("StudentFail").substring(0, 10));

			}

			stat1.close();
			rs.close();
			conn.close();
			System.out.println("==========================================");
			System.out.println("뒤로가기를 원하시면 엔터를 입력해주세요!");
			scan.nextLine();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// t018

	/*
	 * 과정을 중도 탈락해서 성적 처리가 제외된 교육생이더라도 교육생 명단에는 출력되어야 한다. 중도 탈락 여부를 확인할 수 있도록 해야 한다.
	 */
	public void t017() {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();

			System.out.println("교육생 전체의 중도탈락여부 확인.");
			System.out.println("[번호]\t[학생이름]\t[과정종료일]\t[학생상태]\n");

			String sql = "select distinct num,StudentName,CourEnd,StuCondition from vw_FailStudent order by num";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			List<String> list = new ArrayList<String>();

			while (rs.next()) {
//				System.out.printf("%s.\t\t%s\t\t%s\t\t%s\n",
				list.add(String.format("%s.,%5s,%12s,%7s", rs.getString("num"), rs.getString("StudentName"),
						rs.getString("CourEnd").substring(0, 10), rs.getString("StuCondition")));

			}

			Pagingfile file = new Pagingfile();

			file.pageNonum(file.save(list));

			stat1.close();
			rs.close();
			conn.close();
			System.out.println("뒤로가기를 원하시면 엔터를 입력해주세요!");
			scan.nextLine();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// t017

	/**
	 * 성적 등록 여부는 교육생 전체에 대해서 성적을 등록했는지의 여부를 출력한다.
	 */
	public void t016() {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("========================================================================================");
			System.out.println("교육생 전체의 성적등록여부입니다.");
			System.out.println("[학생번호]\t[학생이름]\t[과목명]\t[출결점수]\t[실기점수]\t[필기점수]\t[성적등록여부]\n");

			String sql = "select distinct StuNum,StudentName,SubName,AttSco,PerforSco,WriSco,ScoReg from vw_ScoStudent order by StuNum";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			List<String> list = new ArrayList<String>();

			while (rs.next()) {
				list.add(String.format("%s.,%10s,%10s,%10s,%10s,%10s,%10s",
//			System.out.printf("%s.\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n",
						rs.getString("StuNum"), rs.getString("StudentName"), rs.getString("SubName"),
						rs.getString("AttSco"), rs.getString("PerforSco"), rs.getString("WriSco"),
						rs.getString("ScoReg")));

			}
			Pagingfile file = new Pagingfile();

			file.pageNonum(file.save(list));

			stat1.close();
			rs.close();
			conn.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// t016

	/**
	 * 15-1. 과목 목록 출력시 과목번호, 과정명, 과정기간(시작 년월일, 끝 년월일), 강의실, 과목명, 과목기간(시작 년월일, 끝
	 * 년월일), 교재명, 출결, 필기, 실기 배점, 성적 등록 여부 등이 출력
	 * 
	 * 15-2. 특정 과목을 과목번호로 선택시 교육생 정보(이름, 전화번호, 수료 또는 중도탈락) 및 성적이 출결, 필기, 실기 점수로
	 * 구분되어서 출력되어야 한다.
	 */
	public void t015() { // 페이징하기


		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			System.out.println("====================================================================================================================");
			System.out.println("과목 목록입니다. 선택하실 과목번호를 입력하세요."); // 1번 누르기
			System.out.println();

			System.out.println("[과목번호]\t\t[과정명]\t\t[과정기간]\t\t[강의실]\t[과목명]\t[과목기간]\t\t\t[교재명]"
					+ "\t\t[출결배점]\t[필기배점]\t[실기배점]\t[성적등록여부]\n");

			String sql = "select distinct num,CouName,Course,Clr,SubName,SubPer,BookName,AttSco,"
					+ "WriSco,PerforSco,ScoReg from vw_SeeSubjectStu order by num";

			stat1 = conn.createStatement();

			rs = stat1.executeQuery(sql);

			List<String> list = new ArrayList<String>();

			while (rs.next()) {
//						System.out.printf("%s.\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
				list.add(String.format("%s.,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", rs.getString("num"),
						rs.getString("CouName"), rs.getString("Course"), rs.getString("Clr"), rs.getString("SubName"),
						rs.getString("SubPer"), rs.getString("BookName"), rs.getString("AttSco"),
						rs.getString("WriSco"), rs.getString("PerforSco"), rs.getString("ScoReg")));

			}
			Pagingfile file = new Pagingfile();
			file.pageNonum(file.save(list));

			stat1.close();

			sql = "{ call proc_SubStuSco(?,?) }";

			stat = conn.prepareCall(sql);
			System.out.println("====================================================================================================================");
			System.out.print("과목번호 입력:");
			String Subnum = scan.nextLine();

			stat.setString(1, Subnum); // 과목번호

			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println("=============================================");
			System.out.println("학생 정보");
			System.out.println("=============================================");
			System.out.println("[번호]\t[과목명]\t[과목기간]\t[학생번호]\t[학생이름]\t[학생전화번호]\t[학생상태]\t" + "[필기점수]\t[실기점수]\t[출결점수]\n");

			List<String> list2 = new ArrayList<String>();

			while (rs.next()) {

				list2.add(String.format("%s.,%2s,%8s,%8s,%8s,%8s,%8s,%8s,%8s,%8s",
//						System.out.printf("%s.\t%2s\t%8s\t%8s\t%8s\t%8s\t%8s\t%8s\t%8s\t%8s\n",
						rs.getString("num"), rs.getString("SubName"), rs.getString("SubTerm"), rs.getString("StuNum"),
						rs.getString("StudentName"), rs.getString("StudentTel"), rs.getString("subAndstu"),
						rs.getString("WriteSco"), rs.getString("PerforSco"), rs.getString("AttSco")));

			}

			rs.close();
			stat.close();
			conn.close();
			Pagingfile file2 = new Pagingfile();

			file2.pageNonum(file2.save(list2));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// t015

	/**
	 * 학생별 점수 확인
	 * @param 교사번호
	 */
	public void teacherScoreOut(int Tnum) {

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

			System.out.println("============================================================================");
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
			System.out.println("============================================================================");
			stat1.close();
			rs.close();

			System.out.println();
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();
			System.out.println();

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

			System.out.println("============================================================================");
			System.out.print("과목번호 선택:");
			String Subnum = scan.nextLine();

			System.out.println("============================================================================");
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

			System.out.println("============================================================================");
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
			System.out.println("=============================================");
			System.out.println("학생 성적 입력 확인");
			System.out.println("=============================================");
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

	/**
	 * 교사가 학생 점수 입력
	 * @param 교사번호
	 */
	public void teacherScoreIn(int Tnum) {

		Connection conn = null;
		Statement stat1 = null;
		CallableStatement stat2 = null;
		CallableStatement stat3 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {

			conn = util.open();

			Scanner scan = new Scanner(System.in);
			System.out.println("====================================================================================================================");
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

			System.out.println();
			System.out.print("과정번호 선택:");
			String courseNum = scan.nextLine();
			System.out.println();
			System.out.println("====================================================================================================================");
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
			System.out.println("===============================================================================");
			System.out.println();
			System.out.print("과목번호 선택:");
			String Subnum = scan.nextLine();
			System.out.println("===============================================================================");
			System.out.println("학생 수강신청번호 목록입니다. 선택하실 번호를 입력하세요.");
			System.out.println("[학생번호]\t[학생이름]\n");

			stat1 = conn.createStatement();

			sql = "select StuNum,StudentName from vw_ScoStudent where TeaNum=" + Tnum + " and OSubNum = " + Subnum
					+ " order by OSubNum";

			rs = stat1.executeQuery(sql);

			while (rs.next()) {

				System.out.printf("%s.\t%13s\n", rs.getString("StuNum"), rs.getString("StudentName"));
			}

			stat1.close();
			System.out.println("===============================================================================");
			System.out.print("학생의 수강신청번호 선택:");
			String Stunum = scan.nextLine();

			System.out.println();
			System.out.print("필기성적 점수 입력:");
			String Writnum = scan.nextLine();

			System.out.println();
			System.out.print("실기성적 점수 입력:");
			String Perfornum = scan.nextLine();

			sql = "{ call proc_Score_In(?,?,?,?) }";

			stat2 = conn.prepareCall(sql);

			stat2.setString(1, Stunum);

			stat2.setString(2, Subnum);

			stat2.setString(3, Writnum);

			stat2.setString(4, Perfornum);

			stat2.executeQuery();

			stat2.close();

			sql = "{ call PROC_STUDENT_ATTEND_AUTO(?,?) }";

			stat3 = conn.prepareCall(sql);
//		System.out.println(Stunum);
//		System.out.println(Subnum);
			stat3.setString(1, Stunum);
			stat3.setString(2, Subnum);

//		===============

//		stat2.executeQuery(sql);

			stat3.executeUpdate();

//		System.out.println("완료");
//		
//		
//		rs.close();
//		stat3.close();
//		conn.close();

			while (true) {
				System.out.println("1. 확정하기");
				System.out.println("0. 뒤로 가기");

				System.out.print("입력 : ");
				String answer = scan.nextLine();

				if (answer.equals("1")) {
					stat3.executeUpdate();
					System.out.println("완료");

					rs.close();
					stat3.close();
					conn.close();
					break;

				} else if (answer.equals("0")) {
					// 취소하기

					rs.close();
					stat3.close();
					conn.close();
					break;

				}

			} // while

		} catch (Exception e) {
//		e.printStackTrace();
			System.out.println("성적이 입력되지 않았습니다. 다시 시도해주세요.");
		}

	}// teacherScoreIn

}// class
