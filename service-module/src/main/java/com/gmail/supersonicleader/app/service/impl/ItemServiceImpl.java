package com.gmail.supersonicleader.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.ItemDetailsRepository;
import com.gmail.supersonicleader.app.repository.ItemRepository;
import com.gmail.supersonicleader.app.repository.ItemShopRepository;
import com.gmail.supersonicleader.app.repository.model.Item;
import com.gmail.supersonicleader.app.repository.model.ItemDetail;
import com.gmail.supersonicleader.app.service.ItemService;
import com.gmail.supersonicleader.app.service.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ItemRepository itemRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemShopRepository itemShopRepository;

    public ItemServiceImpl(
            ConnectionRepository connectionRepository,
            ItemRepository itemRepository,
            ItemDetailsRepository itemDetailsRepository,
            ItemShopRepository itemShopRepository) {
        this.connectionRepository = connectionRepository;
        this.itemRepository = itemRepository;
        this.itemDetailsRepository = itemDetailsRepository;
        this.itemShopRepository = itemShopRepository;
    }

    @Override
    public void addItem(ItemDTO itemDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = convertDTOToDatabaseObject(itemDTO);
                item = itemRepository.add(connection, item);
                ItemDetail itemDetail = getItemDetailWithItemId(item);
                itemDetailsRepository.add(connection, itemDetail);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteItemsLinkedWithShops() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Long> linkedItemsId = itemShopRepository.findLinkedItems(connection);
                for (Long itemId : linkedItemsId) {
                    deleteItem(connection, itemId);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Item convertDTOToDatabaseObject(ItemDTO itemDTO) {
        Item item = new Item();
        String name = itemDTO.getName();
        item.setName(name);
        String description = itemDTO.getDescription();
        item.setDescription(description);
        ItemDetail itemDetail = new ItemDetail();
        BigDecimal price = itemDTO.getPrice();
        itemDetail.setPrice(price);
        item.setItemDetail(itemDetail);
        return item;
    }

    private ItemDetail getItemDetailWithItemId(Item item) {
        ItemDetail itemDetail = item.getItemDetail();
        Long id = item.getId();
        itemDetail.setItemId(id);
        return itemDetail;
    }

    private void deleteItem(Connection connection, Long itemId) throws SQLException {
        itemDetailsRepository.deleteById(connection, itemId);
        itemShopRepository.deleteById(connection, itemId);
        itemRepository.deleteById(connection, itemId);
    }

}
