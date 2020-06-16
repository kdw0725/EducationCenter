package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jdbc.DBUtil;
import teacher.TeacherBasic;

public class TeacherInfo {
	
	private List<TeacherBasic> list;
	

	public TeacherInfo() {
		this.list = new ArrayList<TeacherBasic>();
		listTeacher();
	}

	public void showMain(){
		Scanner scan = new Scanner(System.in);
		//교사정보조회 > 조회,수정,삭제 메뉴
		boolean loop = true;
		while(loop) {
			
			System.out.println();
			printTeacherList();
			
			System.out.println();
			System.out.println("0. 뒤로가기");
			System.out.print("교사 번호 선택 : ");
			String teaNum = scan.nextLine();
			String teaseq = null;
			
			if(teaNum.equals("0")) {
				break;
				//뒤로가기
			} else if(Integer.parseInt(teaNum) > 0 && Integer.parseInt(teaNum) <= list.size()) {
//				Connection conn = null;
//				Statement stat = null;
//				ResultSet rs = null;
//				DBUtil util = new DBUtil();
//				
//				int count2 = 0;
//	
//				try {
//					
//					conn = util.open();
//					stat = conn.createStatement();
//	
//					String sql = String.format("select * from vw_ShowTotalTeacher order by seq");
//					
//					rs = stat.executeQuery(sql);
//					
//					while(rs.next()) {
//						count2++;
//						if(teaNum.equals(String.format("%d", count2))) {
//							teaseq = rs.getString("seq");
//						}
//					}
//					
//					stat.close();
//					
//					System.out.println("");
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.println("Fail: Select vw_ShowTotalTeacher ");
//				}
			
				System.out.println("===========");
				System.out.println("1. 조회하기");
				System.out.println("2. 수정하기");
				System.out.println("3. 삭제하기");
				System.out.println("0. 뒤로가기");
				System.out.println("============");
				System.out.print("번호 입력 : ");
				String num = scan.nextLine();
				System.out.println();
				
				if (num.equals("0")) {
					continue;
					//뒤로가기
				} else if (num.equals("1")) {
					//조회하기
					ShowCourseSelectedTeacher(list.get(Integer.parseInt(teaNum)-1));
				} else if (num.equals("2")) {
					//수정하기
					UpdateSelectedTeacher(list.get(Integer.parseInt(teaNum)-1));
				} else if (num.equals("3")) {
					//삭제하기
					DeleteSelectedTeacher(teaseq);
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
					System.out.println();
				}
			} else {
				System.out.println();
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
	}

	private void DeleteSelectedTeacher(String teaNum) {
		Connection conn = null;
		Statement stat = null;
		CallableStatement callStat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan =  new Scanner(System.in);
		
		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("select name, tel, subject from vw_ShowTotalTeacher where seq = %s", teaNum);
			
			rs = stat.executeQuery(sql);
			
			System.out.println("==================================");
			String teachername = "";
			
			if (rs.next()) {
				teachername = rs.getString("name");
				System.out.printf("이름 : %s\r\n", teachername);
				System.out.printf("전화번호 : %s\r\n", rs.getString("tel"));
				System.out.printf("강의 가능 과목 : %s\r\n", rs.getString("subject"));
			}

			
			System.out.println("==================================");

			System.out.println();

			System.out.print("> "+ teachername + " 교사 정보를 삭제하시겠습니까?(y/n)");
			String delete = scan.nextLine();
			
			if (delete.toLowerCase().equals("y")) {
				
				sql = "{ call proc_DeleteCourse(?) }";
				
				conn = util.open();
				callStat = conn.prepareCall(sql);
				
				callStat.setString(1, teaNum);
				
				callStat.executeUpdate();
				
//				System.out.println("Success: Delete tbl_Teacher ");
				System.out.println("삭제를 완료했습니다.");
				
				stat.close();
				callStat.close();
				conn.close();
				
			} else if (delete.toLowerCase().equals("n")) {
				System.out.println("삭제를 중지합니다.");
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}
			System.out.println("이전 페이지로 돌아가겠습니다.");
			System.out.println("계속하시려면 엔터를 눌러주세요.");
			scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Delete tbl_Teacher ");
		}
	}//DeleteSelectedTeacher

	private void UpdateSelectedTeacher(TeacherBasic teacherBasic) {
		// 선택된 교사 정보 출력
		// 1. 전화번호 수정, 2. 강의 가능 과목 추가
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		boolean loop = true;

		while (loop) {

			try {

				conn = util.open();
				stat = conn.createStatement();

				String sql = String.format("select name, tel, subject from vw_ShowTotalTeacher where seq = %s",
						teacherBasic.getSeq());

				rs = stat.executeQuery(sql);

				System.out.println("==================================");

				if (rs.next()) {
					System.out.printf("이름 : %s\r\n", rs.getString("name"));
					System.out.printf("전화번호 : %s\r\n", rs.getString("tel"));
					System.out.printf("강의 가능 과목 : %s\r\n", rs.getString("subject"));
				}

				stat.close();
				conn.close();

				System.out.println("==================================");

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Fail: Select vw_ShowTotalTeacher ");
			}

			System.out.println("1. 전화번호 수정");
			System.out.println("2. 강의 가능 과목 추가");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String updateNum = scan.nextLine();

			if (updateNum.equals("0")) {
				// 뒤로가기
				loop = false;
			} else if (updateNum.equals("1")) {
				UpdateTelSelectedTeacher(teacherBasic);
			} else if (updateNum.equals("2")) {
				InsertAvailSubSelectedTeacher(teacherBasic);
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}

	}// UpdateSelectedTeacher

	private void InsertAvailSubSelectedTeacher(TeacherBasic teacherBasic) {
		//교사 강의 가능 과목 추가
				Connection conn = null;
				CallableStatement callStat = null;
				DBUtil util = new DBUtil();
				Scanner scan = new Scanner(System.in);

				
				ShowSubject();
				System.out.println();
				
				System.out.println("쉼표(,)를 이용하여 다중선택 가능. (예: 1,3,4,5)");
				System.out.print("추가할 강의 가능 과목 입력 : ");
				String subject = scan.nextLine();
				subject = subject.replace(" ", "");
				String[] subjectList = subject.split(",");
				
				try {
					
					for (int i = 0; i < subjectList.length; i++) {
						
						String sql = "{ call proc_AddAvailSubject(?,?) }";
						
						conn = util.open();
						callStat = conn.prepareCall(sql);
						
						callStat.setString(1, teacherBasic.getSeq()+"");
						callStat.setString(2, subjectList[i]);
						
						callStat.executeUpdate();
						
						callStat.close();
						conn.close();
						
					}
					System.out.println("===========================");
					System.out.println("강의 가능 과목이 추가되었습니다.");
					System.out.println("뒤로가려면 엔터를 눌러주세요.");
					scan.nextLine();
					
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Fail: Update tbl_teacher ");
				}
			}//InsertAvailSubSelectedTeacher

	private int ShowSubject() {
		//과목출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;

		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("Select seq, name, period from tbl_subject where delflag = 'Y' order by seq");
			
			rs = stat.executeQuery(sql);
			
			System.out.println("[No.]\t [기간]\t [과목명]");
			
			while(rs.next()) {
				System.out.printf("  %2s\t%3s주\t %s\r\n"
						, rs.getString("seq")
						, rs.getString("period")
						, rs.getString("name"));
				count++;
			}
			
			stat.close();
			conn.close();
			
//			System.out.println("Success: Select tbl_subject ");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select tbl_subject ");
		}
		return count;
	}//ShowSubject		

	private int SaveTotalSubject() {
		//과목출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;

		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("Select seq, name, period from tbl_subject where delflag = 'Y' order by seq");
			
			rs = stat.executeQuery(sql);
			
			System.out.println("[No.]\t [기간]\t [과목명]");
			
			while(rs.next()) {
				System.out.printf("  %2s\t%3s주\t %s\r\n"
						, rs.getString("seq")
						, rs.getString("period")
						, rs.getString("name"));
				count++;
			}
			
			stat.close();
			conn.close();
			
//				System.out.println("Success: Select tbl_subject ");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select tbl_subject ");
		}
		return count;
	}//ShowSubject		

