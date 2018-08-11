package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
	private int Id;
	private String Username;
	private String Password;
	private String U_email;
	private String U_telephone;
	private String U_company;
	private String U_department;
}
