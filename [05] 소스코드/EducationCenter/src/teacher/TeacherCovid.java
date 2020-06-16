package teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import jdbc.DBUtil;
import oracle.jdbc.internal.OracleTypes;

public class TeacherCovid {

   /**
    * 교사 체온확인 메소드
    * 백지현
    * @param teaNum 교사seq
    */
   public void seeTeacherCovid(String teaNum) {

      Connection conn = null;
      CallableStatement stat = null;
      ResultSet rs = null;
      DBUtil util = new DBUtil();
      Scanner scan = new Scanner(System.in);
      ArrayList<String> list = new ArrayList<String>();

      try {

         conn = util.open();

         String sql = "{call PROC_COVID_TEACHER(?,?)}";
         stat = conn.prepareCall(sql);

         stat.setString(1, teaNum);
         stat.registerOutParameter(2, OracleTypes.CURSOR);
         stat.executeQuery();

         rs = (ResultSet) stat.getObject(2);
         

         System.out.println("[날짜]\t\t[오전]\t[오후]");
         System.out.println("===============================");
         while (rs.next()) {

            list.add(String.format("%s\t%s\t%s\n"
            ,rs.getString("DAYS")
            ,rs.getString("AMTEMP")
            ,rs.getString("PMTEMP")));

         }

         // 전체 갯수
         int totalCount = list.size();
         // 한페이지당 개수 
         int countList = 10;
         // 토탈 페이지
         int totalPage = totalCount / countList;
         // 배열 개수 카운트
         int countNum = 1;
         // 한 페이지에 몇개 담았는지 카운트
         int number = 0;

         if (totalCount % countList > 0) {
            totalPage++;
         }

         for (int i = 0; i < totalPage; i++) {
            
            for (int j = 0; j < countList; j++) {

               System.out.println(countNum+"\t"+list.get(countNum));
               countNum++;
               number++;
               
               if (countNum == totalCount)
                  break;

            }
         
            if(countNum%10 == 1) {
               System.out.println("\t현재 페이지 : "+countNum/10);
            }
            else {
               System.out.println("\t현재 페이지 : "+((countNum/10)+1));
               System.out.println("마지막 페이지 입니다.3");
            }
            System.out.println("1. 이전 페이지\t2. 다음 페이지");
            System.out.println("3. 종료");
            System.out.println("===============================");
            System.out.print("번호 : ");
            String num = scan.nextLine();
            System.out.println();
            
            //이전 페이지가 처음 페이지일때
            if (num.equals("1") && i == 0) {
               System.out.println("이전 페이지가 없습니다.\n");
               System.out.println("[날짜]\t\t[오전]\t[오후]");
               System.out.println("===============================");
               countNum = countNum - 10;
               number = 0;
               i--;
            
            //마지막 페이지에서 이전 페이지로 갈 때
            } else if (num.equals("1") && countNum == totalCount) {
               System.out.println("[날짜]\t\t[오전]\t[오후]");
               System.out.println("===============================");
               countNum = countNum - 20 + number;
               number = 0;
               i = i-2;
            
            //기본 이전 페이지
            } else if (num.equals("1")) {
               System.out.println("[날짜]\t\t[오전]\t[오후]");
               System.out.println("===============================");
               countNum = countNum - 20;
               number = 0;
               i = i-2;
            
            //마지막 페이지에서 다음 페이지로 갈 때   
            } else if (num.equals("2") && countNum == totalCount){
               System.out.println("다음 페이지가 없습니다.\n");
               System.out.println("[날짜]\t\t[오전]\t[오후]");
               System.out.println("===============================");
               countNum = countNum - number;
               number = 0;
               i--;
            
            //기본 다음 페이지
            } else if (num.equals("2")) {
               System.out.println("[날짜]\t\t[오전]\t[오후]");
               System.out.println("===============================");
               number = 0;
               continue;
            
            //종료
            } else if (num.equals("3")) {
               
               break;
               
            } else { //그 외 번호 입력 시
               System.out.println("없는 번호입니다.\n");
               System.out.println("[날짜]\t\t[오전]\t[오후]");
               System.out.println("===============================");
               countNum = countNum - 10;
               number = 0;
               i--;
               
            }         
            
         }
         
         countNum = 1;
         
         System.out.println("=========================================================");
         System.out.println();
         
         rs.close();
         stat.close();
         
         conn.close();

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}