package com.nino.engineer.service;

import com.nino.engineer.dao.JpageDao;
import com.nino.engineer.domain.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class JpageService {

    @Autowired
    private JpageDao jpageDao;

    @SneakyThrows
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        return !(request.getSession().getAttribute("u_id")==null||request.getSession().getAttribute("username")==null);
    }

    /* 登陆接口 */
    public boolean matchingLog (String username, String password, HttpServletRequest request){
        List<User> users = jpageDao.matchingLog(username, password);
        if(users == null || users.isEmpty()){
            return false;
        }else {
            for(User user : users){
                /* 添加session信息 */
                request.getSession().setAttribute("u_id",user.getId());
                request.getSession().setAttribute("username",user.getUsername());
            }
            return true;
        }
    }


    /* 注册 */
    public boolean register (User user){
        return jpageDao.register(user) > -1;
    }

    /* 是否存在邮箱地址 */
    public boolean isExistenceMailAddress (String email){
        return jpageDao.isExistenceMailAddress(email)== null;
    }

    /* 是否存在手机号码 */
    public boolean isExistenceTelephone (String telephone){
        return jpageDao.isExistenceTelephone(telephone)== null;
    }

    /* 是否存在用户名 */
    public boolean isExistenceUsername (String username) {
        return jpageDao.isExistenceUsername(username) == null;
    }

    /* 验证用户ID是否存在 */
    public boolean isExistenceUserID (int id) {
        return jpageDao.isExistenceUserID(id) == null;
    }

    /* 查找所有用户 */
    public List<User> lookupAllUsers (String limit) {
        return jpageDao.lookupAllUsers();
    }

    /* 是否具有某种权力 */
    public boolean Matching_user_rights (int u_id,int j_id){
        return j_id >= jpageDao.Matching_user_rights(u_id,j_id);
    }

    /* 是否存在项目名(true:不存在) */
    public boolean IsRepeat (String name) {
        return jpageDao.IsRepeat(name) == null || jpageDao.IsRepeat(name).isEmpty();
    }

    /* 存储地址数据 */
    public boolean Add_address_info (Location location) {
        return jpageDao.Add_address_info(location) > -1;
    }

    /* 添加项目数据 */
    public boolean Create_Project (Project project) {
        return jpageDao.Create_Project(project) > -1;
    }

    /* 添加项目状态 */
    public boolean AddProjectStatusInfo (Project_status project_status) {
        return jpageDao.AddProjectStatusInfo(project_status) > -1;
    }

    /* 添加权限信息 */
    public boolean Add_project_permission_information (Permissions_detailed permissions_detailed) {
        return jpageDao.Add_project_permission_information(permissions_detailed) > -1;
    }
}
