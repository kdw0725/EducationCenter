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
 * 
 * @author 신수진
 * 관리자 학생 관리 클래스
 */
public class AdminStudent {
	static Scanner scan = new Scanner(System.in);
	static List<String> stuList = new ArrayList<String>();
	
	/**
	 * 학생 관리 메인
	 */
	public void showMenu(){
		while(true) {
			System.out.println("====================");
			System.out.println("1. 교육생 등록");
			System.out.println("2. 교육생 조회");
			System.out.println("3. 취업 관리");
			System.out.println("4. 국비지원");
			System.out.println("0. 뒤로가기");
			System.out.println("====================");
			System.out.print("번호 입력 : ");
			String input = scan.nextLine();
			
			if(input.equals("0")) {
				break;
			} else if(input.equals("1")) {
				AddStudent();
			} else if(input.equals("2")) {
				StudentInfoMenu();
			} else if(input.equals("3")) {
				Employment_Pro employementPro = new Employment_Pro();
				employementPro.employment_pro();
			} else if(input.equals("4")) {
				Support support = new Support();
				support.supportmenu();
			} else {
				System.out.println("올바른 번호를 입력해주세요!");
				System.out.println();
				continue;
			}
		}
	}
	

	private void StudentInfoMenu() {
		//교육생전체 조회 (페이징) > 검색

		boolean loop = true;

		while(loop) {
			
			SaveTotalStudent();
			System.out.println("     [이름]    [주민번호]  [전화번호]\t[등록일]    [수강횟수]");
			Pagingfile.page(Pagingfile.save(stuList));

			System.out.println();
			System.out.println("학생 정보 관리");
			System.out.println("==========================");
			System.out.println("1. 번호로 검색");
			System.out.println("2. 아이디로 검색");
			System.out.println("3. 이름으로 검색");
			System.out.println("0. 뒤로가기");
			System.out.println("==========================");
			System.out.print("번호 입력 : ");
			String searchNum = scan.nextLine();
			System.out.println();
			
			if (searchNum.equals("0")) {
				break;
				//뒤로가기
			} else if (searchNum.equals("1")) {
				//학생 번호 입력 > 입력한 학생 정보 출력
				String stuNum = null;
				
				while(true) {
					System.out.println("==========================");
					System.out.print("학생 번호 입력 : ");
					stuNum = scan.nextLine();
					if(Integer.parseInt(stuNum) > 0 && Integer.parseInt(stuNum) <= stuList.size()) {
						ShowStuSelectedNum(stuNum);
						break;
					} else {
						System.out.println("잘못된 번호를 입력하였습니다.");
						System.out.println();
					}
				}
			} else if (searchNum.equals("2")) {
				//아이디로 검색
				SearchStuFromId();
			} else if (searchNum.equals("3")) {
				//이름으로 검색
				SearchStuFromName();
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
				System.out.println();
			}
		}
		
	}//StudentInfoMenu
	
	/**
	 * 수료 및 중도탈락 처리
	 * @param 학생 번호
	 */
	public void UpdateStuStatus(String stuseq) {
		//수료 및 중도탈락처리
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		try {

			conn = util.open();

			System.out.print("> 수료 및 중도 탈락 처리를 하시겠습니까?(y/n)");
			String status = scan.nextLine();
			
			if (status.toLowerCase().equals("y")) {
				
				String sql = "{ call proc_UpdateAppStatus(?) }";
				
				conn = util.open();
				stat = conn.prepareCall(sql);
				
				stat.setString(1, stuseq);
				
				stat.executeUpdate();
				
				System.out.println("수정을 완료했습니다.");
				
				stat.close();
				conn.close();
				
			} else if (status.toLowerCase().equals("n")) {
				System.out.println("수정을 중지합니다.");
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}
			System.out.println("이전 페이지로 돌아가겠습니다.");
			System.out.println("계속하시려면 엔터를 눌러주세요.");
			scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Delete proc_UpdateAppStatus ");
		}
	}//UpdateStuStatus
	
