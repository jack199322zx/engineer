/* 首页js */
//get_pro("1");
/* 调取首页API */
function get_pro(page) {
    $(".content-wrapper").show();
    $(".Other_pages").html("");
    $.ajax({
        data : {
            "page":page
        },
        type : "POST",
        url : "/pro/pro_all",
        contentType : "application/x-www-form-urlencoded;charset=utf-8",
        scriptCharset : 'utf-8',
        success : function(result) {
            layui.use(['laypage', 'layer'], function () {
                var laypage = layui.laypage
                    , layer = layui.layer;
                laypage.render({
                    elem: 'Pagination_container'
                    , count: result[0].pro_size//数据总数result.number
                    , groups: 4 // 连续显示分页数
                    , curr: 1 // 设置起始页
                    , limit : 10
                    , first:'<i class="fa  fa-angle-double-left"></i>' // 首页  若不显示，设置false即可
                    , last: '<i class="fa fa-angle-double-right"></i>' // 尾页  若不显示，设置false即可
                    , prev: '<i class="fa fa-angle-left"></i>' // 上一页  若不显示，设置false即可
                    , next: '<i class="fa fa-angle-right"></i>'// 下一页  若不显示，设置false即可
                    , theme:'pagination'// theme: 'xxx' //将会生成 class="layui-laypage-xxx" 的CSS类
                    , jump: function (obj, first) {
                        if(!first) {
                            console.log("==> "+obj.curr)
                            /* 第一次不执行,一定要记住,这个必须有,要不然就是死循环 */
                            get_pro(obj.curr);
                        }
                    }
                });
            });
            /* 删除 loading */
            $(".overlay").remove();

            /* 清空原始数据 */
            $("#example2 tbody").html("");

            /* 遍历数据 */
            $.each(result,function (i,result) {
                console.log(result)
                /* 拿到所有的数据 */
                if(result.errMsg == "OK"){
                    /* 识别用户权限 */
                    if(result.user_code == 1){
                        /* 遍历返回数据 */
                        $.each(result.pro_info,function (i,pro_info) {
                            $.each(result.pro_status,function (i,pro_status) {
                                $.each(result.pro_det,function (i,pro_det) {
                                    $.each(result.pro_location,function (i,pro_location) {
                                        /* 匹配地址 */
                                        if(pro_info.l_id == pro_location.id){
                                            /* 匹配状态 */
                                            if(pro_status.p_id == pro_info.id){
                                                /* 匹配项目子管理 */
                                                if(pro_det.p_id == pro_info.id){
                                                    if(pro_det.j_id == 3){
                                                        $("#example2 tbody").append(
                                                            '<tr data-pro_id="'+pro_info.id+'"' +
                                                            'data-pro_name="'+pro_info.p_name+'">'+
                                                            '<td>'+pro_info.p_name+'</td>'+
                                                            '<td>'+pro_location.project_location+'</td>'+
                                                            '<td>'+getMyDate(pro_info.p_time.time)+'</td>'+
                                                            '<td>'+pro_status.p_s+'</td>'+
                                                            '<td>'+pro_det.username+'</td>'+
                                                            '<tr>'
                                                        );
                                                    } else {
                                                        $("#example2 tbody").append(
                                                            '<tr data-pro_id="'+pro_info.id+'"' +
                                                            'data-pro_name="'+pro_info.p_name+'">'+
                                                            '<td>'+pro_info.p_name+'</td>'+
                                                            '<td>'+pro_location.project_location+'</td>'+
                                                            '<td>'+getMyDate(pro_info.p_time.time)+'</td>'+
                                                            '<td>'+pro_status.p_s+'</td>'+
                                                            '<td>项目没有子管理</td>'+
                                                            '<tr>'
                                                        );
                                                    }
                                                }
                                            }
                                        }
                                    })
                                })
                            })
                        })

                        /* 为管理员 添加菜单栏功能 */
                    } else if(result.user_code == 2){
                        /* 普通 用户 */

                    } else {
                        popup({type:'error',msg:"用户状态码错误",delay:1500,bg:true});
                    }
                } else {
                    popup({type:'error',msg:result.errMsg,delay:1500,bg:true});
                }
            });
        },
        error : function(jqXHR, textStatus, errorThrown) {
            popup({type:'error',msg:"ajax调用失败",delay:1500,bg:true});
            /*弹出jqXHR对象的信息*/
            console.log(jqXHR.responseText);
            console.log(jqXHR.status);
            console.log(jqXHR.readyState);
            console.log(jqXHR.statusText);
            /*弹出其他两个参数的信息*/
            console.log(textStatus);
            console.log(errorThrown);
        }
    });
}

/* 毫秒转日期 */
function getMyDate(str){
    var oDate = new Date(str),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth()+1,
        oDay = oDate.getDate(),
        oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay);//最后拼接时间
    return oTime;
};
//补0操作
function getzf(num){
    if(parseInt(num) < 10){
        num = '0'+num;
    }
    return num;
}

/* 加载创建项目Page */
$(".create_pro").click(function () {
    loadPage("/create/c_page");
})

/* 加载设置项目子管理Page */
$(".Submanagement").click(function () {
    loadPage("/set/s_page");
})

/* 加载页面 */
function loadPage(url) {
    /* 加载 loading */
    $("body").append('<div class="loading"><div class="mask"></div>' +
        '<div id="animationTipBox" style="width: 180px; height: 150px; margin-left: -90px; margin-top: -75px;"><div class="load"><div class="icon_box"><div class="cirBox1"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox2"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox3"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div></div></div><div class="dec_txt">加载中...</div></div></div>');
    /* 获取需要 加载的 页面 */
    $.post(url,function (result) {
        /* 清除原始数据 */
        $(".Other_pages").html("");
        /* 去掉loading */
        $(".loading").remove();
        /* 隐藏主页 */
        $(".content-wrapper").hide();
        $.each(result,function (i,result) {
            console.log(result)
            if(result.code === '0'){
                /* 加载页面 */
                $(".Other_pages").load(result.data);
            } else {
                popup({type:'error',msg:result.message,delay:1500,bg:true});
            }
        })
    })
}