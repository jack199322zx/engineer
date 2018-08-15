package com.nino.engineer.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.nino.engineer.dao.CreateProjectDao;
import com.nino.engineer.dao.PermissionsDetailedDao;
import com.nino.engineer.domain.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 15:40
 */
@Service
public class CreateProjectService {

    @Autowired
    CreateProjectDao dao;
    @Autowired
    PermissionsDetailedDao detailedDao;
    /* 是否具有某种权力 */
    public boolean Matching_user_rights (int u_id,int j_id){
        return j_id >= dao.Matching_user_rights(u_id,j_id);
    }

    /* 是否存在项目名(true:不存在) */
    public boolean IsRepeat (String name) {
        return dao.IsRepeat(name) == null || dao.IsRepeat(name).isEmpty();
    }

    /* 存储地址数据 */
    public boolean Add_address_info (Location location) {
        return dao.Add_address_info(location) > -1;
    }

    /* 添加项目数据 */
    public boolean Create_Project (Project project) {
        return dao.Create_Project(project) > -1;
    }

    /* 添加项目状态 */
    public boolean AddProjectStatusInfo (Project_status project_status) {
        return dao.AddProjectStatusInfo(project_status) > -1;
    }

    /* 添加权限信息 */
    public boolean Add_project_permission_information (Permissions_detailed permissions_detailed) {
        return dao.Add_project_permission_information(permissions_detailed) > -1;
    }

    /* 读取所有的项目 */
    public PageInfo DisplayAllProject (String limit) {
        PageHelper.startPage(1, 10);
        List<Project> projects = dao.DisplayAllProject();
        PageInfo pageInfo = new PageInfo(projects);
        return pageInfo;
    }

    /* 根据项目ID查询 */
    public List<Project> According_toID (List<Integer> projectID) {
        if(projectID.isEmpty())
            return Lists.newArrayList();
        return dao.According_toID(projectID);
    }

    /* 查询项目总数 */
    public Integer ProjectNumber (int u_id) {
        return u_id == -1 ? dao.projectNumber(0) : detailedDao.userProNumber(u_id);
    }

    /* 添加项目进度信息 */
    public boolean AddProject_progressInfo (Project_progress project_progress) {
        return dao.AddProject_progressInfo(project_progress) > -1;
    }

    /* 为管理员查找所有的 */
    public PageInfo Lookup_all (){
        PageHelper.startPage(1, 10);
        List<Project_status> project_statuses = dao.Lookup_all();
        PageInfo pageInfo = new PageInfo(project_statuses);
        return pageInfo;
    }

    /*  筛选出所有 未完成的(可见) */
    public List<Project_status> Screening_content (Project_status project_status){
        return dao.Screening_content(project_status);
    }


    /* 添加项目状态数据 */
    public boolean Add_project_statusInfo (Project_status project_status) {
        return dao.Add_project_statusInfo(project_status) > 0;
    }
}
