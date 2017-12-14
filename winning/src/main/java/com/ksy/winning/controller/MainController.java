package com.ksy.winning.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ksy.winning.dto.MatchDto;
import com.ksy.winning.dto.MemberDto;
import com.ksy.winning.dto.TeamDto;
import com.ksy.winning.service.MainService;
import com.ksy.winning.util.CalenderUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private MainService mService;

	/**
	 * Index 페이지 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		String thisYear = CalenderUtil.getYear();
		String thisMonth = CalenderUtil.getMonth();
		
		List<MemberDto> allRanking = mService.getMemberAllMatchInfo();
		List<MemberDto> yearRanking = mService.getYearMonthInfo(thisYear);
		List<MemberDto> monthRanking = mService.getMemberMonthInfo(thisYear, thisMonth);

		Collections.sort(allRanking, new CompareSeqDesc());
		Collections.sort(yearRanking, new CompareSeqDesc());
		Collections.sort(monthRanking, new CompareSeqDesc());

		model.addAttribute("ThisMonth", thisMonth);
		model.addAttribute("ThisYear", thisYear);
		model.addAttribute("AllRanking", allRanking);
		model.addAttribute("YearRanking", yearRanking);
		model.addAttribute("MonthRanking", monthRanking);
		return "index";
	}

	/**
	 * 대진표 생성 페이지 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/match", method = RequestMethod.GET)
	public String moveMatch(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		List<MatchDto> matchInfo = mService.getLastMatchInfo(); // 경기 정보
		List<TeamDto> teamList = mService.getTeamList(); // Default 팀 정보

		for (int i = 0; i < matchInfo.size(); i++) {
			for (int j = 0; j < teamList.size(); j++) {
				// 첫번째 팀 정보 추가
				if (matchInfo.get(i).getFirstTeamNo() == teamList.get(j).getTeamNo()) {
					matchInfo.get(i).setFirstTeamName(teamList.get(j).getTeamName());
					matchInfo.get(i).setFirstTeamImgName(teamList.get(j).getTeamImgName());
				}
				// 두번째 팀 정보 추가
				if (matchInfo.get(i).getLastTeamNo() == teamList.get(j).getTeamNo()) {
					matchInfo.get(i).setLastTeamName(teamList.get(j).getTeamName());
					matchInfo.get(i).setLastTeamImgName(teamList.get(j).getTeamImgName());
				}
			}
		}

		model.addAttribute("MatchInfo", matchInfo);
		return "match";
	}

	/**
	 * 대진표 생성
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ajax/match", method = RequestMethod.GET)
	@ResponseBody
	public void matchRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "first") String type) {
		List<TeamDto> teamList = mService.getTeamList();

		// 리트스 랜덤 처리
		Collections.shuffle(teamList);
		// 리스트 12개로 자르기
		List<TeamDto> resultList = teamList.subList(0, 12);

		String[] player0 = { "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진" };
		String[] player1 = { "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수" };
		String[] player2 = { "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진" };
		String[] player3 = { "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수" };
		String[] player4 = { "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤", "장진수", "장휘진", "권세윤" };
		String[] player5 = { "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤", "장휘진", "장진수", "권세윤" };
		String[] choise = null;

		int num = (int) (Math.random() * (5 - 0 + 1)) + 0;

		if (num == 0) {
			choise = player0;
		} else if (num == 1) {
			choise = player1;
		} else if (num == 2) {
			choise = player2;
		} else if (num == 3) {
			choise = player3;
		} else if (num == 4) {
			choise = player4;
		} else if (num == 5) {
			choise = player5;
		}

		for (int i = 0; i < resultList.size(); i++) {
			resultList.get(i).setNo(i);
			resultList.get(i).setPlayer(choise[i]);
		}

		if ("add".equals(type)) {
			resultList = resultList.subList(0, 6);
			mService.addLastMatchInfo("add", resultList);
		} else {
			// 경기정보 생성 후 저장
			mService.addLastMatchInfo("new", resultList);
		}
	}

	/**
	 * 단일 경기정보 저장
	 * 
	 * @param request
	 * @param response
	 * @param no
	 * @param result
	 */
	@RequestMapping(value = "/ajax/matchResultSave", method = RequestMethod.POST)
	@ResponseBody
	public void lastMatchRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "no") int no, @RequestParam(value = "result") String result) {
		mService.addMatchResultInfo(no, result);
	}

	/**
	 * 매치 전체 경기 정보 저장
	 * 
	 * @param request
	 * @param response
	 * @param no
	 * @param result
	 */
	@RequestMapping(value = "/ajax/allMatchResultSave", method = RequestMethod.POST)
	@ResponseBody
	public String allMatchRecord(HttpServletRequest request, HttpServletResponse response) {
		return mService.addAllMatchResultInfo();
	}

	@RequestMapping(value = "/dailyGroup", method = RequestMethod.GET)
	public String dailyGroup(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		List<String> result = mService.dailyGroupByInsertDate();
		model.addAttribute("GroupList", result);
		return "dailyGroup";
	}

	@RequestMapping(value = "/dailyMatchInfo", method = RequestMethod.GET)
	public String dailyMatchInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "insertDate") String insertDate, Model model) throws Exception {
		List<MatchDto> resultList = mService.getDailyMatchInfo(insertDate);
		List<MemberDto> memberList = mService.getMemberDailyInfo(insertDate);
		
		Collections.sort(memberList, new CompareSeqDesc());
		
		model.addAttribute("MatchInfo", resultList);
		model.addAttribute("MemberList", memberList);
		return "dailyMatch";
	}

	// 정렬
	static class CompareSeqDesc implements Comparator<MemberDto> {

		@Override
		public int compare(MemberDto o1, MemberDto o2) {
			// TODO Auto-generated method stub
			return o1.getMemberPoint() > o2.getMemberPoint() ? -1 : o1.getMemberPoint() < o2.getMemberPoint() ? 1 : 0;
		}
	}
	
	
	@RequestMapping(value = "/ajax/matchInfoRemove", method = RequestMethod.POST)
	@ResponseBody
	public void matchInfoRemove(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "matchNo") int matchNo) {
		mService.removeMatchInfo(matchNo);
	}
	
	@RequestMapping(value = "/ajax/removeMatchInfo", method = RequestMethod.POST)
	@ResponseBody
	public void removeMatchInfo(HttpServletRequest request, HttpServletResponse response) {
		mService.removeMatchInfo();
	}
	
	@RequestMapping(value = "/monthMatchInfo", method = RequestMethod.GET)
	public String monthMatchInfo(HttpServletRequest request, HttpServletResponse response, Model model
			, @RequestParam(value = "year", required = false, defaultValue = "") String year
			, @RequestParam(value = "month", required = false, defaultValue = "") String month) throws Exception {
		
		if("".equals(year)) {
			year = CalenderUtil.getYear();
			month = CalenderUtil.getMonth();
		}
		
		List<MemberDto> monthRanking = mService.getMemberMonthInfo(year, month);
		Collections.sort(monthRanking, new CompareSeqDesc());

		model.addAttribute("MonthRanking", monthRanking);
		model.addAttribute("Year", year);
		model.addAttribute("Month", month);
		
		return "monthMatch";
	}
	
	@RequestMapping(value = "/saveMatch", method = RequestMethod.GET)
	public String saveMatch(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<TeamDto> teamList = mService.getTeamList();
		model.addAttribute("TeamList", teamList);
		
		return "saveMatch";
	}
	
	@RequestMapping(value = "/ajax/saveMatchInfo", method = RequestMethod.POST)
	@ResponseBody
	public void saveMatchInfo(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("MatchInfo") MatchDto matchDto) {
		mService.addMatchResultInfo(matchDto);
	}
	
	@RequestMapping(value = "/betweens", method = RequestMethod.GET)
	public String betweens(HttpServletRequest request, HttpServletResponse response, Model model) {
//		List<TeamDto> teamList = mService.getTeamList();
//		model.addAttribute("TeamList", teamList);
		
		return "betweens";
	}
}
