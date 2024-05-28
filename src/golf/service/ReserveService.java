package golf.service;

import static golf.util.GolfPrints.*;
import static golf.util.GolfUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import golf.repository.ObjectRepository;
import golf.vo.FieldVO;
import golf.vo.ReserveVO;
import lombok.Data;

@Data
public class ReserveService{
	
	private ReserveService() {};
	
	private static final ReserveService reserveService = new ReserveService();
	
	public static ReserveService getInstance() {
		return reserveService;
	}

	private List<ReserveVO> reserveList;

	{
		reserveList = ObjectRepository.load("reserve");
		
		if(reserveList == null) {
			reserveList = new ArrayList<>();

			// 여기까진 게임 진행한 샘플 데이터
			reserveList.add(ReserveVO.builder().reserveNo(0).memberNo(1).fieldNo(0).reserveDate("02/12").reserveTime(1).isGamePlayed(false).regDate(new Date()).caddieOption(false).hotelOption(false).payAmount(600_000).build());
			reserveList.add(ReserveVO.builder().reserveNo(1).memberNo(1).fieldNo(0).reserveDate("02/14").reserveTime(1).isGamePlayed(false).regDate(new Date()).caddieOption(false).hotelOption(false).payAmount(600_000).build());
			reserveList.add(ReserveVO.builder().reserveNo(2).memberNo(1).fieldNo(0).reserveDate("03/24").reserveTime(2).isGamePlayed(false).regDate(new Date()).caddieOption(false).hotelOption(false).payAmount(600_000).build());
			reserveList.add(ReserveVO.builder().reserveNo(3).memberNo(1).fieldNo(1).reserveDate("04/22").reserveTime(2).isGamePlayed(false).regDate(new Date()).caddieOption(false).hotelOption(false).payAmount(300_000).build());
			reserveList.add(ReserveVO.builder().reserveNo(4).memberNo(1).fieldNo(1).reserveDate("05/12").reserveTime(1).isGamePlayed(false).regDate(new Date()).caddieOption(false).hotelOption(false).payAmount(300_000).build());
			
			ObjectRepository.save();
		}
	}
	
	// 예약 선택 관련 상태 저장
	private String selectReserveDate; // 선택한 예약 날짜
	private int selectReserveTime; // 선택한 예약 시간 (1부/2부)
	private int selectCaddieOption; // 선택한 캐디 옵션 -> 0을 안 고른 상태로 할 거고, 1은 캐디X, 2는 캐디O
	private int selectHotelOption; // 선택한 숙박 옵션 -> 0을 안 고른 상태로 할 거고, 1은 숙박X, 2는 숙박O
	private long finalPrice; // 현재 하는 예약의 최종 금액
	private boolean goBack; // 뒤로가기 누른 여부
	private ReserveVO playReserve; // 현재 게임 중인 예약
	private int nowReserveViewIndex; // 현재 보고 있는 예약의 번째 수
	private ReserveVO deleteReserve; // 현재 삭제할 예약
	private FieldVO gameField; // 현재 게임 중인 예약 필드
	
