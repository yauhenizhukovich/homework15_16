package com.gmail.supersonicleader.app.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GeneralRepository<T> {

    T add(Connection connection, T t) throws SQLException;

    List<Long> findAllIds(Connection connection) throws SQLException;

    void deleteById(Connection connection, Long itemId) throws SQLException;

}
