package main.java.com.banking.application.dto;


public record EmailDetails(

        String recipient,

        String messageBody,

        String subject,

        String attachment

) {
}
