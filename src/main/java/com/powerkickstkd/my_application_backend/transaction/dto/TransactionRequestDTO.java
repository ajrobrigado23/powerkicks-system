package com.powerkickstkd.my_application_backend.transaction.dto;

import jakarta.validation.constraints.*;

// Create a Transaction Details DAO (expose only the fields we want)
// This is where we would put our Spring Validation (DTO)
public class TransactionRequestDTO {

    // Add validation rules

    @NotNull(message = "First name is required")
    private String firstName="";
    @NotNull(message = "Last name is required")
    private String lastName;

    // Regular Expressions - sequence of characters that define a search pattern (advanced topic)
    /*
        1. This pattern is used to find or match strings
     */
    @NotNull(message = "Email address is required")
    @Email(message = "Please provide a valid email address")
    private String emailAddress;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Payment month is required")
    private String paymentMonth;

    // Change it into a wrapper class (trim it to null)
    @NotNull(message = "Payment amount is required")
    // Validate a numeric range use (@Min and @Max)
    @Min(value=100, message = "Payment amount must be greater than or equal to 100")
    @Max(value=5000, message = "Payment amount must be less than or equal to 5000")
    private Integer amount;

    // constructor

    public TransactionRequestDTO() {

    }

    public TransactionRequestDTO(String firstName, String lastName, String emailAddress,
                                 String paymentMethod, String paymentMonth, Integer amount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.paymentMethod = paymentMethod;
        this.paymentMonth = paymentMonth;
        this.amount = amount;
    }

    // getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentMonth='" + paymentMonth + '\'' +
                ", amount=" + amount +
                '}';
    }
}
