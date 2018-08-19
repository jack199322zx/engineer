package com.nino.engineer.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.nino.engineer.dao.CreateProjectDao;
import com.nino.engineer.dao.JurisDictionDao;
import com.nino.engineer.dao.LocationDao;
import com.nino.engineer.dao.PermissionsDetailedDao;
import com.nino.engineer.domain.Permissions_detailed;
import com.nino.engineer.domain.Project;
import com.nino.engineer.domain.Project_status;
import com.nino.engineer.domain.Result;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:42
 */
@Service
public class PermissionsDetailedService {

    @Autowired
    JurisDictionDao jurisDictionDao;
    @Autowired
    CreateProjectDao createProjectDao;
    @Autowired
    PermissionsDetailedDao permissionsDetailedDao;
    @Autowired
    LocationDao locationDao;

    /* 登陆成功后执行,读取项目 */
//    @SuppressWarnings("unchecked")
//    public Result getProject (int u_id , int page) {
//        if(jurisDictionDao.matchingUserRights(u_id) > -1){/* 如果用户是超管||管理 */
//            /* 输出所有项目 */
//            /* 管理员 读取 所有的项目 */
//            PageHelper.startPage(page, 10);
//            List<Project> projects = createProjectDao.DisplayAllProject();
//            PageInfo pageInfo = new PageInfo(projects);
//            if(projects != null && projects.isEmpty()){
//                    /* 读取的值不为空 */
//                return Result.fail("1", "暂无项目");
//            } else {
//                    /* 为管理员 返回 或有的 项目 */
//                    /* 此处查找 四处 (唯一条件是 limit)*/
//                    /* 1.查找所有的项目 */
//                    /* 2.查找所有的项目状态 */
//                    /* 3.查找所有的项目对应的子管理 */
//                    /* 4.项目对应的地址 */
//                return Result.ok(ImmutableMap.of("pro_info", projects, "pro_det", permissionsDetailedDao.usersProject(projects),
//                            "pro_location", locationDao.accordingToaddressID(projects), "pro_size", Get_projectNumber(u_id), "pro_det",
//                            "pro_status", createProjectDao.Lookup_all()
//                        ));
//                return Result.ok("");
//            }
//        } else {
//            /* 用户ID是否存在 */
//            if(new UserDML().IsExistenceUserID(u_id)){
//                return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("参数错误 用户ID不存在")+"\"}]");
//            } else {
//                String limit = new com.ToolClass.paging.PageData().PangingSQL(new PageBean(page));
//                if(limit.equals("false")){
//                    return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("页码错误")+"\"}]");
//                } else {
//                    /* 获取所有与用户关联的项目 */
//                    List<Permissions_detailed> permissionsDetaileds = new Permissions_detailedDML().DisplayAllProject_according_toUserID(u_id,limit);
//                    /* 对项目进行筛选 */
//                    /* 筛选出所有 未完成的(可见) */
//                    Project_status project_status = new Project_status();
//                    project_status.setU_id(u_id);
//                    /* 拿到用户的项目状态 */
//                    List<Project_status> project_statuses = new Project_statusDML().Screening_content(project_status);
//                    /* 筛选 */
//                    if(project_statuses.isEmpty()){
//                        return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("没有正在进行中的项目")+"\"}]");
//                    } else {
//                        /* 拿到项目ID 根据ID 查询 对应的Info */
//                        List<Integer> integers = new ArrayList<>();
//                        for(Project_status projectStatus : project_statuses){
//                            integers.add(project_status.getP_id());
//                        }
//                        /* 组装 JSON */
//                        if(integers.isEmpty()){
//                            return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("没有查询到项目信息")+"\"}]");
//                        } else {
//                            /* 为用户返回用户能看见的项目 */
//                            /* 1.查找所有的项目 */
//                            /* 2.所有的项目状态均为未完成 */
//                            /* 3.查找所有的项目对应的子管理 */
//                            /* 4.项目对应的地址 */
//                            List<Project> projects = new ProjectDML().According_toID(integers);
//                            return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\"OK\",\"pro_info\":"+new Transformations_JSON().List_Transformations_JSON(projects)+
//                                    ",\"pro_det\":"+new Transformations_JSON().List_Transformations_JSON(new Permissions_detailedDML().Users_project(projects))+
//                                    ",\"pro_location\":"+new Transformations_JSON().List_Transformations_JSON(new LocationDML().According_toaddressID(projects))+
//                                    ",\"pro_size\":"+new Permissions_detailedDML().Get_projectNumber(u_id)+
//                                    ",\"user_code\":2}]");
//                        }
//                    }
//                }
//            }
//        }
//    }

    /* 返回用户对应项目的总数 */
    public int Get_projectNumber (int u_id) {
        int num;
        if(jurisDictionDao.matchingUserRights(u_id) > -1){
            num = createProjectDao.projectNumber(-1);
        } else {
            num = createProjectDao.projectNumber(u_id);
        }
        return num;
    }

    /* 查找所有的项目对应的子管理 */
    public List<Permissions_detailed> Users_project (List<Project> projects) {
        /* 遍历的项目不是空的 */
        if(projects != null)
            return permissionsDetailedDao.usersProject(projects);
        return Lists.newArrayList();
    }

    /* 获取所有与用户相关的项目 */
    public List<Permissions_detailed> DisplayAllProject_according_toUserID (int u_id) {
        return permissionsDetailedDao.displayAllProjectAccordingToUserID(u_id);
    }

    /* 添加项目权限信息 */
    public boolean Add_project_permission_information (Permissions_detailed permissions_detailed) {
        return permissionsDetailedDao.addProjectPermissionInformation(permissions_detailed) > 0;
    }

    /* 是否 拥有 本项目  的  某种权力 */
    public boolean Is_Havepower (int u_id, int p_id , int j_id) {
        int type =  permissionsDetailedDao.isHavePower(u_id, p_id);
        return j_id >= type && type > 0;
    }

    public int findProjectIdByCreator(int u_id) {
        return permissionsDetailedDao.findProjectIdByCreator(u_id);
    }

    public int findPermissionId(int u_id) {
        return permissionsDetailedDao.judgePermission(u_id);
    }

}
