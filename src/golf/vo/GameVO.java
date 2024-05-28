package golf.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

// 게임 기록 엔티티

@Data
@Builder
public class GameVO implements Serializable{
	private int gameNo; // 게임번호 (PK)
	private int memberNo; // 회원번호 (FK)
	private int fieldNo; // 골프장번호 (FK)
	private int reserveNo; // 예약번호 (FK)
	private String gameScore; // 게임점수 (각 홀 기록)
	private int gameScoreSum; // 게임점수 (각 홀 기록의 합)
	private Date regDate; // 등록일자
}
