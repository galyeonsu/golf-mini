package golf.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

// 회원등급 엔티티

@Data
@Builder
public class GradeVO implements Serializable{
	private int gradeNo; // 회원등급 (PK) (0: 일반회원, 1: 우수회원, 2: VIP, 3: 관리자)
	private String gradeName; // 등급명
	private long requireAmount; // 해당 등급 도달에 필요한 누적결제금액 (우수회원: 500,000원, VIP: 2,000,000원)
	private int discountRate; // 해당 등급 도달 시 할인되는 할인율 (% 단위) (일반회원: 0%, 우수회원: 20%, VIP: 50%로 계산)
}
