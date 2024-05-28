package golf.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

// 골프장 엔티티

@Data
@Builder
public class FieldVO implements Serializable{
	private int fieldNo; // 골프장 번호 (PK)
	private String fieldName; // 골프장 이름
	private boolean holeCount; // 골프장 홀 수 (0이면 9홀, 1이면 18홀)
	private String holeInfo; // 홀 타수 정보 (3|4|4|3|4|5|4|3|......|5)
	private int holeSum; // 골프장 모든 홀의 타수의 합 (편의성을 위해 넣음)
	private String fieldRegion; // 골프장 지역
	private String fieldPhoneNumber; // 골프장 전화번호
	private long basicAmount; // 기본금액
	private long caddieAmount; // 캐디금액
	private long hotelAmount; // 숙박금액
	private Date regDate; // 등록일자

	public void updateField (String fieldName, String fieldRegion, String fieldPhoneNumber, long basicAmount, long caddieAmount,
			long hotelAmount) {
		this.fieldName = fieldName;
		this.fieldRegion = fieldRegion;
		this.fieldPhoneNumber = fieldPhoneNumber;
		this.basicAmount = basicAmount;
		this.caddieAmount = caddieAmount;
		this.hotelAmount = hotelAmount;
	}
}
