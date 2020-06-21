package teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author 정희수
 * 페이징 출력을 위한 클래스
 */
public class Pagingfile {

	/**
	 * 페이징 할 값들을 저장하는 클래스
	 * @param 원본 리스트
	 * @return 출력 리스트
	 */
	public List<String> save(List<String> list) {
		
		List<String> list2 = new ArrayList<String>();
		
		for(int i = 0; i < list.size() ; i++) {
			
			String[] rk = list.get(i).split(",");
			String line = "";
			for(int j = 0; j < rk.length; j++) {
				line += rk[j] + "\t";
				
			}
		
			list2.add(line);
		
		
		}
		
		
		return list2;
	}
	
	/**
	 * 페이징한 리스트를 출력해주는 클래스
	 * @param 원본 리스트
	 */
	public void pageNonum(List<String> arrayList) {
		
		
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
			System.out.println("---------------------------------------------");
			for(int i = 0; i < depart.get(0).length; i++) {
				if( depart.get(0)[i] != null) {
				System.out.printf("%s\n",depart.get(0)[i]);
				}
			}
			System.out.println("---------------------------------------------");
			System.out.println("넘어갈 페이지가 없습니다.");
			System.out.println("---------------------------------------------");
		}
		else {
		
		//페이지가 여러개인 경우
		while(true) {
			
			System.out.print("---------------------------------------------\n");
			for(int i = 0; i < 10; i++) {
				if( depart.get(count)[i] != null) {
				System.out.printf("%s\t" , depart.get(count)[i]);
				
				}
				System.out.println();
			}
			//페이지 반복
			System.out.println();
			System.out.println("---------------------------------------------");
			System.out.printf("%d페이지 입니다.\n", count + 1);
			System.out.println("---------------------------------------------");
			System.out.println("1. 이전 페이지");
			System.out.println("2. 다음 페이지");
			System.out.println("0. 취소");
			System.out.println("---------------------------------------------");			
			//번호 고르기
			Scanner scan = new Scanner(System.in);
			System.out.print("입력▶ ");
			int answer = scan.nextInt();
			scan.skip("\r\n"); //엔터 무시
			System.out.println("---------------------------------------------");
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
			} else{
				break;
			} 
			
			
			
			
		} //while

		}
		
		
	}
	
	

	/**
	 * 페이지를 10개씩 분할해주는 메소드
	 * @param 원본 리스트
	 */
	public void page(List<String> arrayList) {
		
		
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
			System.out.println("---------------------------------------------");
			for(int i = 0; i < depart.get(0).length; i++) {
				if( depart.get(0)[i] != null) {
				System.out.printf("%d. %s\n",i+1,depart.get(0)[i]);
				}
			}
			System.out.println("---------------------------------------------");
			System.out.println("넘어갈 페이지가 없습니다.");
			System.out.println("---------------------------------------------");
		}
		else {
		
		//페이지가 여러개인 경우
		while(true) {
			
			System.out.print("---------------------------------------------");
			for(int i = 0; i < 10; i++) {
				System.out.printf("\n%d. ", num);
				if( depart.get(count)[i] != null) {
				System.out.printf("%s\t" , depart.get(count)[i]);
				
				}
				num++;
			}
			//페이지 반복
			System.out.println();
			System.out.println("---------------------------------------------");
			System.out.printf("%d페이지 입니다.\n", count + 1);
			System.out.println("---------------------------------------------");
			System.out.println("1. 이전 페이지");
			System.out.println("2. 다음 페이지");
			System.out.println("0. 취소");
			System.out.println("---------------------------------------------");			
			//번호 고르기
			Scanner scan = new Scanner(System.in);
			System.out.print("입력▶ ");
			int answer = scan.nextInt();
			scan.skip("\r\n"); //엔터 무시
			System.out.println("---------------------------------------------");
			//이전페이지
			if(answer == 1) {
				//첫페이지 일 경우
				if(count == 0) {
					num = depart.size()*10 - 9;
					count = depart.size()-1;
				}
				else {
					count = count-1;
					num -= 20;
				}
				//다음페이지
			} else if(answer == 2){
				
				//마지막 페이지일 경우
				if(count == (depart.size()-1)) {
					
					count = 0;
					num = 1;
				} else {
					count = count+1;
				}
			} else{
				break;
			} 
			
			
			
			
		} //while

		}
		
		
	}
	
	
}
