package com.nino.engineer.controller;

import com.github.pagehelper.StringUtil;
import com.nino.engineer.dao.ConfigurationDao;
import com.nino.engineer.domain.*;
import com.nino.engineer.service.JpageService;
import com.nino.engineer.utils.date.GetTodayTime;
import com.nino.engineer.utils.mail.Main;
import com.nino.engineer.utils.message.SendShortMessage;
import com.nino.engineer.utils.random.Generating_order_number;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;


@RestController
@Api(value = "跳转页面用,URL不显示详细地址")
public class JpageController {

    @Autowired
    JpageService jpageService;

    @Autowired
    ConfigurationDao configurationDao;

    @ApiOperation(value="判断是否登陆", notes="无参")
    @RequestMapping(value = "log/Islogin",method = {RequestMethod.GET,RequestMethod.POST})
    public boolean Islogin (HttpServletRequest request, HttpServletResponse response) throws IOException {
        return jpageService.isLogin(request,response);
    }

    @ApiOperation(value="跳转_登陆页面", notes="无参")
    @RequestMapping(value = "login",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView jumpLogin (HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(jpageService.isLogin(request,response)){
            return new ModelAndView("index");
        } else {
            return new ModelAndView("/login/login.html");
        }
    }

    @ApiOperation(value="跳转_主页", notes="无参")
    @RequestMapping(value = "index", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView jumpIndex (HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(jpageService.isLogin(request,response)){
            return new ModelAndView("/index/index.html");
        } else {
            return new ModelAndView("login");
        }
    }


    /**
     * 用户登陆API
     * @param username
     * @param password
     * @return
     */
    @ApiOperation(value="用户登陆", notes="传入用户名,密码")//给API增加方法说明。
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })//包含一组参数说明。
    @RequestMapping(value = "log/login",method = RequestMethod.POST)
    public Result loginApi(String username, String password, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        return jpageService.matchingLog(username,password,request) ? Result.ok("") : Result.fail("1", "账号密码不匹配");
    }

