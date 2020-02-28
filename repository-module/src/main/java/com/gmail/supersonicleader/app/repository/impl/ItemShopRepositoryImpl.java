package com.gmail.supersonicleader.app.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gmail.supersonicleader.app.repository.ItemShopRepository;
import com.gmail.supersonicleader.app.repository.model.ItemShopLink;
import org.springframework.stereotype.Repository;

@Repository
public class ItemShopRepositoryImpl extends GeneralRepositoryImpl<ItemShopLink> implements ItemShopRepository {

    @Override
    public ItemShopLink add(Connection connection, ItemShopLink itemShopLink) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO item_shop (item_id, shop_id) VALUES (?,?)"
        )) {
            statement.setLong(1, itemShopLink.getItemId());
            statement.setLong(2, itemShopLink.getShopId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item-shop link failed, no rows affected.");
            }
            return itemShopLink;
        }
    }

    @Override
    public void deleteById(Connection connection, Long itemId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM item_shop WHERE item_id = ?"
        )) {
            statement.setLong(1, itemId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting item-shop link failed, no rows affected.");
            }
        }
    }

    @Override
    public List<Long> findLinkedItems(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT DISTINCT item_id FROM item_shop"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Long> itemsId = new ArrayList<>();
                while (resultSet.next()) {
                    Long itemId = resultSet.getLong("item_id");
                    itemsId.add(itemId);
                }
                return itemsId;
            }
        }
    }

    @Override
    public List<Long> findAllIds(Connection connection) {
        throw new UnsupportedOperationException("Find all ids action for item-shop links is not implemented");
    }

}
