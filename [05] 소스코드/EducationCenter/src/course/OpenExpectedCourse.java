package course;

/**
 * 
 * @author 김동욱
 * 개설 과정 DTO
 */
public class OpenExpectedCourse extends CourseBasic{
	
	private int		seq;
	private String  startdate;
	private String  enddate;
	private boolean delflag;
	private int 	cnt;
	private int 	max;
	
	/**
	 * 과정을 상속받은 생성자
	 * @param 고유번호
	 * @param 이름
	 * @param 과정 기간
	 * @param 삭제여부
	 */
	public OpenExpectedCourse(int seq, String name, int period, boolean delflag) {
		super(seq, name, period, delflag);
	}

	/**
	 * 과정을 상속받은 생성자 
	 * @param 고유번호
	 * @param 이름
	 * @param 과정 기간
	 * @param 과정 삭제 여부
	 * @param 개설된 과정의 고유번호
	 * @param 과정 시작일
	 * @param 과정 종료일
	 * @param 개설과정 삭제여부
	 * @param 현재 수강중인 학생의 수
	 * @param 최대 수강 가능한 학생의 수
	 */
	public OpenExpectedCourse(int seq, String name, int period, boolean delflag, int openSeq, String startdate, String enddate, boolean openDelflag, int cnt, int max) {
		super(seq, name, period, delflag);
		this.seq = openSeq;
		this.startdate = startdate;
		this.enddate = enddate;
		this.delflag = openDelflag;
		this.cnt = cnt;
		this.max = max;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public boolean isDelflag() {
		return delflag;
	}

	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	
	
	
	
	

}
