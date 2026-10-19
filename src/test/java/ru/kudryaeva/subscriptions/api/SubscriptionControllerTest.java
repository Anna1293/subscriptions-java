package ru.kudryaeva.subscriptions.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAndCalculateTotal() throws Exception {
        String body = """
                {
                  "service_name": "Yandex Plus",
                  "price": 400,
                  "user_id": "60601fee-2bf1-4721-ae6f-7636e79a0cba",
                  "start_date": "07-2025"
                }
                """;

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.service_name").value("Yandex Plus"));

        mockMvc.perform(get("/api/subscriptions/total")
                        .param("from", "07-2025")
                        .param("to", "12-2025")
                        .param("user_id", "60601fee-2bf1-4721-ae6f-7636e79a0cba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2400));
    }

    @Test
    void rejectsInvalidMonth() throws Exception {
        String body = """
                {
                  "service_name": "Yandex Plus",
                  "price": 400,
                  "user_id": "60601fee-2bf1-4721-ae6f-7636e79a0cba",
                  "start_date": "2025-07"
                }
                """;

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid date format, use MM-YYYY"));
    }
}
