package com.powerkickstkd.my_application_backend.transaction.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class TransactionResponseDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private Integer amount;

    private String paymentMethod;

    private String paymentMonth;

    private String currentMonth;

    private Integer currentYear;

    // NOTE: fix the bug where time doesn't update everytime I go back to the homepage
    private String currentTime;

    public TransactionResponseDTO() {

        // Always update the current time

        // Get the local time
        LocalTime time = LocalTime.now();

        // Define the desired format of our time
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

        // Convert it into string and set the current time
        this.currentTime = time.format(TIME_FORMATTER);

    }

    // getters and setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = capitalizeFirstLetter(paymentMethod);
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = capitalizeFirstLetter(paymentMonth);
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    // Change the letter case for our dashboard display
    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = capitalizeFirstLetter(currentMonth);
    }

    public Integer getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(Integer currentYear) {
        this.currentYear = currentYear;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    // Method that change the first letter case of our words to display to our dashboard
    private String capitalizeFirstLetter(String word) {

        // Function interface (Get String, return String)
        Function<String, String> capitalizeFirstLetter = s ->  s == null || s.isEmpty()
                ? s // return the invalid parameter
                : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase(); // return the valid parameter

        return capitalizeFirstLetter.apply(word);
    }
}