	/**
	 * 교육생 삭제
	 * @param 학생 번호
	 */
	public void DeleteSelectedStu(String stuseq) {
		//학생삭제
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		try {

			conn = util.open();

			System.out.print("> 학생 정보를 삭제하시겠습니까?(y/n)");
			String delete = scan.nextLine();
			
			if (delete.toLowerCase().equals("y")) {
				
				String sql = "{ call proc_DeleteStudent(?) }";
				
				conn = util.open();
				stat = conn.prepareCall(sql);
				
				stat.setString(1, stuseq);
				
				stat.executeUpdate();
				
				System.out.println("삭제를 완료했습니다.");
				
				stat.close();
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
			System.out.println("Fail: Delete proc_DeleteStudent ");
		}
	}//DeleteSelectedStu
	
	/**
	 * 학생 정보 수정
	 * @param 학생 번호
	 */
	public void UpdateSelectedStu(String stuseq) {
		//학생수정
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		System.out.println();
		System.out.print("수정할 전화번호 입력 : ");
		String tel = scan.nextLine();
		
		System.out.print("수정할 계좌번호 입력 : ");
		String acc = scan.nextLine();
		
		try {

				String sql = "{ call proc_UpdateStudent(?,?,?) }";
				
				conn = util.open();
				stat = conn.prepareCall(sql);
				
				stat.setString(1, stuseq);
				stat.setString(2, tel);
				stat.setString(3, acc);
				
				stat.executeUpdate();
				
				stat.close();
				conn.close();
				
				System.out.println("===========================");
				System.out.println("수정 완료.");
				System.out.println("뒤로가려면 엔터를 눌러주세요.");
				scan.nextLine();
				
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Update proc_UpdateStudent ");
		}
	}//UpdateSelectedStu

	/**
	 * 학생 이름으로 검색
	 */
	public void SearchStuFromName() {
		//이름으로 검색
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		DBUtil util = new DBUtil();
		while (true) {		
			System.out.println("0. 뒤로가기");
			System.out.print("검색 하고싶은 학생의 이름 입력 : ");
			String name = scan.nextLine();
	
			if (name.equals("0")) {
				//뒤로가기
				break;
			} else {
				//이름 검사
				try {
					while (true) {
						int count = 0;
						String stuseq = null;
						String stuid = null, stussn = null;
						String stutel = null;
						String stuacc = null;
		
						String sql = "{ call proc_SearchStudent_fromName(?,?) }";
						
						conn = util.open();
						stat = conn.prepareCall(sql);
						
						stat.setString(1, name);
						stat.registerOutParameter(2, OracleTypes.CURSOR);
						
						stat.executeUpdate();
						rs = (ResultSet)stat.getObject(2); 
		
						while (rs.next()) {
							System.out.println();
							System.out.println("==========================");
							System.out.println("[과정 정보]");
							System.out.println("[" + ++count + "번]");
							if(rs.getString("status").equals("진행과정없음")) {
								System.out.printf("이름 : %s\n"
										+ "현재 진행중인 과정이 없습니다.\n"
										, rs.getString("stuName"));
							} else {
								System.out.printf("이름 : %s\n"
										+ "수강 과정 이름 : %s\n"
										+ "수강 과정 기간 : %s\n"
										+ "강의실 : %s\n"
										+ "수료 및 중도탈락 여부 : %s\n"
										+ "수료 및 중도탈락 날짜 : %s\n"
										, rs.getString("stuName")
										, rs.getString("courseName")
										, rs.getString("startdate").split(" ")[0] + " ~ " + rs.getString("enddate").split(" ")[0]
										, rs.getString("room")
										, rs.getString("status")
										, rs.getString("statusdate"));
							}
							
							try {
								stat2 = conn.createStatement();
								String sql2 = String.format("select * from tbl_student order by seq");
								rs2 = stat2.executeQuery(sql2);
								
								while(rs2.next()) {
									if(name.equals(rs2.getString("name"))) {
										stuseq = rs2.getString("seq");
										stuid = rs2.getString("id");
										stussn = rs2.getString("ssn");
										stutel = rs2.getString("tel");
										stuacc = rs2.getString("account");
									}
								}
								stat2.close();	
								
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Fail: Select vw_ShowStudentInfo ");
							}
							
							System.out.println("");
							System.out.println("[개인 정보]");
							System.out.printf("아이디 : %s\n"
									+ "주민 등록 번호 : %s\n"
									+ "전화번호 : %s\n"
									+ "계좌번호 : %s\n"
									, stuid, stussn, stutel, stuacc);
						}
						
						stat.close();
						
						if (count == 0) {
							//일치하는 이름이 없는 경우
							System.out.println("해당 이름은 존재하지 않습니다.");
							System.out.println();
							break;
						} else {
							if (count == 1) {
								//검색결과 1명 일 때, 위의 stuseq 변수를 그대로 사용
							} else {
								//검색결과 N명 일 때, 원하는 번호의 학생에 대한 stuseq값을 받아온다.
								while (true) {
									//제대로된 번호 입력할 때 까지 반복하는 loop
									System.out.print("몇 번 학생의 정보를 관리하겠습니까? : ");
									String num = scan.nextLine();
									if (Integer.parseInt(num) > 0 && Integer.parseInt(num) <= count) {
										int countCheck = 0;
										try {
											stat2 = conn.createStatement();
											String sql2 = String.format("select * from tbl_student order by seq");
											rs2 = stat2.executeQuery(sql2);
											
											while(rs2.next()) {
												if(name.equals(rs2.getString("name"))) {
													//이름이 동일한 학생들을 나열했을 때
													countCheck++;
													if(countCheck == Integer.parseInt(num)) {
														//받아준 숫자 번 째의 학생 시퀀스를 받아온다.
														stuseq = rs2.getString("seq");
													}
												}
											}
											stat.close();	
											
										} catch (Exception e) {
											e.printStackTrace();
											System.out.println("Fail: Select vw_ShowStudentInfo ");
										}
										break;
									} else {
										System.out.println("잘못된 번호를 입력하였습니다.");
										System.out.println();
									}
								}
							}
							//검색결과가 한 명 이상일 떄,
							//정보 수정, 삭제가 가능한 메뉴 출력
							System.out.println("==========================");
							System.out.println("1. 수정하기");
							System.out.println("2. 수료 및 중도탈락 처리");
							System.out.println("3. 삭제하기");
							System.out.println("0. 뒤로가기");
							System.out.println("==========================");
							System.out.print("번호 입력 : ");
							String infoNum = scan.nextLine();
							System.out.println();
							
							if (infoNum.equals("0")) {
								// 뒤로가기
								break;
							} else if (infoNum.equals("1")) {
								// 수정하기
								UpdateSelectedStu(stuseq);
							} else if (infoNum.equals("2")) {
								// 수료 및 중도탈락 처리
								UpdateStuStatus(stuseq);
							} else if (infoNum.equals("3")) {
								// 삭제하기
								DeleteSelectedStu(stuseq);
							} else {
								System.out.println("잘못된 번호를 입력하였습니다.");
								System.out.println();
							}
						}
					}
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Fail: proc_SearchStudent_fromName");
				}
			}
		}
	}//SearchStuFromName

	/**
	 * 아이디로 검색
	 */
	public void SearchStuFromId() {
		//아이디로 검색
		Connection conn = null;
		CallableStatement stat = null;
		Statement stat2 = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		while (true) {		
			System.out.println("0. 뒤로가기");
			System.out.print("검색 하고싶은 학생의 ID 입력 : ");
			String id = scan.nextLine();
	
			if (id.equals("0")) {
				//뒤로가기
				break;
			} else {
				//id 검사
				while (true) {
					
					String stuseq = null;
					String stuid = null, stussn = null;
					String stutel = null;
					String stuacc = null;
					
					try {
						conn = util.open();
						stat2 = conn.createStatement();
			
						String sql = String.format("select * from tbl_student order by seq");
						
						rs = stat2.executeQuery(sql);
						
						while(rs.next()) {
							if(id.equals(rs.getString("id"))) {
								stuseq = rs.getString("seq");
								stuid = rs.getString("id");
								stussn = rs.getString("ssn");
								stutel = rs.getString("tel");
								stuacc = rs.getString("account");
							}
						}
						
						stat2.close();
						System.out.println();
						
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Fail: Select vw_ShowStudentInfo ");
					}
					
					if(stuid != null) {
					//일치하는 id가 있는 경우
						try {
							String sql = "{ call proc_SearchStudent_fromID(?,?,?,?,?,?,?,?) }";
							
							conn = util.open();
							stat = conn.prepareCall(sql);
							
							stat.setString(1, id);
							stat.registerOutParameter(2, OracleTypes.VARCHAR);
							stat.registerOutParameter(3, OracleTypes.VARCHAR);
							stat.registerOutParameter(4, OracleTypes.DATE);
							stat.registerOutParameter(5, OracleTypes.DATE);
							stat.registerOutParameter(6, OracleTypes.NUMBER);
							stat.registerOutParameter(7, OracleTypes.VARCHAR);
							stat.registerOutParameter(8, OracleTypes.DATE);
							
							stat.executeUpdate();
	
							System.out.println("[과정 정보]");
							if(stat.getString(7).equals("진행과정없음")) {
								//수강중이 아닌 사람
								System.out.printf("이름 : %s\n"
										+ "현재 진행중인 과정이 없습니다.\n"
										, stat.getString(2));
							} else {
								//수강중 혹은 수료한 사람
								System.out.printf("이름 : %s\n"
										+ "수강 과정 이름 : %s\n"
										+ "수강 과정 기간 : %s\n"
										+ "강의실 : %s\n"
										+ "수료 및 중도탈락 여부 : %s\n"
										+ "수료 및 중도탈락 날짜 : %s\n"
										, stat.getString(2)
										, stat.getString(3)
										, stat.getString(4).split(" ")[0] + " ~ " + stat.getString(5).split(" ")[0]
												, stat.getString(6)
												, stat.getString(7)
												, stat.getString(8));
							}
							//가입된 모든 학생들의 개인 정보 출력
							System.out.println("==========================");
							System.out.println("[개인 정보]");
							System.out.printf("아이디 : %s\n"
									+ "주민 등록 번호 : %s\n"
									+ "전화번호 : %s\n"
									+ "계좌번호 : %s\n"
									, stuid, stussn, stutel, stuacc);
							
							stat.close();
							conn.close();
							
							System.out.println("==========================");
							System.out.println("1. 수정하기");
							System.out.println("2. 수료 및 중도탈락 처리");
							System.out.println("3. 삭제하기");
							System.out.println("0. 뒤로가기");
							System.out.println("==========================");
							System.out.print("번호 입력 : ");
							String infoNum = scan.nextLine();
							System.out.println();
							
							if (infoNum.equals("0")) {
								// 뒤로가기
								break;
							} else if (infoNum.equals("1")) {
								// 수정하기
								UpdateSelectedStu(stuseq);
							} else if (infoNum.equals("2")) {
								// 수료 및 중도탈락 처리
								UpdateStuStatus(stuseq);
							} else if (infoNum.equals("3")) {
								// 삭제하기
								DeleteSelectedStu(stuseq);
							} else {
								System.out.println("잘못된 번호를 입력하였습니다.");
								System.out.println();
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Fail: proc_SearchStudent_fromID");
						}
					} else {
						// 존재하지 않는 id
						System.out.println("해당 id는 존재하지 않습니다.");
						System.out.println();
					}
				}
			}
		}
	}//SearchStuFromId

	/**
	 * 학생의 번호를 입력받아서 입력한 학생의 정보 출력
	 * @param 학생 번호
	 */
	public void ShowStuSelectedNum(String stuNum) {
		//학생 번호 입력 > 입력한 학생 정보 출력
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		while (true) {
			
			int count = 0;
			String stuseq = null;
			String stuid = null, stussn = null;
			String stutel = null;
			String stuacc = null;
			
			try {
				
				conn = util.open();
				stat = conn.createStatement();
	
				String sql = String.format("select * from tbl_student order by seq");
				
				rs = stat.executeQuery(sql);
				
				while(rs.next()) {
					count++;
					if(stuNum.equals(String.format("%d", count))) {
						stuseq = rs.getString("seq");
						stuid = rs.getString("id");
						stussn = rs.getString("ssn");
						stutel = rs.getString("tel");
						stuacc = rs.getString("account");
					}
				}
				
				stat.close();
				System.out.println();
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Fail: Select vw_ShowStudentInfo ");
			}
			
			try {
				
				conn = util.open();
				stat = conn.createStatement();
	
				String sql = String.format("select * from vw_ShowStudentInfo where stuseq = %s"
						, stuseq);
				
				rs = stat.executeQuery(sql);
				System.out.println();
				
				if (rs.next()) {
					System.out.println("[과정 정보]");
					if(rs.getString("status").equals("진행과정없음")) {
						System.out.printf("이름 : %s\n"
								+ "현재 진행중인 과정이 없습니다.\n"
								, rs.getString("stuName"));
					} else {
						System.out.printf("이름 : %s\n"
								+ "수강 과정 이름 : %s\n"
								+ "수강 과정 기간 : %s\n"
								+ "강의실 : %s\n"
								+ "수료 및 중도탈락 여부 : %s\n"
								+ "수료 및 중도탈락 날짜 : %s\n"
								, rs.getString("stuName")
								, rs.getString("courseName")
								, rs.getString("startdate") + " ~ " + rs.getString("enddate")
								, rs.getString("room")
								, rs.getString("status")
								, rs.getString("statusdate"));
					}
					System.out.println("==========================");
					System.out.println("[개인 정보]");
					System.out.printf("아이디 : %s\n"
							+ "주민 등록 번호 : %s\n"
							+ "전화번호 : %s\n"
							+ "계좌번호 : %s\n"
							, stuid, stussn, stutel, stuacc);
				}
				
				stat.close();
				conn.close();
				
				System.out.println("==========================");
				System.out.println("1. 수정하기");
				System.out.println("2. 수료 및 중도탈락 처리");
				System.out.println("3. 삭제하기");
				System.out.println("0. 뒤로가기");
				System.out.println("==========================");
				System.out.print("번호 입력 : ");
				String infoNum = scan.nextLine();
				System.out.println();
				
				if (infoNum.equals("0")) {
					// 뒤로가기
					break;
				} else if (infoNum.equals("1")) {
					// 수정하기
					UpdateSelectedStu(stuseq);
				} else if (infoNum.equals("2")) {
					// 수료 및 중도탈락 처리
					UpdateStuStatus(stuseq);
				}  else if (infoNum.equals("3")) {
					// 삭제하기
					DeleteSelectedStu(stuseq);
				} else {
					System.out.println("잘못된 번호를 입력하였습니다.");
					System.out.println();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Fail: Select vw_ShowStudentInfo ");
			}
		}
	}//ShowStuSelectedNum

	/**
	 * 전체 학생 조회
	 */
	public void SaveTotalStudent() {
		//전체 학생 조회
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		stuList.clear();
		
		try {
			
			conn = util.open();
			stat = conn.createStatement();

			String sql = String.format("select seq, name, ssn, tel, to_char(regdate, 'yyyy-mm-dd') regdate,"
					+ " cnt from vw_ShowTotalStudent");
			
			rs = stat.executeQuery(sql);
			
			while(rs.next()) {
				stuList.add(String.format("%s,%s,%s,%s,%s"
						, rs.getString("name")
						, rs.getString("ssn")
						, rs.getString("tel")
						, rs.getString("regdate")
						, rs.getString("cnt")));
			}
			
			stat.close();
			conn.close();
			
			System.out.println("=============================================================");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Select vw_ShowTotalStudent ");
		}
	}//ShowTotalStudent

	public void AddStudent() {
		//학생등록
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		try {

			System.out.println("새 학생 정보 등록");
			System.out.print("학생 이름: ");
			String name = scan.nextLine();

			System.out.print("학생 아이디: ");
			String id = scan.nextLine();

			System.out.print("학생 주민등록번호: ");
			String ssn = scan.nextLine();

			System.out.print("학생 전화번호: ");
			String tel = scan.nextLine();

			System.out.println("==================================");

			System.out.println();

			System.out.print("> "+ name + " 학생 정보를 등록하시겠습니까?(y/n)");
			String insert = scan.nextLine();
			
			if (insert.toLowerCase().equals("y")) {
				
				String sql = "{ call proc_AddStudent(?,?,?,?) }";
				
				conn = util.open();
				stat = conn.prepareCall(sql);
				
				stat.setString(1, name);
				stat.setString(2, id);
				stat.setString(3, ssn);
				stat.setString(4, tel);
				
				stat.executeUpdate();
				
//				System.out.println("Success: Insert tbl_student");
				System.out.println("등록을 완료하였습니다.");
				
				stat.close();
				conn.close();
				
				
			} else if (insert.toLowerCase().equals("n")) {
				System.out.println("등록을 중지합니다.");
			} else {
				System.out.println("잘못된 번호를 입력하였습니다.");
			}
			System.out.println("이전 페이지로 돌아가겠습니다.");
			System.out.println("계속하시려면 엔터를 눌러주세요.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail: Insert tbl_student");
		}
	}//AddStudent
	
}
