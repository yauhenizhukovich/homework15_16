package com.gmail.supersonicleader.app.service;

import com.gmail.supersonicleader.app.service.model.ItemDTO;

public interface ItemService {

    void addItem(ItemDTO itemDTO);

    void deleteItemsLinkedWithShops();

}
