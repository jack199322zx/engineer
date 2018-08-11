package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 联系单附件
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contacts_accessories {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int Contacts_id;
	private int F_id;
}
