package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 联系单
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contacts {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private Date C_time;
	private String C_number;
	private int R_id;
	private String C_Summary;
	private String C_content;
}
