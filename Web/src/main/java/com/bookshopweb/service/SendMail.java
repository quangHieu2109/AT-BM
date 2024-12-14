package com.bookshopweb.service;

import com.bookshopweb.beans.Log;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Properties;
import java.sql.Timestamp;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

public class SendMail {
    static final String from = "fitstore567@gmail.com";
    static final String password = "cgkf kchb iaxe egwh";
//	static final String from = "timkodctk4@gmail.com";
//	static final String password = "lbsn hmhr eixz rdmi";


    static Session session;

    public static void main(String[] args) {
//        System.out.println(sendEmail("21130356@st.hcmuaf.edu.vn", "210903","HiuNe"));
//        System.out.println(new Timestamp(Calendar.getInstance().getTimeInMillis() + 300000).toString());
        sendLogForAdmin(new Log("42.114.218.15",4,"delete data","co data",null,new Timestamp(Calendar.getInstance().getTimeInMillis())));
    }

    public static void sendLogForAdmin(Log log) {
//        String title = "Log dangerous từ hệ thống";
//        UserDAO userDAO = new UserDAO();
//        String adminEmail = "";
//        List<User> admin = userDAO.getByRole("ADMIN");
//        for (User user : admin) {
//            adminEmail += user.getEmail()+",";
//        }
//
//        adminEmail = adminEmail.substring(0,adminEmail.length()-1);
////        System.out.println(adminEmail);
//        if(log.getCurValue()==null){
//            log.setCurValue("");
//        }
//        if (log.getPreValue()==null) {
//            log.setPreValue("");
//        }
//
//        File file = new File("Web/src/main/webapp/template/templateEmail.html");
//        try {
//            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
//            content = content.replace("{ID}", log.getId() + "");
//            content = content.replace("{IP}", IPUtils.getIPInfo(log.getIp()).getCountry());
//            content = content.replace("{level}",log.getLevelLog()+"");
//            content = content.replace("{resource}",log.getRes());
//            content = content.replace("{prevalue}", log.getPreValue());
//            content = content.replace("{curvalue}",log.getCurValue());
//            Properties props = new Properties();
//            props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
//            props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//
//            // create Authenticator
//            Authenticator auth = new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    // TODO Auto-generated method stub
//                    return new PasswordAuthentication(from, password);
//                }
//            };
//
//            // Phiên làm việc
//
//            if(session == null){
//                session = Session.getInstance(props, auth);
//            }
//
//
//            // Tạo một tin nhắn
//            MimeMessage msg = new MimeMessage(session);
//                // Kiểu nội dung
//                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//
//                // Người gửi
//                msg.setFrom(from);
//
//                // Người nhận
//                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail, false));
//
//                // Tiêu đề email
//                msg.setSubject(title);
//
//                // Quy đinh ngày gửi
//                msg.setSentDate(new Date(Calendar.getInstance().getTimeInMillis()));
//
//                // Quy định email nhận phản hồi
//                // msg.setReplyTo(InternetAddress.parse(from, false))
//
//                // Nội dung
//                msg.setContent(content, "text/HTML; charset=UTF-8");
//
//                // Gửi email
//                Transport.send(msg);
//                System.out.println("Gửi email thành công");
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    public static boolean sendEmailVerifyUser(String to, String verification, String userName) {
        String title = "Xác nhận tài khoản bookstore";
        String content = "Mã xác nhận của tài khoản " + userName + " là: <strong>" + verification +
                "</strong> mã có hiệu lực trong 5 phút, hết hạn lúc: " + new Timestamp(Calendar.getInstance().getTimeInMillis() + 300000).toString();
        // Properties : khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // create Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(from, password);
            }
        };
        // Phiên làm việc
        if (session == null) {
            session = Session.getInstance(props, auth);
        }
        // Tạo một tin nhắn
        MimeMessage msg = new MimeMessage(session);

        try {
            // Kiểu nội dung
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

            // Người gửi
            msg.setFrom(from);

            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            // Tiêu đề email
            msg.setSubject(title);

            // Quy đinh ngày gửi
            msg.setSentDate(new Date(Calendar.getInstance().getTimeInMillis()));
            // Nội dung
            msg.setContent(content, "text/HTML; charset=UTF-8");
            // Gửi email
            Transport.send(msg);
            System.out.println("Gửi email thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Gặp lỗi trong quá trình gửi email");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendEmailOtpAuth(String to, String otp) {
        String title = "Xác nhận cấp PrivateKey mới";
        try {
            title = MimeUtility.encodeText(title, "UTF-8", "B");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String content = "Mã xác nhận cấp PrivateKey của bạn là: <strong>" + otp +
                "</strong> mã có hiệu lực trong 5 phút, hết hạn lúc: " + new Timestamp(Calendar.getInstance().getTimeInMillis() + 300000).toString();
        // Properties : khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // create Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(from, password);
            }
        };
        // Phiên làm việc
        if (session == null) {
            session = Session.getInstance(props, auth);
        }
        // Tạo một tin nhắn
        MimeMessage msg = new MimeMessage(session);

        try {
            // Kiểu nội dung
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

            // Người gửi
            msg.setFrom(from);

            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            // Tiêu đề email
            msg.setSubject(title);

            // Quy đinh ngày gửi
            msg.setSentDate(new Date(Calendar.getInstance().getTimeInMillis()));
            // Nội dung
            msg.setContent(content, "text/HTML; charset=UTF-8");
            // Gửi email
            Transport.send(msg);
            System.out.println("Gửi email thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Gặp lỗi trong quá trình gửi email");
            e.printStackTrace();
            return false;
        }
    }
}
