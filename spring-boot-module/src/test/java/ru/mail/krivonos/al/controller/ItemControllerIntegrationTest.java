package ru.mail.krivonos.al.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import ru.mail.krivonos.al.repository.model.ItemStatusEnum;
import ru.mail.krivonos.al.service.model.ItemDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@ActiveProfiles("test")
public class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @Mock
    private BindingResult bindingResult;

    @Autowired
    private ItemController itemController;

    @Before
    public void init() {
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                .useMockMvcForHosts("localhost:8080/")
                .build();
    }

    @Test
    public void requestForItemsIsSuccessfullyProcessedWithAvailableItemsList() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Name1");
        itemController.add(itemDTO, bindingResult, new ModelMap());
        this.mockMvc.perform(get("/items").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Name1")));
    }

    @Test
    public void requestForUpdateIsSuccessfullyProcessedForAvailableItem() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Name1");
        itemDTO.setStatus(ItemStatusEnum.READY);
        itemController.add(itemDTO, bindingResult, new ModelMap());
        itemDTO.setId(1L);
        itemDTO.setStatus(ItemStatusEnum.COMPLETED);
        itemController.add(itemDTO, bindingResult, new ModelMap());
        this.mockMvc.perform(get("/items").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("COMPLETED")));
    }
}
