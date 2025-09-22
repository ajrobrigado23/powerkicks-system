package com.powerkickstkd.my_application_backend.transaction.form;

import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMethod;
import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMonth;
import com.powerkickstkd.my_application_backend.transaction.dto.TransactionRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class TransactionFormHelper {

    public void resetAndPopulateDropdowns(TransactionRequestDTO dto, Model theModel) {

        // Reset all the dropdown menu (transaction month & method) to null
        dto.setPaymentMethod(null);
        dto.setPaymentMonth(null);

        // Repopulate the dropdown menu (binding our data (Transaction (ENUMS)Method, (ENUMS)Month and transaction request DTO))
        theModel.addAttribute("listOfPaymentMethod", TransactionMethod.values());
        theModel.addAttribute("listOfMonths", TransactionMonth.values());
    }
}
