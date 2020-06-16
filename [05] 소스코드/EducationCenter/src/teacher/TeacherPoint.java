package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.internal.OracleTypes;

public class TeacherPoint {

	public void pointCourseVw(String teaNum) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();

			String sql = "{call PROC_POINT_COURSE_VW(?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, teaNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println("[번호] [과정명]\t\t\t\t[기간]\t\t[강의상황]");
			System.out.println(
					"====================================================================================================================================================");
			while (rs.next()) {

				System.out.printf("%3s  %s \t %s \t %s\n", rs.getString("ROWNUM"), rs.getString("CRSNAME"),
						rs.getString("CRSTERM"), rs.getString("CRSSTATUS"));

			}
			rs.close();

			System.out.println(
					"====================================================================================================================================================");
			Boolean loop = true;
			while (loop) {

				System.out.println("1. 과정선택");
				System.out.println("0. 뒤로가기");
				System.out.print("입력 : ");
				String num = scan.nextLine();

				if (num.equals("1")) {
					System.out.print("과정번호 입력 :");
					String selectCrsNum = scan.nextLine(); // 3
					String oCrsSeq = "";

					stat.executeQuery();
					rs = (ResultSet) stat.getObject(2);
					while (rs.next()) {
						if (rs.getString("ROWNUM").equals(selectCrsNum)) {
							oCrsSeq = rs.getString("OCRSSEQ");
							break;
						}
					}
					rs.close();
					System.out.println(oCrsSeq);
					pointSubjectVw(teaNum, oCrsSeq);
				} else if (num.equals("0")) {
					return;
				} else {
					System.out.println("올바른 번호를 입력해 주세요.");
					break;

				}
			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("TeacherPoint.pointCourseVw");
			e.printStackTrace();
		}
	}

	public void pointSubjectVw(String teaNum, String oCrsSeq) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			String sql = "{call PROC_POINT_SUBJECT_VW(?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, teaNum);
			stat.setString(2, oCrsSeq);
			stat.registerOutParameter(3, OracleTypes.CURSOR);

			stat.executeQuery();
			rs = (ResultSet) stat.getObject(3);

			System.out.println("[번호] [과정명]\t\t\t\t[과정기간]\t\t[강의실]\t[과목명]\t\t[과목기간]\t\t[교재명]\t\t[필기]\t[실기]\t[출결]");
			System.out.println(
					"====================================================================================================================================================");
			while (rs.next()) {

				//
				System.out.printf("%s  %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("ROWNUM"),
						rs.getString("CRSNAME"), rs.getString("CRSTERM"), rs.getString("ROOMNUM"),
						rs.getString("SUBNAME"), rs.getString("SUBTERM"), rs.getString("BOOKNAME"),
						rs.getString("WRITE"), rs.getString("PERFORMANCE"), rs.getString("ATTEND"));

			}

			rs.close();
			System.out.println(
					"====================================================================================================================================================");
			Boolean loop = true;
			while (loop) {

				System.out.println("1. 과목선택");
				System.out.println("0. 뒤로가기");
				System.out.print("입력 : ");
				String num = scan.nextLine();

				if (num.equals("1")) {
					System.out.print("과목 번호 입력 :");
					String selectSubNum = scan.nextLine(); // 3
					String oSubSeq = "";

					stat.executeQuery();
					rs = (ResultSet) stat.getObject(3);
					while (rs.next()) {
						if (rs.getString("ROWNUM").equals(selectSubNum)) {
							oSubSeq = rs.getString("OSSEQ");
							break;
						}
					}

					rs.close();
					System.out.println(oSubSeq);
					pointSubjectVwDetail(teaNum, oCrsSeq, oSubSeq);
				} else if (num.equals("0")) {
					return;
				} else {
					System.out.println("올바른 번호를 입력해 주세요.");
					break;

				}
			}
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pointSubjectVwDetail(String teaNum, String oCrsSeq, String oSubSeq) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open();
			String sql = "{call PROC_POINT_SUBJECT_VW_DETAIL(?,?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, teaNum);
			stat.setString(2, oCrsSeq);
			stat.setString(3, oSubSeq);
			stat.registerOutParameter(4, OracleTypes.CURSOR);

			stat.executeQuery();
			rs = (ResultSet) stat.getObject(4);

			System.out.println("[번호] [과정명]\t\t\t\t[과정기간]\t\t[강의실]\t[과목명]\t\t[과목기간]\t\t[교재명]\t\t[필기]\t[실기]\t[출결]");
			System.out.println(
					"====================================================================================================================================================");
			while (rs.next()) {

				//
				System.out.printf("%s  %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("ROWNUM"),
						rs.getString("CRSNAME"), rs.getString("CRSTERM"), rs.getString("ROOMNUM"),
						rs.getString("SUBNAME"), rs.getString("SUBTERM"), rs.getString("BOOKNAME"),
						rs.getString("WRITE"), rs.getString("PERFORMANCE"), rs.getString("ATTEND"));

			}

			rs.close();
			System.out.println(
					"====================================================================================================================================================");
			System.out.println("※출결 최소 20점, 필기+실기+출결은 100점이어야 합니다※");
			System.out.println();
			Boolean loop = true;
			while (loop) {
				
				System.out.println();
				System.out.print("필기 배점 입력 : ");
				String write = scan.nextLine();
				System.out.print("실기 배점 입력 : ");
				String performance = scan.nextLine();
				System.out.print("출결 배점 입력 : ");
				String attend = scan.nextLine();
				System.out.print("시험 날짜 입력 (ex 2020-06-31) : ");
				String testdate = scan.nextLine();
				System.out.print("시험 파일 유무 입력 (ex 있음,없음) : ");
				String testfile = scan.nextLine();

				int result = pointInsert(oSubSeq, write, performance, attend, testdate, testfile);

				if (result == 1) {
					System.out.println("입력 완료");
					break;
				} else {
					//뒤로가기가 필요해
					System.out.println("※출결 최소 20점, 필기+실기+출결은 100점이어야 합니다※");
				}

			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int pointInsert(String oSubSeq, String write, String performance, String attend, String testdate,
			String testfile) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		DBUtil util = new DBUtil();

		try {

			conn = util.open();

			// 배점 입력 과목번호,필기,실기,출결,시험날짜,시험파일유무 입력
			String sql = "{call PROC_INSERT_POINT_TEST(?,?,?,?,?,?,?)}";
			stat = conn.prepareCall(sql);

			// 
			stat.setString(1, oSubSeq);
			stat.setString(2, write);
			stat.setString(3, performance);
			stat.setString(4, attend);
			stat.setString(5, testdate);
			stat.setString(6, testfile);
			stat.registerOutParameter(7, OracleTypes.NUMBER);

			stat.executeQuery();

			// 결과가 1일경우 입력 성공, 0일경우 입력 실패 return으로 이전 메소드에 결과값 전달
			int result = 1;
			if (stat.getInt(7) == 1) {
				result = 1;

			} else {
				result = 0;
			}

			stat.close();
			conn.close();
			return result;

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

}
