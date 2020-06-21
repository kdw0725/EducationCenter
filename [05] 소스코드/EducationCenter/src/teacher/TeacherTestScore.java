package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author 백지현
 * 교사 성적 조회
 */
public class TeacherTestScore {

	/**
	 * 교사별 개설과정 확인 메소드
	 * @param teaNum 교사seq
	 */
	public void TeacherScoreCourseVw(String teaNum) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();

			String sql = "{call PROC_POINT_COURSE_VW(?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, teaNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			while (rs.next()) {

				list.add(String.format("%2s\t%s\t%s \t %s\n", rs.getString("ROWNUM"), rs.getString("CRSNAME"),
						rs.getString("CRSTERM"), rs.getString("CRSSTATUS")));

			}
			rs.close();

			Boolean loop = true;
			while (loop) {
				System.out.println("[번호] [과정명]\t\t\t\t\t\t\t[기간]\t\t\t[강의상황]");
				System.out.println(
						"====================================================================================================================================================");
				for (int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
				}
				System.out.println(
						"====================================================================================================================================================");

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
					TeacherScoreSubjectVw(teaNum, oCrsSeq);
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

	/**
	 * 교사별 개설과정별 과목출력 메소드
	 * @param teaNum 교사seq
	 * @param oCrsSeq 개설과정seq
	 */
	public void TeacherScoreSubjectVw(String teaNum, String oCrsSeq) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();
			String sql = "{call PROC_CRS_SUB_VW(?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, teaNum);
			stat.setString(2, oCrsSeq);
			stat.registerOutParameter(3, OracleTypes.CURSOR);

			stat.executeQuery();
			rs = (ResultSet) stat.getObject(3);

			while (rs.next()) {

				list.add(String.format("%2s\t%s\t%s\t%s\n", rs.getString("ROWNUM"), rs.getString("CRSNAME"),
						rs.getString("SUBSTARTDATE") + "-" + rs.getString("SUBENDDATE"), rs.getString("SUBNAME")));

			}

			rs.close();
			Boolean loop = true;
			while (loop) {
				System.out.println("[번호][과정명]\t\t\t\t\t\t[과목기간]\t\t\t[과목명]\t");
				System.out.println(
						"====================================================================================================================================================");
				for (int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
				}
				System.out.println(
						"====================================================================================================================================================");

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
							oSubSeq = rs.getString("OSUBSEQ");
							break;
						}
					}

					rs.close();
					System.out.println(oSubSeq);
					TeacherScoreSubjectStudent(teaNum, oSubSeq);
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

	/**
	 * 개설과목별 교사평가 점수 확인 메소드
	 * @param teaNum 교사seq
	 * @param oSubSeq 개설과목seq
	 */
	public void TeacherScoreSubjectStudent(String teaNum, String oSubSeq) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		DBUtil util = new DBUtil();
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();

			// 과목 선택 시 과목에 등록된 학생 정보 출력
			// 강의스케쥴 출력 프로시저 호출, 교사번호, 개설과정번호, 개설과목번호 목록출력
			String sql = "{call PROC_TEACHER_TEST_SUBJECT(?,?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, teaNum);
			stat.setString(2, oSubSeq);

			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(3);

			// 출력

			while (rs.next()) {

				list.add(String.format("%3s  %s \t %s \t \n", rs.getString("SUBNAME"), rs.getString("STUNAME"),
						rs.getString("TEASCORE")));

			}
			rs.close();

			System.out.println("[번호] [과목명][학생명][평가점수]");
			System.out.println("=============================================================");
			// 전체 갯수
			int totalCount = list.size();
			// 한페이지당 개수
			int countList = 10;
			// 토탈 페이지
			int totalPage = totalCount / countList;
			// 배열 개수 카운트
			int countNum = 1;
			// 한 페이지에 몇개 담았는지 카운트
			int number = 0;

			if (totalCount % countList > 0) {
				totalPage++;
			}

			for (int i = 0; i < totalPage; i++) {

				for (int j = 0; j < countList; j++) {

					System.out.println(countNum + "\t" + list.get(countNum));

					countNum++;
					number++;

					if (countNum == totalCount)
						break;

				}

				if (countNum % 10 == 1) {
					System.out.println("\t현재 페이지 : " + countNum / 10);
				} else {
					System.out.println("\t현재 페이지 : " + ((countNum / 10) + 1));
					System.out.println("마지막 페이지 입니다.");
				}
				System.out.println("1. 이전 페이지\t2. 다음 페이지");
				System.out.println("3. 종료");
				System.out.println("=============================================================");
				System.out.print("번호 : ");
				String num = scan.nextLine();
				System.out.println();

				// 이전 페이지가 처음 페이지일때
				if (num.equals("1") && i == 0) {
					System.out.println("이전 페이지가 없습니다.\n");
					System.out.println("[번호] [과목명][학생명][평가점수]");
					System.out.println("=============================================================");
					countNum = countNum - 10;
					number = 0;
					i--;

					// 마지막 페이지에서 이전 페이지로 갈 때
				} else if (num.equals("1") && countNum == totalCount) {
					System.out.println("[번호] [과목명][학생명][평가점수]");
					System.out.println("=============================================================");
					countNum = countNum - 20 + number;
					number = 0;
					i = i - 2;

					// 기본 이전 페이지
				} else if (num.equals("1")) {
					System.out.println("[번호] [과목명][학생명][평가점수]");
					System.out.println("=============================================================");
					countNum = countNum - 20;
					number = 0;
					i = i - 2;

					// 마지막 페이지에서 다음 페이지로 갈 때
				} else if (num.equals("2") && countNum == totalCount) {
					System.out.println("다음 페이지가 없습니다.\n");
					System.out.println("[번호] [과목명][학생명][평가점수]");
					System.out.println("=============================================================");
					countNum = countNum - number;
					number = 0;
					i--;

					// 기본 다음 페이지
				} else if (num.equals("2")) {
					System.out.println("[번호] [과목명][학생명][평가점수]");
					System.out.println("=============================================================");
					number = 0;
					continue;

					// 종료
				} else if (num.equals("3")) {

					break;

				} else { // 그 외 번호 입력 시
					System.out.println("없는 번호입니다.\n");
					System.out.println("[번호] [과목명][학생명][평가점수]");
					System.out.println("=============================================================");
					countNum = countNum - 10;
					number = 0;
					i--;

				}

			}

			countNum = 1;

			System.out.println("=========================================================");
			System.out.println();

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
