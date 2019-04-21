package ru.mail.krivonos.al.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import ru.mail.krivonos.al.repository.model.ItemStatusEnum;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.model.ItemDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    private ItemController itemController;

    private MockMvc mockMvc;

    private List<ItemDTO> items = Arrays.asList(new ItemDTO("Name1", ItemStatusEnum.READY),
            new ItemDTO("Name2", ItemStatusEnum.COMPLETED));

    @Mock
    private BindingResult bindingResult;

    @Before
    public void init() {
        itemController = new ItemController(itemService);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        Mockito.when(itemService.getItems()).thenReturn(items);
    }

    @Test
    public void shouldReturnItemsPageForItemsGetRequest() {
        Model model = new ExtendedModelMap();
        String items = itemController.getItems(new ItemDTO(), model);
        Assert.assertEquals("items", items);
        Map<String, Object> stringObjectMap = model.asMap();
        Assert.assertEquals(this.items, stringObjectMap.get("items"));
    }

    @Test
    public void shouldReturnAddPageForAddGetRequest() {
        Model model = new ExtendedModelMap();
        String add = itemController.add(new ItemDTO(), model);
        Assert.assertEquals("add", add);
    }

    @Test
    public void shouldReturnAddPageForInvalidItemDTONameForAddPostRequest() {
        Model model = new ExtendedModelMap();
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        String add = itemController.add(new ItemDTO(), bindingResult, new ModelMap());
        Assert.assertEquals("add", add);
    }

    @Test
    public void shouldRedirectToItemsPageAfterSuccessfulProceedOfAddPostRequest() {
        String add = itemController.add(new ItemDTO(), bindingResult, new ModelMap());
        Assert.assertEquals("redirect:/items", add);
    }

    @Test
    public void shouldRedirectToItemsPageAfterSuccessfulProceedOfUpdatePostRequest() {
        Model model = new ExtendedModelMap();
        Mockito.when(itemService.update(1L, "READY")).thenReturn(1);
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setStatus(ItemStatusEnum.READY);
        String add = itemController.updateStatus(itemDTO, model, bindingResult);
        Assert.assertEquals("redirect:/items/updated=1", add);
    }

    @Test
    public void shouldRedirectToErrorPageAfterUnsuccessfulProceedOfUpdatePostRequest() {
        Model model = new ExtendedModelMap();
        Mockito.when(itemService.update(1L, "READY")).thenReturn(-1);
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setStatus(ItemStatusEnum.READY);
        String add = itemController.updateStatus(itemDTO, model, bindingResult);
        Assert.assertEquals("redirect:/error", add);
    }

    @Test
    public void requestForItemsIsSuccessfullyProcessedWithAvailableItemsList() throws Exception {
        this.mockMvc.perform(get("/items.html"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("items", equalTo(items)))
                .andExpect(forwardedUrl("items"));
    }
}
