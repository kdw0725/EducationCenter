package admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pagingfile {
	public static List<String> save(List<String> list) {

		List<String> list2 = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {

			String[] rk = list.get(i).split(",");
			String line = "";
			for (int j = 0; j < rk.length; j++) {
				line += rk[j] + "\t";

			}
			list2.add(line);
		}

		return list2;
	}
	public static void pageNonum(List<String> arrayList) {
		 //10개씩 분할
	      List<String[]> depart = new ArrayList<String[]>();
	      int firstindex = 0;
	      int lastindex = 10;
	      int index = 0;
	      int totalcount = arrayList.size();

	      while (true) {
	         // 10개의 String 이 들어갈 묶음
	         String[] ranking = new String[10];

	         for (int j = firstindex; j < lastindex; j++) {
	            if (j >= arrayList.size()) {
	               break;
	            }
	            ranking[j - (index) * 10] = arrayList.get(j);
	            }
	         depart.add(ranking);
	         
	         firstindex = firstindex + 10;
	         lastindex = lastindex + 10;
	         index++;

	         if (firstindex >= totalcount) {
	            break;
	         }
	      
	      }
	      
	      
	      
	      //페이지 수 
	      int count = 0;
	      
	      //넘버링
	      int num = 1;
	      
	      //페이지가 한개만 있는 경우
	      if(depart.size() == 1) {
	         //첫페이지 보여주기
				System.out.println("==================================================");
	         for(int i = 0; i < depart.get(0).length; i++) {
	            if( depart.get(0)[i] != null) {
	            System.out.printf("%s\n",depart.get(0)[i]);
	            }
	         }
				System.out.println("==================================================");
	         System.out.println("넘어갈 페이지가 없습니다.");
				System.out.println("==================================================");
	      }
	      else {
	      
	      //페이지가 여러개인 경우
	      while(true) {
	         
				System.out.println("==================================================");
	         for(int i = 0; i < 10; i++) {
	            if( depart.get(count)[i] != null) {
	            System.out.printf("%s\t" , depart.get(count)[i]);
	            
	            }
	            System.out.println();
	         }
	         //페이지 반복
	         System.out.println();
				System.out.println("==================================================");
	         System.out.printf("%d페이지 입니다.\n", count + 1);
				System.out.println("==================================================");
	         System.out.println("1. 이전 페이지            2. 다음 페이지");
	         System.out.println("3. 취소");
				System.out.println("==================================================");
	         //번호 고르기
	         Scanner scan = new Scanner(System.in);
	         System.out.print("입력 :  ");
	         int answer = scan.nextInt();
	         scan.skip("\r\n"); //엔터 무시
				System.out.println("==================================================");
	         //이전페이지
	         if(answer == 1) {
	            //첫페이지 일 경우
	            if(count == 0) {
	               count = depart.size()-1;
	            }
	            else {
	               count = count-1;
	            }
	            //다음페이지
	         } else if(answer == 2){
	            
	            //마지막 페이지일 경우
	            if(count == (depart.size()-1)) {
	               
	               count = 0;
	            } else {
	               count = count+1;
	            }
	         } else if (answer == 3){
	            break;
	         }  else {
	        	 System.out.println("번호를 잘 못 입력하였습니다.");
	         }
	         
	         
	         
	         
	      } //while

	      }
	      
		
	}

	public static void page(List<String> arrayList) {

		// 10개씩 분할
		List<String[]> depart = new ArrayList<String[]>();
		int firstindex = 0;
		int lastindex = 10;
		int index = 0;
		int totalcount = arrayList.size();

		while (true) {
			// 10개의 String 이 들어갈 묶음
			String[] ranking = new String[10];

			for (int j = firstindex; j < lastindex; j++) {
				if (j >= arrayList.size()) {
					break;
				}
				ranking[j - (index) * 10] = arrayList.get(j);
			}
			depart.add(ranking);

			firstindex = firstindex + 10;
			lastindex = lastindex + 10;
			index++;

			if (firstindex >= totalcount) {
				break;
			}

		}
		// 페이지 수
		int count = 0;

		// 넘버링
		int num = 1;

		// 페이지가 한개만 있는 경우
		if (depart.size() == 1) {
			// 첫페이지 보여주기
			System.out.println("==================================================");
			for (int i = 0; i < depart.get(0).length; i++) {
				if (depart.get(0)[i] != null) {
					System.out.printf("%3d. %s\n", i + 1, depart.get(0)[i]);
				}
			}
			System.out.println("=============================================");
			System.out.println("넘어갈 페이지가 없습니다.");
			System.out.println("=============================================");
		} else {

			// 페이지가 여러개인 경우
			while (true) {
				System.out.println("==================================================");
				for (int i = 0; i < 10; i++) {
					System.out.printf("\n%3d. ", num);
					if (depart.get(count)[i] != null) {
						System.out.printf("%s\t", depart.get(count)[i]);
					}
					num++;
				}
				// 페이지 반복
				System.out.println();
				System.out.println("==================================================");
				System.out.printf("%d페이지 입니다.\n", count + 1);
				System.out.println("==================================================");
		         System.out.println("1. 이전 페이지            2. 다음 페이지");
				System.out.println("3. 취소");
				System.out.println("==================================================");
				// 번호 고르기
				Scanner scan = new Scanner(System.in);
				System.out.print("입력 : ");
				int answer = scan.nextInt();
				scan.skip("\r\n"); // 엔터 무시
				// 이전페이지
				if (answer == 1) {
					// 첫페이지 일 경우
					if (count == 0) {
						num = depart.size() * 10 - 9;
						count = depart.size() - 1;
					} else {
						count = count - 1;
						num -= 20;
					}
					// 다음페이지
				} else if (answer == 2) {
					// 마지막 페이지일 경우
					if (count == (depart.size() - 1)) {

						count = 0;
						num = 1;
					} else {
						count = count + 1;
					}
				} else {
					break;
				}
			} // while
		}
	}

	

}