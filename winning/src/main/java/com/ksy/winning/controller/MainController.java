package com.ksy.winning.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ksy.winning.dto.TeamDto;
import com.ksy.winning.service.MainService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private MainService mService;
	
	/**
	 * Index 페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		log.info("Index Page Loding");
		return "index";
	}

	/**
	 * 대진표 생성 페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/match", method = RequestMethod.GET)
	public String moveMatch(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		List<TeamDto> teamList = mService.getTeamList();
		
		// 리트스 랜덤 처리
		Collections.shuffle(teamList);
		
		// 리스트 12개로 자르기
		List<TeamDto> resultList =  teamList.subList(0, 12);
		
		for(int i=0; i<resultList.size(); i++) {
			resultList.get(i).setNo(i);
		}

		model.addAttribute("TeamList", resultList);
		return "match";
	}


}
