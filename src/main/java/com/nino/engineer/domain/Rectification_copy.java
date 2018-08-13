package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 隐患整改单抄送
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Rectification_copy {
	private int Id;
	private int U_id;
	private String Username;
	private int P_id;
	private int Rectification_id;
	private int R_id;
}
