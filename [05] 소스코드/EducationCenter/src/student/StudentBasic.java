package student;

/**
 * 
 * 
 * @author 김동욱 학생 DTO
 * 학생 정보 DTO
 */
public class StudentBasic {

	private int seq;
	private String name;
	private String id;
	private String ssn;
	private String tel;
	private String account;
	private String regdate;
	private boolean delflag;
	
	/**
	 * StudentBasic 객체 생성자
	 * @param 학생 이름
	 * @param 아이디
	 * @param 주민번호
	 * @param 연락처
	 * @param 계좌번호
	 */
	public StudentBasic(String name, String id, String ssn, String tel, String account) {
		this.name = name;
		this.id = id;
		this.ssn = ssn;
		this.tel = tel;
		this.account = account;
	}
	
	
	/**
	 * StudentBasic 객체 생성자(OverLoading)
	 * @param 학생번호
	 * @param 학생 이름
	 * @param 아이디
	 * @param 주민번호
	 * @param 연락처
	 * @param 계좌번호
	 * @param 회원가입일
	 * @param 계정사용여부
	 */
	public StudentBasic(int seq, String name, String id, String ssn, String tel, String account, String regdate, String delflag) {
		this.seq = seq;
		this.name = name;
		this.id = id;
		this.ssn = ssn;
		this.tel = tel;
		this.account = account;
		this.regdate = regdate;
		this.delflag = delflag.equals("Y") ? true : false;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public boolean isDelflag() {
		return delflag;
	}

	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}

	@Override
	public String toString() {
		return "StudentBasic [seq=" + seq + ", name=" + name + ", id=" + id + ", ssn=" + ssn + ", tel=" + tel
				+ ", account=" + account + ", regdate=" + regdate + ", delflag=" + delflag + "]";
	}

}
