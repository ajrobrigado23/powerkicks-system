package com.powerkickstkd.my_application_backend.transaction.validation;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Component
public class TransactionValidator {

    private final TransactionReadService transactionReadService;

    @Autowired
    public TransactionValidator(TransactionReadService transactionReadService) {
        this.transactionReadService = transactionReadService;
    }

    public boolean hasValidationErrors(TransactionRequestDTO transactionRequestDTO,
                                       BindingResult bindingResult,
                                       Model model) {

        boolean hasErrors = false;

        // Check if full name already exists (first name and last name)
        boolean personExist = transactionReadService.hasFullName(transactionRequestDTO.getFirstName(),
                transactionRequestDTO.getLastName());

        // If it exists then send an error message
        if (personExist) {
            model.addAttribute("personExist", "First name and Last name already exist!");
            hasErrors = true;
        }

        // Check if there's form binding errors
        if (bindingResult.hasErrors()) {
            hasErrors = true;
        }

        return hasErrors;

    }
}
