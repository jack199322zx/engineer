package com.nino.engineer.common.annotation;

/**
 * @author ss
 * @date 2018/8/17 14:04
 */
public enum StaffType {
    SUPER_ADMIN("0"),
    ADMIN("1"),
    SUB_ADMIN("2"),
    NORMAL("3"),
    APPLY("4"),
    ALL("5");

    private String type;

    StaffType(String type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
