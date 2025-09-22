package com.powerkickstkd.my_application_backend.transaction.mapper;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionResponseDTO;
import com.powerkickstkd.my_application_backend.transaction.dto.UpdateTransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.entity.Payment;
import com.powerkickstkd.my_application_backend.transaction.entity.PaymentDetails;

// Mapper class convert Entity objects to DTO
public class TransactionMapper {

    // Convert Payment entity to TransactionResponseDTO
    public static TransactionResponseDTO convertPaymentEntityToDTO(Payment payment) {

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

        transactionResponseDTO.setAmount(payment.getAmount());
        transactionResponseDTO.setPaymentMonth(payment.getPaymentMonth());
        transactionResponseDTO.setPaymentMethod(payment.getPaymentMethod());

        return transactionResponseDTO;
    }

    // Convert the PaymentDetails Entity to DTO
    public static TransactionResponseDTO convertPaymentDetailsToDTO(PaymentDetails paymentDetails) {

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

        // get the payment details id then convert it into transaction response dto id
        transactionResponseDTO.setId(paymentDetails.getId());

        // occupy the transaction response fields
        transactionResponseDTO.setFirstName(paymentDetails.getPerson().getFirstName());
        transactionResponseDTO.setLastName(paymentDetails.getPerson().getLastName());
        transactionResponseDTO.setAmount(paymentDetails.getPayment().getAmount());
        // Get the year payment details created
        transactionResponseDTO.setCurrentYear(paymentDetails.getCreateOn().getYear());
        transactionResponseDTO.setPaymentMethod(paymentDetails.getPayment().getPaymentMethod());
        transactionResponseDTO.setPaymentMonth(paymentDetails.getPayment().getPaymentMonth());

        // return the transaction responseDTO
        return transactionResponseDTO;
    }

    // Convert Payment Details entity to UpdateTransactionDTO
    public static UpdateTransactionRequestDTO convertToUpdateTransactionDTO(PaymentDetails paymentDetails) {

        UpdateTransactionRequestDTO updateTransactionRequestDTO = new UpdateTransactionRequestDTO();

        // occupy the transaction response fields
        updateTransactionRequestDTO.setId(paymentDetails.getId());
        updateTransactionRequestDTO.setFirstName(paymentDetails.getPerson().getFirstName());
        updateTransactionRequestDTO.setLastName(paymentDetails.getPerson().getLastName());
        updateTransactionRequestDTO.setEmailAddress(paymentDetails.getPerson().getPersonDetails().getEmail());
        updateTransactionRequestDTO.setAmount(paymentDetails.getPayment().getAmount());

        // return the transaction responseDTO
        return updateTransactionRequestDTO;
    }
}
