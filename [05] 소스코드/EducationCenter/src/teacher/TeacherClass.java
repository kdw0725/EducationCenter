package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.internal.OracleTypes;

public class TeacherClass {

	/**
	 * 과목에 등록된 학생 정보 출력 메소드
	 * 백지현
	 * @param teaNum 교사 seq
	 * @param oSubSeq 개설과목 seq
	 */
	public void SubjectStudentInfo(String teaNum, String oSubSeq) {

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
			String sql = "{call PROC_SUBJECT_STUDENTINFO(?,?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, teaNum);
			stat.setString(2, oSubSeq);

			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(3);

			// 출력
			System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
			System.out.println("=============================================================");
			while (rs.next()) {

				list.add(String.format("%s\t%s\t%s\t%s\n", rs.getString("STUNAME"), rs.getString("STUTEL"),
						rs.getString("STUREGDATE"), rs.getString("STUSTATUS")));

			}
			rs.close();
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

					System.out.println(countNum+"\t"+list.get(countNum));
					
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
					System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
					System.out.println("=============================================================");
					countNum = countNum - 10;
					number = 0;
					i--;

					// 마지막 페이지에서 이전 페이지로 갈 때
				} else if (num.equals("1") && countNum == totalCount) {
					System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
					System.out.println("=============================================================");
					countNum = countNum - 20 + number;
					number = 0;
					i = i - 2;

					// 기본 이전 페이지
				} else if (num.equals("1")) {
					System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
					System.out.println("=============================================================");
					countNum = countNum - 20;
					number = 0;
					i = i - 2;

					// 마지막 페이지에서 다음 페이지로 갈 때
				} else if (num.equals("2") && countNum == totalCount) {
					System.out.println("다음 페이지가 없습니다.\n");
					System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
					System.out.println("=============================================================");
					countNum = countNum - number;
					number = 0;
					i--;

					// 기본 다음 페이지
				} else if (num.equals("2")) {
					System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
					System.out.println("=============================================================");
					number = 0;
					continue;

					// 종료
				} else if (num.equals("3")) {

					break;

				} else { // 그 외 번호 입력 시
					System.out.println("없는 번호입니다.\n");
					System.out.println("[번호] [학생명]\t[전화번호]\t[등록일]\t\t[학생상태]");
					System.out.println("=============================================================");
					countNum = countNum - 10;
					number = 0;
					i--;

				}

			}

			countNum = 1;

			System.out.println("=========================================================");
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 강의 스케줄 과정별 과목출력 메소드
	 * 백지현
	 * @param teaNum 교사seq
	 * @param oCrsSeq 개설과정seq
	 */
	public void TeacherScheduleDetail(String teaNum, String oCrsSeq) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();
			// 강의스케쥴 조회 > 과정선택 후 해당 과정에 포함되는 과목 목록 출력
			// 강의스케쥴 출력 프로시저 호출, 교사번호, 개설과정번호, 목록출력
			String sql = "{(call PROC_TEACHER_SCHEDULE_DETAIL(?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, teaNum);
			stat.setString(2, oCrsSeq);
			stat.registerOutParameter(3, OracleTypes.CURSOR);

			stat.executeQuery();
			rs = (ResultSet) stat.getObject(3);

			while (rs.next()) {

				// -- CRSNAME(과정명)의 length가 길경우 ...으로 표시되도록 수정 필요
				list.add(String.format("%s\t%s\t%s\t%3s%10s\t%s\t%s\t%5s\n", rs.getString("ROWNUM"),
						rs.getString("CRSNAME"), rs.getString("CRSTERM"), rs.getString("ROOMNUM"),
						rs.getString("SUBNAME"), rs.getString("SUBTERM"), rs.getString("BOOKNAME"),
						rs.getString("CNTSTU")));

			}

			rs.close();
			Boolean loop = true;
			while (loop) {
				System.out.println("[번호]   [과정명]\t\t\t\t\t\t[과정기간]\t\t[강의실]\t[과목명][과목기간]\t\t\t[교재명]\t\t\t[수강등록인원]");
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

					// rownum과 입력받은 selectSubNum이 같을경우 개설과정번호를 가져옴
					while (rs.next()) {
						if (rs.getString("ROWNUM").equals(selectSubNum)) {
							oSubSeq = rs.getString("OSSEQ");
							break;
						}
					}

					rs.close();
					SubjectStudentInfo(teaNum, oSubSeq);
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
	 * 교사별 개설강의스케쥴 메소드
	 * 백지현 
	 * @param teaNum 교사seq
	 */
	public void TeacherClassSchedule(String teaNum) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		DBUtil util = new DBUtil();
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();

			// 강의스케쥴 출력 프로시저 호출, 교사번호, 목록출력
			String sql = "{call PROC_TEACHER_CLASS_SCHEDULE(?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, teaNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			while (rs.next()) {

				list.add(String.format("%3s\t%s\t%25s\t%25s\n", rs.getString("ROWNUM"), rs.getString("CRSNAME"),
						rs.getString("CRSTERM"), rs.getString("CRSSTATUS")));

			}
			rs.close();
			Boolean loop = true;
			while (loop) {
				System.out.println("[번호][과정명]\t\t\t\t[기간]\t\t[강의상황]");
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
					// rownum과 입력받은 selectCrsNum이 같을경우 개설과정번호를 가져옴
					while (rs.next()) {
						if (rs.getString("ROWNUM").equals(selectCrsNum)) {
							oCrsSeq = rs.getString("OCRSSEQ");
							break;
						}
					}
							
						rs.close();
						// 교사번호와 개설과정번호를 전달
						TeacherScheduleDetail(teaNum, oCrsSeq);

					
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

}
