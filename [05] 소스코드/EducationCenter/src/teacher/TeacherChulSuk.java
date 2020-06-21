package teacher;

import java.util.Scanner;

/**
 * 
 * @author 정희수
 * 교사의 출석을 담당하는 클래스
 */
public class TeacherChulSuk {

	Scanner scan = new Scanner(System.in);

	/**
	 * 교사의 핛갱관리 메뉴
	 * @param 교사번호
	 */
	public void teacher_studentcheck(int Tnum) { // 5. 학생 관리

		try {

			boolean loop = true;

			while (loop) {

				System.out.println("=============================================");
				System.out.println("학생관리 페이지에 들어오셨습니다.");
				System.out.println("1. 중도탈락 확인 여부"); // 상세로 들어가서 중도탈락여부확인, 중도탈락일 확인,
				System.out.println("2. 내 강의 교육생 출결 확인"); // 상세로 1 모든 교육생 출결확인,출결확인 기간별 조회,특정(과정/인원)출결 조회,근태상황조회
				System.out.println("0. 뒤로가기");
				System.out.println("==============================================");
				System.out.print("번호 : ");

				int num = scan.nextInt();
				scan.skip("\r\n");

				if (num == 1) { // 중토탈락 확인 , 상세로 들어가서 중도탈락여부확인, 중도탈락일 확인
					teacher_studentfail(Tnum);
				} else if (num == 2) { // 교육생 출결 확인, 상세로 1 모든 교육생 출결확인,출결확인 기간별 조회,특정(과정/인원)출결 조회,근태상황조회
					teacher_studentattcheck(Tnum);
				} else if (num == 0) { // 뒤로가기
					break;
				} else { // 그 외 번호 입력 시
					System.out.println("잘못된 번호입니다.");
					continue;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 학생 중도탈락 관련 메소드
	 * @param 교사번호
	 */
	public void teacher_studentfail(int Tnum) { // 5-1. 중도탈락 학생 관리
		while (true) {
			System.out.println("===============================================");
			System.out.println("중도탈락 확인여부 페이지에 들어오셨습니다.");
			System.out.println("1. 중도탈락 여부 확인"); // 상세로 들어가서 중도탈락여부확인, 중도탈락일 확인,
			System.out.println("2. 중도탈락일 확인"); // 상세로 1 모든 교육생 출결확인,출결확인 기간별 조회,특정(과정/인원)출결 조회,근태상황조회
			System.out.println("0. 뒤로가기");
			System.out.println("==============================================");
			System.out.print("번호 : ");

			int num = scan.nextInt();
			scan.skip("\r\n");
			Teacher teacher = new Teacher();
			if (num == 1) { // 중도탈락여부 확인 t107
				teacher.t017();
			} else if (num == 2) { // 중도탈락일 출력 t018
				teacher.t018();
			} else if (num == 0) { // 뒤로가기
				break;

			} else { // 그 외 번호 입력 시
				System.out.println("잘못된 번호입니다.");
				continue;

			}

		}

	}

	/**
	 * 학생의 출결을 담당하는 메소드
	 * @param 교사번호
	 */
	public void teacher_studentattcheck(int Tnum) { // 5-2. 학생 출결 관리

		try {

			boolean loop = true;

			while (loop) {

				System.out.println("===============================================");
				System.out.println("학생 출결 관리 페이지에 들어오셨습니다.");
				System.out.println("1. 모든 교육생 출결 조회");
				System.out.println("2. 출결 기간별 조회");
				System.out.println("3. 특정(과정/교육생) 출결 조회");
				System.out.println("4. 학생 근태상황 조회");
				System.out.println("0. 뒤로가기");
				System.out.println("==============================================");
				System.out.print("번호 : ");

				int num = scan.nextInt();
				scan.skip("\r\n");
				Teacher teacher = new Teacher();
				if (num == 1) { // 모든 교육생 출결조회 t020
					teacher.t020(Tnum);
				} else if (num == 2) { // 출결 기간별 조회 출력 t021
					teacher.t021(Tnum);
				} else if (num == 3) { // 특정(과정/교육생) 출결 출력 t022
					teacher.t022(Tnum);
				} else if (num == 4) { // 학생근태상황 조회 출력 t023
					teacher.t023();
				} else if (num == 0) { // 뒤로가기
					break;
				} else { // 그 외 번호 입력 시
					System.out.println("잘못된 번호입니다.");
					continue;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}// class