	public void main() {
		String input;
		
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		ReviewService reviewService = ReviewService.getInstance();
		GameService gameService = GameService.getInstance();
		
		while(true) {
			if(memberService.getNowLoginUser() == null) return;
			if(fieldService.getSelectedField() == null) return;
			
			if(goBack) {
				goBack = false;
				return;
			}
			
			printNav(memberService.getNowLoginUser().getMemberId());
			
			// 현재 선택 중인 골프장
			FieldVO field = fieldService.getSelectedField();

			System.out.println("				  예약하기");
			System.out.println("===========================================================================");
			System.out.println("골프장명		" + field.getFieldName());
			System.out.println("홀수			" + (field.isHoleCount() ? "18홀" : "9홀"));
			System.out.println("지역			" + field.getFieldRegion());
			System.out.println("전화번호		" + field.getFieldPhoneNumber());
			System.out.println("평균평점		" + reviewService.calcReviewStar(field.getFieldNo()) + " " + String.format("(%.1f / 5)", reviewService.calcAverageReview(field.getFieldNo())));
			System.out.println("기본금액		" + moneyFormat(field.getBasicAmount()));
			System.out.println("캐디금액		" + moneyFormat(field.getCaddieAmount()));
			System.out.println("숙박금액		" + moneyFormat(field.getHotelAmount()));
			System.out.print("예약일자		");
			
			List<String> reserveDateListWithDays = reserveDateListWithDays();
			List<String> reserveDateList = reserveDateList();
			
			for (int i = 0; i < reserveDateListWithDays.size(); i++) {
				System.out.print(reserveDateListWithDays.get(i) + "(" + (i + 1) + ")    ");
				
				if(i == 2) {
					System.out.print("\n			");
				}
			}
			System.out.println("\n===========================================================================");
			
			printFooterWithScoreRank();
			
			input = inputString("메뉴를 선택해주세요 > ");
			
			// 메뉴 누르기 전 예약 상태변수 초기화
			selectReserveDate = null;
			selectReserveTime = 0;
			selectCaddieOption = 0;
			selectHotelOption = 0;
			finalPrice = 0;
			
			switch(input) {
				case "1": // 오늘 + 1일
					selectReserveDate = reserveDateList.get(0);
					registerReserveMain();
					break;
				case "2": // 오늘 + 2일
					selectReserveDate = reserveDateList.get(1);
					registerReserveMain();
					break;
				case "3": // 오늘 + 3일
					selectReserveDate = reserveDateList.get(2);
					registerReserveMain();
					break;
				case "4": // 오늘 + 4일
					selectReserveDate = reserveDateList.get(3);
					registerReserveMain();
					break;
				case "5": // 오늘 + 5일
					selectReserveDate = reserveDateList.get(4);
					registerReserveMain();
					break;
				case "6": // 오늘 + 6일
					selectReserveDate = reserveDateList.get(5);
					registerReserveMain();
					break;
				case "s": // 스코어랭킹
					gameService.viewScoreRank();
					break;
				case "b": // 뒤로가기
					return;
				case "o": // 로그아웃
					memberService.setNowLoginUser(null);
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
		}
	}
	
	public void viewReserve() {
		String input;
		
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		GameService gameService = GameService.getInstance();

		nowReserveViewIndex = 0; // 다시 들어온 경우 첫번째 항목부터 보여져야 함
		
		while(true) {
			if(memberService.getNowLoginUser() == null) return;
			
			List<ReserveVO> filteredReserveList = new ArrayList<>();
			
			// 전체 reserveList에서 로그인한 회원의 예약만 뽑아서 저장
			for(ReserveVO reserve : this.reserveList) {
				if(reserve.getMemberNo() == memberService.getNowLoginUser().getMemberNo()) {
					filteredReserveList.add(reserve);
				}
			}

			int reserveCount = filteredReserveList.size();
			
			printNav(memberService.getNowLoginUser().getMemberId());
			
			System.out.println("				예약내역조회");
			System.out.println("===========================================================================");
			
			if(reserveCount == 0) { // 예약한 내역이 없을 경우
				
				System.out.println();
				System.out.println();
				System.out.println("			  예약하신 내역이 없습니다");
				System.out.println();
				System.out.println();

				System.out.println("===========================================================================");

				printFooter();
				
				input = inputString("메뉴를 선택해주세요 > ");
				
				switch(input) {
					case "b": // 뒤로가기
						return;
					case "o": // 로그아웃
						memberService.setNowLoginUser(null);
						return;
					default:
						System.out.println("메뉴를 제대로 입력해주세요");
						break;
				}
				
			}else { // 예약한 내역이 있을 경우
				Collections.reverse(filteredReserveList); // DESC 순으로 뒤집음
				
				ReserveVO nowReserve = filteredReserveList.get(nowReserveViewIndex);
				FieldVO nowField = fieldService.findFieldByNo(nowReserve.getFieldNo());

				System.out.println("골프장명		" + nowField.getFieldName());
				System.out.println("홀수			" + (nowField.isHoleCount() ? "18홀" : "9홀"));
				System.out.println("지역			" + nowField.getFieldRegion());
				System.out.println("전화번호		" + nowField.getFieldPhoneNumber());
				System.out.println("캐디옵션		" + (nowReserve.isCaddieOption() ? "선택함" : "선택하지 않음"));
				System.out.println("숙박옵션		" + (nowReserve.isHotelOption() ? "선택함" : "선택하지 않음"));
				System.out.println(String.format("예약일시		%s / %d부", formatDate(nowReserve.getReserveDate()), nowReserve.getReserveTime()));
				System.out.println("결제금액		" + moneyFormat(nowReserve.getPayAmount()));
				System.out.println("플레이여부		" + (nowReserve.isGamePlayed() ? "O" : "X"));

				System.out.println("===========================================================================");
				System.out.println(String.format("				   %d / %d", nowReserveViewIndex + 1, reserveCount));
				
				// 이전, 다음을 실제 이전, 이후 인덱스가 있는지에 따라 다르게 하기
				// 이전이 더 작은 값임 (-1), 이후는 +1
				boolean isExistPrev = nowReserveViewIndex > 0;
				boolean isExistNext = nowReserveViewIndex < reserveCount - 1;
				
				String prevText = isExistPrev ? "< 이전(1)" : "	";
				String nextText = isExistNext ? "다음(2) > " : "	";
				String prevNextText = prevText + "							  " + nextText;
				System.out.println(prevNextText);
				
				if(nowReserve.isGamePlayed()) { // 이미 플레이한 경우 예약취소 및 게임 불가
					printFooter();
				}else {
					printFooterForReserveView();
				}
				
				input = inputString("메뉴를 선택해주세요 > ");
				
				switch(input) {
					case "1": // 이전
						// filteredList의 이전 인덱스가 있을 경우에만 이동
						if(!isExistPrev) { // 이전이 없을 때
							System.out.println("메뉴를 제대로 입력해주세요");
							break;
						}
						nowReserveViewIndex--;
						break;
					case "2": // 다음
						// filteredList의 이후 인덱스가 있을 경우에만 이동
						if(!isExistNext) { // 이후가 없을 때
							System.out.println("메뉴를 제대로 입력해주세요");
							break;
						}
						nowReserveViewIndex++;
						break;
					case "c": // 예약취소
						if(nowReserve.isGamePlayed()) { // 이미 플레이한 경우
							System.out.println("메뉴를 제대로 입력해주세요");
							break;
						}
						this.deleteReserve = nowReserve;
						cancelReserve();
						
						// 현재 nowReserveViewIndex가 0보다 크면 1 뺌
						if(nowReserveViewIndex > 0) {
							nowReserveViewIndex--;
						}
						break;
					case "g": // 게임하기
						if(nowReserve.isGamePlayed()) { // 이미 플레이한 경우
							System.out.println("메뉴를 제대로 입력해주세요");
							break;
						}
						playReserve = nowReserve;
						gameField = nowField;
						gameService.main();
						
						break;
					case "b": // 뒤로가기
						return;
					case "o": // 로그아웃
						memberService.setNowLoginUser(null);
						return;
					default:
						System.out.println("메뉴를 제대로 입력해주세요");
						break;
				}
			}
		}
	}
	
	public void manageMain() {
		String input;
		
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		
		while (true) {
			if(memberService.getNowLoginUser() == null) return;

			printAdminNav();

			System.out.println(prettyString("예약 관리"));
			System.out.println("===========================================================================");
			System.out.println("번호   	아이디   	골프장명		예약일시	   게임여부");
			System.out.println("===========================================================================");
			
			for (ReserveVO reserve : reserveService.getReserveList()) {
				String fieldName = fieldService.findFieldByNo(reserve.getFieldNo()).getFieldName();
				String memberId = memberService.findMemberByNo(reserve.getMemberNo()).getMemberId();
				
				int fieldNameHangleCount = countHangle(fieldName);
				int fieldNameLength = 23 - fieldNameHangleCount;
				String isGamePlayText = reserve.isGamePlayed() ? "O" : "X";
				
				System.out.println(String.format("%-7d %-15s %-" + fieldNameLength + "s %-24s %s", reserve.getReserveNo() + 1, memberId, fieldName, formatDate(reserve.getReserveDate()), isGamePlayText));
			}
			
			System.out.println("===========================================================================");
			
			printFooter();
			
			input = inputString("메뉴를 선택해주세요 > ");

			switch (input) {
				case "b": // 뒤로가기
					return;
				case "o": // 로그아웃
					memberService.setNowLoginUser(null);
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					break;
			}
		}
	}
	
	public void registerReserveMain() {
		String input;
		
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		
		prettyMsg("예약날짜를 " + selectReserveDate + "으로 선택하셨습니다");
		
		while(true) {
			if(memberService.getNowLoginUser() == null) return;
			if(fieldService.getSelectedField() == null) return;
			
			if(goBack) {
				goBack = false;
				return;
			}
			
			if(selectReserveTime == 0) { // 예약시간을 안 골랐을 때
				
				boolean isFirstTimeReserved = isReserved(selectReserveDate, 1); // 해당 날짜의 1부 예약 여부
				boolean isSecondTimeReserved = isReserved(selectReserveDate, 2); // 해당 날짜의 2부 예약 여부
				
				String isFirstTimeReservedText = isFirstTimeReserved ? "(예약불가)" : "(예약가능)";
				String isSecondTimeReservedText = isSecondTimeReserved ? "(예약불가)" : "(예약가능)";

				System.out.println(String.format("예약시간선택 : 1부%s(1)   2부%s(2)   예약취소(3)", isFirstTimeReservedText, isSecondTimeReservedText));
				
				input = inputString("예약시간을 선택해주세요 > ");
				
				switch(input) {
					case "1": // 1부
						if(isFirstTimeReserved) {
							System.out.println("해당 시간은 현재 예약이 불가능합니다. 다시 선택해주세요.");
							continue;
						}
						selectReserveTime = 1;
						prettyMsg("예약시간을 1부로 선택하셨습니다");
						break;
					case "2": // 2부
						if(isSecondTimeReserved) {
							System.out.println("해당 시간은 현재 예약이 불가능합니다. 다시 선택해주세요.");
							continue;
						}
						selectReserveTime = 2;
						prettyMsg("예약시간을 2부로 선택하셨습니다");
						break;
					case "3": // 취소
						// 취소하면 모든 선택 상태변수 초기화하고 돌아감
						selectReserveDate = null;
						selectReserveTime = 0;
						selectCaddieOption = 0;
						selectHotelOption = 0;
						return;
					case "b": // 뒤로가기
						goBack = true;
						return;
					case "o": // 로그아웃
						memberService.setNowLoginUser(null);
						return;
					default:
						System.out.println("메뉴를 제대로 입력해주세요");
						continue; // 처음부터 다시
				}
			}
			
			if(selectCaddieOption == 0) { // 캐디 옵션을 안 골랐을 때
				System.out.println("캐디옵션선택 : 예(1)   아니오(2)   예약취소(3)");
				input = inputString("캐디 옵션을 추가하시겠습니까? > ");
				
				switch(input) {
				case "1": // 캐디옵션 있음
					selectCaddieOption = 2;
					prettyMsg("캐디 옵션을 선택하셨습니다");
					break;
				case "2": // 캐디옵션 없음
					selectCaddieOption = 1;
					prettyMsg("캐디 옵션을 선택하지 않으셨습니다");
					break;
				case "3": // 취소
					// 취소하면 모든 선택 상태변수 초기화하고 돌아감
					selectReserveDate = null;
					selectReserveTime = 0;
					selectCaddieOption = 0;
					selectHotelOption = 0;
					return;
				case "b": // 뒤로가기
					goBack = true;
					return;
				case "o": // 로그아웃
					memberService.setNowLoginUser(null);
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					continue; // 처음부터 다시

				}
			}

			if(selectHotelOption == 0) { // 호텔 옵션을 안 골랐을 때
				System.out.println("숙박옵션선택 : 예(1)   아니오(2)   예약취소(3)");
				input = inputString("숙박 옵션을 추가하시겠습니까? > ");
				
				switch(input) {
				case "1": // 숙박옵션 있음
					selectHotelOption = 2;
					prettyMsg("숙박 옵션을 선택하셨습니다");
					break;
				case "2": // 숙박옵션 없음
					selectHotelOption = 1;
					prettyMsg("숙박 옵션을 선택하지 않으셨습니다");
					break;
				case "3": // 취소
					// 취소하면 모든 선택 상태변수 초기화하고 돌아감
					selectReserveDate = null;
					selectReserveTime = 0;
					selectCaddieOption = 0;
					selectHotelOption = 0;
					return;
				case "b": // 뒤로가기
					goBack = true;
					return;
				case "o": // 로그아웃
					memberService.setNowLoginUser(null);
					return;
				default:
					System.out.println("메뉴를 제대로 입력해주세요");
					continue; // 처음부터 다시

				}
			}
			
			// 여기까지 왔다는 건 예약 선택을 전부 했다는 것
			// 여기의 정보는 다음 화면에서도 쓸 거라 아직 초기화하지 말 것
			registerReserveCheck();
		}
	}
	
	public void registerReserveCheck() {
		String input;
		
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		GradeService gradeService = GradeService.getInstance();
		
		while(true) {
			if(memberService.getNowLoginUser() == null) return;
			if(fieldService.getSelectedField() == null) return;

			printNav(memberService.getNowLoginUser().getMemberId());

			// 현재 선택 중인 골프장
			FieldVO field = fieldService.getSelectedField();
			
			System.out.println("			       예약최종확인");
			System.out.println("==========================================================================");
			System.out.println("골프장명		" + field.getFieldName());
			System.out.println("홀수			" + (field.isHoleCount() ? "18홀" : "9홀"));
			System.out.println("지역			" + field.getFieldRegion());
			System.out.println("전화번호		" + field.getFieldPhoneNumber());
			System.out.println("기본금액		" + moneyFormat(field.getBasicAmount()));
			
			String caddieOptionText;
			String hotelOptionText;
			
			if(selectCaddieOption == 2) {
				caddieOptionText = String.format("선택함 (+%s)", moneyFormat(field.getCaddieAmount()));
			}else {
				caddieOptionText = "선택하지 않음";
			}

			if(selectHotelOption == 2) {
				hotelOptionText = String.format("선택함 (+%s)", moneyFormat(field.getHotelAmount()));
			}else {
				hotelOptionText = "선택하지 않음";
			}
			
			System.out.println("캐디옵션		" + caddieOptionText);
			System.out.println("숙박옵션		" + hotelOptionText);
			System.out.println(String.format("예약일시		%s / %d부", formatDate(selectReserveDate), selectReserveTime));
			
			long totalPrice = field.getBasicAmount() + (selectCaddieOption == 2 ? field.getCaddieAmount() : 0) + (selectHotelOption == 2 ? field.getHotelAmount() : 0);
			int saleRate = gradeService.findGradeByNo(memberService.getNowLoginUser().getGradeNo()).getDiscountRate();
			long salePrice = (long)(totalPrice * ((double)saleRate / 100));
			long finalPrice = totalPrice - salePrice;
			
			System.out.println("합계금액		" + moneyFormat(totalPrice));
			System.out.println(String.format("할인금액		-%s (%d%%)", moneyFormat(salePrice), saleRate));
			System.out.println("최종금액		" + moneyFormat(finalPrice));
			
			System.out.println("===========================================================================");

			this.finalPrice = finalPrice;
			printFooterWithReserve();
			
			input = inputString("메뉴를 선택해주세요 > ");

			// 여기서 예약완료 시 예약 리스트에 추가 후 영속화 후 현재 선택 중인 골프장 초기화 및 예약 관련 상태 초기화 후 메인까지 올라가야 함 (왜냐면 스택 구조이기 때문이고, 여기서 바로 예약내역조회로 가면 문제 있음. 그래서 안내문 띄운 후 예약내역조회에서 확인해달라고 하기)
			
			switch(input) {
			case "r": // 예약하기
				registerReserve();
				break;
			case "b": // 뒤로가기
				goBack = true;
				return;
			case "o": // 로그아웃
				memberService.setNowLoginUser(null);
				return;
			default:
				System.out.println("메뉴를 제대로 입력해주세요");
				break;
			}
			
		}
	}
	
	private void registerReserve() {
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		
		// 할 일 : 예약 등록, 예약 결제금액에 따른 해당 회원의 누적결제액 변경 및 회원등급 수정
		
		boolean caddieOption = selectCaddieOption == 2 ? true : false;
		boolean hotelOption = selectHotelOption == 2 ? true : false;
		
		ReserveVO reserve = ReserveVO.builder()
									.reserveNo(reserveList.size())
									.memberNo(memberService.getNowLoginUser().getMemberNo())
									.fieldNo(fieldService.getSelectedField().getFieldNo())
									.reserveDate(selectReserveDate)
									.reserveTime(selectReserveTime)
									.isGamePlayed(false)
									.regDate(new Date())
									.caddieOption(caddieOption)
									.hotelOption(hotelOption)
									.payAmount(this.finalPrice)
									.build();
									
		this.reserveList.add(reserve); // 예약을 등록
		// 회원의 누적결제액 변경 및 회원등급 재설정
		memberService.updateTotalAmountAndGrade(this.finalPrice);
		
		// 영속화
		ObjectRepository.save();
		
		// 거슬러 올라가기 전 예약 완료 메시지 작성
		prettyMsg(String.format("%s (%d부) 날짜로 %s 예약이 완료되었습니다. 감사합니다.", selectReserveDate, selectReserveTime, fieldService.getSelectedField().getFieldName()));
		
		// 예약 관련 상태변수 전부 초기화 후 메인 화면까지 감
		selectReserveDate = null;
		selectReserveTime = 0;
		selectCaddieOption = 0;
		selectHotelOption = 0;
		finalPrice = 0;
		
		// 그리고 FieldService의 selectedField의 값을 null로 바꿈으로써 골프장 리스트 페이지까지 while문 스택을 거슬러 올라갈 것임
		fieldService.setSelectedField(null);
		
		// 회원 메인페이지까지 갈 것임
		fieldService.setGoHome(true);
	}
	
	private void cancelReserve() {
		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		
		// 할 일 : 해당 예약내역 삭제 및 해당 회원의 누적결제액 변경 및 회원등급 수정
		
		// 예약내역 삭제
		if(deleteReserve != null) {
			reserveList.remove(deleteReserve);
		}

		// 회원의 누적결제액 변경 및 회원등급 재설정
		memberService.updateTotalAmountAndGrade(deleteReserve.getPayAmount() * -1);
		
		// 영속화
		ObjectRepository.save();

		// 거슬러 올라가기 전 예약 완료 메시지 작성
		prettyMsg(String.format("%s (%d부) 날짜의 %s 예약이 취소되었습니다.", deleteReserve.getReserveDate(), deleteReserve.getReserveTime(), fieldService.findFieldByNo(deleteReserve.getFieldNo()).getFieldName()));
		
		deleteReserve = null;
	}
	
	private boolean isReserved(String date, int time) {
		for (ReserveVO reserve : reserveList) {
			// 해당 일자 및 시간에 예약이 되어있으면서 플레이하지 않은 예약이 있는지 확인
			if(reserve.getReserveDate().equals(date) && reserve.getReserveTime() == time && reserve.isGamePlayed() == false) {
				// 예약이 되어 있는 시간인 경우
				return true;
			}
		}
		
		return false;
	}
	
	public ReserveVO findReserveByNo(int reserveNo) {
		for (ReserveVO reserve : reserveList) {
			if(reserveNo == reserve.getReserveNo()) {
				return reserve;
			}
		}
		return null;
	}
}


















