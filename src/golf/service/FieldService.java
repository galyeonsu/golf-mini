package golf.service;

import static golf.util.GolfPrints.*;
import static golf.util.GolfUtils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import golf.repository.ObjectRepository;
import golf.util.GolfUtils;
import golf.vo.FieldVO;
import lombok.Data;

@Data
public class FieldService {

	private FieldService() {
	};

	private static final FieldService fieldService = new FieldService();

	public static FieldService getInstance() {
		return fieldService;
	}

	private List<FieldVO> fieldList;

	{
		fieldList = ObjectRepository.load("field");

		if (fieldList == null) {
			fieldList = new ArrayList<>();

			// 샘플 데이터 추가
			fieldList.add(FieldVO.builder().fieldNo(fieldList.size()).fieldName("가평베네스트").holeCount(true)
					.holeInfo("3|4|3|3|4|3|3|5|3|3|4|3|3|4|3|3|4|5").holeSum(63).fieldRegion("경기").fieldPhoneNumber("031-589-8001")
					.regDate(new Date()).basicAmount(600_000).caddieAmount(250_000).hotelAmount(210_000).build());

			fieldList.add(FieldVO.builder().fieldNo(fieldList.size()).fieldName("라데나").holeCount(true)
					.holeInfo("3|4|3|3|4|3|3|5|3|3|4|3|3|4|3|3|4|5").holeSum(63).fieldRegion("강원").fieldPhoneNumber("033-260-1114")
					.regDate(new Date()).basicAmount(300_000).caddieAmount(150_000).hotelAmount(200_000).build());

			fieldList.add(FieldVO.builder().fieldNo(fieldList.size()).fieldName("롯데스카이힐제주").holeCount(false)
					.holeInfo("3|4|3|3|4|3|3|5|3").holeSum(31).fieldRegion("제주").fieldPhoneNumber("064-731-2000")
					.regDate(new Date()).basicAmount(400_000).caddieAmount(150_000).hotelAmount(120_000).build());

			fieldList.add(FieldVO.builder().fieldNo(fieldList.size()).fieldName("곤지암").holeCount(true)
					.holeInfo("3|4|3|3|4|3|3|5|3|3|4|3|3|4|3|3|4|5").holeSum(63).fieldRegion("경기").fieldPhoneNumber("031-760-3555")
					.regDate(new Date()).basicAmount(500_000).caddieAmount(200_000).hotelAmount(210_000).build());

			fieldList.add(FieldVO.builder().fieldNo(fieldList.size()).fieldName("설해원").holeCount(true)
					.holeInfo("3|4|3|3|4|3|3|5|3|3|4|3|3|4|3|3|4|5").holeSum(63).fieldRegion("강원").fieldPhoneNumber("033-670-7700")
					.regDate(new Date()).basicAmount(500_000).caddieAmount(100_000).hotelAmount(300_000).build());

			fieldList.add(FieldVO.builder().fieldNo(fieldList.size()).fieldName("엘리시안제주").holeCount(false)
					.holeInfo("3|4|3|3|4|3|3|5|3").holeSum(31).fieldRegion("제주").fieldPhoneNumber("064-798-7000")
					.regDate(new Date()).basicAmount(300_000).caddieAmount(100_000).hotelAmount(100_000).build());
			

			ObjectRepository.save();
		}
	}

	private String selectedRegion; // 현재 고른 지역
	private String searchWord; // 검색어
	private int page; // 현재 페이지
	private int maxPage; // 현재 검색한 리스트의 최대 페이지 (페이지당 5개)
	private FieldVO selectedField; // 상세보기로 고른 골프장

	private boolean goHome; // 메인으로 가는 여부 (예약하기가 끝나는 부분 등에서 사용됨)
	private boolean goBack; // 뒤로가기 여부

