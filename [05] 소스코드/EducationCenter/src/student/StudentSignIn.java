package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Pattern;

import jdbc.DBUtil;

/**
 * 
 * @author 김동욱 학생 회원가입을 위한 클래스
 */
public class StudentSignIn {

	public void signInInput() {
		Scanner sc = new Scanner(System.in);

		System.out.println("======================");
		System.out.println("회원가입");
		System.out.print("이름 : ");
		String name = sc.nextLine();
		System.out.print("아이디(5~14글자 이내): ");
		String id = sc.nextLine();
		System.out.print("주민등록번호(ex.950101-1111111) : ");
		String ssn = sc.nextLine();
		System.out.print("전화번호(ex.010-1234-5678) : ");
		String tel = sc.nextLine();
		System.out.print("계좌번호 : ");
		String account = sc.nextLine();
		System.out.println();
		StudentBasic student = new StudentBasic(name, id, ssn, tel, account);

		boolean nameFail, idFail, ssnFail, telFail, idOverlap, ssnOverlap = true;
		
		if (nameFail = !nameCheck(student.getName())) {
			System.out.println("이름은 2~6글자의 한글로 입력해주세요.");
		}

		if (idFail = !idCheck(student.getId())) {
			System.out.println("아이디는 5~15글자의 영어, 숫자로 입력해주세요.");
		}

		if (ssnFail = !ssnCheck(student.getSsn())) {
			System.out.println("유효하지 않은 주민번호입니다.");
		}

		if (telFail = !telCheck(student.getTel())) {
			System.out.println("유효하지 않은 전화번호입니다.");
		}
		
		if(checkIdOverlap(student.getId()) != 0) {
			System.out.println("이미 사용중인 아이디 입니다.");
			idOverlap = true;
		} else {
			idOverlap = false;
		}
		
		if(checkSsnOverlap(student.getSsn()) != 0) {
			System.out.println("이미 등록된 주민번호입니다.");
			ssnOverlap = true;
		} else {
			ssnOverlap = false;
		}
		
		

		if (nameFail == true || idFail == true || ssnFail == true || telFail == true || idOverlap == true || idOverlap == true || ssnOverlap == true) {
			System.out.println("뒤로 가시려면 0번을, 다시 정보를 입력하시려면 아무 키나 입력해주세요.");
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

	// 전화번호 유효성검사
	private boolean telCheck(String tel) {
		String pattern = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
		return tel.matches(pattern);
	}

	// 주민번호 유효성검사
	private boolean ssnCheck(String ssn) {
		String pattern = "\\d{6}\\-[1-4]\\d{6}";
		return ssn.matches(pattern);
	}

	// id 길이 및 영문 체크 메소드
	private boolean idCheck(String id) {
		String pattern = "^[a-z0-9]{5,15}$";
		return id.toLowerCase().matches(pattern);
	}

	// 이름 길이 및 한글 체크 메소드
	private boolean nameCheck(String name) {
		String pattern = "^[가-힣]{2,6}$";
		return name.matches(pattern);
	}
	
	// id 중복확인
	private int checkIdOverlap(String id) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {
			conn = util.open();
			// 아이디 중복확인
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
	
	// 주민번호 중복확인
	private int checkSsnOverlap(String ssn) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {
			conn = util.open();
			// 아이디 중복확인
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
