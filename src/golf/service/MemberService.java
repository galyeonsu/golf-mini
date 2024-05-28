package golf.service;

import static golf.util.GolfPrints.*;
import static golf.util.GolfUtils.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import golf.repository.ObjectRepository;
import golf.vo.GradeVO;
import golf.vo.MemberVO;
import golf.vo.QuestionVO;
import lombok.Data;

@Data
public class MemberService{
	
	private MemberService() {};
	
	private static final MemberService memberService = new MemberService();
	
	public static MemberService getInstance() {
		return memberService;
	}
	
	private List<MemberVO> memberList;
	
	{
		memberList = ObjectRepository.load("member");
		
		if(memberList == null) {
			memberList = new ArrayList<>();
			
			// 관리자 테스트 아이디
			memberList.add(MemberVO.builder()
						.memberNo(memberList.size())
						.memberId("admin")
						.memberPw("1234")
						.memberName("관리자")
						.regDate(new Date())
						.gradeNo(3)
						.totalAmount(0)
						.questionNo(0)
						.questionAnswer("테스트입니다")
						.build());
			
			// 일반회원 테스트 아이디
			memberList.add(MemberVO.builder()
						.memberNo(memberList.size())
						.memberId("jungmin")
						.memberPw("1234")
						.memberName("정민")
						.regDate(new Date())
						.gradeNo(0)
						.totalAmount(0)
						.questionNo(0)
						.questionAnswer("테스트입니다")
						.build());
			
			// 일반회원 테스트 아이디2
			memberList.add(MemberVO.builder()
						.memberNo(memberList.size())
						.memberId("jungmin2")
						.memberPw("1234")
						.memberName("정민")
						.regDate(new Date())
						.gradeNo(0)
						.totalAmount(0)
						.questionNo(0)
						.questionAnswer("테스트입니다")
						.build());
			
			ObjectRepository.save();
		}
	}
	
	// 회원가입 관련 상태 저장
	private String memberId;
	private String memberPw;
	private String memberName;
	
	// 현재 로그인한 회원 저장
	private MemberVO nowLoginUser;
	
	private boolean goHome; // 메인으로 가는 여부 (예약하기가 끝나는 부분 등에서 사용됨)
	
