package golf.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

// 예약 엔티티

@Data
@Builder
public class ReserveVO implements Serializable{
	private int reserveNo; // 예약번호 (PK)
	private int memberNo; // 회원번호 (FK)
	private int fieldNo; // 골프장번호 (FK)
	private String reserveDate; // 예약일자
	private int reserveTime; // 1부/2부 (1이면 1부, 2이면 2부)
	private boolean isGamePlayed; // 게임완료여부 (0이면 아직 안 함, 1이면 함)
	private boolean caddieOption; // 0이면 선택 안 함. 1이면 선택함
	private boolean hotelOption; // 0이면 선택 안 함. 1이면 선택함
	private long payAmount; // 결제금액 (옵션 선택여부 및 회원등급에 따른 할인율이 계산된 금액)
	private Date regDate; // 등록일자
}
