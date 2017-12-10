package com.ksy.winning.velocity;

import lombok.Data;

@Data
public class SafeHtmlStringBuilder implements SafeHtml {

	StringBuilder html;

	@Override
	public String getSafeHtmlValue() {
		return html.toString();
	}

}