package com.nino.engineer.utils.message;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.nino.engineer.domain.Configure;
import com.nino.engineer.utils.log.LogClass;
import org.json.JSONException;

import java.io.IOException;

/**
 * @program: InnovationEngineering
 * @description: 发送短信
 * @author: Dai Yuanchuan
 * @create: 2018-08-06 17:38
 **/
public class SendShortMessage {
//new com.ToolClass.ShortMessage.SendShortMessage().ShortMessage("17605256047","123456")

    public static boolean shortMessage (Configure configure, String telephone,String code){

        if (configure == null) return false;
        // 短信应用SDK AppID
        int appid = configure.getS_APPID(); // 1400开头

        // 短信应用SDK AppKey
        String appkey = configure.getS_APPKEY();

        // 需要发送短信的手机号码
        String[] phoneNumbers = {telephone};

        // 单发短信
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.send(0, "86", phoneNumbers[0],
                    code+"为您的登录验证码,请于1分钟内填写。如非本人操作,请忽略本短信", "", "");
            if(result != null){
                /* 短信发送成功 */
                return true;
            } else {
               /* 短信发送失败 */
                LogClass.logResult("短信发送失败:\nerrMsg:\n"+result);
                return false;
            }
        } catch (HTTPException e) {
            LogClass.logResult("短信发送失败:\nHTTP响应码错误\nerrMsg:\n"+e);
            // HTTP响应码错误
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            LogClass.logResult("短信发送失败:\njson解析错误\nerrMsg:\n"+e);
            // json解析错误
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            LogClass.logResult("短信发送失败:\n网络IO错误\nerrMsg:\n"+e);
            // 网络IO错误
            e.printStackTrace();
            return false;
        }
    }
}
