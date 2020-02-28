package com.gmail.supersonicleader.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.ItemRepository;
import com.gmail.supersonicleader.app.repository.ItemShopRepository;
import com.gmail.supersonicleader.app.repository.ShopRepository;
import com.gmail.supersonicleader.app.repository.model.ItemShopLink;
import com.gmail.supersonicleader.app.service.ItemShopService;
import com.gmail.supersonicleader.app.service.util.RandomUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ItemShopServiceImpl implements ItemShopService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ShopRepository shopRepository;
    private final ItemRepository itemRepository;
    private final ItemShopRepository itemShopRepository;

    public ItemShopServiceImpl(
            ConnectionRepository connectionRepository,
            ShopRepository shopRepository,
            ItemRepository itemRepository,
            ItemShopRepository itemShopRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.shopRepository = shopRepository;
        this.itemRepository = itemRepository;
        this.itemShopRepository = itemShopRepository;
    }

    @Override
    public void linkShopsWithItems(int averageNumberOfItemsInShop) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Long> shopIds = shopRepository.findAllIds(connection);
                List<Long> itemIds = itemRepository.findAllIds(connection);
                shopIds.stream().
                        peek(shopId -> Collections.shuffle(itemIds)).
                        forEach(shopId ->
                                itemIds.stream().
                                        limit(getItemsPerShop(averageNumberOfItemsInShop)).
                                        forEach(itemId ->
                                                addItemShopLink(connection, shopId, itemId)));
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void addItemShopLink(Connection connection, Long shopId, Long itemId) {
        try {
            itemShopRepository.add(connection, getItemShopLink(shopId, itemId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getItemsPerShop(int averageNumberOfItemsInShop) {
        if (averageNumberOfItemsInShop == 0) {
            throw new IllegalArgumentException("Shop should have at least one item.");
        }
        int minItems = averageNumberOfItemsInShop - 1;
        int maxItems = averageNumberOfItemsInShop + 1;
        return RandomUtil.getElement(minItems, maxItems);
    }

    private ItemShopLink getItemShopLink(Long shopId, Long selectedItemId) {
        ItemShopLink itemShopLink = new ItemShopLink();
        itemShopLink.setShopId(shopId);
        itemShopLink.setItemId(selectedItemId);
        return itemShopLink;
    }

}
