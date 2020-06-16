package covid;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import admin.Pagingfile;
import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

public class Covid19_Teacher {
	
	 static Scanner scan = new Scanner(System.in);

	
	public static void main(String[] args) {
		
		covidCheckMenu();
		
		
		
		
	}


	private static void covidCheckMenu() {
		// 조회하기
		System.out.println("=================================");

		System.out.println("코로나 조회입니다.");
		System.out.println("=================================");
		
		while(true) {
			System.out.println("1. 기간 별 출력");
			System.out.println("2. 교사 별 출력");
			System.out.println("0. 뒤로가기");
			
			
			System.out.print("입력 : ");
			String answer = scan.nextLine();
			
			if(answer.equals("1")) {
				//기간별 출력
				findcovidteadate();
				
			} else if(answer.equals("2")) {
				//교사 별 출력
				findcovidtea();
				
			} else if(answer.equals("0")) {
				break;
			} else {
				System.out.println("번호를 다시 눌러주세요.");
			}
			
			
		}
		
		
		
		
	}


	private static void findcovidtea() {
		// 특정 교사 번호 받아서 출력하기
		
		seecovidtea();
		
		
		
		
	}


	private static void seecovidtea() {
		// 전체 교사의 
		
	}


	private static void findcovidteadate() {
		//코로나 기간별 출력
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

			rs = (ResultSet)stat.getObject(3);
			
			
			List<String> list = new ArrayList<String>();
			
			System.out.println("[번호] [교사이름]\t[교사번호]\t[날짜]\t[오전온도]\t[오후온도]");
			while(rs.next()) {
				list.add(rs.getString("name") +"\t" + rs.getString("teaseq") +"\t" 
			+rs.getString("days").substring(0,10)+"\t" +rs.getString("amtemp") +"\t" + rs.getString("pmtemp"));
			}
			
			Pagingfile.page(list);
			
			
			rs.close();
			conn.close();
			stat.close();
				
				
				
	
			
			
			
		 }catch (Exception e) {
			System.out.println("해당 날짜에 데이터가 존재하지않습니다.");
		} }catch (java.text.ParseException e){
      System.out.println("잘못된 날짜 입력입니다.");
     }
		
		
		
		
	}
	
	
	
	
	
	
}
