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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MainService {

	@Autowired
	private MainMapper mMapper;
	
	// 팀 목록 가져오기
	public List<TeamDto> getTeamList() {
		return mMapper.selectTeamList();
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
	
	// 전체 매칭 정보 기록실로 이관하기
	@Transactional
	public void addAllMatchResultInfo() {
		List<MatchDto> resultList = mMapper.selectLastMatchInfo();
		for(int i=0; i<resultList.size(); i++) {
			mMapper.insertMatchInfoList(resultList.get(i));
		}
		mMapper.deleteLastMatchInfo();
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
	
	// 월별 랭킹
	public List<MemberDto> getMemberMonthInfo(String insertDate) {
		log.error("@@@@@"+insertDate);
		return mMapper.selectMemberMonthInfo(insertDate);
	}
}
