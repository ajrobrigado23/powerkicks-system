package com.powerkickstkd.my_application_backend.transaction.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="amount")
    private int amount;

    @Column(name="payment_method")
    private String paymentMethod;

    @Column(name="payment_month")
    private String paymentMonth;

    // "payment" - relate to the field in the paymentDetails class
    @OneToMany(mappedBy = "payment",
               fetch = FetchType.LAZY,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<PaymentDetails> paymentDetails;

    public Payment() {

    }

    public Payment(int amount, String paymentMethod, String paymentMonth) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentMonth = paymentMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    // paymentDetails - getters / setters

    public List<PaymentDetails> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetails> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    // add convenience method for bi-directional relationship
    public void addPaymentDetails(PaymentDetails tempPaymentDetails) {

        if (this.paymentDetails == null) {
            this.paymentDetails = new ArrayList<>();
        }

        this.paymentDetails.add(tempPaymentDetails);

        // setup the bi-directional relationship
        tempPaymentDetails.setPayment(this);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentMonth='" + paymentMonth + '\'' +
                '}';
    }
}
