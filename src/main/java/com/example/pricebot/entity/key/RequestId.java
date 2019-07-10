package com.example.pricebot.entity.key;

import com.example.pricebot.entity.Client;
import com.example.pricebot.entity.Product;
import com.example.pricebot.entity.Shop;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequestId implements Serializable {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public RequestId() {
    }

    public RequestId(Client client, Product product, Shop shop) {
        this.client = client;
        this.product = product;
        this.shop = shop;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestId)) return false;
        RequestId that = (RequestId) o;
        return Objects.equals(getClient(), that.getClient()) &&
                Objects.equals(getProduct(), that.getProduct()) &&
                Objects.equals(getShop(), that.getShop());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClient(),getProduct(),getShop());
    }
}