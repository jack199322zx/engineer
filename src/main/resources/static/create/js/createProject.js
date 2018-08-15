/* 创建项目用js */
/* 查询天气事件 */
$("#get_Weather").click(function () {
    if($("#pro_address").val().length>1){
        if(Verification($("#pro_address").val())){
            /* 匹配输入的字符是否正确 */
            $.getJSON("/create/js/MrLiang_citys.json", function (data) {
                var type = false;
                $.each(data.provinces, function (i, result) {
                    /* 匹配省 */
                    if($("#pro_address").val() == result.provinceName){
                        type = true;
                    }else{
                        /* 匹配市 */
                        $.each(result.citys,function (i, result) {
                            if($("#pro_address").val() == result.citysName){
                                type = true;
                            }
                        })
                    }
                })
                if(type){
                    GetWeather($("#pro_address").val());
                }else{
                    popup({type:'error',msg:"城市不存在",delay:1500,bg:true});
                }
            });
        }
    }else{
        popup({type:'error',msg:"字符长度非法",delay:1500,bg:true});
    }
})


/* 获取天气 */
function GetWeather(address) {
    $("body").append('<div class="loading"><div class="mask"></div>' +
        '<div id="animationTipBox" style="width: 180px; height: 150px; margin-left: -90px; margin-top: -75px;"><div class="load"><div class="icon_box"><div class="cirBox1"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox2"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox3"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div></div></div><div class="dec_txt">加载中...</div></div></div>');
    /* 参数固定 */
    $.post("https://restapi.amap.com/v3/weather/weatherInfo",{
        key:"c3e6072e30500438987c8b5fb91f6137",
        city:address
    },function (result) {
        console.log(result)
        $(".loading").remove();
        if(result.status=="1" && result.info=="OK" && result.infocode=="10000"){
            $.each(result.lives, function (i, weatherInfo) {
                $("#pro_weather").val(weatherInfo.weather+' '+weatherInfo.temperature+'°C'+weatherInfo.winddirection+'风'+weatherInfo.windpower+'级');
            });
        }else{
            popup({type:'error',msg:"天气查询失败",delay:1500,bg:true});
        }
    });
}

/* 点击了提交 */
$(".submit").click(function () {
    /* 项目名,地址,天气,周围环境 */
    if(Verification($("#por_name").val())&&Verification($("#pro_address").val())&&Verification($("#pro_weather").val())&&Verification($("#pro_ambient").val())){
        $("body").append('<div class="loading"><div class="mask"></div>' +
            '<div id="animationTipBox" style="width: 180px; height: 150px; margin-left: -90px; margin-top: -75px;"><div class="load"><div class="icon_box"><div class="cirBox1"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox2"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox3"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div></div></div><div class="dec_txt">加载中...</div></div></div>');
        /* 调取创建projectAPI */
        $.post("/create/c_pro",{
            p_name:$("#por_name").val(),
            l_id:$("#pro_address").val(),
            p_weather:$("#pro_weather").val(),
            p_ambient:$("#pro_ambient").val()
        },function (result) {
            console.log(result);
            $(".loading").remove();
            $.each(result, function (i, resultInfo) {
                if(resultInfo.errMsg == "OK"){
                    popup({type:"success",msg:"创建成功",delay:1500,bg:true});
                }else{
                    popup({type:'error',msg:resultInfo.errMsg,delay:1500,bg:true});
                }
            });
        });
    }
})
/* 基本的验证字符串 */
function Verification(text) {
    if (text.match(/^\s+$/) || text.match(/^[ ]+$/)
        || text.match(/^[ ]/)
        || text.match(/^[ ]*$/)
        || text.match(/^\s*$/)) {
        popup({type:'error',msg:"输入的字符非法",delay:1500,bg:true});
        return false;
    }else {
        /* 验证是否是真实的城市数据 */
        return true;
    }
}
