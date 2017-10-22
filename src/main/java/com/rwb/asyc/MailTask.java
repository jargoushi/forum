package com.rwb.asyc;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.rwb.contanst.ForumException;
import com.rwb.util.MyConstant;

public class MailTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(MailTask.class);

	private String code;

	private String email;

	private JavaMailSender javaMailSender;

	private int operation;

	public MailTask(String code, String email, JavaMailSender javaMailSender,
			int operation) {
		super();
		this.code = code;
		this.email = email;
		this.javaMailSender = javaMailSender;
		this.operation = operation;
	}

	@Override
	public void run() {

		javaMailSender.send(new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				try {
					log.info("开始发邮件..");
					MimeMessageHelper messageHelper = new MimeMessageHelper(
							mimeMessage, true, "UTF-8");
					messageHelper.setFrom(MyConstant.MAIL_FROM);
					messageHelper.setTo(email);
					messageHelper.setSubject("一封激活邮件");
					StringBuffer sb = new StringBuffer();
					sb.append("<html><head></head><body>");
					
					if (operation == 1) {
						sb.append("<a href=" + MyConstant.DOMAIN_NAME
								+ "activate.do?code=");
						sb.append(code);
						sb.append(">点击激活</a></body>");
					} else {
						sb.append("是否将您的密码修改为:");
						sb.append(code.substring(0, 8));
						sb.append("，<a href=" + MyConstant.DOMAIN_NAME + "verify.do?code=" + code + ">");
						sb.append("点击是</a></body>");
					}
					
					messageHelper.setText(sb.toString(), true);
					
					log.info("邮件发送成功");
				} catch (Exception e) {
					log.error(ForumException.SEND_EMAIL_EXCEPTION, e);
					throw new Exception();
				}
			}

		});
	}

}
