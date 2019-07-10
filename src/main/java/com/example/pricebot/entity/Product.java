package com.example.pricebot.entity;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Product")
public class Product {

    public Product() {
    }

    public Product(String name, String code, String link) {
        this.name = name;
        this.code = code;
        this.link = link;
    }

    public Product(String name, String code, String link, Set<Request> requests) {
        this.name = name;
        this.code = code;
        this.link = link;
        this.requests = requests;
    }

    @Id
    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;


    @Column(name = "link")
    private String link;

    @OneToMany(mappedBy = "requestId.client")
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }
}
