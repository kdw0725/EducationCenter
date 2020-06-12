package teacher;

/**
 * 
 * @author 김동욱
 * 교사 정보 DTO
 */
public class TeacherBasic {

	private int 	seq;
	private String  name;
	private String  id;
	private String  ssn;
	private String  tel;
	private boolean delflag;

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

	@Override
	public String toString() {
		return "TeacherBasic [seq=" + seq + ", name=" + name + ", id=" + id + ", ssn=" + ssn + ", tel=" + tel + "]";
	}

}
