package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 答复单关联附件
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reply_accessories {
	private int Id;
	private int R_id;
	private int F_id;
}
