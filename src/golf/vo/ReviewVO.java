package golf.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

// 평점 엔티티

@Data
@Builder
public class ReviewVO implements Serializable{
	private int reviewNo; // 평점번호 (PK)
	private int memberNo; // 회원번호 (FK)
	private int fieldNo; // 골프장번호 (FK)
	private int gameNo; // 게임번호 (FK)
	private int reviewScore; // 평점 (1, 2, 3, 4, 5 중 하나)
	private Date regDate; // 등록일자
}
