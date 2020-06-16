package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

public class TeacherAttend {

	public void teacherAttendMain(String teaNum) { // 3. 출결 관리
		Scanner scan = new Scanner(System.in);

//   Prompt p = new Prompt();  // 달력 보여주는 객체
//   p.runPrompt();      // 달력 출력

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

				if (num.equals("1")) { // 출결 조회
					seeTeacherAttend(teaNum);

				} else if (num.equals("2")) { // 출결 기록

					checkAttend(teaNum);

				} else if (num.equals("0")) { // 뒤로가기

					break;

				} else { // 그 외 번호 입력 시

					System.out.println("잘못된 번호입니다.");
					continue;

				}

			}

		} catch (Exception e) {
//     e.printStackTrace();
		}

	}

	private void checkAttend(String teaNum) {
		// 교사 출첵

		Connection conn = null;
		Statement stat = null;
		CallableStatement callStat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		try {
			conn = util.open();
			stat = conn.createStatement();
			String sql = String.format("select count(*) cnt from tbl_attend_teacher where teaseq = %s "
					+ "and to_char(days, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd')", teaNum);
			rs = stat.executeQuery(sql);
			int count = 0;

			if (rs.next()) {
				count = Integer.parseInt(rs.getString("cnt"));
			}
			stat.close();

			if (count == 0) {
				// 출근

				Calendar now = Calendar.getInstance();
				System.out.printf("현재 시간 : %tF %tT\r\n", now, now);
				System.out.print("> 출근 하시겠습니까?(y/n)");
				String status = scan.nextLine();

				if (status.toLowerCase().equals("y")) {

					sql = "{ call PROC_TEACHER_ATTEND_RECORD(?) }";

					callStat = conn.prepareCall(sql);

					callStat.setString(1, teaNum);

					callStat.executeUpdate();

					System.out.println("출근을 완료했습니다.");

					callStat.close();

				} else if (status.toLowerCase().equals("n")) {
					System.out.println("출결 기록을 중지합니다.");
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
				}
				System.out.println("이전 페이지로 돌아가겠습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();

			} else if (count > 0) {
				// 퇴근

				Calendar now = Calendar.getInstance();
				System.out.printf("현재 시간 : %tF %tT\r\n", now, now);
				System.out.print("> 퇴근 하시겠습니까?(y/n)");
				String status = scan.nextLine();

				if (status.toLowerCase().equals("y")) {

					sql = "{ call PROC_TEACHER_ATTEND_UPDATE(?) }";

					callStat = conn.prepareCall(sql);

					callStat.setString(1, teaNum);

					callStat.executeUpdate();

					System.out.println("퇴근을 완료했습니다.");

					callStat.close();

				} else if (status.toLowerCase().equals("n")) {
					System.out.println("출결 기록을 중지합니다.");
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
				}
				System.out.println("이전 페이지로 돌아가겠습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();

			} else {
				System.out.println("sql error");
			}
			conn.close();

		} catch (Exception e) {
//		e.printStackTrace();
			System.out.println("출결 기록에 실패하였습니다.");
		}

	}

	public void seeTeacherAttend(String teaNum) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<String>();

		try {

			String sql = "{ call PROC_TEACHER_ATTEND(?,?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setString(1, teaNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			rs = (ResultSet) stat.getObject(2);

			System.out.println("========================출결 조회========================");
			System.out.println("[번호]\t[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[근태]\t[상태]");

			while (rs.next()) {
				list.add(String.format("%3s\t%s\t%s\t%s\t%s\t%s", rs.getString("TEANAME"),
						rs.getString("DAYS").substring(0, 10), rs.getString("INTIME").substring(10),
						rs.getString("OUTTIME").substring(10), rs.getString("CONATT"), rs.getString("STATUS")));

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
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[근태]\t[상태]");
					countNum = countNum - 10;
					number = 0;
					i--;

					// 마지막 페이지에서 이전 페이지로 갈 때
				} else if (num.equals("1") && countNum == totalCount) {
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[근태]\t[상태]");
					countNum = countNum - 20 + number;
					number = 0;
					i = i - 2;

					// 기본 이전 페이지
				} else if (num.equals("1")) {
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[근태]\t[상태]");
					countNum = countNum - 20;
					number = 0;
					i = i - 2;

					// 마지막 페이지에서 다음 페이지로 갈 때
				} else if (num.equals("2") && countNum == totalCount) {
					System.out.println("다음 페이지가 없습니다.\n");
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[근태]\t[상태]");
					countNum = countNum - number;
					number = 0;
					i--;

					// 기본 다음 페이지
				} else if (num.equals("2")) {
					System.out.println("========================출결 조회========================");
					System.out.println("[이름]\t[날짜]\t\t[출근]\t[퇴근]\t[근태]\t[상태]");
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
//  	 e.printStackTrace();
			System.out.println("출결데이터를 불러오지 못했습니다.");
		}
	}
}