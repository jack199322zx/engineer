package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 销项答复单
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reply {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date R_time;
	private String R_explain;
	private String R_result;
	private String Is_delay;
	private Date Delay_time;
	private String Is_confirm;
	private String R_reason;
}
