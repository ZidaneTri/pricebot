package com.example.pricebot.entity;


import com.example.pricebot.entity.key.RequestId;

import javax.persistence.*;

@Entity
@Table(name = "Request")
public class Request {

    @EmbeddedId
    private RequestId requestId;

    public Request() {
    }

    public Request(RequestId requestId) {
        this.requestId = requestId;
    }

    public RequestId getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestId requestId) {
        this.requestId = requestId;
    }

    @Transient
    public Client getClient() {
        return getRequestId().getClient();
    }

    @Transient
    public Product getProduct() {
        return getRequestId().getProduct();
    }

    @Transient
    public Shop getShop() {
        return getRequestId().getShop();
    }

    @Transient
    public void setClient(Client client) {
        this.requestId.setClient(client);
    }

    @Transient
    public void setProduct(Product product) {
        this.requestId.setProduct(product);
    }

    @Transient
    public void setShop(Shop shop) {
        this.requestId.setShop(shop);
    }




}
