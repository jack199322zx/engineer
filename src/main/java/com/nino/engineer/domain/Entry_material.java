package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进场材料
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Entry_material {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private String E_name;
	private int E_number;
	private int R_id;
	private String E_Situation;
	
}
