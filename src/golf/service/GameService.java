package golf.service;

import static golf.util.GolfPrints.*;
import static golf.util.GolfUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import golf.repository.ObjectRepository;
import golf.vo.FieldVO;
import golf.vo.GameVO;
import golf.vo.MemberVO;
import golf.vo.ReserveVO;
import golf.vo.ReviewVO;
import lombok.Data;

@Data
public class GameService {

	private GameService() {
	};

	private static final GameService gameService = new GameService();

	public static GameService getInstance() {
		return gameService;
	}

	private List<GameVO> gameList;

	{
		gameList = ObjectRepository.load("game");

		if (gameList == null) {
			gameList = new ArrayList<>();

			// 샘플 데이터 추가
			gameList.add(GameVO.builder().gameNo(gameList.size()).memberNo(1).fieldNo(0).reserveNo(0)
					.gameScore("-2|0|+2|+1|+1|+2|0|-1|-1|0|-1|+1|+2|+1|-2|+1|+1|0").gameScoreSum(5).regDate(new Date())
					.build());
			gameList.add(GameVO.builder().gameNo(gameList.size()).memberNo(1).fieldNo(0).reserveNo(1)
					.gameScore("-2|0|+2|+1|+1|+2|0|-1|-1|0|+1|+1|+2|+1|+1|+1|+1|0").gameScoreSum(10).regDate(new Date())
					.build());
			gameList.add(GameVO.builder().gameNo(gameList.size()).memberNo(1).fieldNo(0).reserveNo(2)
					.gameScore("-2|0|+2|+1|+1|+2|0|-1|-1|0|-1|+1|-3|+1|+1|+1|+1|0").gameScoreSum(3).regDate(new Date())
					.build());
			gameList.add(GameVO.builder().gameNo(gameList.size()).memberNo(1).fieldNo(1).reserveNo(3)
					.gameScore("-2|0|+2|+1|+1|+2|0|-1|-1|0|0|+1|+2|-2|+1|+1|+1|0").gameScoreSum(6).regDate(new Date())
					.build());
			gameList.add(GameVO.builder().gameNo(gameList.size()).memberNo(1).fieldNo(1).reserveNo(4)
					.gameScore("-2|0|+2|+1|+1|+2|0|-1|-1|0|0|+1|+2|+1|-2|+1|+1|0").gameScoreSum(6).regDate(new Date())
					.build());

			ObjectRepository.save();
		}
	}

	// 페이징 관련 상태 변수
	private int nowPage; // 0으로 시작함
	private int countPerPage = 3;

	
	public void main() {
		String input;
		Random random = new Random();
		MemberService memberService = MemberService.getInstance();
		MemberVO member = memberService.getNowLoginUser();
		ReserveService reserveService = ReserveService.getInstance();
		FieldVO field = reserveService.getGameField();
		ReviewService reviewService = ReviewService.getInstance();
		String gameScore = "";
		int gameScoreSum = 0;
		String review = null;

		String[] scoreName = { "hole in one", "알바트로스", "이글", "버디", "파", "보기", "더블보기", "트리플 보기", "쿼드러플보기" };

		while (true) {
			if (reserveService.getPlayReserve().isGamePlayed()) {
				return;
			}

			if (memberService.getNowLoginUser() == null) {
				return;
			}
			printNav(member.getMemberId());
			System.out.println("				게임하기");
			printFooter();
			input = inputString("엔터를 입력하시면 게임이 시작됩니다. >");

			switch (input) {
			case "b": // 뒤로가기
				return;
			case "o": // 로그아웃
				memberService.setNowLoginUser(null);
				return;
			case "" + "":
				String[] arr = field.getHoleInfo().split("\\|");

				for (int i = 0; i < arr.length; i++) {
					int ta = random.nextInt(7) + 1; // 1 ~ 7 랜덤
					int par = Integer.parseInt(arr[i]); // 현재 홀의 파수
					int index = ta - par + 4;
					int score = ta - par;
					String scoreNameText;
					if (ta == 1) {
						scoreNameText = "hole in one!!";
					} else {
						scoreNameText = scoreName[index];
					}

					System.out.println(String.format("현재 홀의 타수:%d, 내가친타수:%d, 기록:%s", par, ta, scoreNameText));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gameScoreSum += score;

					// 첫번째 스코어 때 '|' 빼는 것과 0에다가 부호 뺴는 것 기억안납니다ㅜ
					 if (i == 0) {
					        gameScore += score;
					    } else {
					        if (score > 0) {
					            gameScore += "|" + "+" + score;
					        } else {
					            gameScore += "|" + score;
					        }
					    }
					}

				while (true) {
					review = inputString("골프장 평점 1, 2, 3, 4, 5 중에 골라주세요");
					if (!checkNumber(review) || review.length() < 1 || 1 > Integer.parseInt(review) || Integer.parseInt(review) > 5 ) {
						System.out.println("보기 중에 골라주세요.");
						continue;
					}
					break;
				}
				GameVO gameVO = GameVO.builder().
								gameNo(gameList.size()).
								memberNo(member.getMemberNo()).
								fieldNo(field.getFieldNo()).
								reserveNo(reserveService.getPlayReserve().getReserveNo()).
								gameScore(gameScore).
								gameScoreSum(gameScoreSum).
								regDate(new Date()).build();

				gameService.gameList.add(gameVO);
				reserveService.getPlayReserve().setGamePlayed(true);

				ReviewVO reviewVO = ReviewVO.builder().
									reviewNo(reviewService.getReviewList().size()).
									memberNo(member.getMemberNo()).
									fieldNo(field.getFieldNo()).
									gameNo(gameList.size()).
									reviewScore(Integer.parseInt(review)).
									regDate(new Date()).build();

				reviewService.getReviewList().add(reviewVO);
				ObjectRepository.save();
				return;

			}
		}
	}


