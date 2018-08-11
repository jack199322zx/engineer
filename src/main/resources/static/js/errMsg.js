/* ajax加载出错 */
$(document).ajaxError(function(event,xhr,settings,errorType){
    $(".loading").remove();
    console.log('ajax执行'+settings.type+'请求'+settings.url+'时失败 Status Code:'+xhr.status);
    popup({type:'error',msg:"Ajax调用失败 code:"+xhr.status,delay:1500,bg:true});
});