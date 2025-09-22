package com.powerkickstkd.my_application_backend.transaction.entity;

import jakarta.persistence.*;

@Entity
@Table(name="person_details")
public class PersonDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="email")
    private String email;

    // Bi-directional for our One-To-One relationship
    @OneToOne(mappedBy = "personDetails",
             cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Person person;

    public PersonDetails() {

    }

    public PersonDetails(String email) {
        this.email = email;
    }

    // getters / setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "PersonDetails{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
