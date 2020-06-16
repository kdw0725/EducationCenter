package teacher;

import java.util.Arrays;

/**
 * 
 * @author 김동욱
 * 교사 정보 DTO
 */
public class TeacherBasic {

	private int      seq;
	private String   name;
	private String   id;
	private String   ssn;
	private String   tel;
	private String[] subject;
	

	public TeacherBasic(int seq, String name, String ssn, String tel, String[] subject) {
		this.seq = seq;
		this.name = name;
		this.ssn = ssn;
		this.tel = tel;
		this.subject = subject;
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
	
	

	public String getSubject() {
		if(subject == null) {
			return null;
		} else {
			String result = "";
			for (int i = 0; i < subject.length -1; i++) {
				result += subject[i] + ", ";
			}
			result += subject[subject.length-1];
			return result;
		}
		
	}

	public void setSubject(String[] subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "TeacherBasic [seq=" + seq + ", name=" + name + ", id=" + id + ", ssn=" + ssn + ", tel=" + tel
				+ ", subject=" + Arrays.toString(subject) + "]";
	}
	


}
