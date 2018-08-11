package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 隐患整改单
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rectification {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date R_time;
	private String R_type;
	private String R_number;
	private int R_id;
	private String R_Summary;
	private String R_Cause;
}