	private void UpdateTelSelectedTeacher(TeacherBasic teacherBasic) {
		// 교사 전화번호 수정
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		System.out.println();
		System.out.print("수정할 전화번호 입력 : ");
		String tel = scan.nextLine();

		try {

			String sql = "{ call proc_UpdateTeacher(?,?) }";

			conn = util.open();
			stat = conn.prepareCall(sql);

			stat.setString(1, teacherBasic.getSeq()+"");
			stat.setString(2, tel);

			stat.executeUpdate();

//						System.out.println("Success: Update tbl_teacher ");

			stat.close();
			conn.close();

			System.out.println("===========================");
			System.out.println("수정 완료.");
			System.out.println("뒤로가려면 엔터를 눌러주세요.");
			scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Update tbl_teacher ");
		}

	}// UpdateTelSelectedTeacher


	private void ShowCourseSelectedTeacher(TeacherBasic teacherBasic) {
		// 선택된 교사 정보 출력
		// 과정출력,선택 > 과목까지 세부정보 출력
		Connection conn = null;
		Statement stat = null;
		Statement statSub = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		Scanner scan = new Scanner(System.in);
		DBUtil util = new DBUtil();
		String course = null;
		int count = 0;
		try {

			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("select distinct OCSeq, teachername, namecourse, termcourse, roomnum, status "
					+ "from vw_ShowTeacherInfo where tseq = %s order by termcourse", teacherBasic.getSeq());

			rs = stat.executeQuery(sql);

			System.out.println("[No.]\t[이름]\t[강의진행여부]\t[강의실]\t[과정기간]\t\t[과정명]");
			System.out.println("=====================================================================================================");
			while (rs.next()) {
				System.out.printf(" %2s\t%s\t  %4s\t%s 강의실\t%s\t%s\r\n", ++count, rs.getString("teachername"),
						rs.getString("status"), rs.getString("roomnum"), rs.getString("termcourse"),
						rs.getString("namecourse"));
				course = rs.getString("namecourse");
			}

			System.out.println("=====================================================================================================");
			System.out.println("0. 뒤로가기");
			System.out.print("과정 번호 선택 : ");
			String ocNum = scan.nextLine();

//					System.out.println("Success: Select vw_ShowTotalTeacher ");

			if (ocNum.equals("0")) {
				// 뒤로가기
			} else if (Integer.parseInt(ocNum) > 0 && Integer.parseInt(ocNum) <= count) {
				count = 0;

				sql = String.format("select distinct OCSeq, teachername, namecourse, termcourse, roomnum, status "
						+ "from vw_ShowTeacherInfo where tseq = %s order by termcourse", teacherBasic.getSeq());

				rs = stat.executeQuery(sql);

				while (rs.next()) {
					// 배정된 과정이 없으면 과목출력하지 않고 첫화면으로 돌아간다.
					if (course == null) {
						System.out.println();
						System.out.println("배정된 과정이 없습니다. 이전 메뉴로 돌아가겠습니다.");
						break;
					}
					count++;
					String OCSeq = rs.getString("OCSeq");
//							System.out.println(count);
//							System.out.println(OCSeq);
					if (ocNum.equals(String.format("%d", count))) {

						statSub = conn.createStatement();
						String sqlSub = String.format(
								"select termsubject, namesubject, bookname from vw_ShowTeacherInfo "
										+ "where tseq = %s and OCSeq = %s order by termsubject",
								teacherBasic.getSeq(), OCSeq);
						rsSub = statSub.executeQuery(sqlSub);

						System.out.println();
						System.out.println("==================================================================================================");
						System.out.println("[과목명]\t\t\t\t[과목기간]\t\t\t\t[교재명]");

						while (rsSub.next()) {
							System.out.printf("%-16s\t%s\t%s\r\n", rsSub.getString("namesubject"),
									rsSub.getString("termsubject"), rsSub.getString("bookname"));
						}

						System.out.println("==================================================================================================");

						rsSub.close();
						statSub.close();

//							} else {
//								System.out.println("검색결과가 없습니다.");
					}

				}
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}

			rs.close();
			stat.close();
			conn.close();

			System.out.println("뒤로 가려면 엔터를 눌러주세요.");
			scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select vw_ShowTotalTeacher ");
		}
	}// ShowCourseSelectedTeacher
	

