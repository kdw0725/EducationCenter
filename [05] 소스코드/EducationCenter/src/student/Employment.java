package student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author 장정우
 * 학생의 취업을 담당하는 클래스
 */
public class Employment {
	
	Connection conn = null;
	Statement stat = null;
	ResultSet rs = null;
	DBUtil project = new DBUtil();
	Scanner scan = new Scanner(System.in);
	//페이징 배열
	List<Paging> list = new ArrayList<Paging>();
	
	/**
	 * 취업 현황 조회
	 */
	public void employment() { //취업 현황 조회
		
		try {
			
			String sql = "select * from vw_employment";
			
			conn = project.open();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			//전체조회
			System.out.println("===========================================================취업 정보===========================================================");
			System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
			
			while (rs.next()) {
				
				//객체에 출결값 담기
				Paging p = new Paging();
				
				p.setName(rs.getString("name"));
				p.setCourseseq(rs.getString("courseseq"));
				p.setCoursename(rs.getString("coursename").length() > 20 ? rs.getString("coursename").substring(0, 20) + "..." : rs.getString("coursename"));
				p.setStatus(rs.getString("status"));
				p.setCompany(rs.getString("company") == null ? " -" : rs.getString("company"));
				p.setSalary(rs.getString("salary") == null ? " -" : rs.getString("salary"));
				
				//객체 배열에 담기
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
					
					System.out.printf("%s\t%5s\t\t%s\t\t%-4s\t\t%-10s\t\t%10s\n"
							, list.get(countNum).getName()
							, list.get(countNum).getCourseseq()
							, list.get(countNum).getCoursename()
							, list.get(countNum).getStatus()
							, list.get(countNum).getCompany()
							, list.get(countNum).getSalary());
					
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
				
				System.out.println("==============================================================================================================================");
				
				System.out.println("1. 이전 페이지\t2. 다음 페이지");
				System.out.println("3. 종료");
				System.out.println("===============================");
				System.out.print("번호 : ");
				String num = scan.nextLine();
				System.out.println();
				
				//이전 페이지가 처음 페이지일때
				if (num.equals("1") && i == 0) {
					
					System.out.println("이전 페이지가 없습니다.\n");
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					countNum = countNum - 10;
					number = 0;
					i--;
				
				//마지막 페이지에서 이전 페이지로 갈 때
				} else if (num.equals("1") && countNum == totalCount) {
					
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					countNum = countNum - 10 - number;
					number = 0;
					i = i-2;
				
				//기본 이전 페이지
				} else if (num.equals("1")) {
					
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					countNum = countNum - 20;
					number = 0;
					i = i-2;
				
				//마지막 페이지에서 다음 페이지로 갈 때	
				} else if (num.equals("2") && countNum == totalCount){
					
					System.out.println("다음 페이지가 없습니다.\n");
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					countNum = countNum - number;
					number = 0;
					i--;
				
				//기본 다음 페이지
				} else if (num.equals("2")) {
					
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					number = 0;
					continue;
				
				//종료
				} else if (num.equals("3")) {
					
					break;
					
				} else { //그 외 번호 입력 시
					
					System.out.println("없는 번호입니다.\n");
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					countNum = countNum - 10;
					number = 0;
					i--;
					
				}			
				
			}
			
			countNum = 1;
			
			System.out.println("==============================================================================================================================");
			System.out.println();
			
			list.clear();
			
			rs.close();
			stat.close();
			
			while(true) {
				
				System.out.println("=========================");
				System.out.println("1. 과정별 취업 현황 조회");
				System.out.println("0. 뒤로가기");
				System.out.println("=========================");
				System.out.print("번호 : ");
				String num = scan.nextLine();
				System.out.println();
				
				
				if (num.equals("1")) {
					
					System.out.print("과정 번호 : ");
					String coursenum = scan.nextLine();
					System.out.println();
					
					sql = "select * from vw_employment where courseseq = " + coursenum;
					stat = conn.createStatement();
					rs = stat.executeQuery(sql);
					
					System.out.println("===========================================================취업 정보===========================================================");
					System.out.println("[이름]\t[과정번호]\t[과정명]\t\t\t\t\t[상태]\t\t[회사명]\t\t    [연봉]");
					
					while (rs.next()) {
						
						System.out.printf("%s\t%5s\t\t%s\t\t%-4s\t\t%-10s\t\t%10s\n"
								, rs.getString("name")
								, rs.getString("courseseq")
								, rs.getString("coursename").length() > 20 ? rs.getString("coursename").substring(0, 20) + "..." : rs.getString("coursename")
								, rs.getString("status")
								, rs.getString("company") == null ? " -" : rs.getString("company")
								, rs.getString("salary") == null ? " -" : rs.getString("salary"));
						
					}
					
					System.out.println("==============================================================================================================================");
					
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
