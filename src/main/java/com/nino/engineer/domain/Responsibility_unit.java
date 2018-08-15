package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 责任单位
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Responsibility_unit {
	private int Id;
	private String R_name;
}
