package com.gmail.supersonicleader.app.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.supersonicleader.app.repository.model.ItemShopLink;

public interface ItemShopRepository extends GeneralRepository<ItemShopLink> {

    List<Long> findLinkedItems(Connection connection) throws SQLException;

}
