package course;

public class CourseBasic {
	private int     seq;
	private String  name;
	private int		period;
	private boolean delflag;
	
	
	
	public CourseBasic(int seq, String name, int period, boolean delflag) {
		this.seq = seq;
		this.name = name;
		this.period = period;
		this.delflag = delflag;
	}

	public int getSeq() {
		return seq;
	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public boolean isDelflag() {
		return delflag;
	}
	
	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}
	
	
}
