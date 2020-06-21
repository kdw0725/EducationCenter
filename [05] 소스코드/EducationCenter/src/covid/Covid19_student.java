package covid;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import admin.Pagingfile;
import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 
 * @author 김영현
 * 학생의 코로나를 담당하는 클래스
 */
public class Covid19_student {
	
	Scanner scan = new Scanner(System.in);
	
	private List<String> seecovid19stutoday() {
		// 오늘 출근한 사람 보여주기
		Connection conn = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		int count = 1;
		
		try {
			conn = util.open();
			//날짜 나중에 수정할 것
			String sql = "select stu.name, stu.seq as stuseq, atts.days  from tbl_attend_student atts \r\n" + 
		               "    inner join tbl_apply appl on atts.appseq = appl.seq \r\n" + 
		               "        inner join tbl_student stu on appl.stuseq = stu.seq\r\n" + 
		               "         where appl.delflag = 'Y'and days =  to_date(sysdate, 'yy-mm-dd')\r\n" + 
		               "            order by name";
		         stat = conn.createStatement();

		         System.out.println("오늘 출근한 명단입니다.");
		         System.out.println("[번호][학생이름][학생번호]  [날짜]");
		         rs = stat.executeQuery(sql);
		         
		         List<String> list = new ArrayList<String>();
		         
		         while(rs.next()) {
		            
		            
		            
		               list.add(rs.getString("name")+"\t"+rs.getString("stuseq")+"\t"+
		                     rs.getString("days").substring(0,10));
		            
		            
		            
		         }//while
			
			Pagingfile.page(list);
			
			
			
			rs.close();
			stat.close();
			conn.close();
			
			return list;
			
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("아직 출근한 사람이 없습니다.");
			return null;
		}
		
	}

	/**
	 * 학생 코로나 메인 함수
	 */
	public void covid19stu() {

		
		while(true) {
			// 코로나 학생 메뉴
			System.out.println("======================================");
			
			System.out.println("학생 코로나 메뉴입니다.");
			System.out.println("======================================");
			System.out.println("1. 체온 기록");
			System.out.println("2. 체온 조회");
			System.out.println("0. 뒤로가기");
			System.out.print("입력 : ");
			String answer = scan.nextLine();
			
			
			if(answer.equals("1")) {
				//체온 기록
				addcovstu();
				
				
			} else if(answer.equals("2")) {
				//코로나 학생 체온 조회
				seecovid();
				
			} else if (answer.equals("0")) {
				break;
			} else {
				System.out.println("잘 못 입력하였습니다.");
			}
			
			
		}
		
		
	}

	/**
	 * 학생 코로나 출력 메소드
	 */
	private void seecovid() {
		// 메뉴

		while(true) {
			System.out.println("====================================");
			System.out.println("학생 코로나 메뉴조회입니다.");
			System.out.println("====================================");
			System.out.println("1. 기간 별 출력");
			System.out.println("2. 학생 별 출력");
			System.out.println("0. 뒤로가기");
			System.out.println("====================================");
			System.out.print("입력:");
			String answer = scan.nextLine();
			
			if(answer.equals("1")) {
				//기간별 출력
				findcovidstudate();
				
			} else if(answer.equals("2")) {
				// 학생별 출력
//				seecovid19stutoday();
				//학생 번호 나오는..출근하는 메소드....?
				
				findcovidstu();
				
			} else if(answer.equals("0")){
				break;
			} else {
				System.out.println("잘못된 번호 입력입니다.");
			}
			
			
			
			
		}
		
		
	}//seecovid




