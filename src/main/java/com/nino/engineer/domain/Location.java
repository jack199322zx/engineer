package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 问题所在位置
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Location {
	private int Id;
	private int U_id;
	private String Username;
	private String L_Building;
	private String L_layer;
	private String L_household;
	private String L_detailed;
	private String Project_location;
	private String L_region;
	private String L_detailed_address;
}
