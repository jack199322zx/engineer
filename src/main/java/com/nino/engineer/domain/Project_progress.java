package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目进度
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project_progress {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date P_time;
	private String P_weather;
	private int L_id;
	private String P_ambient;
	private String P_describe;
	private String P_else;
}
