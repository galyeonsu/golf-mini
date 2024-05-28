package golf.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import golf.service.FieldService;
import golf.service.GameService;
import golf.service.GradeService;
import golf.service.MemberService;
import golf.service.QuestionService;
import golf.service.ReserveService;
import golf.service.ReviewService;

public class ObjectRepository {
	
	public static <T> void save() {
		Map<String, List<?>> map = new HashMap<>();
		
		if(MemberService.getInstance() != null) {
			map.put("member", MemberService.getInstance().getMemberList());
		}
		if(GradeService.getInstance() != null) {
			map.put("grade", GradeService.getInstance().getGradeList());
		}
		if(QuestionService.getInstance() != null) {
			map.put("question", QuestionService.getInstance().getQuestionList());
		}
		if(FieldService.getInstance() != null) {
			map.put("field", FieldService.getInstance().getFieldList());
		}
		if(ReviewService.getInstance() != null) {
			map.put("review", ReviewService.getInstance().getReviewList());
		}
		if(ReserveService.getInstance() != null) {
			map.put("reserve", ReserveService.getInstance().getReserveList());
		}
		if(GameService.getInstance() != null) {
			map.put("game", GameService.getInstance().getGameList());
		}
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("golf.dat"));
			oos.writeObject(map);
			oos.close();
		} catch (IOException e) {
			System.out.println("저장 안 됨");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T load(String name) {
		T ret = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("golf.dat"));
			ret = ((Map<String, T>) ois.readObject()).get(name);
			ois.close();
		} catch (Exception e) {
			
		}
		return ret;
	}
	
}




