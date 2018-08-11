package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 联系单抄送
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contacts_copy {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int Contacts_id;
	private int R_id;
}
