package com.nino.engineer.service;

import com.nino.engineer.dao.JpageDao;
import com.nino.engineer.domain.User;
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
}
