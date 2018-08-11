package com.nino.engineer.utils.mail;

import java.util.Date;

public class Mail {
   
	/*邮箱表:usersemail
	字段	字段类型	字段意义
	Id	Int	邮箱ID
	Sender	Varchar	发件人
	recipients	Varchar	收件人
	Title	Varchar	邮件标题
	Content	Varchar	邮件内容
	Thetime	Varchar	发送时间
	Upload	Varchar	附件
	Type	Int	邮件类型
	(1.收件2.草稿.3.已发.4.删除)--》0.已发 1.草稿
	Status	int	油箱状态
	(0.未读 1.已读)*/
	
	/**邮件id*/
	private Integer mailId;
	
	/**发件人*/
	private String sender;
	
	/**收件人 群发*/
	private String reveicer;
	
	/**抄送人*/
	private String sereveicer;
	
	/**暗送人*/
	private String anreveicer;
	
	/**标题*/
	private String subject;
	
	/**正文*/
	private String content;
	
	/**时间*/
	private String time;
	
	/**附件*/
	private String attachment;
	
	/**发件时间*/
	private Date date;
	
	/**邮件类型*/
    private Integer mtype;  // 0.已发 1.草稿
    
    
    

	public Integer getMailId() {
		return mailId;
	}

	public void setMailId(Integer mailId) {
		this.mailId = mailId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReveicer() {
		return reveicer;
	}

	public void setReveicer(String reveicer) {
		this.reveicer = reveicer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Integer getMtype() {
		return mtype;
	}

	public void setMtype(Integer mtype) {
		this.mtype = mtype;
	}

	public String getSereveicer() {
		return sereveicer;
	}

	public void setSereveicer(String sereveicer) {
		this.sereveicer = sereveicer;
	}

	public String getAnreveicer() {
		return anreveicer;
	}

	public void setAnreveicer(String anreveicer) {
		this.anreveicer = anreveicer;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}	
	
}
