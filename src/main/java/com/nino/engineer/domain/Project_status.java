package com.nino.engineer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: InnovationEngineering
 * @description: 记录项目状态
 * @author: Dai Yuanchuan
 * @create: 2018-08-07 09:34
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Project_status {
    private int Id;
    private int U_id;
    private String Username;
    private int P_id;
    private String P_s;
}
