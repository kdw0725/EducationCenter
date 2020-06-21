package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 
 * @author 김동욱
 * 관리자 로그인 관련 클래스
 */
public class AdminLogIn {
	
	/**
	 * 로그인 관련 메소드
	 * @return 로그인 결과
	 */
	public int logIn() {
		
		// 관리자로그인
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {
			System.out.println("====================");
			System.out.print("관리자 ID: ");
			String id = scan.nextLine();

			System.out.print("관리자 PW: ");
			String pw = scan.nextLine();
			System.out.println("====================");
			
			String sql = "{ call proc_loginadmin(?,?,?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setString(1, id);
			stat.setString(2, pw);
			stat.registerOutParameter(3, OracleTypes.NUMBER);
			
			stat.executeUpdate();
			
			//1이면 로그인성공, 0이면 로그인실패
			int num = stat.getInt(3);
			stat.close();
			conn.close();
			return num;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}//loginAdmin

}
