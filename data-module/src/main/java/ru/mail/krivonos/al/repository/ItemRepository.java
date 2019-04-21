package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.exceptions.DatabaseItemInsertException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemSelectException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemUpdateException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseItemsSelectException;
import ru.mail.krivonos.al.repository.exceptions.ItemExtractionException;
import ru.mail.krivonos.al.repository.exceptions.ItemIDExtractionException;
import ru.mail.krivonos.al.repository.exceptions.ItemsExtractionException;
import ru.mail.krivonos.al.repository.model.Item;

import java.sql.Connection;
import java.util.List;

public interface ItemRepository {

    Item add(Connection connection, Item item) throws DatabaseItemInsertException, ItemIDExtractionException;

    List<Item> findItems(Connection connection) throws DatabaseItemsSelectException, ItemsExtractionException;

    Item findById(Connection connection, Long id) throws DatabaseItemSelectException, ItemExtractionException;

    int update(Connection connection, Long id, String status) throws DatabaseItemUpdateException;


}
