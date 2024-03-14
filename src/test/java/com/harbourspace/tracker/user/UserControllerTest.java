package com.harbourspace.tracker.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harbourspace.tracker.config.SecurityConfiguration;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.user.jdbc.UserJdbcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerTest {

    @MockBean
    private UserService userService; // we are mocking interface!

    @MockBean
    private AuthorizationService authorizationService;

    @Autowired
    private MockMvc mockMvc;

    private final String path = "/api/users";

    @BeforeEach
    void setUp() {
        Mockito.when(userService.getUsers()).thenReturn(UserFixtures.users);
        Mockito.when(userService.getUserById(1L)).thenReturn(UserFixtures.user1);
        Mockito.when(userService.createUser(UserFixtures.newUser)).thenReturn(UserFixtures.user3);
        Mockito.when(userService.updateUser(UserFixtures.user3Updated)).thenReturn(UserFixtures.user3Updated);
    }

    @Test
    @DisplayName("GET /api/users")
    void testGetUsers() throws Exception {
        var expected = "[{\"id\":0,\"name\":\"SYSTEM\"},{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]";
        mockMvc.perform(get(path).header("Authorization", "Basic 0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    @DisplayName("GET /api/users/1")
    void testGetUserById() throws Exception {
        var expected = "{\"id\":1,\"name\":\"John\"}";
        mockMvc.perform(get(path + "/1").header("Authorization", "Basic 0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    @DisplayName("POST /api/users")
    void testCreateUser() throws Exception {
        var expected = "{\"id\":3,\"name\":\"Jack\"}";

        var post = MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(UserFixtures.newUser))
                .header("Authorization", "Basic 0");

        mockMvc.perform(post)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    @DisplayName("PUT /api/users/3")
    void testUpdateUser() throws Exception {
        var expected = "{\"id\":3,\"name\":\"Jack UPDATED\"}";

        var put = MockMvcRequestBuilders.put(path + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(UserFixtures.user3Updated))
                .header("Authorization", "Basic 0");

        mockMvc.perform(put)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    @DisplayName("DELETE /api/users/3")
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete(path + "/3").header("Authorization", "Basic 0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
