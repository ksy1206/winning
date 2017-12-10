package com.ksy.winning.velocity;

import lombok.Data;

@Data
public class SafeHtmlString implements SafeHtml {

	String html;

	@Override
	public String getSafeHtmlValue() {
		return html;
	}

}
