package com.example.pricebot.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Shop")
public class Shop {

    public Shop() {
    }

    public Shop(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Shop(String name, String code, Set<Request> requests) {
        this.name = name;
        this.code = code;
        this.requests = requests;
    }

    @Id
    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "requestId.shop")
    private Set<Request> requests;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }
}
