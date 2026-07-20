package main.java.com.banking.application.service;

import com.banking.banking_app_apis.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

    void sendEMailWithAttachment(EmailDetails emailDetails);
}
