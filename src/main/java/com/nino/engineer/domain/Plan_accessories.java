package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进场计划附件
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Plan_accessories {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int Plan_id;
	private String A_name;
	private int F_id;
}
