package com.powerkickstkd.my_application_backend.transaction.dao;

import com.powerkickstkd.my_application_backend.transaction.entity.Payment;
import com.powerkickstkd.my_application_backend.transaction.entity.PaymentDetails;
import com.powerkickstkd.my_application_backend.transaction.entity.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionDAOImpl implements TransactionDAO {

    private final EntityManager entityManager;

    // Inject entity manager using construction injection
    @Autowired
    public TransactionDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Person findPersonById(int theId){
        return this.entityManager.find(Person.class, theId);
    }

    @Override
    public void createPaymentDetails(PaymentDetails thePaymentDetails) {
        this.entityManager.persist(thePaymentDetails);
    }

    // NOTE: Find the single row Payment Details id (using the JOIN FETCH)
    @Override
    public PaymentDetails findSinglePaymentDetailsId(int theId) {

        // create query
        TypedQuery<PaymentDetails> query = this.entityManager.createQuery(
                "SELECT p FROM PaymentDetails p " +
                        "JOIN FETCH p.person " +
                        "JOIN FETCH p.payment " +
                        "WHERE p.id = :data ", PaymentDetails.class);

        // execute the query
        query.setParameter("data", theId);

        // Return the single result
        return query.getSingleResult();
    }

    // NOTE: Find all payments
    @Override
    public List<Payment> findAllPayments() {

        // create a query
        TypedQuery<Payment> theQuery = this.entityManager.createQuery(
                "SELECT p FROM Payment p " +
                        "JOIN FETCH p.paymentDetails ", Payment.class);

        // execute query and get result list
        List<Payment> payments = theQuery.getResultList();

        return payments;
    }

    // NOTE: Calculate the total payment with a certain year and month condition
    @Override
    public Integer getTotalPaymentsForYearAndMonth(int year, String month) {

        // create a query to sum payments for a specific year and month
        TypedQuery<Long> query = this.entityManager.createQuery(
                "SELECT SUM(p.amount) FROM Payment p " +
                        "JOIN p.paymentDetails pd " +
                        "WHERE YEAR(pd.createOn) =:year " +
                        "AND p.paymentMonth = :month ", Long.class);

        query.setParameter("year", year);
        query.setParameter("month", month);

        Long totalAmount = query.getSingleResult();

        // Recast to int and check if it's null
        return totalAmount != null ? totalAmount.intValue() : 0;
    }

    @Override
    public Integer getNumberOfTransaction(int year, String month) {

        // create a query to get the payments list for a specific year and month
        TypedQuery<Payment> query = this.entityManager.createQuery(
                "SELECT p FROM Payment p " +
                        "JOIN p.paymentDetails pd " +
                        "WHERE YEAR(pd.createOn) =:year " +
                        "AND p.paymentMonth = :month ", Payment.class);

        query.setParameter("year", year);
        query.setParameter("month", month);

        Integer numberOfTransactions = query.getResultList().size();

        return numberOfTransactions;
    }

    // NOTE: Find all the data in our payment details
    @Override
    public List<PaymentDetails> findAllPaymentDetails() {

        // create a query using the join fetch - retrieve person, payment and payment details
        TypedQuery<PaymentDetails> query = this.entityManager.createQuery(
                "SELECT p FROM PaymentDetails p " +
                        "JOIN FETCH p.person " +
                        "JOIN FETCH p.payment ", PaymentDetails.class);

        List<PaymentDetails> paymentDetails = query.getResultList();

        return paymentDetails;
    }

    // NOTE: Find both payment method and month
    @Override
    public List<PaymentDetails> findPaymentMethodAndMonth(String method, String month) {

        // create a query using the join fetch - retrieve person, payment and payment details
        TypedQuery<PaymentDetails> query = this.entityManager.createQuery(
                "SELECT p FROM PaymentDetails p " +
                        "JOIN FETCH p.person " +
                        "JOIN FETCH p.payment " +
                        "WHERE (p.payment.paymentMethod = :method AND p.payment.paymentMonth = :month)", PaymentDetails.class);

        // set the parameters for our query
        query.setParameter("method", method);
        query.setParameter("month", month);

        List<PaymentDetails> paymentDetails = query.getResultList();

        return paymentDetails;
    }

    // NOTE: Find the payment method
    @Override
    public List<PaymentDetails> findPaymentMethod(String method) {
        // create a query using the join fetch - retrieve person, payment and payment details
        TypedQuery<PaymentDetails> query = this.entityManager.createQuery(
                "SELECT p FROM PaymentDetails p " +
                        "JOIN FETCH p.person " +
                        "JOIN FETCH p.payment " +
                        "WHERE p.payment.paymentMethod = :method ", PaymentDetails.class);

        // set the parameters for our query
        query.setParameter("method", method);

        List<PaymentDetails> paymentDetails = query.getResultList();

        return paymentDetails;
    }


    // NOTE: Find the payment month
    @Override
    public List<PaymentDetails> findPaymentMonth(String month) {
        // create a query using the join fetch - retrieve person, payment and payment details
        TypedQuery<PaymentDetails> query = this.entityManager.createQuery(
                "SELECT p FROM PaymentDetails p " +
                        "JOIN FETCH p.person " +
                        "JOIN FETCH p.payment " +
                        "WHERE p.payment.paymentMonth = :month ", PaymentDetails.class);

        // set the parameters for our query
        query.setParameter("month", month);

        List<PaymentDetails> paymentDetails = query.getResultList();

        return paymentDetails;
    }

    // NOTE: Check if both first name and last name exist in our database
    @Override
    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {

        // Optional container object which may or may not contain a value.
        // It's a way to handle cases where a value might be missing or absent without using null, thus preventing NullPointerException
        TypedQuery<Person> query = this.entityManager.createQuery(
                      "SELECT p FROM Person p " +
                         "WHERE p.firstName = :firstName AND " +
                         "p.lastName = :lastName ", Person.class);

        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        try {

            Person tempPerson = query.setMaxResults(1).getSingleResult();

            // If the user is found, return it wrapped in an Optional
            return Optional.ofNullable(tempPerson);

        } catch(NoResultException e) {

            // If no user is found, return an empty Optional
            return Optional.empty();

        }

    }

    // NOTE: Update the payment details
    @Override
    public void updatedPaymentDetails(PaymentDetails paymentDetails) {
        this.entityManager.merge(paymentDetails);
    }


    // NOTE: Delete the paymentDetails
    @Override
    public void deletePaymentDetailsById(int theId) {

        PaymentDetails tempPaymentDetails = this.entityManager.find(PaymentDetails.class, theId);

        this.entityManager.remove(tempPaymentDetails);

    }

}
