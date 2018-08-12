package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息列表
 * @author Administrator
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class File_Info {
	private int Id;
	private int U_id;
	private int P_id;
	private String F_modular;
	private String F_path;
	private String F_network;
	private String F_MD5;
}
