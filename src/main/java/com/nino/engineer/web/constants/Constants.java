package com.nino.engineer.web.constants;

import java.util.Arrays;
import java.util.List;

public interface Constants {
    List<String> ignoreUrl = Arrays.asList("/login", "/log/login", "/login/login.html", "/");
    String RSPCODE_FAILED = "1"; // 失败
    String RSPCODE_STAFF_NOAUTHORIZATION = "12"; // 没有权限
}
