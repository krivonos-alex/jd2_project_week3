package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.ItemRepository;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemInsertException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemSelectException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemUpdateException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemsSelectException;
import ru.mail.krivonos.al.repository.exceptions.ItemExtractionException;
import ru.mail.krivonos.al.repository.exceptions.ItemIDExtractionException;
import ru.mail.krivonos.al.repository.exceptions.ItemsExtractionException;
import ru.mail.krivonos.al.repository.model.Item;
import ru.mail.krivonos.al.repository.model.ItemStatusEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository("itemRepository")
public class ItemRepositoryImpl implements ItemRepository {

    private static final String QUERY_EXCEPTION_ERROR_MESSAGE = "Can't execute query: \"%s\".";
    private static final String ID_EXTRACTION_ERROR_MESSAGE = "Something goes wrong while extracting generated ID.";
    private static final String ITEMS_EXTRACTION_ERROR_MESSAGE = "Something goes wrong while extracting Items list.";
    private static final String ITEM_EXTRACTION_ERROR_MESSAGE = "Something goes wrong while extracting single Item.";

    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @Override
    public Item add(Connection connection, Item item) throws DatabaseItemInsertException, ItemIDExtractionException {
        String sql = "INSERT INTO Item (name, status) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getStatus().name());
            int added = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                return getItemWithId(resultSet, item);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseItemInsertException(String.format(QUERY_EXCEPTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public List<Item> findItems(Connection connection) throws DatabaseItemsSelectException, ItemsExtractionException {
        String sql = "SELECT id, name, status FROM Item WHERE deleted = FALSE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getItems(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseItemsSelectException(String.format(QUERY_EXCEPTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public Item findById(Connection connection, Long id) throws DatabaseItemSelectException, ItemExtractionException {
        String sql = "SELECT id, name, status FROM Item WHERE deleted = FALSE AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getItem(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseItemSelectException(String.format(QUERY_EXCEPTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public int update(Connection connection, Long id, String status) throws DatabaseItemUpdateException {
        String sql = "UPDATE Item SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseItemUpdateException(String.format(QUERY_EXCEPTION_ERROR_MESSAGE, sql), e);
        }
    }

    private Item getItemWithId(ResultSet resultSet, Item item) throws ItemIDExtractionException {
        try {
            if (resultSet.next()) {
                Item returningItem = new Item();
                returningItem.setName(item.getName());
                returningItem.setStatus(item.getStatus());
                returningItem.setId(resultSet.getLong(1));
                return returningItem;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ItemIDExtractionException(ID_EXTRACTION_ERROR_MESSAGE, e);
        }
        return null;
    }

    private List<Item> getItems(ResultSet resultSet) throws ItemsExtractionException {
        List<Item> items = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getLong("id"));
                item.setName(resultSet.getString("name"));
                item.setStatus(ItemStatusEnum.valueOf(resultSet.getString("status")));
                items.add(item);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ItemsExtractionException(ITEMS_EXTRACTION_ERROR_MESSAGE, e);
        }
        return items;
    }

    private Item getItem(ResultSet resultSet) throws ItemExtractionException {
        try {
            if (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getLong("id"));
                item.setName(resultSet.getString("name"));
                item.setStatus(ItemStatusEnum.valueOf(resultSet.getString("status")));
                return item;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ItemExtractionException(ITEM_EXTRACTION_ERROR_MESSAGE, e);
        }
        return null;
    }
}
