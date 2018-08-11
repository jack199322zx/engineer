package com.nino.engineer.utils.mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;


/**
 * @author TH
 */
public class SendMail {

    private Session session = null;
    private Transport transport = null;


    /***
     * 连接SMTP服务器   
     * @param userAuth
     * @param keyword
     * @throws MessagingException
     * 返回SMTPSession
     */
    public Session connectSMTP(UserAuthenticator userAuth, String keyword) throws MessagingException {
        //获取服务
        Properties props = MailUtil.getProps(userAuth.getMailAddr(), keyword);
        //创建服务环境
        if (userAuth.getMailAddr().split("@")[1].startsWith("163")) {
            session = Session.getDefaultInstance(props, userAuth);
        } else {
            session = Session.getInstance(props, userAuth);
        }
        //调试
        session.setDebug(true);

        //发送
        transport = session.getTransport();
        //连接SMTP服务器
        transport.connect(userAuth.getMailAddr(), userAuth.getMailPass());

        return session;
    }

    public void close() throws MessagingException {
        transport.close();
    }


    /**
     * 写邮件主体  返回邮件
     *
     * @param session
     * @param userAuth
     * @param mail
     * @param mailAddr
     * @param filePath
     * @return
     * @throws Exception
     */
    public Message createMessage(Session session, UserAuthenticator userAuth,
                                 Mail mail, MailAddr mailAddr, String filePath) throws Exception {

        Message msg = new MimeMessage(session);//创建邮件对象

        msg.setFrom(new InternetAddress(MimeUtility.encodeText(mail.getSender()) + "<" + userAuth.getMailAddr() + ">"));//可随便取的发件人名字
        msg.setSubject(mail.getSubject());//标题
        msg.setSentDate(new Date());//设置发送时间
        //收件人
        if (MailUtil.isNotNull(mailAddr.getReveiceAddr())) {
            msg.setRecipients(RecipientType.TO, InternetAddress.parse(mailAddr.getReveiceAddr()));
        }
        //抄送人
        if (MailUtil.isNotNull(mailAddr.getSereveicerAddr())) {
            msg.setRecipients(RecipientType.CC, InternetAddress.parse(mailAddr.getSereveicerAddr()));
        }
        //暗送人
        if (MailUtil.isNotNull(mailAddr.getAnreveicerAddr())) {
            msg.setRecipients(RecipientType.BCC, InternetAddress.parse(mailAddr.getAnreveicerAddr()));
        }

        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        Multipart multipart = new MimeMultipart();

        // 添加邮件正文
        if (MailUtil.isNotNull(mail.getContent())) {
            MimeBodyPart contentBodyPart = new MimeBodyPart();
            contentBodyPart.setContent(mail.getContent(), "text/html;charset=UTF-8");
            multipart.addBodyPart(contentBodyPart);
        }
        //添加附件
        if (MailUtil.isNotNull(filePath)) {
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            // 根据附件路径获取文件,
            FileDataSource dataSource = new FileDataSource(filePath);
            attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
            attachmentBodyPart.setFileName(MimeUtility.encodeWord(dataSource.getFile().getName()));
            multipart.addBodyPart(attachmentBodyPart);
        }

        // 邮件的内容
        msg.setContent(multipart);
        return msg;
    }


    /**
     * 综上  发邮件
     *
     * @param userAuth
     * @param mail
     * @param mailAddr
     * @param filePath
     * @return
     */
    public boolean sendMails(Session session, UserAuthenticator userAuth, Mail mail,
                             MailAddr mailAddr, String filePath) {

        try {
            //写邮件
            Message msg = createMessage(session, userAuth, mail, mailAddr, filePath);
            //发送
            transport.send(msg);

            if (transport != null) {
                transport.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
