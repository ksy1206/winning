package com.ksy.winning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksy.winning.dto.TeamDto;
import com.ksy.winning.mapper.MainMapper;

@Service
public class MainService {

	@Autowired
	private MainMapper mMapper;
	
	// 팀 목록 가져오기
	public List<TeamDto> getTeamList() {
		return mMapper.getTeamList();
//		return null;
	}
}
