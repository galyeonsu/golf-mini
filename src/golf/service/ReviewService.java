package golf.service;

import static golf.util.GolfUtils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import golf.repository.ObjectRepository;
import golf.vo.ReviewVO;
import lombok.Data;

@Data
public class ReviewService{
	
	private ReviewService() {};
	
	private static final ReviewService reviewService = new ReviewService();
	
	public static ReviewService getInstance() {
		return reviewService;
	}
	
	private List<ReviewVO> reviewList;

	{
		reviewList = ObjectRepository.load("review");
		
		if(reviewList == null) {
			reviewList = new ArrayList<>();
			
			// 샘플 데이터 추가
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(0).gameNo(0).reviewScore(4).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(0).gameNo(1).reviewScore(2).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(0).gameNo(2).reviewScore(5).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(1).gameNo(3).reviewScore(1).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(1).gameNo(4).reviewScore(2).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(2).gameNo(5).reviewScore(5).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(2).gameNo(6).reviewScore(4).regDate(new Date()).build());
			reviewList.add(ReviewVO.builder().reviewNo(reviewList.size()).memberNo(1).fieldNo(2).gameNo(7).reviewScore(5).regDate(new Date()).build());
			
			ObjectRepository.save();
		}
	}
	
	private List<ReviewVO> findReviewListByFieldNo(int fieldNo){
		List<ReviewVO> reviewList = new ArrayList<>();
		
		for(ReviewVO review : this.reviewList) {
			if(fieldNo == review.getFieldNo()) {
				reviewList.add(review);
			}
		}
		
		return reviewList;
	}
	
	public double calcAverageReview(int fieldNo) {
		List<ReviewVO> reviewList = findReviewListByFieldNo(fieldNo);
		
		if(reviewList.size() == 0) {
			return 0;
		}else {
			int sum = 0;
			
			for(ReviewVO review : reviewList) {
				sum += review.getReviewScore();
			}
			
			return (double)sum / reviewList.size();
		}
	}
	
	public String calcReviewStar(int fieldNo) {
		double reviewAvg = calcAverageReview(fieldNo);
		int star = (int)Math.round(reviewAvg);
		String str = "";
		str += repeatString("★", star) + repeatString("☆", 5 - star);
		
		return str;
	}
	
	public ReviewVO findReviewByNo(int reviewNo) {
		for (ReviewVO review : this.reviewList) {
			if(reviewNo == review.getReviewNo()) {
				return review;
			}
		}
		return null;
	}
}













