package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 验收
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Acceptance_check {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date A_time;
	private int L_id;
	private String Is_qualified;
	private String A_Explain;
}
