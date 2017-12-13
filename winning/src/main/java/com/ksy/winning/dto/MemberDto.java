package com.ksy.winning.dto;

import lombok.Data;

@Data
public class MemberDto {

	private Integer		memberNo;
	private String		memberName;
	private Integer		memberVictory;
	private Integer		memberDraw;
	private Integer		memberDefeat;
	
	public int getMemberPoint() {
		return (this.memberVictory * 3) + (this.memberDraw);
	}
}
