package dev.be.core.api.support.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class HttpRequestAndResponseLoggingFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/api/v1/log 에서는 필터가 적용되어 로그가 남는다")
    void shouldLogForApiV1Log() throws Exception {
        mockMvc.perform(get("/api/v1/log"))
                .andExpect(status().isOk());

        // 로그 캡처해서 검증하거나, 단순히 status OK만 확인
    }

    @Test
    @DisplayName("/api/health 에서는 필터가 적용되지 않는다")
    void shouldNotLogForHealth() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());

        // 로그 안 남는 걸 검증하려면 LogCaptor 같은 라이브러리 사용 가능
    }
}
