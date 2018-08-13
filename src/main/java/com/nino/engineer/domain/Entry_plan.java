package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进场计划
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Entry_plan {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int E_id;
	private String Plan_name;
	private String Plan_detail;
	private String Is_Agree;
	private String E_reason;
}
