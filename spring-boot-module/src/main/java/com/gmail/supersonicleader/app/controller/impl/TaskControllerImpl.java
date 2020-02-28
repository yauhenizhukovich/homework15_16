package com.gmail.supersonicleader.app.controller.impl;

import java.math.BigDecimal;

import com.gmail.supersonicleader.app.controller.TaskController;
import com.gmail.supersonicleader.app.service.ItemService;
import com.gmail.supersonicleader.app.service.ItemShopService;
import com.gmail.supersonicleader.app.service.ShopService;
import com.gmail.supersonicleader.app.service.model.ItemDTO;
import com.gmail.supersonicleader.app.service.model.ShopDTO;
import com.gmail.supersonicleader.app.service.util.RandomUtil;
import org.springframework.stereotype.Controller;

import static com.gmail.supersonicleader.app.controller.constant.ItemConstant.COUNT_OF_ITEMS;
import static com.gmail.supersonicleader.app.controller.constant.ItemConstant.MAX_ITEM_PRICE;
import static com.gmail.supersonicleader.app.controller.constant.ItemConstant.MIN_ITEM_PRICE;
import static com.gmail.supersonicleader.app.controller.constant.ShopConstant.AVERAGE_NUMBER_OF_ITEMS_IN_SHOP;
import static com.gmail.supersonicleader.app.controller.constant.ShopConstant.COUNT_OF_SHOPS;

@Controller
public class TaskControllerImpl implements TaskController {

    private final ItemService itemService;
    private final ShopService shopService;
    private final ItemShopService itemShopService;

    public TaskControllerImpl(ItemService itemService, ShopService shopService, ItemShopService itemShopService) {
        this.itemService = itemService;
        this.shopService = shopService;
        this.itemShopService = itemShopService;
    }

    @Override
    public void runTask() {
        addItems();
        addShops();
        itemShopService.linkShopsWithItems(AVERAGE_NUMBER_OF_ITEMS_IN_SHOP);
        itemService.deleteItemsLinkedWithShops();
    }

    private void addItems() {
        for (int i = 1; i <= COUNT_OF_ITEMS; i++) {
            ItemDTO itemDTO = getItemDTO(i);
            itemService.addItem(itemDTO);
        }
    }

    private void addShops() {
        for (int i = 1; i <= COUNT_OF_SHOPS; i++) {
            ShopDTO shopDTO = getShopDTO(i);
            shopService.addShop(shopDTO);
        }
    }

    private ItemDTO getItemDTO(int i) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("itemName" + i);
        itemDTO.setDescription("itemDescription" + i);
        int randomPrice = RandomUtil.getElement(MIN_ITEM_PRICE, MAX_ITEM_PRICE);
        BigDecimal price = BigDecimal.valueOf(randomPrice);
        itemDTO.setPrice(price);
        return itemDTO;
    }

    private ShopDTO getShopDTO(int i) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setName("shopName" + i);
        shopDTO.setLocation("shopLocation" + i);
        return shopDTO;
    }

}
