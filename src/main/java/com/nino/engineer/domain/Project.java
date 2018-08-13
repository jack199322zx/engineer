package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Project {
	private int Id;
	private String P_name;
	private String P_creator;
	private Date P_time;
	private int L_id;
	private String P_weather;
	private String P_ambient;
	private String pro_status;// 状态
	private String Submanagement;// 子管理
}