    @ApiOperation(value="发送邮件API", notes="传入邮件地址")
    @ApiImplicitParam(name = "MailAddress", value = "邮件地址", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "reg/SendMail",method = RequestMethod.POST)
    public Result sendMailAPI(String MailAddress, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        boolean Isallow = false;
        /* 禁止60s内调用 */
        if(request.getSession().getAttribute("mail_time") == null){
            /* 从未调用过 */
            Isallow = true;
        } else {
            long time = (long) request.getSession().getAttribute("mail_time");
            /* 判断是否超过60s */
            if(time <= new GetTodayTime().DateConversion_to_Milliseconds(new GetTodayTime().GetNetworkTodayTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss")){
                Isallow = true;
            } else {
                Isallow = false;
            }
        }
        if(Isallow){

            /* 删除session */
            request.getSession().removeAttribute("mail_code");
            request.getSession().removeAttribute("mail_address");
            request.getSession().removeAttribute("mail_time");

            /* 判断邮箱是否存在 */
            if(jpageService.isExistenceMailAddress(MailAddress)){
                /* 生成6为数验证码 */
                String code = new Generating_order_number().getItemID(6);
                /* 当邮箱不存在时,发送邮件 */
                if(Main.sendMailByQQ(configurationDao.readConfigure(), MailAddress, "邮箱验证码", "您的验证码是:" + code).equals("Success")){

                    /* 存入session:code */
                    request.getSession().setAttribute("mail_code",code);
                    /* 存入session:mailAddress */
                    request.getSession().setAttribute("mail_address",MailAddress);
                    /* 存入session:time */
                    request.getSession().setAttribute("mail_time",new GetTodayTime().DateConversion_to_Milliseconds(new GetTodayTime().GetNetworkTodayTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss")+60000);

                    return Result.ok("");
                } else {
                    /* 邮件发送失败 */
                    return Result.fail("1","邮件发送失败");
                }
            } else {
                return Result.fail("1","邮箱已存在");
            }
        } else {
            return Result.fail("1","请勿重复发送");
        }
    }

    /**
     * 发送短信API
     * @param telephone
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value="发送短信API", notes="传入手机号码")
    @ApiImplicitParam(name = "telephone", value = "手机号码", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/SendShortMessage",method = RequestMethod.POST)
    public Result sendShortMessageAPI(String telephone, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        boolean Isallow = false;
        /* 禁止60s内调用 */
        if(request.getSession().getAttribute("ShortMessage_time") == null){
            /* 从未调用过 */
            Isallow = true;
        } else {
            long time = (long) request.getSession().getAttribute("ShortMessage_time");
            /* 判断是否超过60s */
            if(time <= new GetTodayTime().DateConversion_to_Milliseconds(new GetTodayTime().GetNetworkTodayTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss")){
                Isallow = true;
            } else {
                Isallow = false;
            }
        }
        if(Isallow){

            /* 删除session */
            request.getSession().removeAttribute("ShortMessage_code");
            request.getSession().removeAttribute("Telephone");
            request.getSession().removeAttribute("ShortMessage_time");

            /* 判断手机号码是否是数字 */
            if(isInteger(telephone)&&telephone.length()>=11){
                /* 判断手机号码是否存在 */
                if(jpageService.isExistenceTelephone(telephone)){
                    /* 生成6为数验证码 */
                    String code = new Generating_order_number().getItemID(6);
                    /* 发送短信 */
                    if(SendShortMessage.shortMessage(configurationDao.readConfigure(), telephone,code)){

                        /* 存人session:ShortMessage_code */
                        request.getSession().setAttribute("ShortMessage_code",code);
                        /* 存人session:Telephone */
                        request.getSession().setAttribute("Telephone",telephone);
                        /* 存人session:ShortMessage_time */
                        request.getSession().setAttribute("ShortMessage_time",new GetTodayTime().DateConversion_to_Milliseconds(new GetTodayTime().GetNetworkTodayTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss")+60000);

                        return Result.ok("");
                    } else {
                        return Result.fail("1", "短信发送失败");
                    }
                } else {
                    return Result.fail("1", "该号码已存在");
                }
            } else {
                return Result.fail("1", "请输入正确的手机号码");
            }
        } else {
            return Result.fail("1", "请勿重复发送");
        }
    }


    /**
     * 注册用户的API
     * @return
     */
    @ApiOperation(value="注册用户的API", notes="传入相应的用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮件地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "邮件验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "telephone", value = "电话号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "company", value = "单位", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "department", value = "部门", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public Result register(String username, String password, String email, String code, String telephone, String company, String department,
                                   HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        /* 验证 邮箱 验证码 */
        if(request.getSession().getAttribute("mail_code") != null){
            if(request.getSession().getAttribute("mail_code").equals(code)&&request.getSession().getAttribute("mail_address").equals(email)){
                /* 验证用户名是否存在 */
                if(jpageService.isExistenceUsername(username)){
                    /* 验证邮箱是否存在 */
                    if(jpageService.isExistenceMailAddress(email)){
                        /* 判断手机号码是否是数字 */
                        if(isInteger(telephone)&&telephone.length()>=11){
                            /* 判断手机号码是否存在 */
                            if(jpageService.isExistenceTelephone(telephone)){
                                /* 生成 user 实体 */
                                User user = User.builder()
                                        .Username(username)
                                        .Password(password)
                                        .U_email(email)
                                        .U_company(company)
                                        .U_telephone(telephone)
                                        .U_company(company)
                                        .U_department(department)
                                        .build();
                                /* 提交信息  完成  注册 */
                                if(jpageService.register(user)){
                                    return Result.ok("");
                                } else {
                                    return Result.fail("1", "注册失败,请重试");
                                }
                            } else {
                                return Result.fail("1", "该号码已存在");
                            }
                        } else {
                            return Result.fail("1", "请输入正确的手机号码");
                        }
                    } else {
                        return Result.fail("1", "邮箱已存在");
                    }
                } else {
                    return Result.fail("1", "用户名已存在");
                }
            } else {
                return Result.fail("1", "验证码输入错误");
            }
        } else {
            return Result.fail("1", "请获取邮箱验证码");
        }
    }


    @ApiOperation(value = "获取创建项目页面", notes = "无参")
    @RequestMapping(value = "/c_page", method = RequestMethod.POST)
    public @ResponseBody
    Result Cre_page(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        /* 判断是否登陆 */
        if (jpageService.isLogin(request,response)) {
            /* 创建项目 需要 有 管理员 的权力 */
            if(jpageService.Matching_user_rights((Integer) request.getSession().getAttribute("u_id"),2)){
                return Result.ok("[{\"url\":\"/create/createProject.html\"}]");
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
            if(jpageService.Matching_user_rights((Integer) request.getSession().getAttribute("u_id"),2)){
                /* 项目名不能重复 */
                if(jpageService.IsRepeat(p_name)){
                    /* 添加地址信息 */
                    Location location = Location.builder()
                            .U_id((Integer) request.getSession().getAttribute("u_id"))
                            .Username((String) request.getSession().getAttribute("username"))
                            .Project_location(l_id)
                            .build();
                    /* 存储地址数据 */
                    if(jpageService.Add_address_info(location)){
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
                        if(jpageService.Create_Project(project)){
                            if(project.getId()>0){
                                /* 配置项目状态info(默认:未完成) */
                                Project_status project_status = Project_status.builder()
                                        .U_id((Integer) request.getSession().getAttribute("u_id"))
                                        .Username((String) request.getSession().getAttribute("username"))
                                        .P_id(project.getId())
                                        .P_s("未完成")
                                        .build();
                                /* 添加项目状态 */
                                if(jpageService.AddProjectStatusInfo(project_status)){
                                    /* 配置项目权限信息(默认:管理员) */
                                    Permissions_detailed permissions_detailed = Permissions_detailed.builder()
                                            .U_id((Integer) request.getSession().getAttribute("u_id"))
                                            .Username((String) request.getSession().getAttribute("username"))
                                            .J_id(1)
                                            .P_id(project.getId())
                                            .build();
                                    /* 添加权限信息 */
                                    if(jpageService.Add_project_permission_information(permissions_detailed)){
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