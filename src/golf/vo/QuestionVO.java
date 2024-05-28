package golf.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

// 질문 엔티티

@Data
@Builder
public class QuestionVO implements Serializable{
	private int questionNo; // 질문번호 (PK)
	private String questionContent; // 질문내용
}
