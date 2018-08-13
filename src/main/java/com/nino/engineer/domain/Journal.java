package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 日志表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Journal {
	private int Id;
	private int U_id;
	private int P_id;
	private String P_name;
	private Date J_Time;
	private String J_week;
	private String J_weather;
	private String J_ambient;
	private String J_work;
	private String J_Enter;
	private String J_inspection;
	private String J_patrol;
	private String J_parallel;
	private String J_reply;
	private String J_rectification;
	private String J_unanswered;
	private String J_other;
	
}
