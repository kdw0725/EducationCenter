package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 기초정보 관리 관련 클래스
 * @author 신수진
 *
 */
public class BasicControl {
	
	static Scanner scan = new Scanner(System.in);
	static List<String> subList = new ArrayList<String>();
	static List<String> bookList = new ArrayList<String>();


	/**
	 * 기초 정보 관리
	 */
	public void basicDataMenu() {
		//기초 정보 관리
		
		while (true) {
			
			System.out.println("==========================");
			System.out.println("1. 과정 관리");
			System.out.println("2. 과목 관리");
			System.out.println("3. 교재 관리");
			System.out.println("4. 강의실 관리");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String num = scan.nextLine();
			System.out.println();
			
			if (num.equals("1")) {
				//과정관리
				CourseManagement();
			} else if (num.equals("2")) {
				//과목관리
				SubjectManagement();
			} else if (num.equals("3")) {
				//교재관리
				BookManagement();
			} else if (num.equals("4")) {
				//강의실관리
				RoomManagement();
			} else if (num.equals("0")) {
				//뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
		
	}//BasicDataMenu

	private void RoomManagement() {
		//강의실관리
		while (true) {
			Connection conn = null;
			CallableStatement stat = null;
			Statement stat2 = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			
			int count = 0;
			String seq = null;

			try {

				conn = util.open();
				count = ShowClassroom();
				System.out.println();
				
				System.out.println("0. 뒤로가기");
				System.out.print("조회할 강의실 번호 입력 :");
				String num = scan.nextLine();
				System.out.println();

				if(num.equals("0")) {
					System.out.println("이전 페이지로 돌아갑니다.");
					System.out.println("계속 하시려면 엔터를 눌러주세요.");
					scan.nextLine();
					break;
				} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {

					int count2 = 0;
					stat2 = conn.createStatement();
					String sql = String.format("Select seq, maximum from tbl_Classroom where delflag = 'Y' order by seq");
					rs = stat2.executeQuery(sql);

					while(rs.next()) {
						count2++;
						if(count2 == Integer.parseInt(num)) {
							seq = rs.getString("seq");
						}
					}
					stat2.close();
					
					System.out.println("===========================");
					
					stat2 = conn.createStatement();
					sql = String.format("select distinct teachername, nameCourse, termCourse, roomNum "
							+ "from vw_ShowTeacherInfo where status = '강의중' and roomnum = %s", seq);
					rs = stat2.executeQuery(sql);

					if (rs.next()) {
						System.out.printf("[%s 강의실]\n"
								+ "진행과정명 : %s\n"
								+ "진행과정기간 : %s\n"
								+ "담당교사 : %s\n"
								, rs.getString("roomNum")
								, rs.getString("nameCourse")
								, rs.getString("termCourse")
								, rs.getString("teachername"));
					}
					stat2.close();
					System.out.println("===========================");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}//RoomManagement

	private void BookManagement() {
		//교재관리
		while (true) {
			System.out.println("==========================");
			System.out.println("1. 교재 등록");
			System.out.println("2. 교재 수정");
			System.out.println("3. 교재 삭제");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String num = scan.nextLine();
			System.out.println();
			
			if (num.equals("1")) {
				//교재 등록
				AddBook();
			} else if (num.equals("2")) {
				//교재 수정
				UpdateBook();
			} else if (num.equals("3")) {
				//교재 삭제
				DeleteBook();
			} else if (num.equals("0")) {
				//뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
	}//BookManagement

	private void SubjectManagement() {
		//과목관리
		while (true) {
			System.out.println("==========================");
			System.out.println("1. 과목 등록");
			System.out.println("2. 과목 수정");
			System.out.println("3. 과목 삭제");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String num = scan.nextLine();
			System.out.println();
			
			if (num.equals("1")) {
				//과목 등록
				AddSubject();
			} else if (num.equals("2")) {
				//과목 수정
				UpdateSubject();
			} else if (num.equals("3")) {
				//과목 삭제
				DeleteSubject();
			} else if (num.equals("0")) {
				//뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
	}//SubjectManagement

	private void CourseManagement() {
		//과정관리
		while (true) {
			System.out.println("==========================");
			System.out.println("1. 과정 등록");
			System.out.println("2. 과정 수정");
			System.out.println("3. 과정 삭제");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String num = scan.nextLine();
			System.out.println();
			
			if (num.equals("1")) {
				//과정 등록
				AddCourse();
			} else if (num.equals("2")) {
				//과정 수정
				UpdateCourse();
			} else if (num.equals("3")) {
				//과정 삭제
				DeleteCourse();
			} else if (num.equals("0")) {
				//뒤로가기
				break;
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
	}//CourseManagement

	private void DeleteBook() {
		//교재삭제
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		String seq = null;
		int count = 0;
		
		try {

			conn = util.open();
			count = ShowBook();
			System.out.println("[No.]\t [과목명]\t\t [출판사]\t\t [교재명]");
			Pagingfile.page(Pagingfile.save(bookList));
			System.out.println();
			
			System.out.println("0. 뒤로가기");
			System.out.print("삭제할 교재 번호 입력:");
			String num = scan.nextLine();
			System.out.println();
			
			if(num.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
			} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {

				int count2 = 0;
				stat2 = conn.createStatement();
				String sql = String.format("select b.seq seq, b.name bname, b.publisher publ, s.name sname from tbl_book b\r\n" + 
						"inner join tbl_subject s on b.subseq = s.seq\r\n" + 
						"where b.delflag = 'Y' and s.delflag = 'Y' order by b.seq");
				rs = stat2.executeQuery(sql);

				while(rs.next()) {
					count2++;
					if(count2 == Integer.parseInt(num)) {
						seq = rs.getString("seq");
					}
				}
				stat2.close();
				
				System.out.print(">"+ seq + "번 교재를 삭제하시겠습니까?(y/n)");
				String delete = scan.nextLine();
				
				if (delete.toLowerCase().equals("y")) {
					
					sql = "{ call proc_DeleteBook(?) }";
					
					stat = conn.prepareCall(sql);
					
					stat.setString(1, seq);
					
					stat.executeUpdate();
					
					stat.close();
					conn.close();

					System.out.println("===============================");
					System.out.println("교재 삭제가 완료되었습니다.");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
					
				} else if (delete.toLowerCase().equals("n")) {
					System.out.println("삭제를 중지합니다. 이전 페이지로 돌아갑니다.");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				}
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Delete tbl_book ");
		}
	}//DeleteBook

	private void DeleteClassroom() {
		//강의실삭제
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		int count = 0;
		
		try {
			
			count = ShowClassroom();
			System.out.println("0. 뒤로가기");
			System.out.print("삭제할 강의실 번호 입력:");
			String seq = scan.nextLine();
			System.out.println();
			
			if(seq.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
			} else if (Integer.parseInt(seq) > 0 && Integer.parseInt(seq) <= count) {
				
				System.out.print(">"+ seq + "번 강의실을 삭제하시겠습니까?(y/n)");
				String delete = scan.nextLine();
				
				if (delete.toLowerCase().equals("y")) {
					
					String sql = "{ call proc_DeleteClassroom(?) }";
					
					conn = util.open();
					stat = conn.prepareCall(sql);
					
					stat.setString(1, seq);
					
					stat.executeUpdate();
					
					System.out.println("Success: Delete tbl_classroom ");
					
					stat.close();
					conn.close();
					
				} else if (delete.toLowerCase().equals("n")) {
					System.out.println("삭제를 중지합니다. 이전 페이지로 돌아갑니다.");
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
				}
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Delete tbl_classroom ");
		}
	}//DeleteClassroom

	private void DeleteSubject() {
		//과목삭제
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		String seq = null;
		int count = 0;
		
		try {

			conn = util.open();
			count = ShowSubject();
			System.out.println("[No.]\t [기간]\t [과목명]");
			Pagingfile.page(Pagingfile.save(subList));
			System.out.println();
			
			System.out.println("0. 뒤로가기");
			System.out.print("삭제할 과목 번호 입력:");
			String num = scan.nextLine();
			System.out.println();
			
			if(num.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {

				int count2 = 0;
				stat2 = conn.createStatement();
				String sql = String.format("Select seq, name, period from tbl_subject where delflag = 'Y' order by seq");
				rs = stat2.executeQuery(sql);

				while(rs.next()) {
					count2++;
					if(count2 == Integer.parseInt(num)) {
						seq = rs.getString("seq");
					}
				}
				stat2.close();
				
				System.out.print(">"+ seq + "번 과목을 삭제하시겠습니까?(y/n)");
				String delete = scan.nextLine();
				
				if (delete.toLowerCase().equals("y")) {
					
					sql = "{ call proc_DeleteSubject(?) }";
					
					stat = conn.prepareCall(sql);
					
					stat.setString(1, seq);
					
					stat.executeUpdate();
					
					stat.close();
					conn.close();

					System.out.println("===============================");
					System.out.println("과목 삭제가 완료되었습니다.");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
					
				} else if (delete.toLowerCase().equals("n")) {
					System.out.println("삭제를 중지합니다. 이전 페이지로 돌아갑니다.");
					System.out.println("계속 하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
					System.out.println("계속 하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				}
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Delete tbl_Subject ");
		}
	}//DeleteSubject

	private void DeleteCourse() {
		//과정삭제
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		String seq = null;
		int count = 0;
		
		try {

			conn = util.open();
			count = ShowCourse();
			System.out.println("0. 뒤로가기");
			System.out.print("삭제할 과정 번호 입력:");
			String num = scan.nextLine();
			System.out.println();
			
			if(num.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {
				
				int count2 = 0;
				stat2 = conn.createStatement();
				String sql = String.format("Select seq, name, period from tbl_course where delflag = 'Y' order by seq");
				rs = stat2.executeQuery(sql);

				while(rs.next()) {
					count2++;
					if(count2 == Integer.parseInt(num)) {
						seq = rs.getString("seq");
					}
				}
				stat2.close();
				
				System.out.print(">"+ num + "번 과정을 삭제하시겠습니까?(y/n)");
				String delete = scan.nextLine();
				
				if (delete.toLowerCase().equals("y")) {
					
					sql = "{ call proc_DeleteCourse(?) }";
					
					stat = conn.prepareCall(sql);
					
					stat.setString(1, seq);
					
					stat.executeUpdate();
					
					stat.close();
					conn.close();
					
					System.out.println("===============================");
					System.out.println("과정 삭제가 완료되었습니다.");
					System.out.println("계속하시려면 엔터를 눌러주세요.");
					scan.nextLine();
					
				} else if (delete.toLowerCase().equals("n")) {
					System.out.println("삭제를 중지합니다. 이전 페이지로 돌아갑니다.");
					System.out.println("계속 하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
					System.out.println("계속 하시려면 엔터를 눌러주세요.");
					scan.nextLine();
				}
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Delete tbl_Course ");
		}
	}//DeleteCourse

	private void UpdateBook() {
		//교재수정
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;
		String seq = null;

		try {

			conn = util.open();
			count = ShowBook();
			System.out.println("[No.]\t [과목명]\t\t [출판사]\t\t [교재명]");
			Pagingfile.page(Pagingfile.save(bookList));
			System.out.println();
			
			System.out.println("0. 뒤로가기");
			System.out.print("수정할 교재 번호 입력:");
			String num = scan.nextLine();
			System.out.println();

			if(num.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {

				int count2 = 0;
				stat2 = conn.createStatement();
				String sql = String.format("select b.seq seq, b.name bname, b.publisher publ, s.name sname from tbl_book b\r\n" + 
						"inner join tbl_subject s on b.subseq = s.seq\r\n" + 
						"where b.delflag = 'Y' and s.delflag = 'Y' order by b.seq");
				rs = stat2.executeQuery(sql);

				while(rs.next()) {
					count2++;
					if(count2 == Integer.parseInt(num)) {
						seq = rs.getString("seq");
					}
				}
				stat2.close();
				
				System.out.println("===========================");
				System.out.println("수정할 내용을 입력하세요.");
				System.out.print("교재 이름: ");
				String name = scan.nextLine();
				
				System.out.print("출판사 이름: ");
				String publisher = scan.nextLine();			
				
				sql = "{ call proc_UpdateBook(?,?,?) }";
				
				stat = conn.prepareCall(sql);
				
				stat.setString(1, seq);
				stat.setString(2, name);
				stat.setString(3, publisher);
				
				stat.executeUpdate();
				
				stat.close();
				conn.close();

				System.out.println("===============================");
				System.out.println("교재 수정이 완료되었습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
				
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Update tbl_book ");
		}
	}//UpdateBook

	private void UpdateClassroom() {
		//강의실수정
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		int count = 0;
		
		try {
			
			count = ShowClassroom();
			System.out.println("0. 뒤로가기");
			System.out.print("수정할 강의실 번호 입력:");
			String seq = scan.nextLine();
			System.out.println();

			if(seq.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
			} else if (Integer.parseInt(seq) > 0 && Integer.parseInt(seq) <= count) {
				
				System.out.println("===========================");
				System.out.println("수정할 내용을 입력하세요.");
				System.out.print("강의실 수용 인원: ");
				String maximum = scan.nextLine();
				
				String sql = "{ call proc_UpdateClassroom(?,?) }";
				
				conn = util.open();
				stat = conn.prepareCall(sql);
				
				stat.setString(1, maximum);
				stat.setString(2, seq);
				
				stat.executeUpdate();
				
				System.out.println("Success: Update tbl_classroom ");
				
				stat.close();
				conn.close();
				
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Update tbl_classroom ");
		}
	}//UpdateClassroom

	private void UpdateSubject() {
		//과목수정
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;
		String seq = null;

		try {

			conn = util.open();
			count = ShowSubject();
			System.out.println("[No.]\t [기간]\t [과목명]");
			Pagingfile.page(Pagingfile.save(subList));
			System.out.println();
			
			System.out.println("0. 뒤로가기");
			System.out.print("수정할 과목 번호 입력:");
			String num = scan.nextLine();
			System.out.println();

			if(num.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {

				int count2 = 0;
				stat2 = conn.createStatement();
				String sql = String.format("Select seq, name, period from tbl_subject where delflag = 'Y' order by seq");
				rs = stat2.executeQuery(sql);

				while(rs.next()) {
					count2++;
					if(count2 == Integer.parseInt(num)) {
						seq = rs.getString("seq");
					}
				}
				stat2.close();
				
				System.out.println("===========================");
				System.out.println("수정할 내용을 입력하세요.");
				System.out.print("과목 이름: ");
				String name = scan.nextLine();
				
				System.out.print("과목 기간: ");
				String period = scan.nextLine();
				
				sql = "{ call proc_UpdateSubject(?,?,?) }";
				
				stat = conn.prepareCall(sql);
				
				stat.setString(1, seq);
				stat.setString(2, name);
				stat.setString(3, period);
				
				stat.executeUpdate();
				
				stat.close();
				conn.close();

				System.out.println("===============================");
				System.out.println("과목 수정이 완료되었습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
				
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Update tbl_subject ");
			
		}
	}//UpdateSubject

	private void UpdateCourse() {
		//과정수정
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;
		String seq = null;
		
		try {

			conn = util.open();
			count = ShowCourse();
			System.out.println("0. 뒤로가기");
			System.out.print("수정할 과정 번호 입력:");
			String num = scan.nextLine();
			System.out.println();
			
			if(num.equals("0")) {
				System.out.println("이전 페이지로 돌아갑니다.");
				System.out.println("계속 하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			} else if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {
				
				int count2 = 0;
				stat2 = conn.createStatement();
				String sql = String.format("Select seq, name, period from tbl_course where delflag = 'Y' order by seq");
				rs = stat2.executeQuery(sql);

				while(rs.next()) {
					count2++;
					if(count2 == Integer.parseInt(num)) {
						seq = rs.getString("seq");
					}
				}
				stat2.close();
				
				System.out.println("===========================");
				System.out.println("수정할 내용을 입력하세요.");
				System.out.print("과정 이름: ");
				String name = scan.nextLine();
				
				System.out.print("과정 기간: ");
				String period = scan.nextLine();
				
				sql = "{ call proc_updc(?,?,?) }";
				
				stat = conn.prepareCall(sql);
				
				stat.setString(1, name);
				stat.setString(2, period);
				stat.setString(3, seq);
				
				stat.executeUpdate();
				stat.close();
				conn.close();
				
				System.out.println("===============================");
				System.out.println("과정 수정이 완료되었습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
				
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Update tbl_Course ");
		}
	}//UpdateCourse

	private int ShowBook() {
		//교재출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		bookList.clear();
		
		int count = 0;

		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("select b.seq seq, b.name bname, b.publisher publ, s.name sname from tbl_book b\r\n" + 
					"    inner join tbl_subject s on b.subseq = s.seq \r\n" + 
					"where b.delflag = 'Y' and s.delflag = 'Y' order by b.seq");
			
			rs = stat.executeQuery(sql);
			
			while(rs.next()) {
				
				String sname = rs.getString("sname");
				String publ = rs.getString("publ");

				bookList.add(String.format("%-16s,%-12s,%s"
						, sname
						, publ
						, rs.getString("bname")));
				count++;
			}
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select tbl_book inner join tbl_subject ");
		}
		return count;
	}//ShowBook

	private int ShowClassroom() {
		//강의실출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;

		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("Select seq, maximum from tbl_Classroom where delflag = 'Y' order by seq");
			
			rs = stat.executeQuery(sql);

			System.out.println("===============================");
			System.out.println(" [강의실]  [인원수]");
			
			while(rs.next()) {
				System.out.printf("%2s 강의실  %3s 명\r\n"
						, rs.getString("seq")
						, rs.getString("maximum"));
				count++;
			}
			
			stat.close();
			conn.close();

			System.out.println("===============================");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select tbl_Classroom ");
		}
		return count;
	}//ShowClassroom

	private int ShowSubject() {
		//과목출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		subList.clear();
		
		int count = 0;

		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("Select seq, name, period from tbl_subject where delflag = 'Y' order by seq");
			
			rs = stat.executeQuery(sql);

			while(rs.next()) {
				subList.add(String.format("%s주,%s"
						, rs.getString("period")
						, rs.getString("name")));
				count++;
			}
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select tbl_subject ");
		}
		return count;
	}//ShowSubject

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

			System.out.println("===============================");
			System.out.println("[No.]\t [기간]\t [과정명]");
			
			while(rs.next()) {
				System.out.printf("  %2s\t%3s개월\t %s\r\n"
						, ++count
						, rs.getString("period")
						, rs.getString("name"));
			}
			stat.close();
			conn.close();

			System.out.println("===============================");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select tbl_course ");
		}
		return count;
	}//ShowCourse

	private void AddBook() {
		//교재추가
		Connection conn = null;
		Statement stat = null;
		CallableStatement cstat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {

			conn = util.open();
			stat = conn.createStatement();
			int count = ShowSubject();
			System.out.println("[No.]\t [기간]\t [과목명]");
			Pagingfile.page(Pagingfile.save(subList));
			System.out.println();
			
			stat.close();
			
			System.out.print("과목 번호: ");
			String subseq = scan.nextLine();
			if (Integer.parseInt(subseq) > 0 && Integer.parseInt(subseq) <= count) {
				
				System.out.println("새 교재 입력");
				System.out.print("교재 이름: ");
				String name = scan.nextLine();
				
				System.out.print("교재 출판사: ");
				String publisher = scan.nextLine();
				
				String sql = "{ call proc_AddBook(?,?,?) }";
				
				cstat = conn.prepareCall(sql);
				
				cstat.setString(1, name);
				cstat.setString(2, publisher);
				cstat.setString(3, subseq);
				
				cstat.executeUpdate();
				
				cstat.close();
				conn.close();
				
				System.out.println("===============================");
				System.out.println("교재 등록이 완료되었습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
				
			} else {
				System.out.println("잘못된 번호를 입력하였습니다. 이전 메뉴로 돌아가겠습니다.");
				System.out.println("계속하시려면 엔터를 눌러주세요.");
				scan.nextLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_book ");
		}
		
	}//AddBook

	private void AddClassroom() {
		//강의실추가
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		try {

			System.out.println("새 과정 입력");
			System.out.print("최대 수용 인원: ");
			String maximum = scan.nextLine();
			
			String sql = "{ call proc_AddClassroom(?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setString(1, maximum);
			
			stat.executeUpdate();
			
			System.out.println("Success: Insert tbl_Classroom ");
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_Classroom ");
		}
	}//AddClassroom

	private void AddSubject() {
		//과목추가
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		try {

			System.out.println("새 과정 입력");
			System.out.print("과목 이름: ");
			String name = scan.nextLine();

			System.out.print("과목 기간: ");
			String period = scan.nextLine();
			
			String sql = "{ call proc_AddSubject(?,?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setString(1, name);
			stat.setString(2, period);
			
			stat.executeUpdate();
			
			stat.close();
			conn.close();

			System.out.println("===============================");
			System.out.println("과정 등록이 완료되었습니다.");
			System.out.println("계속하시려면 엔터를 눌러주세요.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_Subject ");
		}
	}

	private void AddCourse() {
		//과정추가
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		try {

			System.out.println("새 과정 입력");
			System.out.print("과정 이름: ");
			String name = scan.nextLine();

			System.out.print("과정 기간: ");
			String period = scan.nextLine();
			
			String sql = "{ call proc_AddC(?,?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setString(1, name);
			stat.setString(2, period);
			
			stat.executeUpdate();
			
			stat.close();
			conn.close();
			
			System.out.println("===============================");
			System.out.println("과정 등록이 완료되었습니다.");
			System.out.println("계속하시려면 엔터를 눌러주세요.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_Course ");
			
		}
	}//AddCourse

	private void AddAdmin() {
		//관리자추가
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		try {

			System.out.println("새 관리자 입력");
			System.out.print("관리자 ID: ");
			String id = scan.nextLine();

			System.out.print("관리자 PW: ");
			String pw = scan.nextLine();
			
			String sql = "{ call proc_AddAdmin(?,?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setString(1, id);
			stat.setString(2, pw);
			
			stat.executeUpdate();
			
			System.out.println("Success: Insert tbl_Admin");
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_Admin");
		}
	}//AddAdmin

	private void loginAdmin() {
		// 관리자로그인
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		try {
			
			System.out.println("관리자 로그인");
			System.out.print("관리자 ID: ");
			String id = scan.nextLine();

			System.out.print("관리자 PW: ");
			String pw = scan.nextLine();
			
			String sql = "{ call proc_loginadmin(?,?,?) }";
			
			conn = util.open();
			stat = conn.prepareCall(sql);
			
			stat.setString(1, id);
			stat.setString(2, pw);
			stat.registerOutParameter(3, OracleTypes.NUMBER);
			
			stat.executeUpdate();
			
			//1이면 로그인성공, 0이면 로그인실패
			int num = stat.getInt(3);
			
			if(num == 1) {
				System.out.println("로그인 성공");
				// 로그인 성공
			} else {
				System.out.println("로그인 실패");
				// 로그인 실패
			}
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//loginAdmin
	
}
