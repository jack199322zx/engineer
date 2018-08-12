/* 登陆注册页面切换 */
function login_switch(hide,show) {
    $(hide).hide();
    $(show).show();
}

/* 调取登陆API */
function login() {
    /* 不能低于5位数 */
    if($('#admin_login input[name="username"]').val().length>=5&&$('#admin_login input[name="password"]').val().length>=5){
    	/* 调取login api */
    	$.post("/log/login",{
            username:$('#admin_login input[name="username"]').val(),
    		password:$('#admin_login input[name="password"]').val()
    	},function (result){
            console.log(result);
            if(result.code==="0"){
                location.href = '/index';
                location.reload();
            } else {
                $('.loading').remove();
                popup({type:'error',msg:result.message,delay:1500,bg:true});
            }
            // $.each(result,function (i, result) {
            //
            // });
    	});
    }else{
        /* 去掉loading */
        $('.loading').remove();
        popup({type:'error',msg:"用户名或密码长度小于5",delay:1500,bg:true});
    }
}

/* 调取注册API */
function register() {
    /* 不能低于5位数 */
    if($('#register input[name="username"]').val().length>=5&&$('#register input[name="password"]').val().length>=5){
        /* 验证邮箱 */
        if(Verifying_email_address('#register input[name="email"]')){
            /* 验证手机号 */
            if(Verifying_telephone('#register input[name="telephone"]')){
                /* 调用注册API */
                $("body").append('<div class="loading"><div class="mask"></div>' +
                    '<div id="animationTipBox" style="width: 180px; height: 150px; margin-left: -90px; margin-top: -75px;"><div class="load"><div class="icon_box"><div class="cirBox1"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox2"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox3"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div></div></div><div class="dec_txt">加载中...</div></div></div>');
                $.post("/reg/reg",{
                    username:$('#register input[name="username"]').val(),
                    password:$('#register input[name="password"]').val(),
                    email:$('#register input[name="email"]').val(),
                    code:$('.code_icon input[name="email_code"]').val(),
                    telephone:$('#register input[name="telephone"]').val(),
                    company:$('#register input[name="company"]').val(),
                    department:$('#register input[name="department"]').val()
                },function(e){
                    if(e.code=="0"){
                        $('#admin_login input[name="username"]').val($('#register input[name="username"]').val());
                        $('#admin_login input[name="password"]').val($('#register input[name="password"]').val());
                        login();
                    } else {
                        $('.loading').remove();
                        popup({type:'error',msg:e.message,delay:1500,bg:true});
                    }
                });
            }
        }
    }else{
        /* 去掉loading */
        $('.loading').remove();
        popup({type:'error',msg:"用户名或密码长度小于5",delay:1500,bg:true});
    }
}

/* 获取邮箱验证码 */
function email_code(){
	/* 验证邮箱 */
	if(Verifying_email_address('#register input[name="email"]')){
		/* 调用发送邮件API */
		$.post("/reg/SendMail",{
            MailAddress:$('#register input[name="email"]').val()
		},function (e) {
            if(e.code!="0"){
                /* 去掉loading */
                $('.loading').remove();
                popup({type:'error',msg:e.message,delay:1500,bg:true});
            }
        });
		Prohibit();
	}
}

/* 设置 禁止60s 方法*/
var wait = 60;
function Prohibit() {
    if (wait == 0) {
        $("#verificationCode").attr("disabled",false);
        $("#verificationCode").html("获取验证码");
        $("#verificationCode").css("font-size","16px");
        $("#verificationCode").css("top","0px");
        wait = 60;
    } else {
        $("#verificationCode").attr("disabled",true);
        $("#verificationCode").html(wait+"秒后可以重新发送");
        $("#verificationCode").css("font-size","13px");
        $("#verificationCode").css("position","relative");
        $("#verificationCode").css("top","-5px");
        wait--;
        setTimeout(function() {
            Prohibit()
        }, 1000)
    }
}

/* 验证 邮箱格式*/
function Verifying_email_address(obj) {
    var text = $(obj).val();
    var regex = /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
    if (text.match(/^\s+$/) || text.match(/^[ ]+$/)
        || text.match(/^[ ]/)
        || text.match(/^[ ]*$/)
        || text.match(/^\s*$/)
        || text.length<3) {
        // 关闭loading
        $('.loading').remove();
        popup({type:'error',msg:"邮件地址不符合标准",delay:1500,bg:true});
        return false
    } else {
        if(text.length>85){
            // 关闭loading
            $('.loading').remove();
            popup({type:'error',msg:"邮件地址不符合标准",delay:1500,bg:true});
            return false
        }else{
            if (regex.test(text)) {
                return true
            } else {
                // 关闭loading
                $('.loading').remove();
                popup({type:'error',msg:"邮件地址不符合标准",delay:1500,bg:true});
                return false
            }
        }
    }
}

/* 验证电话格式 */
function Verifying_telephone(obj) {
    var telephone = $(obj).val();
    //手机的格式
    var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
    //如果手机号码的格式与正则的不符合，就提醒
    if (reg.test(telephone)) {
        return true;
    }else{
        // 关闭loading
        $('.loading').remove();
        popup({type:'error',msg:"手机号码不符合标准",delay:1500,bg:true});
        return false;
    }
}