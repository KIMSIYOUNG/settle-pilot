package in.woowa.pilot.admin.acceptance;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiCall extends AcceptanceTest {

    protected <T> String 데이터생성요청(Runnable session, T content, String resource) throws Exception {
        session.run();

        return mockMvc.perform(post(resource)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(content))
        )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
    }

    protected <T> T 데이터요청(Runnable session, Class<T> responseType, String resource) throws Exception {
        session.run();

        return objectMapper.readValue(mockMvc.perform(get(resource)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray(), responseType);
    }

    protected <T> T 데이터요청(Runnable session, Class<T> responseType, String resource, MultiValueMap<String, String> queryParams) throws Exception {
        session.run();

        return objectMapper.readValue(mockMvc.perform(get(resource)
                .params(queryParams)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray(), responseType);
    }

    protected void 데이서요청및_응답코드확인(Runnable session, ResultMatcher status, String resource) throws Exception {
        session.run();

        mockMvc.perform(get(resource))
                .andExpect(status);
    }

    protected <T> String 데이터전체_수정요청(Runnable session, T content, String resource) throws Exception {
        session.run();
        return mockMvc.perform(put(resource)

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(content))
        )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
    }

    protected <T> String 데이터_수정_요청(Runnable session, T content, String resource) throws Exception {
        session.run();

        return mockMvc.perform(patch(resource)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(content))
        )
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getHeader("Location");
    }

    protected void 데이터_수정_요청(Runnable session, String resource) throws Exception {
        session.run();

        mockMvc.perform(patch(resource))
                .andExpect(status().isNoContent());
    }

    protected void 데이터삭제요청(Runnable session, String resource) throws Exception {
        session.run();

        mockMvc.perform(delete(resource))
                .andExpect(status().isNoContent());
    }
}
