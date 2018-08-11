package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 隐患整改单附件
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rectification_accessories {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int R_id;
	private int F_id;
}
