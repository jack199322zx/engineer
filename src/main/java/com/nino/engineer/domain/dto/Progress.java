package com.nino.engineer.domain.dto;

import lombok.Data;

@Data
public class Progress {

    private String time;
    private String weather;
    private String ambient;
    private int isNewTypeOfWork;
    private String newTypeOfWorkJSON;
    private String progressInfo;
    private String elseInfo;
    private int pId;
    private String enclosure;
    private String enclosureJSON;
    private String pName;

}