	public void main() {
		
		String input;
		while(true) {
			if(nowLoginUser != null) {
				if(nowLoginUser.getGradeNo() >= 3) { // 관리자
					adminMain();
				}else { // 일반회원
					userMain();
				}
			}
			
			printHeader();
			System.out.println("				  메인메뉴");
			System.out.println();
			System.out.println("				1. 회원가입");
			System.out.println("				2. 로그인");
			System.out.println("				3. 비밀번호 찾기");
			printFooterNoBack();
			
			input = inputString("메뉴를 선택해주세요 > ");

			switch(input) {
				case "1": // 회원가입
					printHeader();
					System.out.println("				 회원가입");
					printFooter();
					
					memberId = null;
					memberPw = null;
					memberName = null;
					register();
					break;
				case "2": // 로그인
					printHeader();
					System.out.println("				  로그인");
					printFooter();
					
					login();
					if(nowLoginUser != null) {
						if(nowLoginUser.getGradeNo() >= 3) { // 관리자
							adminMain();
						}else { // 일반회원
							userMain();
						}
					}
					break;
				case "3": // 비밀번호 찾기
					printHeader();
					System.out.println("			       비밀번호 찾기");
					printFooter();
					
					searchPw();
					break;
					
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
			
		}
	}
	
	public void userMain() {
		String input;
		
		FieldService fieldService = FieldService.getInstance();
		ReserveService reserveService = ReserveService.getInstance();
		
		while(true) {
			if(nowLoginUser == null) return;
			
			printNav(memberService.getNowLoginUser().getMemberId());
			System.out.println("			     " + memberService.getNowLoginUser().getMemberName() + "님, 환영합니다!");
			System.out.println();
			System.out.println("			    1. 예약하기 (골프장검색)");
			System.out.println("			    2. 예약내역조회");
			System.out.println("			    3. 마이페이지");
			printFooterNoBack();
			
			input = inputString("메뉴를 선택해주세요 > ");

			switch(input) {
				case "1": // 예약하기 (골프장검색)
					fieldService.main();
					break;
				case "2": // 예약내역조회
					reserveService.viewReserve();
					break;
				case "3": // 마이페이지
					mypageMain();
					break;
				case "o": // 로그아웃
					nowLoginUser = null;
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
		}
		
	}
	
	public void adminMain() {
		String input;
		
		FieldService fieldService = FieldService.getInstance();
		ReserveService reserveService = ReserveService.getInstance();
		
		while(true) {
			if(nowLoginUser == null) return;
			
			printAdminNav();
			System.out.println("			    관리자님, 환영합니다!");
			System.out.println();
			System.out.println("			      1. 골프장관리");
			System.out.println("			      2. 예약관리");
			System.out.println("			      3. 회원관리");
			printFooterNoBack();
			
			input = inputString("메뉴를 선택해주세요 > ");
			
			switch(input) {
				case "1": // 골프장관리
					fieldService.manageMain();
					break;
				case "2": // 예약관리
					reserveService.manageMain();
					break;
				case "3": // 회원관리
					memberService.manageMain();
					break;
				case "o": // 로그아웃
					nowLoginUser = null;
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
		}
	}

	public void mypageMain() {
		String input;
		
		GradeService gradeService = GradeService.getInstance();
		ReserveService reserveService = ReserveService.getInstance();
		GameService gameService = GameService.getInstance();

		while (true) { // 제대로 안 고르면 계속 나옴
			if(nowLoginUser == null) return;

			printNav(memberService.getNowLoginUser().getMemberId());
			
			// 현재 로그인한 회원의 등급 번호에 따라 다르게 표시
			int gradeNo = memberService.getNowLoginUser().getGradeNo();
			GradeVO grade = gradeService.findGradeByNo(gradeNo);

			System.out.println(String.format("			    회원등급 : %s", grade.getGradeName()));
			System.out.println(String.format("			    누적결제액 : %s", moneyFormat(memberService.getNowLoginUser().getTotalAmount())));
			System.out.println(String.format("			    할인율 : %d%%", grade.getDiscountRate()));
			System.out.println();
			System.out.println("			    1. 예약내역조회");
			System.out.println("			    2. 게임기록조회");
			System.out.println("			    3. 비밀번호변경");
			
			printFooter();

			input = inputString("메뉴를 선택해주세요 > ");
			
			switch(input) {
				case "1": // 예약내역조회
					reserveService.viewReserve();
					break;
				case "2": // 게임기록조회
					gameService.viewGameList();
					break;
				case "3": // 비밀번호변경
					changePw();
					break;
				case "b": // 뒤로가기
					return;
				case "o": // 로그아웃
					nowLoginUser = null;
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
		}
	}
	
	public void manageMain() {
		String input;
		
		GradeService gradeService = GradeService.getInstance();
		while (true) {
			if (nowLoginUser == null)
				return;
			
			List<MemberVO> memberVOs = memberList;
			printAdminNav();
			
			System.out.println(prettyString("회원 관리"));
			System.out.println("===========================================================================");
			System.out.println("번호    등급          아이디         이름               가입일");
			System.out.println("===========================================================================");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			for (MemberVO member : memberVOs) {
				
				String gradeName = gradeService.findGradeByNo(member.getGradeNo()).getGradeName();
				
				int gradeNameHangleCount = countHangle(gradeName);
				int gradeNameLength = 13 - gradeNameHangleCount;
				
				int memberNameHangleCount = countHangle(member.getMemberName());
				int memberNameLength = 18 - memberNameHangleCount;
				
				System.out.println(String.format("%-7d %-" + gradeNameLength + "s %-14s %-" + memberNameLength + "s %s", member.getMemberNo()+1, gradeName, member.getMemberId(), member.getMemberName(), sdf.format(member.getRegDate())));
			}

			System.out.println("===========================================================================");
			
			printFooter();
			
			input = inputString("메뉴를 선택해주세요 > ");

			switch (input) {
				case "b": // 뒤로가기
					return;
				case "o": // 로그아웃
					nowLoginUser = null;
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
		}
	}
	
	private void register() {
		QuestionService questionService = QuestionService.getInstance();
		
		// 모든 파라미터 제대로 받을 때까지 반복함
		while(true) {
			if(memberId == null) {
				String memberId = inputString("아이디를 입력해주세요 > ");
				
				if(inputBack(memberId)) {
					return;
				}
			
				if(!checkId(memberId)) {
					System.out.println("아이디는 6 ~ 12글자 영문 대소문자 및 숫자로 입력해주세요");
					continue; // 다시 처음으로 감
				}
				
				if(hasDuplicateId(memberId)) {
					System.out.println("이미 존재하는 아이디입니다");
					continue;
				}
				
				// 제대로 입력받았다면
				this.memberId = memberId;
			}
			
			if(memberPw == null) {
				String memberPw = inputString("비밀번호를 입력해주세요 > ");

				if(inputBack(memberPw)) {
					return;
				}
				
				if(!checkPw(memberPw)) {
					System.out.println("비밀번호는 8 ~ 16글자 영문 대소문자, 숫자, 특수문자(!@#$%^&*())를 각각 1개 이상 포함하여 입력해주세요");
					continue;
				}
				
				this.memberPw = memberPw;
			}
			
			if(memberName == null) {
				String memberName = inputString("이름을 입력해주세요 > ");

				if(inputBack(memberName)) {
					return;
				}
				
				if(!checkHangle(memberName) || memberName.length() < 2 || memberName.length() > 4) {
					System.out.println("이름은 2 ~ 4글자 완성형 한글로 입력해주세요");
					continue;
				}
				
				this.memberName = memberName;
			}
			
			// 질문 테이블에서 가져옴
			List<QuestionVO> questionList = questionService.getQuestionList();
			Collections.shuffle(questionList); // 섞음
			
			QuestionVO question = questionList.get(0); // 질문 하나 꺼냄
			
			int questionNo = question.getQuestionNo();
			String questionContent = question.getQuestionContent();
			
			String questionAnswer = inputString(questionContent + " > ");

			if(inputBack(questionAnswer)) {
				return;
			}
			
			// 여기까지 정상적으로 됐으면 Member 생성 후 List에 넣고 break
			MemberVO memberVO = MemberVO.builder()
								.memberNo(this.memberList.size())
								.memberId(this.memberId)
								.memberPw(this.memberPw)
								.memberName(this.memberName)
								.regDate(new Date())
								.gradeNo(0)
								.totalAmount(0)
								.questionNo(questionNo)
								.questionAnswer(questionAnswer)
								.build();
			
			memberList.add(memberVO);
			
			// 회원가입 완료
			
			// 영속화
			ObjectRepository.save();

			prettyMsg("회원가입에 성공했습니다");
			break;
		}
	}
	
	private void login() {
		while(true) {
			
			String id = inputString("아이디를 입력해주세요 > ");
			
			if(inputBack(id)) {
				return;
			}
			
			MemberVO loginMember = findMemberById(id);
			
			if(loginMember == null) {
				System.out.println("존재하지 않는 아이디입니다");
				continue;
			}
			
			String pw = inputString("비밀번호를 입력해주세요 > ");
			
			if(inputBack(pw)) {
				return;
			}
			
			if(pw.equals(loginMember.getMemberPw())) {
				// 로그인 성공 시
				nowLoginUser = loginMember;
				prettyMsg("로그인에 성공했습니다");
				break;
			}else {
				System.out.println("비밀번호가 일치하지 않습니다");
				continue;
			}
			
		}
	}
	
	private void searchPw() {
		QuestionService questionService = QuestionService.getInstance();
		
		while(true) {
			String id = inputString("아이디를 입력해주세요 > ");

			if(inputBack(id)) {
				return;
			}
			
			MemberVO loginMember = findMemberById(id);
			
			if(loginMember == null) {
				System.out.println("존재하지 않는 아이디입니다");
				continue;
			}
			
			// 있으면 해당 member의 비밀번호 찾기 질문을 가져옴
			int questionNo = loginMember.getQuestionNo();
			
			// 해당 질문번호의 질문 내용을 가져옴
			String questionContent = questionService.findQuestionByNo(questionNo).getQuestionContent();
			
			String answer = inputString(questionContent + " > ");

			if(inputBack(answer)) {
				return;
			}
			
			if(answer.equals(loginMember.getQuestionAnswer())) {
				// 비밀번호 재설정을 함
				
				while(true) {
					String pw = inputString("변경할 비밀번호를 입력해주세요 > ");

					if(inputBack(pw)) {
						return;
					}
					
					if(!checkPw(pw)) {
						System.out.println("비밀번호는 8 ~ 16글자 영문 대소문자, 숫자, 특수문자(!@#$%^&*())를 각각 1개 이상 포함하여 입력해주세요");
						continue;
					}
					
					// 정상적으로 입력받았을 시
					loginMember.changePw(pw);
					break;
				}
				
				// 여기까지 비밀번호 재설정 완료.
			}else { // 틀렸을 경우
				System.out.println("비밀번호 답변이 맞지 않습니다");
				continue;
			}

			// 영속화
			ObjectRepository.save();
			
			prettyMsg("비밀번호 재설정에 성공했습니다");
			return;
		}

	}
	
	
	private void changePw() {
		while(true) {
			String pw = inputString("변경할 비밀번호를 입력해주세요 > ");
			if(inputBack(pw)) {
				return;
			}

			if(inputLogout(pw)) {
				nowLoginUser = null;
				return;
			}
			
			if(!checkPw(pw)) {
				System.out.println("비밀번호는 8 ~ 16글자 영문 대소문자, 숫자, 특수문자(!@#$%^&*())를 각각 1개 이상 포함하여 입력해주세요");
				continue;
			}
			
			nowLoginUser.changePw(pw);

			// 영속화
			ObjectRepository.save();
			
			prettyMsg("비밀번호 변경에 성공했습니다");
			break;
		}
	}
	
	public void updateTotalAmountAndGrade(long amount) {
		GradeService gradeService = GradeService.getInstance();
		
		nowLoginUser.updateTotalAmount(amount); // 누적결제액 변경 (예약 취소의 경우 -가 됨)
		
		// 등급 테이블을 순회하며 해당 회원이 어느 등급으로 업데이트되어야 하는지 확인
		// 단, 관리자는 안 되고 0 ~ 2까지만 확인해야 함
		
		// 일반회원 ~ 우수회원까지 확인
		// VIP회원부터 거꾸로 확인해야 한다 (당연)
		for (int i = 2; i >= 0; i--) {
			GradeVO nowGrade = gradeService.getGradeList().get(i);
			
			// 현재 회원의 누적결제액이 해당 회원등급에 도달할만큼 큰 경우
			if(nowLoginUser.getTotalAmount() >= nowGrade.getRequireAmount()) {
				nowLoginUser.updateGrade(nowGrade.getGradeNo());
				return;
			}
		}
		
	}
	
	private boolean hasDuplicateId(String id) {
		for(MemberVO member : this.memberList) {
			if(id.equals(member.getMemberId())) {
				return true;
			}
		}
		return false;
	}
	
	public MemberVO findMemberById(String memberId) {
		for(MemberVO member : this.memberList) {
			if(memberId.equals(member.getMemberId())) {
				return member;
			}
		}
		return null;
	}

	public MemberVO findMemberByNo(int memberNo) {
		for(MemberVO member : this.memberList) {
			if(memberNo == member.getMemberNo()) {
				return member;
			}
		}
		return null;
	}
}












