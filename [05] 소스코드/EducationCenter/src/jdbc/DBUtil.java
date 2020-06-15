package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 
 * @author 김동욱
 * 오라클 연결 클래스입니다.
 */
public class DBUtil {
	
	private Connection conn = null;
	
	/**
	 * oracle 서버 연결
	 * @return 연결 객체를 반환합니다.
	 */
	public Connection open() {
//		String url = "jdbc:oracle:thin:@211.63.89.53:1521:xe";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
//		String url = "jdbc:oracle:thin:@localhost:1521:kdw";
		String id = "project";
		String pw = "java1234";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, id, pw);
			return conn;
		} catch (Exception e) {
			System.out.println("DBUtil.open()");
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * DB연결 종료
	 */
	public void close() {
		try {
			conn.close();
		} catch (Exception e) {
			System.out.println("DBUtil.close()");
			e.printStackTrace();
		}
	}
	
	
}
