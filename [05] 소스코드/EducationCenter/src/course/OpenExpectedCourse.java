package course;

public class OpenExpectedCourse extends CourseBasic{
	
	private int		seq;
	private String  startdate;
	private String  enddate;
	private boolean delflag;
	private int 	cnt;
	private int 	max;
	
	public OpenExpectedCourse(int seq, String name, int period, boolean delflag) {
		super(seq, name, period, delflag);
	}

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
