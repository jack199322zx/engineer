package com.nino.engineer.controller;

import com.nino.engineer.dao.ConfigurationDao;
import com.nino.engineer.domain.Configure;
import com.nino.engineer.domain.Result;
import com.nino.engineer.domain.User;
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

    @ApiOperation(value="跳转_登陆页面", notes="无参")
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView jumpLogin (HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(jpageService.isLogin(request,response)){
            return new ModelAndView("/index");
        } else {
            return new ModelAndView("/login/login.html");
        }
    }

    @ApiOperation(value="跳转_主页", notes="无参")
    @GetMapping("/index")
    public ModelAndView jumpIndex (HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(jpageService.isLogin(request,response)){
            return new ModelAndView("/index/index.html");
        } else {
            return new ModelAndView("/login");
        }
    }

    /**
     * 判断用户是否登陆
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value="判断用户是否登陆", notes="无参,返回(true||false)")
    @RequestMapping(value = "log/Islogin",method = RequestMethod.GET)
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return jpageService.isLogin(request, response);
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