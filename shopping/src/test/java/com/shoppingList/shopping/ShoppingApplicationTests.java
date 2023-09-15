package com.shoppingList.shopping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingList.entities.ShoppingItem;
import com.shoppingList.repositories.ShoppingItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

	@BeforeEach
    void setUp() {
        shoppingItemRepository.deleteAll();
    }

    @Test
    void testAddItem() throws Exception {
        ShoppingItem item = new ShoppingItem();
        item.setName("Item 1");
        item.setQuantity(5);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/shopping-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllItems() throws Exception {
        shoppingItemRepository.save(new ShoppingItem());
        shoppingItemRepository.save(new ShoppingItem());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/shopping-items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Item 1"))
                .andExpect(jsonPath("$[0].quantity").value(3))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value("Item 2"))
                .andExpect(jsonPath("$[1].quantity").value(2));
    }
    
    @Test
    public void testUpdateShoppingItem() throws Exception {
        ShoppingItem item = new ShoppingItem();
        item.setName("Oranges");
        item.setQuantity(8);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/shopping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));

        item.setQuantity(15);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/shopping/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    public void testDeleteShoppingItem() throws Exception {
        ShoppingItem item = new ShoppingItem();
        item.setName("Milk");
        item.setQuantity(2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/shopping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/shopping/1"))
                .andExpect(status().isOk());
    }
}
