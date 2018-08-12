package com.nino.engineer.dao;

import com.nino.engineer.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JpageDao {

    List<User> matchingLog(@Param("Username") String userName,@Param("Password") String password);
    User isExistenceMailAddress(String email);
    User isExistenceTelephone(String phone);
    User isExistenceUsername(String userName);
    User isExistenceUserID(int id);
    int register(User user);
    List<User> lookupAllUsers();
    /* 匹配用户权限 */
    Integer Matching_user_rights(@Param("u_id") int u_id,int j_id);
    /* 是否存在项目名 */
    List<Project> IsRepeat(String name);
    /* 添加地址信息 */
    int Add_address_info(Location location);
    /* 添加项目数据 */
    int Create_Project (Project project);
    /* 添加项目状态 */
    int AddProjectStatusInfo(Project_status project_status);
    /* 添加权限信息 */
    int Add_project_permission_information (Permissions_detailed permissions_detailed);
}
