package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Configure {
	private int Id;
	private int S_APPID;
	private String S_APPKEY;
	private int S_template_Id;
	private String S_sign;
	private String QQ_Email;
	private String QQ_SMTP;
}
