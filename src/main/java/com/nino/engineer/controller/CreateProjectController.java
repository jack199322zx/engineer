package com.nino.engineer.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;
import com.nino.engineer.common.annotation.Logical;
import com.nino.engineer.common.annotation.StaffType;
import com.nino.engineer.common.annotation.SysPermission;
import com.nino.engineer.domain.*;
import com.nino.engineer.domain.dto.Progress;
import com.nino.engineer.domain.dto.System;
import com.nino.engineer.service.CreateProjectService;
import com.nino.engineer.service.JpageService;
import com.nino.engineer.service.PermissionsDetailedService;
import com.nino.engineer.utils.date.GetTodayTime;
import com.nino.engineer.web.context.ThreadLocalContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.scripts.JO;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ss
 * @date 2018/8/14 15:38
 */
@RestController
@RequestMapping("project")
public class CreateProjectController {

    @Autowired
    CreateProjectService createProjectService;
    @Autowired
    JpageService jpageService;
    @Autowired
    PermissionsDetailedService permissionService;

    @ApiOperation(value = "获取创建项目页面", notes = "无参")
    @RequestMapping(value = "/c_page", method = RequestMethod.GET)
    @SysPermission(value = {StaffType.ADMIN, StaffType.SUPER_ADMIN}, logical = Logical.OR)
    public ModelAndView Cre_page() {
        return new ModelAndView("/create/createProject.html");
    }


