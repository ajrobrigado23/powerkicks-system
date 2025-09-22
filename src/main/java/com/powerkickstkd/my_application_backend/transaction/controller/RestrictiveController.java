package com.powerkickstkd.my_application_backend.transaction.controller;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionFilterService;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionReadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestrictiveController {

    private final TransactionReadService transactionReadService;

    private final TransactionFilterService transactionFilterService;

    @Autowired
    public RestrictiveController(TransactionReadService transactionReadService,
                                 TransactionFilterService transactionFilterService) {

        this.transactionReadService = transactionReadService;
        this.transactionFilterService = transactionFilterService;

    }

    // DRY (Don't Repeat Yourself)

    // Use a helper method or @ModelAttribute to populate shared model data
    // This will run before every controller method in this controller.
    @ModelAttribute
    public void populateCommonModelData(Model model) {

        // TIPS -> Ensure getTotalEarningsThisMonth() and getTotalOfTransactions()
        // aren’t doing expensive DB operations if they’re not needed for every request.

        // bind the total earnings for the current month
        model.addAttribute("totalEarnings", this.transactionReadService.getTotalEarningsThisMonth());

        // bind the total of transaction for a particular month
        model.addAttribute("totalTransactions", this.transactionReadService.getNumberOfTransactions());
    }

    // Show the home page
    @GetMapping("/homepage")
    public String showHome(HttpSession session, Model theModel) {

        // Display the session ID
        String sessionId = session.getId();
        // Add it to our model so that it will display
        theModel.addAttribute("sessionId", sessionId);

        // bind our transactionRequestDTO for our filter searches
        theModel.addAttribute("transactionRequestDTO", new TransactionRequestDTO());

        // bind the transactionResponseDTO
        theModel.addAttribute("transactionResponseDTO", this.transactionReadService.findAllPaymentDetails());

        return "html/home";
    }

    // POST for our filter section (Show only the relevant searches)
    @PostMapping("/searchTransactionDetails")
    public String filterTransactions(@ModelAttribute("transactionRequestDTO") TransactionRequestDTO transactionRequestDTO,
                                          Model theModel) {

        // Result of the filter search using the payment month and method
        theModel.addAttribute("transactionResponseDTO",
                                          this.transactionFilterService.filterSearchPaymentDetails(
                                                                    transactionRequestDTO.getPaymentMethod(),
                                                                    transactionRequestDTO.getPaymentMonth()));

        // DTO will clear the searches
        theModel.addAttribute("transactionRequestDTO", new TransactionRequestDTO());

        return "html/home";
    }
}
