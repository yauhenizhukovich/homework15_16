package com.gmail.supersonicleader.app.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.gmail.supersonicleader.app.repository.ItemDetailsRepository;
import com.gmail.supersonicleader.app.repository.model.ItemDetail;
import org.springframework.stereotype.Repository;

@Repository
public class ItemDetailsRepositoryImpl extends GeneralRepositoryImpl<ItemDetail> implements ItemDetailsRepository {

    @Override
    public ItemDetail add(Connection connection, ItemDetail itemDetail) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO item_details (item_id, price) VALUES (?,?)"
        )) {
            statement.setLong(1, itemDetail.getItemId());
            statement.setBigDecimal(2, itemDetail.getPrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item details failed, no rows affected.");
            }
            return itemDetail;
        }
    }

    @Override
    public void deleteById(Connection connection, Long itemId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM item_details WHERE item_id = ?"
        )) {
            statement.setLong(1, itemId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting item details failed, no rows affected.");
            }
        }
    }

    @Override
    public List<Long> findAllIds(Connection connection) {
        throw new UnsupportedOperationException("Find all ids action for item details is not implemented.");
    }

}
