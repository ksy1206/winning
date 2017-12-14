package com.ksy.winning.dto;

import lombok.Data;

@Data
public class MatchDto {

	// Last_match_info 번호
	private Integer		no;
	
	// Match_info_list 번호
	private Integer		matchNo;

	// 첫번째 선수 & 팀 정보
	private Integer		firstTeamNo;
	private String		firstTeamName;
	private String		firstTeamImgName;
	private String		firstPlayer;

	// 두번째 선수 & 팀 정보
	private Integer		lastTeamNo;
	private String		lastTeamName;
	private String		lastTeamImgName;
	private String		lastPlayer;
	
	// 경기 결과
	private String		result;
	private String		insertDate;
	
	// 년 월 구분
	private String		year;
	private String		month;
}
