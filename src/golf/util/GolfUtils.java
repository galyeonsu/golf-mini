package golf.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

// 공통 유틸 클래스

public class GolfUtils {
	
	private static Scanner sc = new Scanner(System.in);
	
	public static String inputString(String msg) {
		System.out.print(msg);
		return sc.nextLine();
	}
	
	public static int inputInt(String msg) {
		return Integer.parseInt(inputString(msg));
	}
	
	// 여기서부터는 유효성 검사	
	public static boolean checkHangle(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < '가' || str.charAt(i) > '힣') {
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkId(String str) {
		String regex = "^[a-zA-Z0-9]{6,12}$";
		return Pattern.matches(regex, str); // 매칭되면 true, 아니면 false 리턴
	}
	
	public static boolean checkPw(String str) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*()]).{8,16}$";
		return Pattern.matches(regex, str);
	}
	
	public static boolean checkOneOrTwo(int num) { // 0, 1만 됨
		if(num != 1 && num != 2) {
			return false;
		}
		return true;
	}
	
	public static boolean checkNumber(String str) {
		String regex = ".*\\D.*"; // 숫자가 아닌 문자가 하나라도 있는지 체크
		return !Pattern.matches(regex, str);
	}
	
	public static boolean checkPhoneNumber(String str) {
		String regex = "^[0-9]{7}$";
		return Pattern.matches(regex, str);
	}
	
	public static boolean checkReviewScore(int score) {
		if(score < 1 || score > 5) {
			return false;
		}
		return true;
	}
	
	public static int countHangle(String str) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 'ㄱ' && str.charAt(i) <= '힣') {
				count++;
			}
		}
		return count;
	}
	
	public static boolean inputBack(String str) {
		if(str.equals("b")) {
			return true;
		}else {
			return false;
		}
	}

	public static boolean inputLogout(String str) {
		if(str.equals("o")) {
			return true;
		}else {
			return false;
		}
	}

	public static void prettyMsg(String msg) {
		int count = countHangle(msg); // 메시지 내의 한글 개수 셈
		int length = 75 - (msg.length() + count);
		System.out.println("***************************************************************************");
		System.out.println(repeatString(" ", length / 2) + msg + repeatString(" ", length / 2));
		System.out.println("***************************************************************************");
	}

	public static String prettyString(String str) {
		int count = countHangle(str);
		int length = 75 - (str.length() + count);
		return (repeatString(" ", length / 2) + str + repeatString(" ", length / 2));
	}
	
	public static String moneyFormat(long money) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###");
		
		return decimalFormat.format(money) + "원";
	}
	
	public static String repeatString(String str, int n) {
		String res = "";
        for (int i = 0; i < n; i++) {
            res += str;
        }
        return res;
	}

	public static List<String> reserveDateList() {
		LocalDate today = LocalDate.now(); // 오늘 날짜로 LocalDate 객체 생성
		
		List<String> dateList = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			today = today.plusDays(1); // 1일씩 더함
			
			dateList.add(String.format("%02d/%02d", today.getMonthValue(), today.getDayOfMonth()));
		}
	
		return dateList;
	}
	
	public static List<String> reserveDateListWithDays() {
		LocalDate today = LocalDate.now(); // 오늘 날짜로 LocalDate 객체 생성
		
		List<String> dateList = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			today = today.plusDays(1); // 1일씩 더함
			
			dateList.add(String.format("%02d/%02d(%s)", today.getMonthValue(), today.getDayOfMonth(), today.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN)));
		}
	
		return dateList;
	}
	
	public static String formatDate(String date) {
		int year = 2023;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.parse(year + "/" + date, dtf);
		
		return String.format("%02d/%02d(%s)", localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN));
	}

}



























