package com.powerkickstkd.my_application_backend.transaction.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="payment_details")
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="created_at")
    private LocalTime createdAt;

    @Column(name="created_on")
    private LocalDate createOn;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    // "payment_id" - is the foreign key
    @JoinColumn(name="person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    // "payment_id" - is the foreign key
    @JoinColumn(name="payment_id")
    private Payment payment;

    public PaymentDetails() {

    }

    public PaymentDetails(LocalTime createdAt, LocalDate createOn) {
        this.createdAt = createdAt;
        this.createOn = createOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getCreateOn() {
        return createOn;
    }

    public void setCreateOn(LocalDate createOn) {
        this.createOn = createOn;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "createOn=" + createOn +
                ", createdAt=" + createdAt +
                ", id=" + id +
                '}';
    }
}
