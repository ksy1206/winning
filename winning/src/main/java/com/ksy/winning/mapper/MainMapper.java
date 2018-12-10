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
import com.ksy.winning.dto.MemberDto;
import com.ksy.winning.dto.TeamDto;

@Mapper
public interface MainMapper {


	@Results({
		@Result(property = "teamNo", column = "team_no"),
		@Result(property = "teamName", column = "team_name"),
		@Result(property = "teamImgName", column = "team_img_name"),
		@Result(property = "group", column = "group"),
		@Result(property = "useYn", column = "use_yn")
	})
	@Select("<script>"
			+ "SELECT * FROM team "
			+ "<if test = 'type != \"ALL\"'>"
			+ " WHERE use_yn = \"Y\" "
			+ "</if>"
			+ "ORDER BY team_no ASC"
			+ "</script>")
	public List<TeamDto> selectTeamList(@Param("type") String type);

	
	@Update("UPDATE team SET use_yn = #{useYn} WHERE team_no = #{teamNo}")
	public void updateTeamInfo(@Param("useYn") String useYn, @Param("teamNo") int teamNo);

	@Delete("DELETE FROM last_match_info")
	public void deleteLastMatchInfo();
	
	@Delete("DELETE FROM match_info_list WHERE match_no = #{matchNo}")
	public void deleteMatchInfo(@Param("matchNo") int matchNo);


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
			+ "(first_team_no, first_player, last_team_no, last_player, result, insert_date, year, month) "
			+ "VALUES "
			+ "(#{firstTeamNo}, #{firstPlayer}, #{lastTeamNo}, #{lastPlayer}, #{result}, #{insertDate}, #{year}, #{month})")
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
	
	@Results({
		@Result(property = "matchNo", column = "match_no"),
		@Result(property = "firstTeamNo", column = "first_team_no"),
		@Result(property = "firstPlayer", column = "first_player"),
		@Result(property = "lastTeamNo", column = "last_team_no"),
		@Result(property = "lastPlayer", column = "last_player"),
		@Result(property = "result", column = "result"),
		@Result(property = "insertDate", column = "insert_date")
	})
	@Select("SELECT * FROM match_info_list WHERE insert_date = #{insertDate} ORDER BY match_no ASC")
	public List<MatchDto> selectDailyMatchInfo(@Param("insertDate") String insertDate);
	
	@Results({
		@Result(property = "memberName", column = "member_name"),
		@Result(property = "memberVictory", column = "member_victory"),
		@Result(property = "memberDraw", column = "member_draw"),
		@Result(property = "memberDefeat", column = "member_defeat")
	})
	@Select("SELECT DISTINCT member_name "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"first\" AND insert_date = DATE(#{insertDate}) "
			+ "OR "
			+ "last_player = a.member_name AND result = \"last\" AND insert_date = DATE(#{insertDate}) ) as member_victory "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"draw\" AND insert_date = DATE(#{insertDate}) "
			+ "OR "
			+ "last_player = a.member_name AND result = \"draw\" AND insert_date = DATE(#{insertDate}) ) AS member_draw "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"last\" AND insert_date = DATE(#{insertDate}) "
			+ "OR "
			+ "last_player = a.member_name AND result = \"first\" AND insert_date = DATE(#{insertDate}) ) AS member_defeat "
			+ "FROM member a")
	public List<MemberDto> selectMemberDailyMatchInfo(@Param("insertDate") String insertDate);
	
	
	
	@Results({
		@Result(property = "memberName", column = "member_name"),
		@Result(property = "memberVictory", column = "member_victory"),
		@Result(property = "memberDraw", column = "member_draw"),
		@Result(property = "memberDefeat", column = "member_defeat")
	})
	@Select("SELECT DISTINCT member_name "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"first\" "
			+ "OR "
			+ "last_player = a.member_name AND result = \"last\") as member_victory "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"draw\" "
			+ "OR "
			+ "last_player = a.member_name AND result = \"draw\") AS member_draw "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"last\" "
			+ "OR "
			+ "last_player = a.member_name AND result = \"first\" ) AS member_defeat "
			+ "FROM member a")
	public List<MemberDto> selectMemberAllMatchInfo();
	
	@Results({
		@Result(property = "memberName", column = "member_name"),
		@Result(property = "memberVictory", column = "member_victory"),
		@Result(property = "memberDraw", column = "member_draw"),
		@Result(property = "memberDefeat", column = "member_defeat")
	})
	@Select("SELECT DISTINCT member_name "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"first\" AND year = #{year} "
			+ "OR "
			+ "last_player = a.member_name AND result = \"last\" AND year = #{year} ) as member_victory "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"draw\" AND year = #{year} "
			+ "OR "
			+ "last_player = a.member_name AND result = \"draw\" AND year = #{year} ) AS member_draw "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"last\" AND year = #{year} "
			+ "OR "
			+ "last_player = a.member_name AND result = \"first\" AND year = #{year} ) AS member_defeat "
			+ "FROM member a")
	public List<MemberDto> selectDateYearInfo(@Param("year") String year);
	
	@Results({
		@Result(property = "memberName", column = "member_name"),
		@Result(property = "memberVictory", column = "member_victory"),
		@Result(property = "memberDraw", column = "member_draw"),
		@Result(property = "memberDefeat", column = "member_defeat")
	})
	@Select("SELECT DISTINCT member_name "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"first\" AND year = #{year} AND month = #{month} "
			+ "OR "
			+ "last_player = a.member_name AND result = \"last\" AND year = #{year} AND month = #{month} ) as member_victory "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"draw\" AND year = #{year} AND month = #{month} "
			+ "OR "
			+ "last_player = a.member_name AND result = \"draw\" AND year = #{year} AND month = #{month} ) AS member_draw "
			+ ", (SELECT COUNT(*) FROM match_info_list "
			+ "WHERE "
			+ "first_player = a.member_name AND result = \"last\" AND year = #{year} AND month = #{month} "
			+ "OR "
			+ "last_player = a.member_name AND result = \"first\" AND year = #{year} AND month = #{month} ) AS member_defeat "
			+ "FROM member a")
	public List<MemberDto> selectDateMonthInfo(@Param("year") String year, @Param("month") String month);
	
	// 상대 전적 승
	@Select("SELECT COUNT(*) FROM match_info_list WHERE first_player = #{name} AND last_player = #{vsName} AND result = \"first\" OR first_player = #{vsName} AND last_player = #{name} AND result = \"last\";")
	public int getVSInfoVictory(@Param("name") String name, @Param("vsName") String vsName);
	
	// 상대 전적 무승부
	@Select("SELECT COUNT(*) FROM match_info_list WHERE first_player = #{name} AND last_player = #{vsName} AND result = \"draw\" OR first_player = #{vsName} AND last_player = #{name} AND result = \"draw\";")
	public int getVSInfoDraw(@Param("name") String name, @Param("vsName") String vsName);
	
	// 상대 전적 패
	@Select("SELECT COUNT(*) FROM match_info_list WHERE first_player = #{name} AND last_player = #{vsName} AND result = \"last\" OR first_player = #{vsName} AND last_player = #{name} AND result = \"first\";")
	public int getVSInfoDefeat(@Param("name") String name, @Param("vsName") String vsName);
}
