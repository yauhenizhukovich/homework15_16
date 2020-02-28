package com.gmail.supersonicleader.app.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gmail.supersonicleader.app.repository.ItemRepository;
import com.gmail.supersonicleader.app.repository.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl extends GeneralRepositoryImpl<Item> implements ItemRepository {

    @Override
    public Item add(Connection connection, Item item) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO item (name, description) VALUES (?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    item.setId(id);
                    return item;
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
        }

    }

    @Override
    public List<Long> findAllIds(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM item"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Long> ids = new ArrayList<>();
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    ids.add(id);
                }
                return ids;
            }
        }
    }

    @Override
    public void deleteById(Connection connection, Long itemId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM item WHERE id = ?"
        )) {
            statement.setLong(1, itemId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting item failed, no rows affected.");
            }
        }
    }

}