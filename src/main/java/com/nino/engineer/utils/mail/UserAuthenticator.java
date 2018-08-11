package com.nino.engineer.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class UserAuthenticator extends Authenticator {
       
	/**邮件地址*/
	private String mailAddr;
	
	/**邮件密码*/
	private String mailPass;
	
	
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(mailAddr, mailPass);
	}
	

	public String getMailAddr() {
		return mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	public String getMailPass() {
		return mailPass;
	}

	public void setMailPass(String mailPass) {
		this.mailPass = mailPass;
	}
	
	
	
}
