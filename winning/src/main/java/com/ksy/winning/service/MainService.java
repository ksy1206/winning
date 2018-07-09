package com.ksy.winning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksy.winning.dto.MatchDto;
import com.ksy.winning.dto.MemberDto;
import com.ksy.winning.dto.TeamDto;
import com.ksy.winning.mapper.MainMapper;
import com.ksy.winning.util.CalenderUtil;

@Service
public class MainService {

	@Autowired
	private MainMapper mMapper;
	
	// 팀 목록 가져오기
	public List<TeamDto> getTeamList(String type) {
		return mMapper.selectTeamList(type);
	}
	
	// 팀 목록 셋팅하기
	@Transactional
	public void updateSettingTeam(String[] teamNoListY, String[] teamNoListN) {
		
		// 팀 Y목록 업데이트
		for(int i=0; i<teamNoListY.length; i++) {
			int team_no = Integer.parseInt(teamNoListY[i]);
			mMapper.updateTeamInfo("Y", team_no);
		}
		
		// 팀 N목록 업데이트
		for(int j=0; j<teamNoListN.length; j++) {
			int team_no = Integer.parseInt(teamNoListN[j]);
			mMapper.updateTeamInfo("N", team_no);
		}
		
	}

	// 경기 매칭 정보 가져오기
	public List<MatchDto> getLastMatchInfo() {
		return mMapper.selectLastMatchInfo();
	}

	// 매칭정보 저장하기
	@Transactional
	public void addLastMatchInfo(String type, List<TeamDto> list) {
		
		
		if("new".equals(type)) {
			// 기존 정보 모두 삭제
			mMapper.deleteLastMatchInfo();
		}
		// 업데이트 변수 설정
		int upadteNo = 0;

		for(int i=0; i<list.size(); i++) {
			TeamDto data = list.get(i);

			// 홀짝으로 대진 구분
			if(i % 2 == 0) { // 짝수
				mMapper.insertEvenLastMatchInfo(data.getTeamNo(), data.getPlayer(), CalenderUtil.getToday());
				upadteNo = mMapper.getLastInfoNo();
			} else { // 홀수
				mMapper.insertOddLastMatchInfo(upadteNo, data.getTeamNo(), data.getPlayer());
			}
		}
	}
	
	// 경기 결과 저장하기
	public void addMatchResultInfo(int no, String result) {
		mMapper.updateMatchResultInfo(no, result);
	}
	
	// 대진표 삭제
	public void removeMatchInfo() {
		mMapper.deleteLastMatchInfo();
	}

	// 전체 매칭 정보 기록실로 이관하기
	@Transactional
	public String addAllMatchResultInfo() {
		List<MatchDto> resultList = mMapper.selectLastMatchInfo();
		String returnDate = "";
		for(int i=0; i<resultList.size(); i++) {
			String insertDate = resultList.get(i).getInsertDate();
			String[] data = insertDate.split("-");
			resultList.get(i).setYear(data[0]);
			resultList.get(i).setMonth(data[1]);
			mMapper.insertMatchInfoList(resultList.get(i));
			returnDate = insertDate;
		}
		mMapper.deleteLastMatchInfo();
		return returnDate;
	}
	
	// 단일 매칭 경기 결과 기록실 저장
	public void addMatchResultInfo(MatchDto matchDto) {
		String[] data = matchDto.getInsertDate().split("-");
		matchDto.setYear(data[0]);
		matchDto.setMonth(data[1]);
		mMapper.insertMatchInfoList(matchDto);
	}
	
	// 일별 경기 기록 가져오기
	public List<String> dailyGroupByInsertDate() {
		return mMapper.selectGroupByInsertDateMatchInfo();
	}
	
	// 일별 경기 상세 목록 가져오기
	public List<MatchDto> getDailyMatchInfo(String insertDate) {
		return mMapper.selectDailyMatchInfo(insertDate);
	}
	
	// 일별 경기 상세 목록 (개인기록)
	public List<MemberDto> getMemberDailyInfo(String insertDate) {
		return mMapper.selectMemberDailyMatchInfo(insertDate);
	}
	
	
	// 통합 랭킹
	public List<MemberDto> getMemberAllMatchInfo() {
		return mMapper.selectMemberAllMatchInfo();
	}
	
	// 년도별 랭킹
	public List<MemberDto> getYearMonthInfo(String year) {
		return mMapper.selectDateYearInfo(year);
	}

	// 월별 랭킹
	public List<MemberDto> getMemberMonthInfo(String year, String month) {
		return mMapper.selectDateMonthInfo(year, month);
	}
	
	public void removeMatchInfo(int matchNo) {
		mMapper.deleteMatchInfo(matchNo);
	}
}
