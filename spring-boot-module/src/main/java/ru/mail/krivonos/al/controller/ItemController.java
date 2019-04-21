package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mail.krivonos.al.repository.model.ItemStatusEnum;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.model.ItemDTO;

import javax.validation.Valid;
import java.util.List;

@Controller("itemController")
public class ItemController {

    private static final String UNSUCCESSFUL_UPDATE_MESSAGE = "Item had the same status.";
    private static final String SUCCESSFUL_UPDATE_MESSAGE = "Item status successfully updated.";

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String getItems(ItemDTO itemDTO, Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("statuses", ItemStatusEnum.values());
        return "items";
    }

    @GetMapping("/items/updated={updatedRows}")
    public String getItems(
            @PathVariable int updatedRows,
            ItemDTO itemDTO, Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("statuses", ItemStatusEnum.values());
        String message;
        switch (updatedRows) {
            case 0:
                message = UNSUCCESSFUL_UPDATE_MESSAGE;
                break;
            case 1:
                message = SUCCESSFUL_UPDATE_MESSAGE;
                break;
            default:
                return "redirect:/error";
        }
        model.addAttribute("message", message);
        return "items";
    }

    @GetMapping("/items/add")
    public String add(ItemDTO itemDTO, Model model) {
        return "add";
    }

    @PostMapping("/items/add")
    public String add(
            @Valid @ModelAttribute("itemDTO") ItemDTO itemDTO, BindingResult result, ModelMap modelMap
    ) {
        if (result.hasErrors()) {
            return "add";
        }
        itemService.add(itemDTO);
        return "redirect:/items";
    }

    @PostMapping("/items/update")
    public String updateStatus(
            @ModelAttribute("itemDTO") ItemDTO itemDTO,
            Model model, BindingResult bindingResult
    ) {
        int updated = itemService.update(itemDTO.getId(), itemDTO.getStatus().name());
        switch (updated) {
            case 0:
            case 1:
                return "redirect:/items/updated=" + updated;
            default:
                return "redirect:/error";
        }
    }
}
