package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Pattern;

import jdbc.DBUtil;

/**
 * 
 * @author ±èµ¿¿í ÇĞ»ı È¸¿ø°¡ÀÔÀ» À§ÇÑ Å¬·¡½º
 */
public class StudentSignIn {

	public void signInInput() {
		Scanner sc = new Scanner(System.in);

		System.out.println("======================");
		System.out.println("È¸¿ø°¡ÀÔ");
		System.out.print("ÀÌ¸§ : ");
		String name = sc.nextLine();
		System.out.print("¾ÆÀÌµğ(5~14±ÛÀÚ ÀÌ³»): ");
		String id = sc.nextLine();
		System.out.print("ÁÖ¹Îµî·Ï¹øÈ£(ex.950101-1111111) : ");
		String ssn = sc.nextLine();
		System.out.print("ÀüÈ­¹øÈ£(ex.010-1234-5678) : ");
		String tel = sc.nextLine();
		System.out.print("°èÁÂ¹øÈ£ : ");
		String account = sc.nextLine();
		System.out.println();
		StudentBasic student = new StudentBasic(name, id, ssn, tel, account);

		boolean nameFail, idFail, ssnFail, telFail, idOverlap, ssnOverlap = true;
		
		if (nameFail = !nameCheck(student.getName())) {
			System.out.println("ÀÌ¸§Àº 2~6±ÛÀÚÀÇ ÇÑ±Û·Î ÀÔ·ÂÇØÁÖ¼¼¿ä.");
		}

		if (idFail = !idCheck(student.getId())) {
			System.out.println("¾ÆÀÌµğ´Â 5~15±ÛÀÚÀÇ ¿µ¾î, ¼ıÀÚ·Î ÀÔ·ÂÇØÁÖ¼¼¿ä.");
		}

		if (ssnFail = !ssnCheck(student.getSsn())) {
			System.out.println("À¯È¿ÇÏÁö ¾ÊÀº ÁÖ¹Î¹øÈ£ÀÔ´Ï´Ù.");
		}

		if (telFail = !telCheck(student.getTel())) {
			System.out.println("À¯È¿ÇÏÁö ¾ÊÀº ÀüÈ­¹øÈ£ÀÔ´Ï´Ù.");
		}
		
		if(checkIdOverlap(student.getId()) != 0) {
			System.out.println("ÀÌ¹Ì »ç¿ëÁßÀÎ ¾ÆÀÌµğ ÀÔ´Ï´Ù.");
			idOverlap = true;
		} else {
			idOverlap = false;
		}
		
		if(checkSsnOverlap(student.getSsn()) != 0) {
			System.out.println("ÀÌ¹Ì µî·ÏµÈ ÁÖ¹Î¹øÈ£ÀÔ´Ï´Ù.");
			ssnOverlap = true;
		} else {
			ssnOverlap = false;
		}
		
		

		if (nameFail == true || idFail == true || ssnFail == true || telFail == true || idOverlap == true || idOverlap == true || ssnOverlap == true) {
			System.out.println("µÚ·Î °¡½Ã·Á¸é 0¹øÀ», ´Ù½Ã Á¤º¸¸¦ ÀÔ·ÂÇÏ½Ã·Á¸é ¾Æ¹« Å°³ª ÀÔ·ÂÇØÁÖ¼¼¿ä.");
			String input = sc.nextLine();
			if (!input.equals("0")) {
				signInInput();
			}
		} else {
			
			try {
				
			} catch (Exception e) {
				System.out.println("StudentSignIn.signInInput()");
				e.printStackTrace();
			}
		}

	}

	// ÀüÈ­¹øÈ£ À¯È¿¼º°Ë»ç
	private boolean telCheck(String tel) {
		String pattern = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
		return tel.matches(pattern);
	}

	// ÁÖ¹Î¹øÈ£ À¯È¿¼º°Ë»ç
	private boolean ssnCheck(String ssn) {
		String pattern = "\\d{6}\\-[1-4]\\d{6}";
		return ssn.matches(pattern);
	}

	// id ±æÀÌ ¹× ¿µ¹® Ã¼Å© ¸Ş¼Òµå
	private boolean idCheck(String id) {
		String pattern = "^[a-z0-9]{5,15}$";
		return id.toLowerCase().matches(pattern);
	}

	// ÀÌ¸§ ±æÀÌ ¹× ÇÑ±Û Ã¼Å© ¸Ş¼Òµå
	private boolean nameCheck(String name) {
		String pattern = "^[°¡-ÆR]{2,6}$";
		return name.matches(pattern);
	}
	
	// id Áßº¹È®ÀÎ
	private int checkIdOverlap(String id) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {
			conn = util.open();
			// ¾ÆÀÌµğ Áßº¹È®ÀÎ
			String sql = "SELECT COUNT(*) AS CNT FROM TBL_STUDENT WHERE ID = ?";

			stat = conn.prepareStatement(sql);
			stat.setString(1, id);
			rs = stat.executeQuery();

			if (rs.next()) {
				return rs.getInt("CNT");
			}
		}catch (Exception e) {
			System.out.println("StudentSignIn.checkIdOverlap()");
			e.printStackTrace();
		}
		return 1;

	}
	
	// ÁÖ¹Î¹øÈ£ Áßº¹È®ÀÎ
	private int checkSsnOverlap(String ssn) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {
			conn = util.open();
			// ¾ÆÀÌµğ Áßº¹È®ÀÎ
			String sql = "SELECT COUNT(*) AS CNT FROM TBL_STUDENT WHERE SSN = ?";

			stat = conn.prepareStatement(sql);
			stat.setString(1, ssn);
			rs = stat.executeQuery();

			if (rs.next()) {
				return rs.getInt("CNT");
			}
		}catch (Exception e) {
			System.out.println("StudentSignIn.checkIdOverlap()");
			e.printStackTrace();
		}
		return 1;

	}

	


}
