package admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class OpenCourseControl {
	
	 Scanner scan = new Scanner(System.in);
	
	 public void manageocrs() {
		// 개설과정 관리

		System.out.println("==============================");
		System.out.println("개설 과정 관리입니다.");
		System.out.println("==============================");

		while (true) {
			System.out.println("1. 개설과정");
			System.out.println("2. 개설 예정 과정");
			System.out.println("0. 뒤로가기 ");
			System.out.println("==============================");

			System.out.print("입력 : ");
			String answer = scan.nextLine();

			if (answer.equals("1")) {
				ongoingocrs();
			} else if (answer.equals("2")) {
				expectcrs();
			} else if (answer.equals("0")) {
				break;
			} else {
				System.out.println("번호를 다시 입력하시오.");
			}

		}

	}

	 private void ongoingocrs() {
		// 3-4-1. 개설 과정
		System.out.println("=================================");
		System.out.println("개설 과정 관리입니다.");
		System.out.println("=================================");

		while (true) {

			System.out.println("1. 진행중인 과정");
			System.out.println("2. 완료 과정");
			System.out.println("0. 뒤로 가기");
			System.out.println("=================================");
			System.out.print("입력 : ");
			String answer = scan.nextLine();

			if (answer.equals("1")) {
				manongoing();

			} else if (answer.equals("2")) {
				mancomplete();

			} else if (answer.equals("0")) {

				break;
			} else {
				System.out.println("번호를 다시 입력하시오.");
			}

		}

	}

	 private void mancomplete() {
		// 완료된 과정
		System.out.println("==================================");
		System.out.println("완료과정입니다.");
		System.out.println("==================================");
		while (true) {
			int count = seecomoc();

			System.out.println("1. 학생별 출력");
			System.out.println("2. 과목별 출력");
			System.out.println("0. 뒤로 가기");

			System.out.print("입력:");
			String answer = scan.nextLine();
			if (answer.equals("1")) {
				// 학생별 출력
				getinfocomcsStu(count);

			} else if (answer.equals("2")) {
				// 과목별 출력
				Getinfocomocrs(count);
			} else if (answer.equals("0")) {
				break;

			} else {
				System.out.println("번호를 다시 입력하여 주세요.");
			}

		}

	}

	 private void getinfocomcsStu(int count) {
		// 개설 완료된 과정에 대한 학생들 입력

		System.out.print("보고싶은 과정의 번호를 눌러주시오. : ");
		int answer = scan.nextInt();
		scan.skip("\r\n");

		if (answer >= 1 && answer <= count) {
			// 유효성 검사
			Connection conn = null;
			Statement stat = null;
			CallableStatement stat2 = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			List<String> list = new ArrayList<String>();

			try {

				conn = util.open();
				stat = conn.createStatement();

				String sql = String.format("select * from vw_SeeOC where \"enddate\" < sysdate");
				stat = conn.createStatement();

				rs = stat.executeQuery(sql);
				while (rs.next()) {
					list.add(String.format(("%s,%s,%s" + "개월," + "%s,%s,%s,%s" + "명"), rs.getString("num"),
							rs.getString("nameofcourse"), rs.getString("period"),
							rs.getString("startdate").substring(0, 10) + " ~ "
									+ rs.getString("enddate").substring(0, 10),
							rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum")));
				}

				String ocrsnum = list.get(answer - 1).split(",")[0];

				System.out.println(ocrsnum);
				sql = String.format(" { call proc_Getinfocomocrs(?, ?)} ");
				stat2 = conn.prepareCall(sql);

				stat2.setString(1, ocrsnum);
				stat2.registerOutParameter(2, OracleTypes.CURSOR);

				stat2.executeQuery();

				rs = (ResultSet) stat2.getObject(2);

				List<String> list2 = new ArrayList<String>();
				System.out.println("[학생이름]\t[과정명]\t     [주민번호 뒷자리]\t   [전화번호]\t     [등록일]\t[수료여부]\t[수료날짜]");
				while (rs.next()) {
					list2.add(String.format("%s,%s,%s,%s,%s,%s,%s", rs.getString("stuname"), rs.getString("coursename"),
							rs.getString("stussn"), rs.getString("stutel"), rs.getString("sturegdate").substring(0, 10),
							rs.getString("status"), rs.getString("statusdate")));

				}

				Pagingfile.page(Pagingfile.save(list2));
				pause();

				stat2.close();
				rs.close();
				stat.close();
				conn.close();

			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("해당 과정이 없습니다. ");
			}

		} else {
			System.out.println("잘못 입력된 번호입니다.");
		}

	}

	 private void Getinfocomocrs(int count) {
		// 개설 완료된 과정
		System.out.print("보고싶은 과정의 번호를 눌러주시오. : ");
		int answer = scan.nextInt();
		scan.skip("\r\n");

		if (answer >= 1 && answer <= count) {
			// 유효성 검사
			Connection conn = null;
			Statement stat = null;
			CallableStatement stat2 = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			List<String> list = new ArrayList<String>();

			try {

				conn = util.open();
				stat = conn.createStatement();

				String sql = String.format("select * from vw_SeeOC where \"enddate\" < sysdate");
				stat = conn.createStatement();

				System.out.println(
						"[번호]\t[과정명]\t\t\t\t\t         [기간]\t[시작일]\t[종료일]\t[교실]\t[과목 등록 여부]\t[교육생 등록 인원]");
				rs = stat.executeQuery(sql);
				while (rs.next()) {
					list.add(String.format(("%s,%s,%s" + "개월," + "%s,%s,%s,%s" + "명"), rs.getString("num"),
							rs.getString("nameofcourse"), rs.getString("period"),
							rs.getString("startdate").substring(0, 10) + " ~ "
									+ rs.getString("enddate").substring(0, 10),
							rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum")));
				}

				String ocrsnum = list.get(answer - 1).split(",")[0];

				sql = String.format(" { call proc_GetinfoOC(?, ?)} ");
				stat2 = conn.prepareCall(sql);

				stat2.setString(1, ocrsnum + "");
				stat2.registerOutParameter(2, OracleTypes.CURSOR);

				stat2.executeQuery();

				rs = (ResultSet) stat2.getObject(2);

				System.out.println("[과정명]\t[과목명]\t[기간]\t[교재명]\t[선생님성함]");
				while (rs.next()) {
					System.out.printf("%s\t%s\t%s\t%s\t%s\n", rs.getString("coursename"), rs.getString("subjectname"),
							rs.getString("startdate").substring(0, 10) + " ~ "
									+ rs.getString("enddate").substring(0, 10),
							rs.getString("bkname"), rs.getString("teaname"));

				}

				pause();

				stat2.close();
				rs.close();
				stat.close();
				conn.close();

			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("해당 과정이 없습니다. ");
			}

		} else {
			System.out.println("잘못 입력된 번호입니다.");
		}

	}// getinfocomocrs

	 private int seecomoc() {
		// 개설 과정 정보 보기 완료
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		List<String> list = new ArrayList<String>();

		try {

			conn = util.open();
			stat = conn.createStatement();

			int count = 0;
			String sql = String.format("select * from vw_SeeOC where \"enddate\" < sysdate");
			System.out.println("===========================================================");
			System.out.println("해당 과정 전체보기입니다.");
			System.out.println("===========================================================");
			stat = conn.createStatement();

			System.out.println(
					"[번호]\t[과정명]\t\t\t\t\t         [기간]\t[시작일]\t[종료일]\t[교실]\t[과목 등록 여부]\t[교육생 등록 인원]");
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				list.add(String.format(("%s,%s" + "개월," + "%s,%s,%s,%s" + "명")

						, rs.getString("nameofcourse"), rs.getString("period"),
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10),
						rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum")));
				count++;
			}

			Pagingfile.page(Pagingfile.save(list));

			rs.close();
			stat.close();
			conn.close();

			return count;

		} catch (Exception e) {
			System.out.println("완료된 과정이 없습니다.");
		}
		return 0;

	}

	 private void manongoing() {
		// 3-4-1-1. 진행중인 과정
		// 진행중인 과정

		while (true) {
			seeocrs();

			System.out.println("========================");
			System.out.println("1. 학생별 출력");
			System.out.println("2. 과목별 출력");
			System.out.println("3. 수정하기");
			System.out.println("0. 뒤로가기");
			System.out.println("========================");

			System.out.print("입력 : ");
			String answer = scan.nextLine();

			if (answer.equals("1")) {
				
				getinfoocs();

			} else if (answer.equals("2")) {
				getinfooc();
			} else if (answer.equals("0")) {
				break;
			} else if (answer.equals("3")) {
				// 수정 이상 선생님 테이블 수정 만든 사람 있는지 확인하기
//				updOC();
//				updcrstea(count);

			} else {
				System.out.println("번호를 다시 입력하시오.");
			}

		}

	}

	 private void seeocrs() {
		//지금 진행 중인 개설 과정
		
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		try {
			conn = util.open();
			stat = conn.createStatement();

			String sql = "select * from vw_SeeOC where \"startdate\" <= sysdate and \"enddate\" >= sysdate";
			
			
			
			stat = conn.createStatement();

			rs = stat.executeQuery(sql);

			System.out.println("[번호]\t[과정 이름]\t[개월수]\t[과정 기간]\t[교실번호]\t[과목 등록 여부]\t[학생수]");
			while (rs.next()) {

				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("num"), rs.getString("nameofcourse"),
						rs.getString("period"),
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10),
						rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum"));


			}

			rs.close();
			conn.close();
			stat.close();
			
			
			
			
			
		} catch (Exception e) {
			System.out.println("현재 진행중인 과정이 없습니다.");
		}
		
		
		
		
		
	}

	 private void updcrstea(int count) {
		// 선생님 수정하기

		while (true) {
			System.out.println("==================================");
			System.out.println("수정을 원하는 과정의 번호를 입력하시오. : ");
			System.out.println("==================================");

			System.out.print("입력 :");
			String answer = scan.nextLine();

			if (Integer.parseInt(answer) >= 1 && Integer.parseInt(answer) <= count) {

				Connection conn = null;
				Statement stat = null;
				ResultSet rs = null;
				DBUtil util = new DBUtil();
				List<String> list = new ArrayList<String>();

				try {

					conn = util.open();
					stat = conn.createStatement();

					String sql = String
							.format("select * from vw_SeeOC where \"startdate\" <= sysdate and \"enddate\" >= sysdate");

					stat = conn.createStatement();

					rs = stat.executeQuery(sql); 
					while (rs.next()) {
						list.add(String.format(("%s,%s,%s" + "개월," + "%s,%s,%s,%s" + "명"), rs.getString("seq"),
								rs.getString("nameofcourse"), rs.getString("period"),
								rs.getString("startdate").substring(0, 10) + " ~ "
										+ rs.getString("enddate").substring(0, 10),
								rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum")));
					}

					System.out.println("선택하신 과정입니다. ");
					System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\n", count,
							list.get(Integer.parseInt(answer)).split(",")[1],
							list.get(Integer.parseInt(answer)).split(",")[2],
							list.get(Integer.parseInt(answer)).split(",")[3],
							list.get(Integer.parseInt(answer)).split(",")[4],
							list.get(Integer.parseInt(answer)).split(",")[5],
							list.get(Integer.parseInt(answer)).split(",")[6]

					);

					String seq = list.get(Integer.parseInt(answer)).split(",")[0];

					rs.close();
					stat.close();
					conn.close();

				} catch (Exception e) {
					System.out.println("해당 과정 번호는 없습니다.");
				}

			}

		}

	}

	 private void expectcrs() {
		seeexpectocrs();

		while (true) {

			System.out.println("======================================");
			System.out.println("개설 예정 메뉴 입니다.");
			System.out.println("======================================");
			System.out.println("======================================");
			System.out.println("1. 추가하기");
			System.out.println("2. 수정하기");
			System.out.println("3. 삭제하기");
			System.out.println("0. 뒤로가기");
			System.out.println("======================================");

			System.out.print("입력 : ");
			String answer = scan.nextLine();
			if (answer.equals("1")) {
				// 개설 예정 과정 추가
				addoc();
			} else if (answer.equals("2")) {
				// 개설 예정 과정 수정하기
				seeexpectocrs();
				updOC();

			} else if (answer.equals("3")) {
				// 개설 예정 과정 삭제하기
				deleteoc();
			} else if (answer.equals("0")) {
				// 뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");

			}

		}

	}// expect

	

	 private int seeexpectocrs() {
		// 개설 예정 과정 메뉴
		System.out.println("======================================");
		System.out.println("개설 예정 과정 입니다.");
		System.out.println("======================================");

		Connection conn = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		int count = 0;

		try {

			conn = util.open();
			String sql = "select * from vw_seeoc where \"startdate\" >= sysdate and delflag = \'Y\'";

			stat = conn.createStatement();

			rs = stat.executeQuery(sql);

			System.out.println("[번호]\t[과정 이름]\t[개월수]\t[과정 기간]\t[교실번호]\t[과목 등록 여부]\t[학생수]");
			while (rs.next()) {

				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("num"), rs.getString("nameofcourse"),
						rs.getString("period"),
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10),
						rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum"));

				count++;

			}

			rs.close();
			conn.close();
			stat.close();

			return count;

		} catch (Exception e) {
			System.out.println("개설 예정 과정을 불러오지 못하였습니다. ");
		}
		return 0;
	}

	 private void stuscore(String string) {
//		A037 : 교육생 개인별 출력시 교육생 이름, 주민번호 뒷자리, 
//		개설 과정명, 개설 과정기간, 강의실명 등을 출력하고, 교육생 개인이 수강한 모든 개설 과목에 대한 성적 정보(개설 과목명, 개설 과목 기간, 교사명, 출력, 필기, 실기)를 같이 출력한다.

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
//		string = 2+ "";

		try {

			conn = util.open();

			String sql = " { call proc_stuScore(?, ?) } ";
			stat = conn.prepareCall(sql);

			stat.setString(1, string);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println(
					"[학생번호]\t[학생이름]\t[학생 주민번호]\t[과정번호]\t[과정이름]\t[과정시작일]\t[과정종료일]\t[과정기간]\t[교실번호]\t[과목이름]\t[과목시작일]\t[과목종료일]\t[과목기간]\t[담당선생님]\t[필기]\t[실기]");

			while (rs.next()) {

				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
						rs.getString("stunum"), rs.getString("stuname"), rs.getString("stussn"),
						rs.getString("ocrsseq"), rs.getString("crsname"), rs.getString("crsstartdate").substring(0, 10),
						rs.getString("crsenddate").substring(0, 10), rs.getString("crsperiod"), rs.getString("roomnum"),
						rs.getString("subname"), rs.getString("substartdate").substring(0, 10),
						rs.getString("subenddate").substring(0, 10), rs.getString("subperiod"), rs.getString("teaname"),
						rs.getString("write"), rs.getString("performance")

				);

			}

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("정보를 불러오지 못했습니다.");
		}

	}// stuscore

	 private void subscore(String pnum) {
//		A036 : 과목별 출력시 개설 과정명, 개설 과정기간, 강의실명, 개설 과목명, 교사명, 교재명 등을 출력하고, 해당 개설 과목을 수강한 모든 교육생들의 성적 정보(교육생 이름, 주민번호 뒷자리, 필기, 실기)를 같이 출력한다.

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
//		pnum = 2+ "";

		try {

			conn = util.open();

			String sql = " { call proc_subScore(?, ?) } ";
			stat = conn.prepareCall(sql);

			stat.setString(1, pnum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println(
					"[과정번호]\t[과정이름]\t[과목이름]\t[담당선생님]\t[교재명]\t[과목시작일]\t[과목종료일]\t[교실번호]\t[학생번호]\t[학생이름]\t[학생주민번호]\t[필기]\t[실기]");

			while (rs.next()) {

				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("ocrsseq"),
						rs.getString("crsname"), rs.getString("subname"), rs.getString("teaname"),
						rs.getString("bkname"), rs.getString("startdate").substring(0, 10),
						rs.getString("enddate").substring(0, 10), rs.getString("roomseq"), rs.getString("stuseq"),
						rs.getString("stuname"), rs.getString("stussn"), rs.getString("write"),
						rs.getString("performance")

				);

			}

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("정보를 불러오지 못했습니다.");
		}

	}// subscore

	 private void checktest(String answer) {
		// A034 : 특정 개설 과정을 선택하는 경우 등록된 개설 과목 정보를 출력하고, 개설 과목 별로 성적 등록 여부, 시험 문제 파일 등록
		// 여부를 확인할 수 있어야 한다.

//		seeoc();
//		System.out.print("과목별로 확인하고 싶은 과정의 번호를 입력해주시오.:");
//		String answer = scan.nextLine();

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;

		try {

			conn = util.open();

			String sql = " { call proc_checkTest(?, ?) } ";
			stat = conn.prepareCall(sql);

			stat.setString(1, answer);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println("[과정번호]\t[과정이름]\t[과목번호]\t[과목이름]\t[과목시작일]\t[과목종료일]\t[시험파일등록여부]\t[시험여부]");

			while (rs.next()) {

				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("ocseq"), rs.getString("crsname"),
						rs.getString("seq"), rs.getString("subname"), rs.getString("substartdate").substring(0, 10),
						rs.getString("subenddate").substring(0, 10), rs.getString("checktestfile"),
						rs.getString("checktest"));

			}

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("정보를 불러오지 못했습니다.");
		}

	}// checktest

	 private void deleteoc() {
		// -------------------개설 과정에 대한 삭제 + 개설된 강의실

		seeexpectocrs();

		System.out.print("삭제하려는 개설 과정의 번호를 눌러주세요.:");
		String pseq = scan.nextLine();

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		try {

			conn = util.open();

			String sql = " { call proc_deleteoc(?) } ";
			stat = conn.prepareCall(sql);

			while (true) {
				System.out.println("1. 확정하기");
				System.out.println("2. 취소하기");
				System.out.println("0. 뒤로가기");
				System.out.print("입력 : ");
				String answer = scan.nextLine();

				if (answer.equals("1")) {

					stat.setString(1, pseq);

					stat.executeUpdate();
					System.out.println("삭제되었습니다.");
					break;

				} else if (answer.equals("2")) {
					// 취소하기..
					System.out.println("취소되었습니다.");
					deleteoc();
					break;
				} else if (answer.equals("0")) {
					break;
				} else {
					System.out.println("번호를 다시 입력하시오.");
				}

			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("과목 번호를 잘 못입력하였습니다.");
		}

	}

	 private void updOC() {
		// --------------------------------개설 과정에 대한 수정
//		seeoc();

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		System.out.print("수정하고 싶은 과정의 번호를 눌러주시오.: ");
		String answer = scan.nextLine();

		System.out.print("수정하고 싶은 과정의 시작일을 눌러주시오. (yy-MM-dd): ");
		String startdate = scan.nextLine();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
		dateFormat.setLenient(false);
		try {
			Date date = dateFormat.parse(startdate);

			Calendar c1 = Calendar.getInstance();
			c1.setTime(date);

			try {

				String startdate_1 = String.format("%tF", c1);
				String sql = "select * from tbl_course";
				conn = util.open();

				stat = conn.createStatement();

				rs = stat.executeQuery(sql);

				System.out.println("[번호]\t  [기간]\t\t\t\t[과정명]");

				int count = 1;
				List<String> list = new ArrayList<String>();

				while (rs.next()) {

					list.add(rs.getString("period") + "," + rs.getString("name"));

				}

				Pagingfile.page(Pagingfile.save(list));

				System.out.print("수정할 과정의 번호를 누르시오:");
				String crsseq = scan.nextLine();

				sql = "select * from tbl_course where seq = " + crsseq;

				stat = conn.createStatement();

				rs = stat.executeQuery(sql);

				if (rs.next()) {
					double addm = rs.getDouble("period");
					if (addm == 5.5) {
						c1.set(Calendar.MONTH, c1.get(Calendar.MONTH) + (int) addm);
						c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 15);
					} else {

						c1.set(Calendar.MONTH, c1.get(Calendar.MONTH) + (int) addm);
					}
					System.out.printf("입력하신 과정의 종료날짜는 %tF 입니다.\n", c1);
				}

				String enddate_1 = String.format("%tF", c1);

				CallableStatement stat2 = null;

				sql = " { call proc_updOC(?, ?, ?, ?)} ";
				stat2 = conn.prepareCall(sql);

				while (true) {
					System.out.println("===========================");
					System.out.println("1. 확정하기");
					System.out.println("2. 다시쓰기");
					System.out.println("0. 뒤로가기");
					System.out.println("===========================");

					System.out.print("입력 : ");
					String answer2 = scan.nextLine();

					if (answer2.equals("1")) {
						// 확정하기
						stat2.setString(1, answer);
						stat2.setString(2, startdate_1);
						stat2.setString(3, enddate_1);
						stat2.setString(4, crsseq);

						stat2.executeUpdate();
						System.out.println("수정완료되었습니다.");
						break;

					} else if (answer2.equals("2")) {
						// 다시쓰기
						System.out.println("메뉴로 되돌아갑니다.");
						updOC();
						break;

					} else if (answer2.equals("0")) {
						break;

					} else {
						System.out.println("번호를 다시 입력하시오.");
					}

				}

				rs.close();
				stat.close();
				stat2.close();
				conn.close();

			} catch (SQLException e) {
				System.out.println("잘못된 입출력입니다.");
			}
		} catch (java.text.ParseException e) {
			System.out.println("잘못된 날짜 입력입니다.");
		}

	}

	 private void seefinish(String answer) {
		// -------------특정 개설 과정 선택시 수료 여부 확인용 프로시저
		// 해당 개설 과정 번호 매개변수값으로 가지기

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		System.out.println("===================특정 개설 과정 별 수료확인입니다.==============");

		try {

			conn = util.open();

			String sql = String.format(" { call proc_SeeFinish(?, ?)} ");

			stat = conn.prepareCall(sql);

			stat.setString(1, answer);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			// cursor == resultset
			rs = (ResultSet) stat.getObject(2);

			System.out.println("[과정번호]\t[과정명]\t[시작일]\t[종료일]\t[수료여부]\t[학생번호]\t[학생이름]\t[날짜]");
			while (rs.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", rs.getString("crsnum"), rs.getString("crsname"),
						rs.getString("startdate").substring(0, 10), rs.getString("enddate").substring(0, 10),
						rs.getString("checkfinish"), rs.getString("stuseq"), rs.getString("stuname"),
						rs.getString("status").substring(0, 10)

				);

			}

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("보기");
		}

	}

	 private void updfinish() {
//		A017 : 특정 개설 과정이 수료한 경우 등록된 교육생 전체에 대해서 수료날짜를 지정할 수 있어야 한다. 단, 중도 탈락자는 제외한다
//
//		----------------------------학생 수료날짜지정
		seeoc();
		System.out.print("보고싶은 과정의 번호를 누르시오 : ");
		String answer = scan.nextLine();
		seefinish(answer);

		System.out.print("수료날짜를 지정하고 싶은 학생의 번호를 눌러주세요.:");
		String answer2 = scan.nextLine();

		System.out.print("해당 학생의 수료날짜를 입력해주세요.(yy-mm-dd) : ");
		String date = scan.nextLine().trim();

		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
			dateFormat.setLenient(false);

			try {
				Date date2 = dateFormat.parse(date);

				Connection conn = null;
				CallableStatement stat = null;
				DBUtil util = new DBUtil();

				conn = util.open();

				String sql = String.format(" { call proc_updfinish(?, ?, ?) } ");

				stat = conn.prepareCall(sql);
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date2);

				String date3 = String.format("%tF", c1);

				stat.setString(1, answer);
				stat.setString(2, answer2);
				stat.setString(3, date3);// 날짜 넣기

				stat.executeUpdate();

				stat.close();
				conn.close();

				System.out.println("완료");

			} catch (java.text.ParseException e) {
				System.out.println("잘못된 날짜 입력입니다.");
			}

		} catch (Exception e) {
//			e.printStackTrace();
		}

	}// updfinish

	 private void getinfooc() {
		// ------------------------개설과목 정보
		// 해당 개설 과정
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		System.out.println("===================특정 개설 과정 별 해당 과목 정보 보기입니다.==============");
		System.out.print("보고싶은 과정의 번호를 누르시오 : ");
		String answer = scan.nextLine();

		try {

			conn = util.open();

			String sql = String.format(" { call proc_GetinfoOC(?, ?)} ");

			stat = conn.prepareCall(sql);

			stat.setString(1, answer);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			// cursor == resultset
			rs = (ResultSet) stat.getObject(2);
			List<String> list = new ArrayList<String>();

			System.out.println("[과정명]\t[과목명]\t[기간]\t[교재명]\t[선생님성함]");
			while (rs.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\n", rs.getString("coursename"), rs.getString("subjectname"),
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10),
						rs.getString("bkname"), rs.getString("teaname"));

			}

			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