    @ApiOperation(value = "创建项目", notes = "传入相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p_name", value = "项目名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "l_id", value = "项目地址参数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "p_weather", value = "当时天气", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "p_ambient", value = "周围环境", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/c_pro", method = RequestMethod.POST)
    @SysPermission(value = {StaffType.ADMIN, StaffType.SUPER_ADMIN}, logical = Logical.OR)
    public Result Cre_pro(String p_name, String l_id, String p_weather, String p_ambient, HttpServletRequest request) {
        /* 项目名不能重复 */
        if(createProjectService.IsRepeat(p_name)){
            /* 添加地址信息 */
            int userId = ThreadLocalContext.getUserId();
            String userName = (String) request.getSession().getAttribute("username");
            Location location = Location.builder()
                    .U_id(userId)
                    .Username(userName)
                    .Project_location(l_id)
                    .build();
            /* 存储地址数据 */
            if(createProjectService.Add_address_info(location)){
                /* 配置项目基本信息 */
                Project project = Project.builder()
                        .P_name(p_name)
                        .P_weather(p_weather)
                        .P_ambient(p_ambient)
                        .P_time(new GetTodayTime().NetworkTime())
                        .L_id(location.getId())
                        .P_creator_Id(String.valueOf(userId))
                        .build();
                /* 添加项目数据 */
                if(createProjectService.Create_Project(project)){
                    if(project.getId()>0){
                        /* 配置项目状态info(默认:未完成) */
                        Project_status project_status = Project_status.builder()
                                .U_id(userId)
                                .Username(userName)
                                .P_id(project.getId())
                                .P_s("未完成")
                                .build();
                        /* 添加项目状态 */
                        if(createProjectService.AddProjectStatusInfo(project_status)){
                            return Result.ok("");
                        } else {
                            return Result.fail("1", "添加项目状态时失败");
                        }
                    } else {
                        return Result.fail("1", "创建项目时返回无返回参数");
                    }
                } else {
                    return Result.fail("1", "项目创建时失败");
                }
            } else {
                return Result.fail("1", "地址信息添加失败");
            }
        } else {
            return Result.fail("1", "项目名重复");
        }
    }

    @ApiOperation(value = "创建项目进度", notes = "传入对应参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time", value = "记录日期", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "weather", value = "天气", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ambient", value = "现场环境", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "IsNew_types_of_work", value = "是否新增了工种", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "New_types_of_workJSON", value = "新增工种的JSON数据", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ProgressInfo", value = "进度信息", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "else_info", value = "其他的信息", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "Enclosure", value = "是否存在附件", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "EnclosureJSON", value = "附件的JSON数据", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "p_name", value = "项目名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "p_id", value = "项目ID", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/c_progress", method = RequestMethod.POST)
    @SysPermission(value = {StaffType.ADMIN, StaffType.SUPER_ADMIN, StaffType.SUB_ADMIN}, logical = Logical.OR)
    public Result createProgress(@RequestBody Progress progress, HttpServletRequest request) {
        /* 验证传来的参数 */
        if(progress != null && validateParam(progress)){
            int j_id = permissionService.findPermissionId(ThreadLocalContext.getUserId());
            if (String.valueOf(j_id) == StaffType.SUPER_ADMIN.getType()) {
                // 是子管理员
                int p_id = permissionService.findProjectIdByCreator(ThreadLocalContext.getUserId());
                if (p_id != progress.getPId()) return Result.fail("1", "不具备的权力");
            }
            /* 编写项目进度实体 */
            Project_progress project_progress = new Project_progress();
            project_progress.setU_id((Integer) request.getSession().getAttribute("u_id"));
            project_progress.setUsername((String) request.getSession().getAttribute("username"));
            project_progress.setP_id(progress.getPId());
            project_progress.setP_time(new GetTodayTime().NetworkTime());
            project_progress.setP_weather(progress.getWeather());
            project_progress.setP_ambient(progress.getAmbient());
            project_progress.setP_describe(progress.getProgressInfo());
            project_progress.setP_else(progress.getElseInfo());
            /* 判断是否 增加了工种 */
            if(progress.getIsNewTypeOfWork() == 1){
                /* 增加工作种类信息 */
                String Json = Addjob_categoryInfo(Integer.valueOf(progress.getPId()), progress.getNewTypeOfWorkJSON());
                if(StringUtils.isNotBlank(Json)){
                    /* 继续完成添加进度信息操作 */
                    return Continue_adding_operations(progress.getEnclosure(),(Integer) request.getSession().getAttribute("u_id"),progress.getPId(),progress.getEnclosureJSON(),project_progress,progress.getPName());
                } else {
                    return Result.ok(Json);
                }
            } else if(progress.getIsNewTypeOfWork() == 0) {
                /*  没有工种,继续完成添加进度信息操作 */
                return Continue_adding_operations(progress.getEnclosure(),(Integer) request.getSession().getAttribute("u_id"),progress.getPId(),progress.getEnclosureJSON(),project_progress,progress.getPName());
            } else {
                return Result.fail("1", "参数错误");
            }
        } else {
            return Result.fail("1", "参数错误");
        }
    }

    /* 对接  添加  工作种类  之后  继续完成添加  项目进度信息 */
    public Result Continue_adding_operations (String Enclosure,int u_id , int p_id , String EnclosureJSON,Project_progress project_progress,String p_name) {
        /* 判断是否需要添加附件 */
        if(Enclosure.equals("true")){
            /* 编写 日志  实体 */
            Journal journal = new Journal();
            journal.setU_id(u_id);
            journal.setP_id(p_id);
            journal.setP_name(p_name);
            journal.setJ_Time(new GetTodayTime().NetworkTime());
            journal.setJ_week("周"+new GetTodayTime().getDay());
            journal.setJ_weather(project_progress.getP_weather());
            journal.setJ_ambient(project_progress.getP_ambient()+"\n记录人:"+project_progress.getUsername());
//            journal.setJ_work();
//            journal.setJ_Enter();
//            journal.setJ_inspection();
//            journal.setJ_patrol();
//            journal.setJ_parallel();
//            journal.setJ_reply();
//            journal.setJ_rectification();
//            journal.setJ_unanswered();
//            journal.setJ_other();

            String json = Add_attachments_Info(u_id,p_id,EnclosureJSON);
            if(json != null){
                /* 添加项目日志 */
                /* 附件添加成功  添加项目进度信息 */
                /* 不需要添加附件,继续完成 添加项目进度信息 */
                if(createProjectService.AddProject_progressInfo(project_progress)){
                    return Result.ok("");
                } else {
                    return Result.fail("1", "项目信息添加失败");
                }
            } else {
                /* 附件添加失败 */
                return Result.fail("1", "添加附件失败");
            }
        } else if(Enclosure.equals("false")){
            /* 不需要添加附件,继续完成 添加项目进度信息 */
            if(createProjectService.AddProject_progressInfo(project_progress)){
                return Result.ok("");
            } else {
                return Result.fail("1", "项目信息添加失败");
            }
        } else {
            return Result.fail("1", "Enclosure参数错误");
        }
    }

    /* 验证工种JSON的正确性 */
    private boolean VerificationJSON (String JSON) {
        /* 验证 格式 errMsg:OK Status_Code:200*/
//        if(AnalysisJSONArray.AnalysisJSONArray(JSON,"errMsg").equals("OK")&&AnalysisJSONArray.AnalysisJSONArray(JSON,"Status_Code").equals("200")){
//            return true;
//        } else {
//            /* 解析失败 */
//            return false;
//        }
        return true;
    }

    private boolean validateParam(@NonNull Progress progress) {
        return !(StringUtils.isBlank(progress.getAmbient()) ||
                StringUtils.isBlank(progress.getElseInfo()) ||
                StringUtils.isBlank(progress.getEnclosure()) ||
                StringUtils.isBlank(progress.getEnclosureJSON()) ||
                StringUtils.isBlank(progress.getNewTypeOfWorkJSON()) ||
                StringUtils.isBlank(progress.getProgressInfo()) ||
                StringUtils.isBlank(progress.getTime()) ||
                StringUtils.isBlank(progress.getPName()) ||
                StringUtils.isBlank(progress.getWeather()) ||
                progress.getIsNewTypeOfWork() == 0 ||
                progress.getPId() == 0);
    }

    /* 添加工作种类 */
    private String Addjob_categoryInfo (int p_id,String New_types_of_workJSON) {
        /* 验证工种JSON的正确性 */
//        if (VerificationJSON(New_types_of_workJSON)) {
//            List<Type_of_work> type_of_works = new ArrayList<>();
//            /* 生成工种实体 */
//            for (JsonElement jsonElement : AnalysisJSONArray.AnalysisJSONArray(New_types_of_workJSON,"Type_of_work",true)) {
//                JsonObject subObject = jsonElement.getAsJsonObject();
//                Type_of_work type_of_work = new Type_of_work();
//                type_of_work.setT_name(subObject.get("T_name").getAsString());
//                type_of_work.setT_number(subObject.get("T_number").getAsInt());
//                type_of_work.setP_id(p_id);
//                type_of_works.add(type_of_work);
//            }
//
//            /* 保存工作种类信息 */
//            if(new Type_of_workDML().Add_job_categoryInfo(type_of_works)){
//                /* 保存成功 */
//                return "[{\"errMsg\":\"Ok\"}]";
//            } else {
//                /* 保存失败 */
//                return "[{\"errMsg\":\"添加工作种类信息时失败\"}]";
//            }
//        } else {
//            return "[{\"errMsg\":\""+new StringUtil().convert("New_types_of_workJSON 解析失败")+"\"}]";
//        }
        return "";
    }

    /* 添加附件信息 */
    public String Add_attachments_Info (int u_id , int p_id , String EnclosureJSON) {
        /* 解析附件JSON数据 */
//        if (VerificationJSON(EnclosureJSON)) {
//            List<File_Info> file_infos = new ArrayList<>();
//
//            /* 解析JOSN数据 */
//            for (JsonElement jsonElement : AnalysisJSONArray.AnalysisJSONArray(EnclosureJSON,"fileInfo",true)) {
//                JsonObject subObject = jsonElement.getAsJsonObject();
//                /* 生成文件列表实体类 */
//                File_Info file_info = new File_Info();
//                file_info.setU_id(u_id);
//                file_info.setP_id(p_id);
//                file_info.setF_modular("项目进度附件");
//                file_info.setF_path(subObject.get("F_path").getAsString());// 真实路径
//                file_info.setF_network(subObject.get("F_network").getAsString());// 网络路劲
//                file_info.setF_MD5(subObject.get("F_md5").getAsString());// 文件MD5
//                file_infos.add(file_info);
//            }
//
//            /* 验证 文件MD5 是否重复 */
//            if(new File_InfoDML().Matching(file_infos)){
//                /* 添加文件附件 信息 */
//                if(new File_InfoDML().Add_fileInfo(file_infos)){
//                    return "[{\"errMsg\":\"OK\"}]";
//                } else {
//                    return "[{\"errMsg\":\""+new StringUtil().convert("文件信息写入失败")+"\"}]";
//                }
//            } else {
//                return "[{\"errMsg\":\""+new StringUtil().convert("存在重复的文件MD5值")+"\"}]";
//            }
//        } else {
//            return "[{\"errMsg\":\""+new StringUtil().convert("EnclosureJSON 解析失败")+"\"}]";
//        }
        return "";
    }


    public System findProjectInfo() {
        return null;
    }


    /**
     * 正则验证是否是整数
     * @param str
     * @return
     */
    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
