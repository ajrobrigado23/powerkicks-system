package com.powerkickstkd.my_application_backend.transaction.service;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.dto.UpdateTransactionRequestDTO;

public interface TransactionWriteService {

    // Use the Transaction DTO (Controller -> DTO -> Service)
    void createPaymentDetails(TransactionRequestDTO theTransactionRequestDTO);

    // NOTE: Save the updated transactions details
    void saveUpdatedTransactionDetails(int id, UpdateTransactionRequestDTO updateTransactionRequestDTO);

    // NOTE: DEVELOPER MODE
    void deletePaymentDetailsById(int theId);
}