	public void main() {
		String input;

		// 이렇게 인스턴스를 가져오면 MemberService쪽에서 이미 초기화가 끝난 모든 클래스 중 원하는 객체를 불러와서 사용할 수 있다
		MemberService memberService = MemberService.getInstance();

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;

			if (goHome) {
				goHome = false;
				return;
			}

			printNav(memberService.getNowLoginUser().getMemberId());

			System.out.println("				  예약하기");
			System.out.println();
			System.out.println("				1. 전체보기");
			System.out.println("				2. 지역분류");
			System.out.println("				3. 이름검색");
			printFooter();

			input = inputString("메뉴를 선택해주세요 > ");

			// 메뉴 누르기 전 검색 상태변수 초기화
			selectedRegion = null;
			searchWord = null;

			switch (input) {
			case "1": // 전체보기
				viewFieldList();
				break;
			case "2": // 지역분류
				selectRegion();
				break;
			case "3": // 이름검색
				searchName();
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

	public void selectRegion() {
		String input;

		MemberService memberService = MemberService.getInstance();

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;

			if (goHome)
				return;

			printNav(memberService.getNowLoginUser().getMemberId());
			System.out.println("				  지역분류");
			System.out.println();
			System.out.println("				  1. 경기");
			System.out.println("				  2. 강원");
			System.out.println("				  3. 제주");
			printFooter();

			input = inputString("검색하실 지역을 골라주세요 > ");

			switch (input) {
			case "1": // 경기
				selectedRegion = "경기";
				viewFieldList();
				break;
			case "2": // 강원
				selectedRegion = "강원";
				viewFieldList();
				break;
			case "3": // 제주
				selectedRegion = "제주";
				viewFieldList();
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

	public void searchName() {
		String input;

		MemberService memberService = MemberService.getInstance();

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;

			if (goHome)
				return;

			printNav(memberService.getNowLoginUser().getMemberId());
			System.out.println("				  이름검색");
			printFooter();

			input = inputString("검색어를 입력해주세요 > ");

			switch (input) {
			case "b": // 뒤로가기
				return;
			case "o": // 로그아웃
				memberService.setNowLoginUser(null);
				return;
			default:
				searchWord = input;
				viewFieldList();
				break;
			}
		}
	}

	public void viewFieldList() {
		String input;

		MemberService memberService = MemberService.getInstance();
		ReviewService reviewService = ReviewService.getInstance();
		ReserveService reserveService = ReserveService.getInstance();

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;

			if (goHome)
				return;

			// 일단 전부 가져와서 거기서 상태변수에 따라 필터링함
			List<FieldVO> filteredList = new ArrayList<>();

			if (selectedRegion != null) { // 지역분류의 경우
				for (FieldVO field : this.fieldList) {
					if (field.getFieldRegion().equals(selectedRegion)) {
						filteredList.add(field);
					}
				}
			} else if (searchWord != null) { // 이름검색의 경우
				for (FieldVO field : this.fieldList) {
					if (field.getFieldName().contains(searchWord)) {
						filteredList.add(field);
					}
				}
			} else { // 전체인 경우
				filteredList = this.fieldList;
			}

			printNav(memberService.getNowLoginUser().getMemberId());

			if (searchWord != null) { // 이름으로 검색한 경우
				System.out.println(String.format("			    '%s' 검색결과", searchWord));
			} else if (selectedRegion != null) {
				System.out.println(String.format("			     %s 지역 골프장", selectedRegion));
			} else {
				System.out.println("			    전체 골프장 리스트");
			}

			System.out.println("===========================================================================");
			System.out.println("번호	[이름]			<지역>		전화번호	   평균평점");
			System.out.println("===========================================================================");

			if (filteredList.size() == 0) {
				System.out.println();
				System.out.println();
				System.out.println("                  해당 조건의 골프장이 존재하지 않습니다");
				System.out.println();
				System.out.println();
			} else {
				for (FieldVO field : filteredList) {
					int hangleCount = GolfUtils.countHangle(field.getFieldName());
					int nameLength = 23 - hangleCount;

					// 해당 골프장 번호로 평균 평점 얻음
					double reviewAvg = reviewService.calcAverageReview(field.getFieldNo());
					String reviewAvgText = String.format("%.1f / 5", reviewAvg);

					System.out
							.println(String.format("%-7d %-" + nameLength + "s %-13s %-19s %s", field.getFieldNo() + 1,
									"[" + field.getFieldName() + "]", "<" + field.getFieldRegion() + ">", field.getFieldPhoneNumber(), reviewAvgText));
				}
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
				// 여기서 만약 해당 입력에 대한 게 숫자이고, 그 인덱스의 필드가 존재하면 상세보기로 가기
				// 숫자로만 이뤄졌는지 확인
				if (GolfUtils.checkNumber(input) && input.length() > 0) { // 숫자로만 이뤄진 경우
					int fieldNo = Integer.parseInt(input) - 1;

					FieldVO nowField = null;

					// 근데 골프장 전체에서 찾는 게 아니라, 현재 리스트 내에서 찾아야 함
					for (FieldVO field : filteredList) {
						if (fieldNo == field.getFieldNo()) {
							nowField = field;
						}
					}

					if (nowField == null) {
						System.out.println("번호를 제대로 입력해주세요");
					} else {
						// 선택한 골프장 설정 후 넘어간다
						selectedField = nowField;
						reserveService.main();
					}
				} else {
					System.out.println("메뉴를 제대로 입력해주세요");
				}
				break;
			}
		}
	}

	public void manageMain() {
		MemberService memberService = MemberService.getInstance();
		ReviewService reviewService = ReviewService.getInstance();

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;
			printAdminNav();
			System.out.println("				골프장관리");
			System.out.println("								등록하기(r)");
			System.out.println("===========================================================================");
			System.out.println("번호	[이름]			<지역>		전화번호	   평균평점");
			System.out.println("===========================================================================");
			for (FieldVO field : this.fieldList) {
				int hangleCount = GolfUtils.countHangle(field.getFieldName());
				int nameLength = 23 - hangleCount;
				double reviewAvg = reviewService.calcAverageReview(field.getFieldNo());
				String reviewAvgText = String.format("%.1f / 5", reviewAvg);
				System.out.println(String.format("%-7d %-" + nameLength + "s %-13s %-19s %s", field.getFieldNo() + 1,
						"[" + field.getFieldName() + "]", "<" + field.getFieldRegion() + ">", field.getFieldPhoneNumber(), reviewAvgText));
			}
			System.out.println("===========================================================================");
			System.out.println("								뒤로가기(b)");
			System.out.println("===========================================================================");

			String input = inputString("골프장 번호를 입력하세요.> ");
			if (input.length() < 1) {
				System.out.println("메뉴를 제대로 입력해주세요.");
				continue;
			}
			switch (input) {
			case "r":
				registerField();
				break;
			case "b":
				return;
			case "o":
				memberService.setNowLoginUser(null);
				return;
			default:
				if (checkNumber(input)) {
					try {
						int fieldNo = Integer.parseInt(input) - 1;
						if (fieldList.get(fieldNo) != null) {
							view(Integer.parseInt(input));
						}
					} catch (IndexOutOfBoundsException e) {
						System.out.println("없는 골프장입니다.");
						continue;
					}
				}
				System.out.println("메뉴를 제대로 입력해주세요.");
				break;
			}
		}
	}

	// 수정 등록시저장할 내용 
		private String fieldName = null;
		private String regionName = null;
		private String fieldPhoneNumber = null;
		private String basicAmount = null;
		private String caddieAmount = null;
		private String hotelAmount = null;
		private boolean holeCount;
		private String holeInfo;
		private String fieldRegion;

		public void view(int fieldNo) {
			MemberService memberService = MemberService.getInstance();
			if (memberService.getNowLoginUser() == null)
				return;

			while (true) {
				FieldVO field = findFieldByNo(fieldNo - 1);

				if (field != null) {
					printAdminNav();
					System.out.println("				골프장수정");
					System.out.println("===========================================================================");
					System.out.println("이름				" + field.getFieldName());
					System.out.println("홀수				" + field.getHoleInfo());
					System.out.println("지역				" + field.getFieldRegion());
					System.out.println("전화번호			" + field.getFieldPhoneNumber());
					System.out.println("기본금액			" + moneyFormat(field.getBasicAmount()));
					System.out.println("캐디금액			" + moneyFormat(field.getCaddieAmount()));
					System.out.println("숙박금액			" + moneyFormat(field.getHotelAmount()));
					System.out.println();
					System.out.println("===========================================================================");
					System.out.println("수정하기(m)							뒤로가기(b)");
					System.out.println("===========================================================================");

					String input = inputString("입력하십시오. > ");
					if (input.length() < 1) {
						System.out.println("메뉴를 제대로 입력해주세요.");
						continue;
					}
					switch (input) {
					case "m":
						fieldName = null;
						regionName = null;
						fieldPhoneNumber = null;
						basicAmount = null;
						caddieAmount = null;
						hotelAmount = null;
						selectedField = field;
						modifyField();
						break;
					case "b":
						return;
					case "o":
						memberService.setNowLoginUser(null);
						return;
					default:
						System.out.println("메뉴를 제대로 입력해주세요.");
						break;
					}
				}
			}
		}

		
		public void registerField() {
			MemberService memberService = MemberService.getInstance();
			String localNumber = "";
			while (true) {
				int fieldNo = fieldList.size();
				String fieldName = inputString("골프장명을 입력하세요 > ");
				if (fieldName.length() >= 8) {
					System.out.println("골프장명은 8글자 이하여야합니다 > ");
					return;
				}
				// 두글자에서 여덟글자 사이로 유효성 검사
				String holeCountText = inputString("홀 개수를 입력하세요 > (1: 9홀, 2: 18홀)");
				if (inputBack(holeCountText)) {
					return;
				}
				if (holeCountText.equals("1") || holeCountText.equals("2")) {
				} else {
					continue;
				}

				boolean holeCount;
				if (holeCountText == "1") {
					holeCount = false; // 9
				} else {
					holeCount = true; // 18
				}
				// 홀수에 따른 코스
				String holeInfo = "";
				Random random = new Random();
				if (holeCount) {
					for (int i = 1; i < 19; i++) {
						int j = random.nextInt(3) + 3;
						holeInfo += j;
						if (j < 17) {
							holeInfo += "|";
						}
					}
				} else {
					for (int i = 1; i < 10; i++) {
						int j = random.nextInt(3) + 3;
						holeInfo += j;
						if (j < 8) {
							holeInfo += "|";
						}
					}
				}
				String fieldRegion = inputString("지역을 입력하세요 > (1: 경기 2: 강원 3: 제주)");
				if (!fieldRegion.equals("1") && !fieldRegion.equals("2") && !fieldRegion.equals("3")) {
					// 유효하지 않은 입력 처리
					continue;
				}
				
				switch (fieldRegion) {

				case "1":
					fieldRegion = "경기";
					localNumber = "031";
					break;
				case "2":
					fieldRegion = "강원";
					localNumber = "033";
					break;
				case "3":
					fieldRegion = "제주";
					localNumber = "064";
					break;
				default:
					break;
				}
				String fieldPhoneNumber = inputString("전화번호를 입력하세요 > ('-', 지역번호 빼고 입력해주세요)");
				if (inputBack(fieldPhoneNumber)) return;
				
				if(!checkPhoneNumber(fieldPhoneNumber)) return;
				if(!checkNumber(fieldPhoneNumber)) {
					System.out.println("");
					return;
				}
				String phoneNumberText = localNumber + "-";
				phoneNumberText += fieldPhoneNumber.substring(0, 3) + "-" + fieldPhoneNumber.substring(3);
				
				long basicAmount = Long.parseLong(inputString("기본금액을 입력하세요 >"));
				long caddieAmount = Long.parseLong(inputString("캐디금액을 입력하세요 >"));
				long hotelAmount = Long.parseLong(inputString("숙박금액을 입력하세요 >"));
				
				FieldVO fieldVO = FieldVO.builder()
								.fieldNo(fieldList.size())
								.fieldName(fieldName)
								.holeCount(holeCount)
								.holeInfo(holeInfo)
								.fieldRegion(fieldRegion)
								.fieldPhoneNumber(phoneNumberText)
								.basicAmount(basicAmount)
								.caddieAmount(caddieAmount)
								.hotelAmount(hotelAmount)
								.build();
				
				fieldList.add(fieldVO);			
				ObjectRepository.save();
				
				prettyMsg("골프장 등록이 완료되었습니다");
				break;
				
			}
		}
	public void modifyField() {
		
		printAdminNav();
		System.out.println("				골프장수정");
		printFooter();
		while (true) {
			if (fieldName == null) {
				String fieldName = inputString("수정할 골프장명을 입력하세요.(2 ~ 8글자) > ");
				if (inputBack(fieldName)) {
					return;
				}
				if (!checkHangle(fieldName) || fieldName.length() < 2 || fieldName.length() > 8) {
					System.out.println("한글로 입력해주세요.(2 ~ 8글자)");
					continue;
				}
				this.fieldName = fieldName;
			}
			if (regionName == null) {
				String regionName = inputString("수정할 지역명을 입력하세요.> ");
				if (!isValidRegion(regionName)) {
					System.out.println("경기, 강원, 제주 중에 수정가능합니다.");
					continue;
				}
				if (inputBack(regionName)) {
					return;
				}
				if (!checkHangle(regionName) || regionName.length() < 1) {
					System.out.println("한글로 입력해주세요.");
					continue;
				}
				this.regionName = regionName;
			}

			String phoneNumberText = "";
			
			if (fieldPhoneNumber == null) {
				String fieldPhoneNumber = inputString("수정할 전화번호를 - 없이 입력하세요.(국번없이 7자리)> ");
				if (inputBack(fieldPhoneNumber)) {
					return;
				}
				if (!checkPhoneNumber(fieldPhoneNumber)) {
					System.out.println("- 없이 7자리를 입력해주세요");
					continue;
				}
				if (!checkNumber(fieldPhoneNumber)) {
					System.out.println("숫자 또는 번호 갯수를 올바르게 입력해주세요.");
					continue;
				}
				
				switch(regionName) {
				case "경기":
					phoneNumberText = "031-";
					break;
				case "강원":
					phoneNumberText = "033-";
					break;
				case "제주":
					phoneNumberText = "064-";
					break;
				default:
					break;
				}
				
				phoneNumberText += fieldPhoneNumber.substring(0, 3) + "-" + fieldPhoneNumber.substring(3);

				if (findFieldByPhoneNo(phoneNumberText)) {
					System.out.println("중복된 전화번호가 있습니다.");
					continue;
				}
			}
			
			if (basicAmount == null) {
				String basicAmount = inputString("수정할 기본금액을 입력하세요.> ");
				if (inputBack(basicAmount)) {
					return;
				}
				if (basicAmount.length() < 1) {
					System.out.println("숫자로 입력해주세요.");
					continue;
				}
				if (!checkNumber(String.valueOf(basicAmount))) {
					System.out.println("숫자로 입력해주세요.");
					continue;
				}
				this.basicAmount = basicAmount;
			}
			if (caddieAmount == null) {
				String caddieAmount = inputString("수정할 캐디금액을 입력하세요.> ");
				if (caddieAmount.length() < 1) {
					System.out.println("숫자로 입력해주세요.");
					continue;
				}
				if (inputBack(basicAmount)) {
					return;
				}
				if (!checkNumber(String.valueOf(caddieAmount))) {
					System.out.println("숫자로 입력해주세요.");
					continue;
				}
				this.caddieAmount = caddieAmount;
			}
			String hotelAmount = inputString("수정할 숙박금액을 입력하세요.> ");
			if (hotelAmount.length() < 1) {
				System.out.println("숫자로 입력해주세요.");
				continue;
			}
			if (inputBack(basicAmount)) {
				return;
			}
			if (!checkNumber(String.valueOf(hotelAmount))) {
				System.out.println("숫자로 입력해주세요.");
				continue;
			}
			selectedField.updateField(fieldName, regionName, phoneNumberText, Long.parseLong(basicAmount),
					Long.parseLong(caddieAmount), Long.parseLong(hotelAmount));
			System.out.println("수정완료되었습니다.");
			
			ObjectRepository.save();
			
			return;
		}
	}


	public FieldVO findFieldByNo(int fieldNo) {
		for (FieldVO field : this.fieldList) {
			if (fieldNo == field.getFieldNo()) {
				return field;
			}
		}
		return null;
	}

	private boolean findFieldByPhoneNo(String fieldPhoneNumber) {
		for (FieldVO field : this.fieldList) {
			if (fieldPhoneNumber.equals(field.getFieldPhoneNumber())) {
				return true;
			}
		}
		return false;
	}
	private boolean isValidRegion(String regionName) {
		String[] validRegion = { "경기", "강원", "제주" };

		for (int i = 0; i < validRegion.length; i++) {
			if (regionName.equals(validRegion[i])) {
				return true;
			}
		}
		return false;
	}
}












