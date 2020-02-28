package com.gmail.supersonicleader.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.ShopRepository;
import com.gmail.supersonicleader.app.repository.model.Shop;
import com.gmail.supersonicleader.app.service.ShopService;
import com.gmail.supersonicleader.app.service.model.ShopDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ShopRepository shopRepository;

    public ShopServiceImpl(ConnectionRepository connectionRepository, ShopRepository shopRepository) {
        this.connectionRepository = connectionRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public void addShop(ShopDTO shopDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Shop shop = convertDTOToDatabaseObject(shopDTO);
                shopRepository.add(connection, shop);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Shop convertDTOToDatabaseObject(ShopDTO shopDTO) {
        Shop shop = new Shop();
        String name = shopDTO.getName();
        shop.setName(name);
        String location = shopDTO.getLocation();
        shop.setLocation(location);
        return shop;
    }

}
