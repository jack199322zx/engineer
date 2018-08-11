package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 材料送检
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Inspection {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int E_id;
	private Date I_time;
	private String Is_agree;
	private String I_reason;
}
