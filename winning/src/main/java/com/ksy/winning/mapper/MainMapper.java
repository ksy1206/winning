package com.ksy.winning.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ksy.winning.dto.MatchDto;
import com.ksy.winning.dto.TeamDto;

@Mapper
public interface MainMapper {


	@Results({
		@Result(property = "teamNo", column = "team_no"),
		@Result(property = "teamName", column = "team_name"),
		@Result(property = "teamImgName", column = "team_img_name"),
	})
	@Select("SELECT * FROM team")
	public List<TeamDto> selectTeamList();


	@Delete("DELETE FROM last_match_info")
	public void deleteLastMatchInfo();


	@Results({
		@Result(property = "no", column = "no"),
		@Result(property = "firstTeamNo", column = "first_team_no"),
		@Result(property = "firstPlayer", column = "first_player"),
		@Result(property = "lastTeamNo", column = "last_team_no"),
		@Result(property = "lastPlayer", column = "last_player"),
		@Result(property = "result", column = "result"),
		@Result(property = "insertDate", column = "insert_date")
	})
	@Select("SELECT * FROM last_match_info ORDER BY no ASC")
	public List<MatchDto> selectLastMatchInfo();

	@Insert("INSERT INTO match_info_list "
			+ "(first_team_no, first_player, last_team_no, last_player, result, insert_date) "
			+ "VALUES "
			+ "(#{firstTeamNo}, #{firstPlayer}, #{lastTeamNo}, #{lastPlayer}, #{result}, #{insertDate})")
	public void insertMatchInfoList(MatchDto data);
	

	@Insert("INSERT INTO last_match_info "
			+ "(first_team_no, first_player, insert_date)"
			+ "VALUES"
			+ "(#{teamNo}, #{player}, #{today})")
	public void insertEvenLastMatchInfo(@Param("teamNo") int teamNo, @Param("player") String player, @Param("today") String today);


	@Select("SELECT MAX(no) AS no FROM last_match_info")
	public int getLastInfoNo();


	@Update("UPDATE last_match_info "
			+ "SET last_team_no = #{teamNo}, last_player = #{player} "
			+ "where no = #{upadteNo}")
	public void insertOddLastMatchInfo(@Param("upadteNo") int upadteNo, @Param("teamNo") int teamNo, @Param("player") String player);


	@Update("UPDATE last_match_info "
			+ "SET result = #{result} "
			+ "where no = #{no}")
	public void updateMatchResultInfo(@Param("no") int no, @Param("result") String result);

	
	@Select("SELECT insert_date FROM match_info_list GROUP BY insert_date DESC")
	public List<String> selectGroupByInsertDateMatchInfo();
}