	public void viewGameList() {
		String input;

		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		ReserveService reserveService = ReserveService.getInstance();

		MemberVO nowMember = memberService.getNowLoginUser();

		nowPage = 0; // 다시 들어오면 0으로 시작하게 함

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;

			// 해당 회원의 gameList를 불러온다
			List<GameVO> filteredGameList = new ArrayList<>();
			for (GameVO game : gameList) {
				if (game.getMemberNo() == nowMember.getMemberNo()) {
					filteredGameList.add(game);
				}
			}

			int gameCount = filteredGameList.size();
			int maxPage = (gameCount / 3) + 1;

			if (filteredGameList.size() == 0) { // 사용자의 게임기록이 없을 때

				printNav(memberService.getNowLoginUser().getMemberId());

				System.out.println("			       게임기록조회");
				System.out.println("===========================================================================");

				System.out.println();
				System.out.println();
				System.out.println("			 게임을 하신 기록이 없습니다");
				System.out.println();
				System.out.println();

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

			} else { // 사용자의 게임기록이 있을 때

				printShortNav(memberService.getNowLoginUser().getMemberId());

				System.out.println("			       게임기록조회");
				System.out.println("===========================================================================");

				Collections.reverse(filteredGameList);

				List<GameVO> nowPageGameList = new ArrayList<>();

				for (int i = nowPage * 3; i < (nowPage + 1) * 3; i++) {
					if (i < filteredGameList.size() && filteredGameList.get(i) != null) {
						nowPageGameList.add(filteredGameList.get(i));
					}
				}

				for (GameVO game : nowPageGameList) {
					FieldVO nowField = fieldService.findFieldByNo(game.getFieldNo());
					ReserveVO nowReserve = reserveService.findReserveByNo(game.getReserveNo());
					String reserveDate = String.format("%s(%d부)", nowReserve.getReserveDate(),
							nowReserve.getReserveTime());

					String fieldName = nowField.getFieldName();

					int holeCount = (nowField.isHoleCount()) ? 18 : 9;

					String[] holeArr = nowField.getHoleInfo().split("\\|"); // 이스케이핑 처리
					String[] scoreArr = game.getGameScore().split("\\|");

					String scoreText = String.format("%d(%s%d)", nowField.getHoleSum() + game.getGameScoreSum(),
							game.getGameScoreSum() > 0 ? "+" : "",
							game.getGameScoreSum());
					String headerText = String.format("	%s / %d홀 / %s %s", fieldName, holeCount, reserveDate,
							scoreText);
					int headerTextHangleCount = countHangle(headerText);
					int blankLength = 64 - headerText.length() - headerTextHangleCount;
					String blankText = repeatString(" ", blankLength);

					System.out.println("	==============================================================");
					System.out.println(String.format("	%s / %d홀 / %s%s%s", fieldName, holeCount, reserveDate,
							blankText, scoreText));
					System.out.println("	==============================================================");

					String holeText = "	홀	";

					if (nowField.isHoleCount()) {
						holeText += "  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18";
					} else {
						holeText += "  1  2  3  4  5  6  7  8  9";
					}

					System.out.println(holeText);

					String parText = "	파	";
					for (int i = 0; i < holeArr.length; i++) {
						parText += String.format("%3s", holeArr[i]);
					}

					System.out.println(parText);

					String scoreText2 = "	점수	";
					for (int i = 0; i < scoreArr.length; i++) {
						scoreText2 += String.format("%3s", scoreArr[i]);
					}

					System.out.println(scoreText2);
					System.out.println("	==============================================================");
				}

				System.out.println("===========================================================================");
				System.out.println(String.format("				   %d / %d", nowPage + 1, maxPage));

				// 이전, 다음을 실제 이전, 이후 인덱스가 있는지에 따라 다르게 하기
				// 이전이 더 작은 값임 (-1), 이후는 +1
				boolean isExistPrev = nowPage > 0;
				boolean isExistNext = nowPage < maxPage - 1;

				String prevText = isExistPrev ? "< 이전(1)" : "	";
				String nextText = isExistNext ? "다음(2) > " : "	";
				String prevNextText = prevText + "							  " + nextText;
				System.out.println(prevNextText);

				printShortFooter();

				input = inputString("메뉴를 선택해주세요 > ");

				switch (input) {
				case "1": // 이전
					if (!isExistPrev) { // 이전이 없을 때
						System.out.println("메뉴를 제대로 입력해주세요");
						break;
					}
					nowPage--;
					break;
				case "2": // 다음
					if (!isExistNext) { // 이후가 없을 때
						System.out.println("메뉴를 제대로 입력해주세요");
						break;
					}
					nowPage++;
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

	public void viewScoreRank() {
		String input;

		MemberService memberService = MemberService.getInstance();
		FieldService fieldService = FieldService.getInstance();
		ReserveService reserveService = ReserveService.getInstance();

		while (true) {
			if (memberService.getNowLoginUser() == null)
				return;
			if (fieldService.getSelectedField() == null)
				return;

			printNav(memberService.getNowLoginUser().getMemberId());

			FieldVO nowField = fieldService.getSelectedField();

			System.out.println(prettyString(String.format("%s 스코어 랭킹", nowField.getFieldName())));
			System.out.println("===========================================================================");
			System.out.println("순위	아이디		이름		스코어			 날짜");
			System.out.println("===========================================================================");

			List<GameVO> filteredGameList = new ArrayList<>();
			for (GameVO game : gameList) {
				if (nowField.getFieldNo() == game.getFieldNo()) {
					filteredGameList.add(game);
				}
			}

			if (filteredGameList.size() == 0) { // 게임한 기록이 없을 때
				System.out.println();
				System.out.println();
				System.out.println(prettyString("해당 골프장에서 게임한 기록이 없습니다"));
				System.out.println();
				System.out.println();
			} else { // 게임한 기록이 있을 때

				// 버블 정렬
				filteredGameList = bubbleSortGameList(filteredGameList);

				int temp = 1; // 순위 정렬용

				for (GameVO game : filteredGameList) {
					MemberVO nowMember = memberService.findMemberByNo(game.getMemberNo());
					ReserveVO nowReserve = reserveService.findReserveByNo(game.getReserveNo());

					// 아이디는 이름이 같을 경우를 대비해 표시는 하되 모두에게 보이므로 마스킹해서 출력
					String memberId = nowMember.getMemberId();
					String maskedId = memberId.substring(0, 3) + "***";

					String scoreText = String.format("%d(%s%d)", nowField.getHoleSum() + game.getGameScoreSum(),
							game.getGameScoreSum() > 0 ? "+" : "",
							game.getGameScoreSum());

					int countMemberNameHangle = countHangle(nowMember.getMemberName());
					int memberNameLength = 16 - countMemberNameHangle;

					String scoreRankText = String.format("%-8s%-16s%-" + memberNameLength + "s%-25s%s(%d부)", temp,
							maskedId, nowMember.getMemberName(), scoreText, nowReserve.getReserveDate(),
							nowReserve.getReserveTime());

					System.out.println(scoreRankText);

					temp++;
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
				System.out.println("메뉴를 제대로 입력해주세요");
				break;
			}
		}
	}

	private List<GameVO> bubbleSortGameList(List<GameVO> gameList) {
		for (int i = 0; i < gameList.size(); i++) {
			for (int j = i + 1; j < gameList.size(); j++) {
				// 앞의 것이 점수가 높으면 뒤로 옮김 (점수가 낮은 게 높은 것)
				if (gameList.get(i).getGameScoreSum() > gameList.get(j).getGameScoreSum()) {
					GameVO temp = gameList.get(i);
					gameList.set(i, gameList.get(j));
					gameList.set(j, temp);
				}
			}
		}

		return gameList; // 정렬된 리스트 반환
	}

	public GameVO findGameByNo(int gameNo) {
		for (GameVO game : this.gameList) {
			if (gameNo == game.getGameNo()) {
				return game;
			}
		}
		return null;
	}
}
