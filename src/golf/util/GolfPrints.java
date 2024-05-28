package golf.util;

// 출력 메서드 모음
public class GolfPrints {
	
	public static void printHeader() {
		System.out.println("=========================== 골프 예약 프로그램 ============================");
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public static void printShortHeader() {
		System.out.println("=========================== 골프 예약 프로그램 ============================");
		System.out.println();
	}

	public static void printNav(String memberId) {
		System.out.println("=========================== 골프 예약 프로그램 ============================");
		System.out.printf("%s님			   				로그아웃(o)\n", memberId);
		System.out.println();
		System.out.println();
	}

	public static void printShortNav(String memberId) {
		System.out.println("=========================== 골프 예약 프로그램 ============================");
		System.out.printf("%s님			   				로그아웃(o)\n", memberId);
		System.out.println();
	}

	public static void printAdminNav() {
		System.out.println("=========================== 골프 예약 프로그램 ============================");
		System.out.printf("최고관리자님			   				로그아웃(o)\n");
		System.out.println();
		System.out.println();
	}

	public static void printFooter() {
		System.out.println();
		System.out.println();
		System.out.println("								뒤로가기(b)");
		System.out.println("===========================================================================");
	}

	public static void printShortFooter() {
		System.out.println();
		System.out.println("								뒤로가기(b)");
		System.out.println("===========================================================================");
	}

	public static void printFooterNoBack() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("===========================================================================");
	}

	public static void printFooterWithScoreRank() {
		System.out.println();
		System.out.println();
		System.out.println("스코어랭킹(s)							뒤로가기(b)");
		System.out.println("===========================================================================");
	}

	public static void printFooterWithReserve() {
		System.out.println();
		System.out.println();
		System.out.println("예약하기(r)							뒤로가기(b)");
		System.out.println("===========================================================================");
	}

	public static void printFooterForReserveView() {
		System.out.println();
		System.out.println("예약취소(c)							게임하기(g)");
		System.out.println("								뒤로가기(b)");
		System.out.println("===========================================================================");
	}
	
}











