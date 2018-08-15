package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Jurisdiction {
	private int Id;
	private int J_id;
	private String J_explain;
}
