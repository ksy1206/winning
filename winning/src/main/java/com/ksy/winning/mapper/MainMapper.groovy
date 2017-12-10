package com.ksy.winning.mapper

import com.ksy.winning.dto.TeamDto

import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select

public interface MainMapper {
	
	@Results([
		@Result(property="teamNo", column="team_no")
		, @Result(property="teamName", column="team_name")
		, @Result(property="teamImgName", column="team_img_name")
	])
	@Select("""
		SELECT * FROM team
	""")
	List<TeamDto> getTeamList();

}
