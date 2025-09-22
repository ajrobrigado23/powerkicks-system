package com.powerkickstkd.my_application_backend.transaction.service;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionResponseDTO;

import java.util.List;

public interface TransactionFilterService {

    // NOTE: Use the filter search to search payment methods and months
    List<TransactionResponseDTO> filterSearchPaymentDetails(String method, String month);

}
