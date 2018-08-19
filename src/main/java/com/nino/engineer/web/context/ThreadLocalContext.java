package com.nino.engineer.web.context;

/**
 * @author ss
 * @date 2018/8/17 15:15
 */
public class ThreadLocalContext {
    private static final ThreadLocal<Integer> userLocal = new InheritableThreadLocal<>();
    private static final ThreadLocal<Integer> projectLocal = new InheritableThreadLocal<>();

    public static void setUserId(Integer u_id) {
        userLocal.set(u_id);
    }

    public static Integer getUserId() {
        return userLocal.get();
    }

    public static void setProjectId(Integer p_id) {
        projectLocal.set(p_id);
    }

    public static Integer getProjectId() {
        return projectLocal.get();
    }

    public static void clear() {
        userLocal.remove();
        projectLocal.remove();
    }
}
