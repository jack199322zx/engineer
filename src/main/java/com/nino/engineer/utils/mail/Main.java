package com.nino.engineer.utils.mail;

import com.nino.engineer.domain.Configure;
import com.nino.engineer.utils.log.LogClass;

import javax.mail.Session;

public class Main {

    public static String sendMailByQQ(Configure configure, String toMail, String subject, String content) {
        try {

            String sender = configure.getQQ_Email();//请填写您自己的QQ邮箱
            String mailPass = configure.getQQ_SMTP();//请填写正确的校验码
            String reveicer = toMail;

            Mail mail = new Mail();
            mail.setSender(sender);
            mail.setReveicer(reveicer);
            mail.setSubject(subject);
            mail.setContent(content);
            mail.setMtype(0);// 已发


            UserAuthenticator userAuth = new UserAuthenticator();
            userAuth.setMailAddr(sender);
            userAuth.setMailPass(mailPass);

            // 封装地址
            MailAddr mailAddr = new MailAddr();
            mailAddr.setReveiceAddr(reveicer);


            // 获取SMTPSession
            SendMail sendMail = new SendMail();

            Session session = sendMail.connectSMTP(userAuth, "SMTP");
            // 发邮件
            boolean bo = sendMail.sendMails(session, userAuth, mail, mailAddr,
                    null);
            sendMail.close();
            if(bo){
            	System.out.println("邮件发送成功:"+content);
            	return "Success";
            }else {
				return "error";
			}
        } catch (Exception e) {
        	/* 邮件发送失败 */
        	LogClass.logResult("邮件发送失败:\n邮件发送地址:"+toMail+"\n邮件标题:"+subject+"\n邮件内容:"+content+"\n错误:"+e);
        	return "error";
        }
    }
}
