package main.controllers;

import main.jms.producers.TestProducer;
import main.services.SenderResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    @Qualifier("javaMailSenderImpl")
    private JavaMailSenderImpl javaMailSender;
    @Autowired
    private SenderResolver senderResolver;
    @Autowired
    private TestProducer testProducer;

    @GetMapping(value = "/check")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<String> check() {
        return Arrays.asList("A", "B", "C", "D");
    }

    @GetMapping(value = "/send/{position}")
    @CrossOrigin(origins = "http://localhost:4200")
    public boolean send(@PathVariable("position")String position) {
        try {
            senderResolver.resolveSender(javaMailSender, Integer.valueOf(position));
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("From");
            mimeMessageHelper.setTo("adaw36909@gmail.com");
            mimeMessageHelper.setSubject("Subject");
            mimeMessageHelper.setText("Text");
            javaMailSender.send(mimeMessage);
            LOGGER.info("suc sended");
            return true;
        } catch (MessagingException e) {
            LOGGER.error("Error");
            return false;
        }
    }

    @GetMapping("/sendInMQ")
    public void inMq() {
        LOGGER.info("SEND");
        testProducer.send();
    }
}