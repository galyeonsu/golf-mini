package golf.service;

import lombok.Data;

@Data
public class GolfService{

	/**
	 * 최초 진입점에서 미리 로드하여 영속화 관련 문제를 해결하였다
	 * 이후 다른 서비스 내에서는 필요할 때만 메서드 안에서 초기화를 해주면
	 * 동시 상호참조 초기화로 인한 null이 뜨는 것을 방지할 수 있다.
	 */
	
	private MemberService memberService = MemberService.getInstance();
	private GradeService gradeService = GradeService.getInstance();
	private QuestionService questionService = QuestionService.getInstance();
	private FieldService fieldService = FieldService.getInstance();
	private ReviewService reviewService = ReviewService.getInstance();
	private ReserveService reserveService = ReserveService.getInstance();
	private GameService gameService = GameService.getInstance();
	
	// 앱 시작
	public void start() {
		memberService.main();
	}

}
