package com.powerkickstkd.my_application_backend.transaction.service;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionResponseDTO;
import com.powerkickstkd.my_application_backend.transaction.dto.UpdateTransactionRequestDTO;

import java.util.List;

public interface TransactionReadService {

    // NOTE: Find all payment details (including the person and payment)
    List<TransactionResponseDTO> findAllPaymentDetails();

    // NOTE: Find payments list (DEVELOPER MODE)
    List<TransactionResponseDTO> findAllPayments();

    // NOTE: Get the total earnings for this month
    Integer getTotalEarningsThisMonth();

    // NOTE: Get the current month and year now
    TransactionResponseDTO getCurrentDate();

    // NOTE: Get the total number of transactions for the current month
    Integer getNumberOfTransactions();

    // NOTE: Find and get the single payment details
    UpdateTransactionRequestDTO findSinglePaymentDetailsId(int theId);

    // NOTE: Find if both first name and last name exist in our database
    boolean hasFullName(String firstName, String lastName);

}
