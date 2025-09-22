package com.powerkickstkd.my_application_backend.transaction.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    // One-to-One Relationship
    // CascadeTypeAll - apply to any operation (SAVE, DELETE OR UPDATE)
    @OneToOne(cascade = CascadeType.ALL)
    // Foreign key - reference to our table
    @JoinColumn(name = "person_details_id")
    private PersonDetails personDetails;

    // One-to-Many Relationship
    @OneToMany(mappedBy = "person",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<PaymentDetails> paymentDetails;

    public Person() {

    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getters / setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // getters / setters for person details

    public PersonDetails getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(PersonDetails personDetails) {
        this.personDetails = personDetails;
    }

    // getters / setters for payment details


    public List<PaymentDetails> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetails> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    // add convenience method
    public void addPaymentDetails(PaymentDetails tempPaymentDetails) {

        if (this.paymentDetails == null) {
            this.paymentDetails = new ArrayList<>();
        }

        this.paymentDetails.add(tempPaymentDetails);

        tempPaymentDetails.setPerson(this);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personDetails=" + personDetails +
                '}';
    }
}
