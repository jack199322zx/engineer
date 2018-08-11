package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限关联表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Permissions_association {
	private int Id;
	private int U_id;
	private String username;
	private int J_id;
	private String J_explain;
}