	private void printTeacherList() {
		System.out.println("[No.]\t[이름]\t[주민번호]\t\t[전화번호]\t\t[강의가능과목]");
		System.out.println("=======================================================================================================================================================================================");

		for (int i = 0; i < list.size(); i++) {
			System.out.printf(" %2s\t%s\t %s\t%s\t%s\r\n"
					, String.format("%d", i + 1)
					, list.get(i).getName()
					, list.get(i).getSsn()
					, list.get(i).getTel()
					, list.get(i).getSubject() == null ? "강의가능 과목을 등록해 주시기 바랍니다." : list.get(i).getSubject());
		}
		System.out.println("=======================================================================================================================================================================================");

	}

	private void listTeacher() {
		//교사전체출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int count = 0;

		try {
			
			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("select * from vw_ShowTotalTeacher order by seq");
			
			rs = stat.executeQuery(sql);
			
			
			while(rs.next()) {
				TeacherBasic teacher = new TeacherBasic(
						rs.getInt("SEQ"), 
						rs.getString("NAME"), 
						rs.getString("ssn"), 
						rs.getString("TEL"), 
						rs.getString("SUBJECT") == null? null :rs.getString("SUBJECT").split(", ")
						);
				list.add(teacher);
			}
			
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select vw_ShowTotalTeacher ");
		}
	}
	
}
