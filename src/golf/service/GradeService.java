package golf.service;

import java.util.ArrayList;
import java.util.List;

import golf.repository.ObjectRepository;
import golf.vo.GradeVO;
import lombok.Data;

@Data
public class GradeService{
	
	private GradeService() {};
	
	private static final GradeService gradeService = new GradeService();
	
	public static GradeService getInstance() {
		return gradeService;
	}
	
	private List<GradeVO> gradeList;
	
	{
		gradeList = ObjectRepository.load("grade");
		
		if(gradeList == null) {
			gradeList = new ArrayList<>();
			
			gradeList.add(GradeVO.builder().gradeNo(gradeList.size()).gradeName("일반회원").requireAmount(0).discountRate(0).build()); // 일반회원
			gradeList.add(GradeVO.builder().gradeNo(gradeList.size()).gradeName("우수회원").requireAmount(500_000).discountRate(20).build()); // 우수회원
			gradeList.add(GradeVO.builder().gradeNo(gradeList.size()).gradeName("VIP회원").requireAmount(2_000_000).discountRate(50).build()); // VIP
			gradeList.add(GradeVO.builder().gradeNo(gradeList.size()).gradeName("관리자").requireAmount(0).discountRate(0).build()); // 관리자
	
			ObjectRepository.save();
		}
	}
	
	public GradeVO findGradeByNo(int gradeNo) {
		for(GradeVO grade : this.gradeList) {
			if(gradeNo == grade.getGradeNo()) {
				return grade;
			}
		}
		return null;
	}
}