	private void findcovidstudate() {
		// 특정 기간 입력받아서 조회하기

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
				String sql = " { call proc_findcovidstudate(?, ?, ?) } ";
				stat = conn.prepareCall(sql);

				stat.setString(1, start);
				stat.setString(2, end);
				stat.registerOutParameter(3, OracleTypes.CURSOR);

				stat.executeQuery();

				rs = (ResultSet) stat.getObject(3);

				List<String> list = new ArrayList<String>();

				System.out.println("[번호]\t[학생이름]\t[학생번호]\t[날짜]\t[오전온도]\t[오후온도]");
				while (rs.next()) {
					list.add(rs.getString("name") + "\t" + rs.getString("stuseq") + "\t"
							+ rs.getString("days").substring(0, 10) + "\t" + rs.getString("amtemp") + "\t"
							+ rs.getString("pmtemp"));
				}

				Pagingfile.page(list);

				rs.close();
				conn.close();
				stat.close();

			} catch (Exception e) {
				System.out.println("학생의 번호가 존재하지않습니다.");
			}
		} catch (java.text.ParseException e) {
			System.out.println("잘못된 날짜 입력입니다.");
		}
	
		
		
		
		
	}//findcovidstudate

	private void findcovidstu() {
		//특정 학생 입력받아서 출력
		
		
		
		
		
		
		
		
		
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		
		try {
			conn = util.open();
			String sql = "select distinct name, stuseq from vw_seecovidstu order by stuseq";
			stat2 = conn.createStatement();

			
			rs = stat2.executeQuery(sql);
			
			List<String> list2 = new ArrayList<String>();
			
			
			
			
			while(rs.next()) {
				
				
				
					list2.add(rs.getString("name")+"\t"+rs.getString("stuseq"));
				
				
				
			}//while
			System.out.println("학생 명단입니다. ");
			System.out.println("[번호] [학생 이름] [학생 번호]");
			Pagingfile.page(list2);
			
			System.out.print("조회하고싶은 학생의 번호를 입력하여 주십시오. : ");
			String stunum = scan.nextLine();
			
			
			sql = " { call proc_findcovidstu(?, ?) } ";
			stat = conn.prepareCall(sql);

			stat.setString(1, stunum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			rs = (ResultSet)stat.getObject(2);
			
			
			
			List<String> list = new ArrayList<String>();
			System.out.println("[번호] [학생이름] [학생번호]\t[날짜]\t[오전온도]\t[오후온도]");
			while(rs.next()) {
				
				list.add(String.format("%s\t%s\t%s\t%s"+"ºC"+"\t%s"+"ºC"
						, rs.getString("name")
						, rs.getString("stuseq")
						, rs.getString("days").substring(0,10)
						, rs.getString("amtemp")
						, rs.getString("pmtemp")));
			}
			
			Pagingfile.page(list);
			
			
			stat2.close();
			rs.close();
			conn.close();
			stat.close();
				
				
				
			
			
			
		} catch (Exception e) {
			System.out.println("학생의 번호가 존재하지않습니다.");
		}
		
		
		
		
		
		
	}//findcovidstu

	private void addcovstu() {
		//학생 코비드 입력
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;

		System.out.println("================================");
		List<String> list = seecovid19stutoday();
		System.out.println("================================");

		
		System.out.print("기록을 원하는 학생의 번호를 입력하여 주세요 :");
		String stseq = scan.nextLine();
		
		
		
		
		System.out.print("학생의 오전 온도를 입력하여 주세요. : ");
		String amtemp1 = scan.nextLine();
		
		System.out.print("학생의 오후 온도를 입력하여 주세요. : ");
		String pmtemp1 = scan.nextLine();
		
		//정규식 하기
		if(amtemp1.isEmpty()) {
			amtemp1 = "0.0";
		}
		if(pmtemp1.isEmpty()) {
			pmtemp1 = "0.0";
		}
		
		if( ((Double.parseDouble(amtemp1) <= 37.5 && Double.parseDouble(amtemp1) >= 35) || amtemp1.equals("0.0")) && ((Double.parseDouble(pmtemp1) <= 40 && Double.parseDouble(pmtemp1) >= 35) || pmtemp1.equals("0.0")) ) {
//			System.out.println(123456789);
		
		try {
			conn = util.open();
			
			
		
			
			if(amtemp1.equals("0.0")) {
				amtemp1 = null;
			}if(pmtemp1.equals("0.0")) {
				pmtemp1 = null;
			}
			
			
			String sql = " { call proc_addcovstu(?, ?, ?)} ";
			
			
			stat = conn.prepareCall(sql);				
			
			
			System.out.println("1. 확정하기");
			System.out.println("0. 뒤로가기");
			while(true) {
				System.out.print("입력 : ");
				String answer = scan.nextLine();
				if(answer.equals("1")) {
			
//					System.out.println(amtemp1);
//					System.out.println(pmtemp1);
					stat.setString(1, amtemp1);
					stat.setString(2, pmtemp1);
					stat.setString(3, stseq);
					
					stat.executeUpdate();
					System.out.println("입력되었습니다.");
					break;
					
				} else if(answer.equals("0")){
					
					break;
				} else {
					System.out.println("번호를 다시 입력해주세요.");
				}
			
				
			
			}
			
			
			
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("없는 학생 번호입니다.");
		}
		
	
	} else {
		System.out.println("온도를 잘못입력하였습니다.");
	}
		
	}//addcovid

	private void seecovidstu() {
		//학생 코비드 보는 뷰
		Connection conn = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		
		try {
			conn = util.open();
			
			String sql = "select * from vw_seecovidstu";
			stat = conn.createStatement();

			System.out.println("[번호]\t[학생이름]\t[학생번호]\t[날짜]\t[오전온도]\t[오후온도]");
			rs = stat.executeQuery(sql);
			while(rs.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s"+"ºC"+"\t%s"+"ºC"+"\n"
						, rs.getString("seq")
						, rs.getString("name")
						, rs.getString("stuseq")
						, rs.getString("days").substring(0,10)
						, rs.getString("amtemp")
						, rs.getString("pmtemp"));
			}
			
			
			rs.close();
			conn.close();
			stat.close();
			
			
		} catch (Exception e) {
			// 
		}
		
		
		
		
		
	}//seecovidstu
	
	
	
	
}//covidstudent
