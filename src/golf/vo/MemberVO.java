package golf.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

// 회원 엔티티

@Data
@Builder
public class MemberVO implements Serializable{
	private int memberNo; // 회원번호 (PK)
	private String memberId; // 아이디 (4 ~ 12자, 영문 대소문자, 숫자만 가능) (Unique)
	private String memberPw; // 비밀번호 (8 ~ 16자, 영문 대소문자, 숫자, 특수문자 !@#$%^&*() 중 하나 이상 포함)
	private String memberName; // 이름 (완성형 한글 2 ~ 4자)
	private int gradeNo; // 회원등급 (FK) -> 기본값 0
	private long totalAmount; // 누적결제금액 -> 기본값 0
	private int questionNo; // 질문번호 (FK)
	private String questionAnswer; // 질문답변
	private Date regDate; // 등록일자

	public void changePw(String pw) {
		this.memberPw = pw;
	}
	
	public void updateTotalAmount(long amount) {
		this.totalAmount += amount;
		
		// 음수가 되면 0으로 고정
		if(totalAmount < 0) {
			totalAmount = 0;
		}
	}
	
	public void updateGrade(int gradeNo) {
		this.gradeNo = gradeNo;
	}
}

















