package apply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jdbc.DBUtil;

/**
 * 수강신청을 담당하는 클래스
 * @author 김동욱
 *
 */
public class ApplyMain {
	
	/**
	 * 수강신청 메인 함수
	 */
	public void showMain() {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		try {
			conn = util.open();
			String sql = "SELECT COURSE, CNT, MAXIMUM, STARTDATE, ENDDATE FROM VW_OPEN_EXPECTED_COURSE";
			
			stat = conn.prepareStatement(sql);
			
			rs = stat.executeQuery();
			
			while(rs.next()) {
				
			}
			
			conn.close();
		} catch (Exception e) {
			System.out.println("ApplyMain.showMain()");
			e.printStackTrace();
		}
	}
	
}
