package golf.service;

import java.util.ArrayList;
import java.util.List;

import golf.repository.ObjectRepository;
import golf.vo.QuestionVO;
import lombok.Data;

@Data
public class QuestionService{

	private QuestionService() {};
	
	private static final QuestionService questionService = new QuestionService();
	
	public static QuestionService getInstance() {
		return questionService;
	}
	
	private List<QuestionVO> questionList;
	
	{
		questionList = ObjectRepository.load("question");
		
		if(questionList == null) {
			questionList = new ArrayList<>();

			String[] questionTextList = {"기억에 남는 추억의 장소는?", "자신의 인생 좌우명은?", "자신의 보물 1호는?", "가장 기억에 남는 선생님 성함은?", "추억하고 싶은 날짜가 있다면?", "받았던 선물 중 기억에 남는 독특한 선물은?", "유년시절 가장 생각나는 친구 이름은?", "인상 깊게 읽은 책 이름은?", "다시 태어나면 되고 싶은 것은?"};
			
			for(String qText : questionTextList) {
				questionList.add(QuestionVO.builder().questionNo(questionList.size()).questionContent(qText).build());
			}

			ObjectRepository.save();
		}
	}
	
	public QuestionVO findQuestionByNo(int questionNo) {
		for(QuestionVO question : this.questionList) {
			if(questionNo == question.getQuestionNo()) {
				return question;
			}
		}
		return null;
	}
}


