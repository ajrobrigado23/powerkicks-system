package com.powerkickstkd.my_application_backend.config;

import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMethod;
import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMonth;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    private final TransactionReadService transactionReadService;

    @Autowired
    public GlobalModelAttributeAdvice(TransactionReadService transactionReadService) {
        this.transactionReadService = transactionReadService;
    }

    @ModelAttribute
    public void populateCommonModelData(Model model) {

        // Time is always called fresh for every request, so it always gives the current system time.

        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // bind our data (transactionResponseDTO) to our DateAndTime
        model.addAttribute("dateAndTime", transactionReadService.getCurrentDate());

        // binding our data (Transaction (ENUMS)Method, (ENUMS)Month)
        model.addAttribute("listOfPaymentMethod", TransactionMethod.values());
        model.addAttribute("listOfMonths", TransactionMonth.values());
    }
}
