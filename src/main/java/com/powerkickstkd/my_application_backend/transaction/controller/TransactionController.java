package com.powerkickstkd.my_application_backend.transaction.controller;

import com.powerkickstkd.my_application_backend.transaction.dto.TransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.dto.UpdateTransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.form.TransactionFormHelper;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionFilterService;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionReadService;
import com.powerkickstkd.my_application_backend.transaction.service.TransactionWriteService;
import com.powerkickstkd.my_application_backend.transaction.validation.TransactionValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
// Base Mapping for url requests
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionReadService transactionReadService;
    private final TransactionWriteService transactionWriteService;
    private final TransactionFilterService transactionFilterService;
    private final TransactionValidator transactionValidator;
    private final TransactionFormHelper transactionFormHelper;

    @Autowired
    public TransactionController(TransactionReadService transactionReadService,
                                 TransactionWriteService transactionWriteService,
                                 TransactionFilterService transactionFilterService,
                                 TransactionValidator transactionValidator,
                                 TransactionFormHelper transactionFormHelper) {

        this.transactionReadService = transactionReadService;
        this.transactionWriteService = transactionWriteService;
        this.transactionFilterService = transactionFilterService;
        this.transactionValidator = transactionValidator;
        this.transactionFormHelper = transactionFormHelper;
    }

    // NOTE: Add the Model for our MVC (show the add transaction form)
    @GetMapping("/showTransactionFormAdd")
    public String showTransactionFormAdd(Model theModel) {

        // Create model attribute for our transaction request DTO
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO();

        // binding our data (Transaction (ENUMS)Method, (ENUMS)Month and transaction request DTO)
        this.transactionFormHelper.resetAndPopulateDropdowns(transactionRequestDTO, theModel);

        theModel.addAttribute("transactionRequestDTO", transactionRequestDTO);


        return "html/transaction-form";
    }

    // NOTE: save the transaction form data into our service layer
    @PostMapping("/saveTransactionDetails")
    public String saveTransactionDetails(@Valid @ModelAttribute("transactionRequestDTO") TransactionRequestDTO transactionRequestDTO,
                                         // Perform the validation, and get the results if there were any errors.
                                         BindingResult theBindingResult,
                                         Model theModel) {

        // Check if there's any errors in our input fields
        boolean hasValidationErrors = this.transactionValidator.hasValidationErrors(
                                                                    transactionRequestDTO,
                                                                    theBindingResult,
                                                                    theModel);

        if (hasValidationErrors) {
            // Reset all the data in our dropdown menu
            this.transactionFormHelper.resetAndPopulateDropdowns(transactionRequestDTO, theModel);
            // Errors will still be in the binding result
            return "html/transaction-form";
        }


        // save the transactionRequestDTO in our service layer
        this.transactionWriteService.createPaymentDetails(transactionRequestDTO);

        // NOTE: redirect to prevent duplicate submissions ("POST/ REDIRECT/ GET")
        // PRG PATTERN : Redirect to GET
        return "redirect:/transactions/confirmation";
    }

    // NOTE: Page that shows the success page or message
    @GetMapping("/confirmation")
    public String showConfirmationPage() {

        return "html/transaction-success";
    }

    // GET - endpoint that updates the transaction details (fill-up the form automatically)
    @GetMapping("/updateTransactionDetails")
    public String updateTransactions(@RequestParam("id") int id,
                                     Model theModel) {

        // Return the update transaction response dto
        theModel.addAttribute("updateTransaction", this.transactionReadService.findSinglePaymentDetailsId(id));

        return "html/transaction-update";
    }

    @PostMapping("/updatedTransactionDetails")
    public String saveUpdatedTransactions(@ModelAttribute("updateTransaction")UpdateTransactionRequestDTO updateTransactionRequestDTO) {

        // Perform the new and updated payment details
        this.transactionWriteService.saveUpdatedTransactionDetails(updateTransactionRequestDTO.getId(),
                                                              updateTransactionRequestDTO);

        return "redirect:/transactions/confirmation";
    }

    // Search transaction details section

    @GetMapping("/showTransactionDetails")
    public String showTransactionDetails(Model theModel) {

        // bind our transactionRequestDTO for our filter searches
        theModel.addAttribute("transactionRequestDTO", new TransactionRequestDTO());

        // bind the transactionResponseDTO list (sorted by the year then months)
        theModel.addAttribute("transactionResponseDTO", this.transactionReadService.findAllPaymentDetails());

        return "html/transaction-search";
    }

    // POST for our filter section (Show only the relevant searches)
    @PostMapping("/resultTransactionDetails")
    public String filterTransactions(@ModelAttribute("transactionRequestDTO") TransactionRequestDTO transactionRequestDTO,
                                     Model theModel) {

        // Result of the filter search using the payment month and method
        theModel.addAttribute("transactionResponseDTO",
                this.transactionFilterService.filterSearchPaymentDetails(
                        transactionRequestDTO.getPaymentMethod(),
                        transactionRequestDTO.getPaymentMonth()));

        // DTO will clear the searches
        theModel.addAttribute("transactionRequestDTO", new TransactionRequestDTO());

        return "html/transaction-search";
    }

    // NOTE: Delete transaction details
    @GetMapping("/deleteTransactionDetails")
    public String deleteTransactionDetails(@RequestParam("id") int id) {

        // delete the transaction details
        this.transactionWriteService.deletePaymentDetailsById(id);

        return "redirect:/homepage";
    }

}
