package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开工指令附件
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Start_accessories {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int S_id;
	private int F_id;
}
