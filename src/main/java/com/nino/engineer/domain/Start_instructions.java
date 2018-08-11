package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 开工指令
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Start_instructions {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date S_time;
	private String S_Summary;
}
