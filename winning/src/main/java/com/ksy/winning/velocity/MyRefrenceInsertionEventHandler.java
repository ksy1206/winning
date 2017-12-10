package com.ksy.winning.velocity;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.springframework.web.util.HtmlUtils;

/**
 * @author 1001083 velocity html escape 사용법
 * - 모든 텍스트는 html escapsed
 * - application.yml 에 다음 설정 추가 하기
 *   eventhandler.referenceinsertion.class:
 *   net.clcoms.cs4.service.velocity.MyRefrenceInsertionEventHandler
 * - escape를 원치 않을 경우에는 아래의 2가지 방법이 있음
 *   1) SafeHtml을 implements 하기
 *   2) 변수명을 NoEscape 로 끝나게 하기
 */
public class MyRefrenceInsertionEventHandler implements ReferenceInsertionEventHandler {

	@Override
	public Object referenceInsert(String reference, Object value) {
		if (value == null) {
			return null;
		} else if (reference.endsWith("NoEscape") || reference.endsWith("NoEscape}")) {
			return value;
		} else if (value instanceof SafeHtml) {
			return ((SafeHtml) value).getSafeHtmlValue();
		} else {
			return HtmlUtils.htmlEscape(value.toString());
		}
	}

}