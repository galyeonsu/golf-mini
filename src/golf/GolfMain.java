package golf;

import golf.service.GolfService;

public class GolfMain {
	public static void main(String[] args) {
		
		GolfService golfService = new GolfService();
		
		golfService.start();
	}
}
