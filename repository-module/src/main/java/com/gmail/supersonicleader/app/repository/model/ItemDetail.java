package com.gmail.supersonicleader.app.repository.model;

import java.math.BigDecimal;

public class ItemDetail {

    private Long itemId;
    private BigDecimal price;

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
