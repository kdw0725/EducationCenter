package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;
import student.DBUtil;
import student.Paging;

public class AdminCovid {
	
	Connection conn = null;
	CallableStatement stat = null;
	ResultSet rs = null;
	DBUtil util = new DBUtil();
	List<Paging> list = new ArrayList<Paging>();
	Scanner scan = new Scanner(System.in);
	
	public void admincovid() {
		
		String sql = "";
		
		try {
			
			conn = util.open();
			
			while(true) {
				
				System.out.println("========================");
				System.out.println("1. 체온 기록");
				System.out.println("2. 체온 조회");
				System.out.println("0. 뒤로가기");
				System.out.println("========================");
				System.out.print("번호 : ");
				String num = scan.nextLine();
				System.out.println();
				
				if (num.equals("1")) {
					
					System.out.print("관리자 번호 : ");
					String admseq = scan.nextLine();
					
					System.out.print("오전 체온 : ");
					String am = scan.nextLine();
					
					System.out.print("오후 체온 : ");
					String pm = scan.nextLine();
					
					sql = "{ call PROC_INSERT_ADMIN_COVID19(?, ?, ?, ?) }";
					stat = conn.prepareCall(sql);
					stat.setString(1, am);
					stat.setString(2, pm);
					stat.setString(3, admseq);
					stat.registerOutParameter(4, OracleTypes.NUMBER);
					
					stat.executeQuery();
					
					if (stat.getInt(4) == 1) {
						
						System.out.println("추가 완료");
						
					} else {
						
						System.out.println("추가 실패");
						continue;
						
					}
					
					stat.close();
					
				} else if (num.equals("2")) {
					
					System.out.print("관리자 번호 : ");
					String admseq = scan.nextLine();
					
					sql = "{ call PROC_SEE_COVID19_ADMIN(?, ?) }";
					stat = conn.prepareCall(sql);
					stat.setString(1, admseq);
					stat.registerOutParameter(2, OracleTypes.CURSOR);
					
					stat.executeQuery();
					
					rs = (ResultSet)stat.getObject(2);
					
					System.out.println("================체온 조회================");
					System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
					
					while (rs.next()) {
						
						//객체에 출결값 담기
						Paging p = new Paging();
						
						p.setAdseq(rs.getString("adseq"));
						p.setDays(rs.getString("days").substring(0, 10));
						p.setAmtemp(rs.getString("amtemp"));
						p.setPmtemp(rs.getString("pmtemp"));
						
						list.add(p);
						
					}
					
					//전체 개수
					int totalCount = list.size();
					//페이지 당 개수
					int countList = 10;
					//페이지 수 (나머지 처리는 밑에서)
					int totalPage = totalCount / countList;
					//배열 카운트 숫자
					int countNum = 0;
					//한 페이지에 몇개 담았는지 카운트
					int number = 0;
					
					//마지막 페이지에 10칸이 다 안차도 페이지 부여
					if (totalCount % countList > 0) {
						totalPage++;
					}
					
					
					//페이지 수 만큼
					for (int i=0; i<totalPage; i++) {
						
						//한 페이지에 10개씩
						for (int j=0; j<countList; j++) {
							
							System.out.printf("%5s\t\t%s\t%s\t%s\n"
									, list.get(countNum).getAdseq()
									, list.get(countNum).getDays()
									, list.get(countNum).getAmtemp()
									, list.get(countNum).getPmtemp());
							
							//배열 카운트 증가
							countNum++;
							//한 페이지에 몇개 담았는지 카운트
							number++;
							
							//마지막에 전부 다 담았으면 break
							//break 안하면 index에러남
							if (countNum == totalCount) {
								break;
							}
							
						}
						
						System.out.println("=========================================");
						
						System.out.println("1. 이전 페이지\t2. 다음 페이지");
						System.out.println("3. 종료");
						System.out.println("===============================");
						System.out.print("번호 : ");
						String input = scan.nextLine();
						System.out.println();
						
						//이전 페이지가 처음 페이지일때
						if (input.equals("1") && i == 0) {
							
							System.out.println("이전 페이지가 없습니다.\n");
							System.out.println("================체온 조회================");
							System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
							countNum = countNum - 10;
							number = 0;
							i--;
						
						//마지막 페이지에서 이전 페이지로 갈 때
						} else if (input.equals("1") && countNum == totalCount) {
							
							System.out.println("================체온 조회================");
							System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
							countNum = countNum - 10 - number;
							number = 0;
							i = i-2;
						
						//기본 이전 페이지
						} else if (input.equals("1")) {
							
							System.out.println("================체온 조회================");
							System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
							countNum = countNum - 20;
							number = 0;
							i = i-2;
						
						//마지막 페이지에서 다음 페이지로 갈 때	
						} else if (input.equals("2") && countNum == totalCount){
							
							System.out.println("다음 페이지가 없습니다.\n");
							System.out.println("================체온 조회================");
							System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
							countNum = countNum - number;
							number = 0;
							i--;
						
						//기본 다음 페이지
						} else if (input.equals("2")) {
							
							System.out.println("================체온 조회================");
							System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
							number = 0;
							continue;
						
						//종료
						} else if (input.equals("3")) {
							
							break;
							
						} else { //그 외 번호 입력 시
							
							System.out.println("없는 번호입니다.\n");
							System.out.println("================체온 조회================");
							System.out.println("[관리자번호]\t[날짜]\t\t[오전]\t[오후]");
							countNum = countNum - 10;
							number = 0;
							i--;
							
						}			
						
					}
					
					countNum = 1;
					
					System.out.println("=========================================");
					System.out.println();
					
					list.clear();
					
					rs.close();
					stat.close();
					
				} else if (num.equals("0")) {
					
					break;
					
				} else {
					
					System.out.println("없는 번호입니다.");
					continue;
					
				}
				
				
			}
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
