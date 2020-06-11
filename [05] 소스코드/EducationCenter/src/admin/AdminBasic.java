package admin;

/**
 * 
 * @author 김동욱
 * 관리자 정보 DTO
 */
public class AdminBasic {

	private int    seq;
	private String  id;
	private String  pw;
	private boolean delflag;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public boolean isDelflag() {
		return delflag;
	}

	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}

	@Override
	public String toString() {
		return "AdminBasic [seq=" + seq + ", id=" + id + ", pw=" + pw + ", delflag=" + delflag + "]";
	} 


}
