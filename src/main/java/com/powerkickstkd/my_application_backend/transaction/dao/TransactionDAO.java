package com.powerkickstkd.my_application_backend.transaction.dao;

import com.powerkickstkd.my_application_backend.transaction.entity.Payment;
import com.powerkickstkd.my_application_backend.transaction.entity.PaymentDetails;
import com.powerkickstkd.my_application_backend.transaction.entity.Person;

import java.util.List;
import java.util.Optional;

public interface TransactionDAO {

    // CRUD - (Create, Read, Update and Delete)

    // NOTE: CREATE SECTION

    // save the payment details
    void createPaymentDetails(PaymentDetails thePaymentDetails);

    // NOTE : READ SECTION

    // Find the payment details using the payment id
    PaymentDetails findSinglePaymentDetailsId(int theId);

    // Find all data in our payment details table
    List<PaymentDetails> findAllPaymentDetails();

    // find the payment using the payment method and month
    List<PaymentDetails> findPaymentMethodAndMonth(String method, String month);

    // find the payment using the payment method only
    List<PaymentDetails> findPaymentMethod(String method);

    // find payment using the payment month only
    List<PaymentDetails> findPaymentMonth(String month);

    // find person by id
    Person findPersonById(int theId);

    // find all payments
    List<Payment> findAllPayments();

    // find if the person exist by their first name and last name
    Optional<Person> findByFirstNameAndLastName(String firstName, String lastName);

    // NOTE: UPDATE SECTION

    void updatedPaymentDetails(PaymentDetails paymentDetails);

    // NOTE: DELETE SECTION

    void deletePaymentDetailsById(int theId);

    // NOTE: Perform calculation in the database

    // Calculate the total payment for a specific year and month
    Integer getTotalPaymentsForYearAndMonth(int year, String month);

    Integer getNumberOfTransaction(int year, String month);
}
