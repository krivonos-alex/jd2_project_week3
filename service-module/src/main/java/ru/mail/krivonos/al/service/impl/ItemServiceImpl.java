package ru.mail.krivonos.al.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.repository.ItemRepository;
import ru.mail.krivonos.al.repository.connection.ConnectionHandler;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemInsertException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemSelectException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemUpdateException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemsSelectException;
import ru.mail.krivonos.al.repository.exceptions.ItemExtractionException;
import ru.mail.krivonos.al.repository.exceptions.ItemIDExtractionException;
import ru.mail.krivonos.al.repository.exceptions.ItemsExtractionException;
import ru.mail.krivonos.al.repository.model.Item;
import ru.mail.krivonos.al.repository.model.ItemStatusEnum;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.converter.ItemConverter;
import ru.mail.krivonos.al.service.exceptions.ConnectionAutoCloseException;
import ru.mail.krivonos.al.service.exceptions.ItemAddException;
import ru.mail.krivonos.al.service.exceptions.ItemsGetException;
import ru.mail.krivonos.al.service.exceptions.ItemsUpdateException;
import ru.mail.krivonos.al.service.model.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("itemService")
public class ItemServiceImpl implements ItemService {

    private static final String ITEM_ADDING_DEFAULT_STATUS = "READY";
    private static final String ITEM_ADDING_ERROR_MESSAGE = "Error while adding Item.";
    private static final String ITEMS_GETTING_ERROR_MESSAGE = "Error while getting Items list.";
    private static final String ITEM_UPDATING_ERROR_MESSAGE = "Error while updating Item status.";
    private static final String CONNECTION_CLOSE_ERROR_MESSAGE = "Error while closing connection.";

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private ItemRepository itemRepository;
    private ItemConverter itemConverter;
    private ConnectionHandler connectionHandler;

    @Autowired
    public ItemServiceImpl(
            ItemRepository itemRepository,
            ItemConverter itemConverter,
            ConnectionHandler connectionHandler
    ) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Item item = itemConverter.fromDTO(itemDTO);
                item.setStatus(ItemStatusEnum.valueOf(ITEM_ADDING_DEFAULT_STATUS));
                Item addedItem = itemRepository.add(connection, item);
                connection.commit();
                return itemConverter.toDTO(addedItem);
            } catch (SQLException | DatabaseItemInsertException | ItemIDExtractionException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ItemAddException(ITEM_ADDING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public List<ItemDTO> getItems() {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Item> items = itemRepository.findItems(connection);
                List<ItemDTO> itemDTOs = getItemDTOs(items);
                connection.commit();
                return itemDTOs;
            } catch (SQLException | ItemsExtractionException | DatabaseItemsSelectException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ItemsGetException(ITEMS_GETTING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int update(Long id, String status) {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Item item = itemRepository.findById(connection, id);
                if (item == null) {
                    connection.commit();
                    return -1;
                }
                if (item.getStatus().name().equals(status)) {
                    connection.commit();
                    return 0;
                }
                int updatedLines = itemRepository.update(connection, id, status);
                connection.commit();
                return updatedLines;
            } catch (SQLException | DatabaseItemSelectException | ItemExtractionException |
                    DatabaseItemUpdateException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ItemsUpdateException(ITEM_UPDATING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    private List<ItemDTO> getItemDTOs(List<Item> items) {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        for (Item item : items) {
            itemDTOs.add(itemConverter.toDTO(item));
        }
        return itemDTOs;
    }
}
