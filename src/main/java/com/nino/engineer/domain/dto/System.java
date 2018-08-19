package com.nino.engineer.domain.dto;

import com.nino.engineer.domain.Project;
import lombok.Data;

import java.util.List;

@Data
public class System {

    private int id;
    private String systemNotify;
    private List<Project> projects;

}
