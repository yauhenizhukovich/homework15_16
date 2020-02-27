package com.gmail.supersonicleader.app.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gmail.supersonicleader.app.repository.ShopRepository;
import com.gmail.supersonicleader.app.repository.model.Shop;
import org.springframework.stereotype.Repository;

@Repository
public class ShopRepositoryImpl extends GeneralRepositoryImpl<Shop> implements ShopRepository {

    @Override
    public Shop add(Connection connection, Shop shop) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO shop (name, location) VALUES (?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, shop.getName());
            statement.setString(2, shop.getLocation());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating shop failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    shop.setId(id);
                    return shop;
                } else {
                    throw new SQLException("Creating shop failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public List<Long> findAllIds(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM shop"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Long> ids = new ArrayList<>();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(1);
                    ids.add(id);
                }
                return ids;
            }
        }
    }

    @Override
    public void deleteById(Connection connection, Long itemId) {
        throw new UnsupportedOperationException("Deleting action for shop is not implemented.");
    }

}