//			e.printStackTrace();
		}

		pause();

	}

	 private void pause() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("엔터 누르면 뒤로 갑니다.");
		try {
			reader.readLine();
		} catch (IOException e) {
			System.out.println("다시 입력하세요.");
//			e.printStackTrace();
		}
	}

	 private void getinfoocs() {
		// 특정 개설 과정 선택시 개설 과정에 등록된 개설 과목 정보(과목명, 과목기간(시작 년월일, 끝 년월일),
//		교재명, 교사명) 및 등록된 교육생 정보(교육생 이름, 주민번호 뒷자리, 전화번호, 등록일, 수료 및 중도탈락)을 확인할 수 있어야 한다.
		// 교육생 정보 확인

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		System.out.println("===================특정 개설 과정 별 해당 교육생 정보 보기입니다.==============");
		System.out.print("보고싶은 과정의 번호를 누르시오 : ");
		String answer = scan.nextLine();

		try {

			conn = util.open();

			String sql = String.format(" { call proc_GetinfoOCSs(?, ?)} ");

			stat = conn.prepareCall(sql);

			stat.setString(1, answer);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			// cursor == resultset
			rs = (ResultSet) stat.getObject(2);

			List<String> list = new ArrayList<String>();
			System.out.println("[학생이름]\t[과정명]\t     [주민번호 뒷자리]\t   [전화번호]\t     [등록일]\t[수료여부]\t[수료날짜]");
			while (rs.next()) {
				list.add(String.format("%s,%s,%s,%s,%s,%s,%s", rs.getString("stuname"), rs.getString("coursename"),
						rs.getString("stussn"), rs.getString("stutel"), rs.getString("sturegdate").substring(0, 10),
						rs.getString("status"), rs.getString("statusdate")));

			}

			Pagingfile.page(Pagingfile.save(list));
			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	 private int seeoc() {
		// 개설 과정 정보 보기 현재
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		List<String> list = new ArrayList<String>();

		try {

			conn = util.open();
			stat = conn.createStatement();

			int count = 0;
			String sql = String
					.format("select * from vw_SeeOC where \"startdate\" <= sysdate and \"enddate\" >= sysdate");
			System.out.println("===========================================================");
			System.out.println("개설 과정 전체보기입니다.");
			System.out.println("===========================================================");
			stat = conn.createStatement();

			System.out.println(
					"[번호]\t[과정명]\t\t\t\t\t         [기간]\t[시작일]\t[종료일]\t[교실]\t[과목 등록 여부]\t[교육생 등록 인원]");
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				list.add(String.format(("%s,%s" + "개월," + "%s,%s,%s,%s" + "명")

						, rs.getString("nameofcourse"), rs.getString("period"),
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10),
						rs.getString("classroom"), rs.getString("regcheck"), rs.getString("stunum")));
				count++;
			}

			Pagingfile.page(Pagingfile.save(list));

			rs.close();
			stat.close();
			conn.close();

			return count;

		} catch (Exception e) {
			System.out.println("현재 진행중인 과정이 없습니다.");
		}
		return 0;

	}

	 private int ShowCourse() {
		// 과정출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		int count = 0;

		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("Select seq, name, period from tbl_course where delflag = 'Y' order by seq");

			rs = stat.executeQuery(sql);

			System.out.println("[No.]\t [기간]\t [과정명]");

			while (rs.next()) {
				System.out.printf(" %2s\t%3s개월\t %s\r\n", rs.getString("seq"), rs.getString("period"),
						rs.getString("name"));
				count++;
			}

			stat.close();
			conn.close();

			System.out.println("Success: Select tbl_course ");

		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("Fail: Select tbl_course ");
		}
		return count;
	}// ShowCourse

	 private void addoc() {
		// 동적인 과정 추가하기

		Connection conn = null;
		CallableStatement stat2 = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {

			conn = util.open();

			System.out.println("===========================================================");
			System.out.println("개설 과정 추가하기입니다.");
			System.out.println("===========================================================");

			System.out.println("정적인 과정입니다.");
			String sql = String.format("select * from tbl_course where delflag = \'Y\'order by seq");

			stat = conn.createStatement();

			rs = stat.executeQuery(sql);

			System.out.println("[번호]\t[기간]\t[과정번호]\t[과정명]");

			List<String> list = new ArrayList<String>();

			while (rs.next()) {

				list.add(String.format("%5s" + "개월\t" + "%40s", rs.getString("period"), rs.getString("name")));

			}

			Pagingfile.page(list);

			System.out.print("추가할 과정 번호를 입력해주세요 : ");
			int anum = scan.nextInt();
			scan.skip("\r\n");

			System.out.print("과정 시작 일을 입력해주세요. (yy-MM-dd): ");
			String date = scan.nextLine();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
			dateFormat.setLenient(false);
			try {
				Date date2 = dateFormat.parse(date);

				Calendar c1 = Calendar.getInstance();
				c1.setTime(date2);

				String date1 = String.format("%tF", c1);

				sql = "select * from tbl_course where seq = " + anum;

				stat = conn.createStatement();
				rs = stat.executeQuery(sql);

				if (rs.next()) {
					double addm = rs.getDouble("period");
					if (addm == 5.5) {
						c1.set(Calendar.MONTH, c1.get(Calendar.MONTH) + (int) addm);
						c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 15);
					} else {
						c1.set(Calendar.MONTH, c1.get(Calendar.MONTH) + (int) addm);
					}
					System.out.printf("입력하신 과정의 종료날짜는 %tF 입니다.\n", c1);
				}

				String date3 = String.format("%tF", c1);

				sql = "select * from vw_Seeclassroom";

				stat = conn.createStatement();
				rs = stat.executeQuery(sql);

				System.out.println("[강의실번호]\t[강의실 인원수]");
				while (rs.next()) {
					System.out.printf("%s\t%s\n", rs.getString("classnum"), rs.getString("cnt"));
				}

				System.out.print("강의실 번호를 눌러주세요.");
				String roomnum = scan.nextLine();

				sql = "{ call proc_AddOC(?, ?, ?, ?) }";
				stat2 = conn.prepareCall(sql);

				System.out.println("해당과정을 추가하시겠습니까?");
				System.out.println("1. 확정하기");
				System.out.println("2. 다시쓰기");
				System.out.println("0. 뒤로가기");

				while (true) {
					System.out.print("입력:");
					String answer = scan.nextLine();
					if (answer.equals("1")) {
						stat2.setString(1, "" + anum);
						stat2.setString(2, date1);
						stat2.setString(3, date3);
						stat2.setString(4, roomnum);
						stat2.executeUpdate();

						System.out.println("개설 과정에 추가 완료 되었습니다.");

						break;

					} else if (answer.equals("2")) {
						// 재귀 가능,,?
						addoc();
						break;

					} else if (answer.equals("0")) {

						break;
					} else {
						System.out.println("번호를 다시 입력하시오.");
					}

				}

				stat.close();
				stat2.close();
				conn.close();

			} catch (java.text.ParseException e) {
				System.out.println("잘못된 날짜 입력입니다.");
				addoc();
			}

		} catch (Exception e) {
			System.out.println("잘못입력하였습니다. 다시 메뉴로 되돌아갑니다.");
			addoc();

		}

	}
}
