package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.internal.OracleTypes;

public class TeacherMain {

	/**
	 * 교사 메인 메소드
	 * 백지현
	 * @param teaNum 교사SEQ
	 */
	public void teacherMain(String teaNum) {

		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<String>();

		try {

			conn = util.open();
			// 로그인 시 해당 교사의 현재 진행중 or 제일 마지막으로 진행했던 과정 보여주는 쿼리
			String sql = String.format("SELECT A.* "
					+ "FROM (SELECT DISTINCT TEACHERNAME,NAMECOURSE, TERMCOURSE, STATUS " + "FROM VW_SHOWTEACHERINFO "
					+ "WHERE TSEQ = %s ORDER BY TERMCOURSE DESC) A WHERE ROWNUM = 1", teaNum);

			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			int checkcourse = 1;
			if (rs.next()) {
				// 배열에 담기
				list.add(rs.getString("TEACHERNAME") + "님 안녕하세요.\n");
				list.add("과정명 : " + rs.getString("NAMECOURSE") + "\n");
				list.add("과정기간 : " + rs.getString("TERMCOURSE") + "\n");
				list.add("진행상황: " + rs.getString("STATUS") + "\n");
				// 진행중이거나 진행하였던 개설과정이 없을 경우 
				if (rs.getString("NAMECOURSE") == null) {					
					checkcourse = 0;
				}
			}
			rs.close();
			Boolean loop = true;
			while (loop) {

				System.out.println("=====================================================");
				for (int i = 0; i < list.size(); i++) {
					// checkcourse값이 1일 경우에만 배열 출력
					if (checkcourse == 1) {
						System.out.println(list.get(i));
					} else {
						// checkcourse값이 1이 아닐경우 진행중이거나 진행하였던 강의존재 x 배열출력하지 않고 문구 출력
						System.out.println("진행중인 혹은 진행하였던 강의가 존재하지 않습니다.");
						break;
					}
						
						
				}
				System.out.println("=====================================================");

				System.out.print("1. 출결관리\t");
				System.out.print("2. 강의스케쥴 조회\t");
				System.out.print("3. 배점관리\t");
				System.out.print("4. 성적 관리\t");
				System.out.println();
				System.out.print("5. 학생 관리\t");
				System.out.print("6. 평가 관리\t");
				System.out.print("7. 체온 조회\t");
				System.out.println("0. 뒤로가기\t");

				System.out.println();
				System.out.print("입력 : ");
				String selectNum = scan.nextLine();

				if (selectNum.equals("1")) {
					// 출결관리 메소드 -- 희수씨
					TeacherAttend teacherAttend = new TeacherAttend();
					teacherAttend.teacherAttendMain(teaNum);
				} else if (selectNum.equals("2")) {
					// 강의스케쥴 조회 메소드
					TeacherClass tc = new TeacherClass();
					tc.TeacherClassSchedule(teaNum);
				} else if (selectNum.equals("3")) {
					// 배점관리 메소드-- 희수씨
					TeacherPoint tp = new TeacherPoint();
					tp.pointCourseVw(teaNum);
				} else if (selectNum.equals("4")) {
					// 성적 관리 메소드-- 희수씨
					TeacherScore teacherScore = new TeacherScore();
					teacherScore.teacher_scorecheck(Integer.parseInt(teaNum));
				} else if (selectNum.equals("5")) {
					// 학생 관리 메소드-- 희수씨
					TeacherChulSuk teacherChulSuk = new TeacherChulSuk();
					teacherChulSuk.teacher_studentcheck(Integer.parseInt(teaNum));
				} else if (selectNum.equals("6")) {
					TeacherTestScore ts = new TeacherTestScore();
					ts.TeacherScoreCourseVw(teaNum);
				} else if (selectNum.equals("7")) {
					// 체온 조회 메소드
					TeacherCovid tc = new TeacherCovid();
					tc.seeTeacherCovid(teaNum);
				} else if (selectNum.equals("0")) {
					return;
				} else {
					System.out.println("올바른 번호를 입력해 주세요.");
					continue;
				}
			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 교사 로그인 메소드
	 * 백지현
	 */
	public void teacherLogin() {

		Connection conn = null;
		CallableStatement stat = null;
		Scanner scan = new Scanner(System.in);
		DBUtil util = new DBUtil();
	
		try {
			// 로그인 시ID,PW 입력
			System.out.print("아이디 : ");
			String id = scan.nextLine().toLowerCase();
			System.out.print("패스워드 : ");
			String pw = scan.nextLine();

			// PROJECT DB 연결
			conn = util.open();

			// TEACHER_LOGIN 프로시저 호출 ID,PW,유효성검사결과값,학생번호전달
			String sql = "{call PROC_TEACHER_LOGIN(?,?,?,?)}";
			stat = conn.prepareCall(sql);

			// LOGIN 프로시저에 ID,PW값 삽입
			stat.setString(1, id);
			stat.setString(2, pw);
			stat.registerOutParameter(3, OracleTypes.NUMBER);
			stat.registerOutParameter(4, OracleTypes.NUMBER);

			stat.executeQuery();

			
			// 유효성검사 값이 1 > 로그인 성공
			if (stat.getInt(3) == 1) {
				System.out.println("로그인 성공");
				// teacherMain 메소드로 teaSeq 전달
				teacherMain(stat.getString(4));

				// 유효성검사 1 x> 로그인 실패
			} else {
				System.out.println("계정 정보를 확인바랍니다.");
			}

			
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("TeacherMain_teacherLogin()");
			e.printStackTrace();
		}
	}
};