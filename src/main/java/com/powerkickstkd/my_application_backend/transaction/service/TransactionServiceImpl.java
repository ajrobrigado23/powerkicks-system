package com.powerkickstkd.my_application_backend.transaction.service;

import com.powerkickstkd.my_application_backend.transaction.mapper.TransactionMapper;
import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMonth;
import com.powerkickstkd.my_application_backend.transaction.dao.TransactionDAO;
import com.powerkickstkd.my_application_backend.transaction.dto.TransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.dto.TransactionResponseDTO;
import com.powerkickstkd.my_application_backend.transaction.dto.UpdateTransactionRequestDTO;
import com.powerkickstkd.my_application_backend.transaction.entity.Payment;
import com.powerkickstkd.my_application_backend.transaction.entity.PaymentDetails;
import com.powerkickstkd.my_application_backend.transaction.entity.Person;
import com.powerkickstkd.my_application_backend.transaction.entity.PersonDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionReadService, TransactionWriteService, TransactionFilterService {

    // CONSTANTS (DATE)

    // Get the local date current month
    private static final LocalDate CURRENT_DATE = LocalDate.now();
    // Store the current month now
    private static final Month MONTH = CURRENT_DATE.getMonth();
    // Convert it into a string
    private static final String CURRENT_MONTH = MONTH.name();

    // CONSTANT (TIME)

    // Get the current time
    private static final LocalTime CURRENT_TIME = LocalTime.now();

    private final TransactionDAO transactionDAO;

    @Autowired
    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    @Transactional
    public void createPaymentDetails(TransactionRequestDTO theTransactionRequestDTO) {
        Person tempPerson = new Person(
                theTransactionRequestDTO.getFirstName(),
                theTransactionRequestDTO.getLastName()
        );

        PersonDetails tempPersonDetails = new PersonDetails(
                theTransactionRequestDTO.getEmailAddress()
        );

        // associate the object
        tempPerson.setPersonDetails(tempPersonDetails);

        Payment tempPayment = new Payment(
                theTransactionRequestDTO.getAmount(),
                theTransactionRequestDTO.getPaymentMethod(),
                theTransactionRequestDTO.getPaymentMonth()
        );

        // Get the both current time and current date
        PaymentDetails tempPaymentDetails = new PaymentDetails(
                CURRENT_TIME.truncatedTo(ChronoUnit.SECONDS),
                CURRENT_DATE
        );

        // associate the objects
        tempPerson.addPaymentDetails(tempPaymentDetails);
        tempPayment.addPaymentDetails(tempPaymentDetails);

        this.transactionDAO.createPaymentDetails(tempPaymentDetails);
    }

    // Find all Payments (ENTITY to DTO)
    @Override
    public List<TransactionResponseDTO> findAllPayments() {

        return this.transactionDAO.findAllPayments()
                .stream()
                // Use the Transaction Mapper class
                .map(payment -> TransactionMapper.convertPaymentEntityToDTO(payment))
                .collect(Collectors.toList());
    }

    // Find total earnings(amount) for a certain month (like AUGUST)
    @Override
    public Integer getTotalEarningsThisMonth() {

        return this.transactionDAO.getTotalPaymentsForYearAndMonth(CURRENT_DATE.getYear(), CURRENT_MONTH);
    }

    // Get the current year and month today
    @Override
    public TransactionResponseDTO getCurrentDate() {

        TransactionResponseDTO tempTransactionResponse = new TransactionResponseDTO();

        // Store the current month and year
        int year = CURRENT_DATE.getYear();

        // Set it into our transaction response dto
        tempTransactionResponse.setCurrentMonth(CURRENT_MONTH);
        tempTransactionResponse.setCurrentYear(year);

        return tempTransactionResponse;
    }

    // Get the total number of transactions
    @Override
    public Integer getNumberOfTransactions() {

        return this.transactionDAO.getNumberOfTransaction(CURRENT_DATE.getYear(), CURRENT_MONTH);
    }

    // Find all payment details (include the persons and payment) - using the JOIN FETCH
    @Override
    public List<TransactionResponseDTO> findAllPaymentDetails() {

        // Return the sorted transaction response dto (payment details) sorted
        return this.sortPaymentDetailsResults(this.transactionDAO.findAllPaymentDetails())
                .stream()
                .map(payment -> TransactionMapper.convertPaymentDetailsToDTO(payment))
                .collect(Collectors.toList());
    }

    // Sort the payment details results (year first then months)
    private List<PaymentDetails> sortPaymentDetailsResults(List<PaymentDetails> resultPaymentDetails) {

        // Sort(months and year) the payment details results list
        resultPaymentDetails.sort
                // Use the comparator to sort the year then months
                (Comparator
                        // Sort the year (get the payment details year it created)
                        .comparingInt((PaymentDetails paymentDetails) ->
                                // Return paymentDetails year (int)
                                paymentDetails.getCreateOn().getYear())
                        // Sort the months
                        .thenComparingInt(paymentDetails ->
                // Return the enum constant(valueOf) - then get payment month from the DAO
                TransactionMonth.valueOf(paymentDetails.getPayment().getPaymentMonth())
                        // Order the results from the enum
                        .getOrder()));

        // Return the sorted payment details list
        return resultPaymentDetails;
    }

    // NOTE: Filter search the data using the payment method or month
    public List<TransactionResponseDTO> filterSearchPaymentDetails(String method, String month) {

        // Look which filter search the user want to see
        List<PaymentDetails> results;

        // if it's payment method
        if (month == null && method != null) {

            results = this.transactionDAO.findPaymentMethod(method);

        // if it's payment month
        } else if (method == null && month != null) {

            results = this.transactionDAO.findPaymentMonth(month);

        // Both payment month and method aren't null
        } else if (month != null && method != null){

            results = this.transactionDAO.findPaymentMethodAndMonth(method, month);

        // Both are null just search all payment details
        } else {

            results = this.transactionDAO.findAllPaymentDetails();
        }

        // Sort the result of the payment details list
        List<PaymentDetails> sortedPaymentDetailsResults = sortPaymentDetailsResults(results);

        // if all false then the user wants to see both payment method and month
        return sortedPaymentDetailsResults
                .stream()
                .map(payment -> TransactionMapper.convertPaymentDetailsToDTO(payment))
                .collect(Collectors.toList());
    }

    // Find the single payment details using paymentDetails id
    @Override
    public UpdateTransactionRequestDTO findSinglePaymentDetailsId(int theId) {
        // Return the converted payment details to update transaction request dto
        return TransactionMapper.convertToUpdateTransactionDTO(this.transactionDAO.findSinglePaymentDetailsId(theId));
    }

    // Check if both first name and last name exist in our database
    public boolean hasFullName(String firstName, String lastName) {

        Optional<Person> person = this.transactionDAO.findByFirstNameAndLastName(firstName, lastName);

        // Return if the person exist already
        return person.isPresent();
    }

    // Save the updated payment details
    @Override
    @Transactional
    public void saveUpdatedTransactionDetails(int id, UpdateTransactionRequestDTO updateTransactionRequestDTO) {

        // Find the payment details using the id and return it
        PaymentDetails tempPaymentDetails = this.transactionDAO.findSinglePaymentDetailsId(id);

        // Set the payment amount to our new value
        tempPaymentDetails.getPayment().setAmount(updateTransactionRequestDTO.getAmount());

        // Save the updated payment details
        this.transactionDAO.updatedPaymentDetails(tempPaymentDetails);
    }

    // NOTE: Delete paymentDetails
    @Override
    @Transactional
    public void deletePaymentDetailsById(int theId) {
        this.transactionDAO.deletePaymentDetailsById(theId);
    }

}