package com.example.pricebot.entity;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Client")
public class Client {

    public Client(){}

    public Client(Long chatId, String firstName, String lastName, String username) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    @Id
    @Column(name = "CHAT_ID", unique = true)
    private Long chatId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "ADD_STATE")
    @ColumnDefault("0")
    private int addState;

    @Column(name = "CURRENT_REQUEST")
    private String currentProduct;

    @OneToMany(mappedBy = "requestId.product")
    private Set<Request> requests;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAddState() {
        return addState;
    }

    public void setAddState(int addState) {
        this.addState = addState;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public String getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(String currentProduct) {
        this.currentProduct = currentProduct;
    }


}
