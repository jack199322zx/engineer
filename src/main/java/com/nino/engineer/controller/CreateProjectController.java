package com.nino.engineer.controller;

import com.github.pagehelper.StringUtil;
import com.nino.engineer.domain.*;
import com.nino.engineer.service.CreateProjectService;
import com.nino.engineer.service.JpageService;
import com.nino.engineer.utils.date.GetTodayTime;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("create")
public class CreateProjectController {

    @Autowired
    CreateProjectService createProjectService;
    @Autowired
    JpageService jpageService;

    @ApiOperation(value = "获取创建项目页面", notes = "无参")
    @RequestMapping(value = "/c_page", method = RequestMethod.POST)
    public @ResponseBody
    Result Cre_page(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        /* 判断是否登陆 */
        if (jpageService.isLogin(request,response)) {
            /* 创建项目 需要 有 管理员 的权力 */
            if(createProjectService.Matching_user_rights((Integer) request.getSession().getAttribute("u_id"),2)){
                return Result.ok("/create/createProject.html");
            } else {
                return Result.fail("1","不具备创建的权力");
            }
        } else {
            return Result.fail("1", "请先登陆");
        }
    }


    @ApiOperation(value = "创建项目", notes = "传入相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p_name", value = "项目名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "l_id", value = "项目地址参数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "p_weather", value = "当时天气", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "p_ambient", value = "周围环境", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/c_pro", method = RequestMethod.POST)
    public Result Cre_pro(String p_name, String l_id, String p_weather, String p_ambient, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        /* 验证是否登陆 */
        if (jpageService.isLogin(request,response)) {
            /* 创建项目 需要 有 管理员 的权力 */
            if(createProjectService.Matching_user_rights((Integer) request.getSession().getAttribute("u_id"),2)){
                /* 项目名不能重复 */
                if(createProjectService.IsRepeat(p_name)){
                    /* 添加地址信息 */
                    Location location = Location.builder()
                            .U_id((Integer) request.getSession().getAttribute("u_id"))
                            .Username((String) request.getSession().getAttribute("username"))
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
                                .P_creator((String) request.getSession().getAttribute("username"))
                                .build();
                        /* 添加项目数据 */
                        if(createProjectService.Create_Project(project)){
                            if(project.getId()>0){
                                /* 配置项目状态info(默认:未完成) */
                                Project_status project_status = Project_status.builder()
                                        .U_id((Integer) request.getSession().getAttribute("u_id"))
                                        .Username((String) request.getSession().getAttribute("username"))
                                        .P_id(project.getId())
                                        .P_s("未完成")
                                        .build();
                                /* 添加项目状态 */
                                if(createProjectService.AddProjectStatusInfo(project_status)){
                                    /* 配置项目权限信息(默认:管理员) */
                                    Permissions_detailed permissions_detailed = Permissions_detailed.builder()
                                            .U_id((Integer) request.getSession().getAttribute("u_id"))
                                            .Username((String) request.getSession().getAttribute("username"))
                                            .J_id(1)
                                            .P_id(project.getId())
                                            .build();
                                    /* 添加权限信息 */
                                    if(createProjectService.Add_project_permission_information(permissions_detailed)){
                                        return Result.ok("");
                                    } else {
                                        return Result.fail("1", "配置项目权限时失败");
                                    }
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
            } else {
                return Result.fail("1", "不具备创建的权力");
            }
        } else {
            return Result.fail("1", "请先登陆");
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
    public Result createProgress(String time, String weather, String ambient, String IsNew_types_of_work, String New_types_of_workJSON, String ProgressInfo, String else_info, String p_id, String Enclosure,String EnclosureJSON,String p_name,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        /* 验证传来的参数 */
        if(time.length()>0&&weather.length()>0&&ambient.length()>0&&IsNew_types_of_work.length()>0&&ProgressInfo.length()>0&&else_info.length()>0&&Enclosure.length()>0&&p_name.length()>0&&p_id.length()>0&& isInteger(p_id)&&(IsNew_types_of_work.equals("false")||IsNew_types_of_work.equals("true"))&&(Enclosure.equals("false")||Enclosure.equals("true"))){

            /* 验证是否登陆 */
            if (jpageService.isLogin(request, response)) {
                /* 创建项目进度信息 需要 有 本项目 用户 的权力 */
                if(new Permissions_detailedDML().Is_Havepower((Integer) request.getSession().getAttribute("u_id"),Integer.valueOf(p_id),4)){
                    /* 编写项目进度实体 */
                    Project_progress project_progress = new Project_progress();
                    project_progress.setU_id((Integer) request.getSession().getAttribute("u_id"));
                    project_progress.setUsername((String) request.getSession().getAttribute("username"));
                    project_progress.setP_id(Integer.valueOf(p_id));
                    project_progress.setP_time(new GetTodayTime().NetworkTime());
                    project_progress.setP_weather(weather);
                    project_progress.setP_ambient(ambient);
                    project_progress.setP_describe(ProgressInfo);
                    project_progress.setP_else(else_info);


                    /* 判断是否 增加了工种 */
                    if(IsNew_types_of_work.equals("true")){
                        /* 增加工作种类信息 */
                        String JOSN = Addjob_categoryInfo(Integer.valueOf(p_id),New_types_of_workJSON);
                        if(AnalysisJSONArray.AnalysisJSONArray(JOSN,"errMsg").equals("OK")){
                            /* 继续完成添加进度信息操作 */
                            return Continue_adding_operations(Enclosure,(Integer) request.getSession().getAttribute("u_id"),Integer.valueOf(p_id),EnclosureJSON,project_progress,p_name);
                        } else {
                            return new Transformations_JSON().String_Transformations_JSON(JOSN);
                        }
                    } else if(IsNew_types_of_work.equals("false")) {
                        /*  没有工种,继续完成添加进度信息操作 */
                        return Continue_adding_operations(Enclosure,(Integer) request.getSession().getAttribute("u_id"),Integer.valueOf(p_id),EnclosureJSON,project_progress,p_name);
                    } else {
                        return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("IsNew_types_of_work 参数错误")+"\"}]");
                    }
                } else {
                    return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("不具备的权力")+"\"}]");
                }
            } else {
                return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\"" + new StringUtil().convert("请先登陆") + "\"}]");
            }
        } else {
            return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\"" + new StringUtil().convert("参数错误") + "\"}]");
        }
    }

    /* 对接  添加  工作种类  之后  继续完成添加  项目进度信息 */
    public net.sf.json.JSONArray Continue_adding_operations (String Enclosure,int u_id , int p_id , String EnclosureJSON,Project_progress project_progress,String p_name) {
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

            String JSON = AnalysisJSONArray.AnalysisJSONArray(Add_attachments_Info(u_id,p_id,EnclosureJSON),"errMsg");
            if(JSON.equals("OK")){
                /* 添加项目日志 */



                /* 附件添加成功  添加项目进度信息 */
                /* 不需要添加附件,继续完成 添加项目进度信息 */
                if(new Project_progressDML().AddProject_progressInfo(project_progress)){
                    return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\"OK\"}]");
                } else {
                    return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("项目信息添加失败")+"\"}]");
                }
            } else {
                /* 附件添加失败 */
                return new Transformations_JSON().String_Transformations_JSON(JSON);
            }
        } else if(Enclosure.equals("false")){
            /* 不需要添加附件,继续完成 添加项目进度信息 */
            if(new Project_progressDML().AddProject_progressInfo(project_progress)){
                return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\"OK\"}]");
            } else {
                return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("项目信息添加失败")+"\"}]");
            }
        } else {
            return new Transformations_JSON().String_Transformations_JSON("[{\"errMsg\":\""+new StringUtil().convert("Enclosure参数错误")+"\"}]");
        }
    }

    /* 验证工种JSON的正确性 */
    private boolean VerificationJSON (String JSON) {
        /* 验证 格式 errMsg:OK Status_Code:200*/
        if(AnalysisJSONArray.AnalysisJSONArray(JSON,"errMsg").equals("OK")&&AnalysisJSONArray.AnalysisJSONArray(JSON,"Status_Code").equals("200")){
            return true;
        } else {
            /* 解析失败 */
            return false;
        }
    }

    /* 添加工作种类 */
    private String Addjob_categoryInfo (int p_id,String New_types_of_workJSON) {
        /* 验证工种JSON的正确性 */
        if (VerificationJSON(New_types_of_workJSON)) {
            List<Type_of_work> type_of_works = new ArrayList<>();
            /* 生成工种实体 */
            for (JsonElement jsonElement : AnalysisJSONArray.AnalysisJSONArray(New_types_of_workJSON,"Type_of_work",true)) {
                JsonObject subObject = jsonElement.getAsJsonObject();
                Type_of_work type_of_work = new Type_of_work();
                type_of_work.setT_name(subObject.get("T_name").getAsString());
                type_of_work.setT_number(subObject.get("T_number").getAsInt());
                type_of_work.setP_id(p_id);
                type_of_works.add(type_of_work);
            }

            /* 保存工作种类信息 */
            if(new Type_of_workDML().Add_job_categoryInfo(type_of_works)){
                /* 保存成功 */
                return "[{\"errMsg\":\"Ok\"}]";
            } else {
                /* 保存失败 */
                return "[{\"errMsg\":\"添加工作种类信息时失败\"}]";
            }
        } else {
            return "[{\"errMsg\":\""+new StringUtil().convert("New_types_of_workJSON 解析失败")+"\"}]";
        }
    }

    /* 添加附件信息 */
    public String Add_attachments_Info (int u_id , int p_id , String EnclosureJSON) {
        /* 解析附件JSON数据 */
        if (VerificationJSON(EnclosureJSON)) {
            List<File_Info> file_infos = new ArrayList<>();

            /* 解析JOSN数据 */
            for (JsonElement jsonElement : AnalysisJSONArray.AnalysisJSONArray(EnclosureJSON,"fileInfo",true)) {
                JsonObject subObject = jsonElement.getAsJsonObject();
                /* 生成文件列表实体类 */
                File_Info file_info = new File_Info();
                file_info.setU_id(u_id);
                file_info.setP_id(p_id);
                file_info.setF_modular("项目进度附件");
                file_info.setF_path(subObject.get("F_path").getAsString());// 真实路径
                file_info.setF_network(subObject.get("F_network").getAsString());// 网络路劲
                file_info.setF_MD5(subObject.get("F_md5").getAsString());// 文件MD5
                file_infos.add(file_info);
            }

            /* 验证 文件MD5 是否重复 */
            if(new File_InfoDML().Matching(file_infos)){
                /* 添加文件附件 信息 */
                if(new File_InfoDML().Add_fileInfo(file_infos)){
                    return "[{\"errMsg\":\"OK\"}]";
                } else {
                    return "[{\"errMsg\":\""+new StringUtil().convert("文件信息写入失败")+"\"}]";
                }
            } else {
                return "[{\"errMsg\":\""+new StringUtil().convert("存在重复的文件MD5值")+"\"}]";
            }
        } else {
            return "[{\"errMsg\":\""+new StringUtil().convert("EnclosureJSON 解析失败")+"\"}]";
        }
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
