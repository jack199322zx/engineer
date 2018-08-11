package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 平行检查(销项单)
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Parallel {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date P_time;
	private String P_number;
	private String E_code;
	private int R_id;
	private int L_id;
	private String P_describe;
	private String P_requirement;
	private Date P_term;
	private String P_type;
}
