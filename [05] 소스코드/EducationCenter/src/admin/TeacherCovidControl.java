package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

public class TeacherCovidControl {
	
	Connection conn = null;
	CallableStatement stat = null;
	Statement stat1 = null;
	ResultSet rs = null;
	DBUtil util = new DBUtil();
	Scanner scan = new Scanner(System.in);
	
	public void teachercovid() {

		String sql = "";

		try {

			conn = util.open();

			while (true) {

				sql = "select * from vw_seecovidtea";
				stat1 = conn.createStatement();

				rs = stat1.executeQuery(sql);
				System.out.println("==========================교사 체온==========================");
				System.out.println("[교사명]\t[교사번호]\t[날짜]\t\t[오전]\t[오후]");
				while (rs.next()) {

					System.out.printf("%s\t\t    %s\t\t%s\t%s\t%s\n", rs.getString("name"), rs.getString("teaseq"),
							rs.getString("days").substring(0, 10), rs.getString("amtemp"), rs.getString("pmtemp"));

				}

				rs.close();
				stat1.close();

				System.out.println("============================================================");
				System.out.println("1. 체온 기록");
				System.out.println("2. 체온 조회");
				System.out.println("0. 뒤로가기");
				System.out.println("========================");
				System.out.print("번호 : ");
				String num = scan.nextLine();
				System.out.println();

				if (num.equals("1")) {

					System.out.print("교사 번호 : ");
					String teaseq = scan.nextLine();

					System.out.print("오전 체온 : ");
					String am = scan.nextLine();

					System.out.print("오후 체온 : ");
					String pm = scan.nextLine();

					sql = "{ call PROC_ADDCOVTEA(?, ?, ?, ?) }";
					stat = conn.prepareCall(sql);
					stat.setString(1, am);
					stat.setString(2, pm);
					stat.setString(3, teaseq);
					stat.registerOutParameter(4, OracleTypes.NUMBER);

					stat.executeQuery();

					if (stat.getInt(4) == 1) {

						System.out.println("추가 완료");

					} else {

						System.out.println("추가 실패");
						continue;

					}

					stat.close();

				} else if (num.equals("2")) {

					// 조회하기
					System.out.println("=================================");

					System.out.println("교사 조회입니다.");
					System.out.println("=================================");

					while (true) {
						System.out.println("1. 기간 별 출력");
						System.out.println("2. 교사 별 출력");
						System.out.println("0. 뒤로가기");

						System.out.print("입력 : ");
						String answer = scan.nextLine();

						if (answer.equals("1")) {
							// 기간별 출력
							findcovidteadate();

						} else if (answer.equals("2")) {
							// 교사 별 출력
							findcovidtea();

						} else if (answer.equals("0")) {
							break;
						} else {
							System.out.println("번호를 다시 눌러주세요.");
						}

					}

				} else if (num.equals("0")) {

					break;

				} else {

					System.out.println("잘못된 번호입니다.");
					continue;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void findcovidtea() {

		// 특정 교사 번호 받아서 출력하기

		System.out.println("교사 명단입니다. ");
		System.out.println("[번호] [교사 이름] [연락처]\t[주민번호]");
		seetea();

		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;

		try {
			conn = util.open();

			System.out.print("조회하고싶은 교사의 번호를 입력하여 주십시오. : ");
			String stunum = scan.nextLine();

			String sql = " { call proc_findcovidtea(?, ?) } ";
			stat = conn.prepareCall(sql);

			stat.setString(1, stunum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			List<String> list = new ArrayList<String>();
			System.out.println("[번호]  [교사이름] [교사번호]\t[날짜]\t[오전온도]\t[오후온도]");
			while (rs.next()) {

				list.add(String.format("%s\t%s\t%s\t%s" + "ºC" + "\t%s" + "ºC", rs.getString("name"),
						rs.getString("teaseq"), rs.getString("days").substring(0, 10), rs.getString("amtemp"),
						rs.getString("pmtemp")));
			}

			Pagingfile.page(list);

			stat2.close();
			rs.close();
			conn.close();
			stat.close();

		} catch (Exception e) {
			System.out.println("교사의 번호가 존재하지않습니다.");
		}

	}

	private void seetea() {

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();
			String sql = "select * from tbl_teacher where delflag = 'Y' order by seq";
			stat = conn.createStatement();

			rs = stat.executeQuery(sql);

			while (rs.next()) {
				list.add(String.format("%3s\t%s\t%s\t%s", rs.getString("seq"), rs.getString("NAME"),
						rs.getString("TEL"), rs.getString("SSN")));

			}

			Pagingfile.pageNonum(list);
			rs.close();
			stat.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void findcovidteadate() {

		// 코로나 기간별 출력
		System.out.print("조회하고싶은 시작일을 입력하여 주십시오. (yy-mm-dd): ");
		String startdate = scan.nextLine();

		System.out.print("조회하고싶은 종료일을 입력하여 주십시오. (yy-mm-dd): ");
		String enddate = scan.nextLine();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
		dateFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(startdate);

			Calendar c1 = Calendar.getInstance();
			c1.setTime(date);
			String start = String.format("%tF", c1);

			date = dateFormat.parse(enddate);
			c1.setTime(date);
			String end = String.format("%tF", c1);

			Connection conn = null;
			CallableStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;

			try {
				conn = util.open();
				String sql = " { call proc_findcovidteadate(?, ?, ?) } ";
				stat = conn.prepareCall(sql);

				stat.setString(1, start);
				stat.setString(2, end);
				stat.registerOutParameter(3, OracleTypes.CURSOR);

				stat.executeQuery();

				rs = (ResultSet) stat.getObject(3);

				List<String> list = new ArrayList<String>();

				System.out.println("[번호] [교사이름]\t[교사번호]\t[날짜]\t[오전온도]\t[오후온도]");
				while (rs.next()) {
					list.add(rs.getString("name") + "\t" + rs.getString("teaseq") + "\t"
							+ rs.getString("days").substring(0, 10) + "\t" + rs.getString("amtemp") + "\t"
							+ rs.getString("pmtemp"));
				}

				Pagingfile.page(list);

				rs.close();
				conn.close();
				stat.close();

			} catch (Exception e) {
				System.out.println("해당 날짜에 데이터가 존재하지않습니다.");
			}
		} catch (java.text.ParseException e) {
			System.out.println("잘못된 날짜 입력입니다.");
		}

	}
}
