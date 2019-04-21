package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.ItemDTO;

import java.util.List;

public interface ItemService {

    ItemDTO add(ItemDTO item);

    List<ItemDTO> getItems();

    int update(Long id, String status);
}
