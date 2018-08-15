package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡视检查
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Patrol {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private String P_name;
	private int L_id;
	private String P_content;
	private String Is_qualified;
	private String P_reason;
	private String P_Situation;
}
