package com.example.demo.user.controller;

import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.service.UserService;
import com.example.demo.exceptions.models.ExceptionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<UserDTO> userDTOList;

    @BeforeEach
    void setUp() {

        userDTOList = new ArrayList<>();

        for(int i = 1; i <= 10; i++) {
            UserDTO userDTO = new UserDTO(
                    Long.valueOf(Integer.valueOf(i).toString()),
                    "User " + i,
                    "123456789" + i,
                    LocalDate.now()
            );
            userDTOList.add(userDTO);
        }
    }

    @Test
    void getUsers() throws Exception {

        // Mock the service
        when(userService.getUsers()).thenReturn(userDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userDTOList.size())));
    }


    @Test
    void getUserById() throws Exception {

        // Mock the service
        when(userService.getUserById(any(Long.class))).thenReturn(userDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDTOList.get(0).getName())))
                .andExpect(jsonPath("$.phoneNumber", is(userDTOList.get(0).getPhoneNumber())))
                .andExpect(jsonPath("$.registrationDate", is(userDTOList.get(0).getRegistrationDate().toString())));

    }

    @Test
    void getUserByIdNotFoundException() throws Exception {

        // Mock the service
        when(userService.getUserById(any(Long.class))).thenThrow(new UserException(new ExceptionEntity(501, "User not found with id: 1")));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(501)))
                .andExpect(jsonPath("$.message", is("User not found with id: 1")));
    }

    @Test
    void createUser() throws Exception {
        UserDTO newUserDTO = new UserDTO(
                11L,
                "New User",
                "1234567890",
                LocalDate.now()
        );

        // Mock the service
        when(userService.createUser(any(UserDTO.class))).thenReturn(newUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\"id\":11,\"name\":\"New User\",\"phoneNumber\":\"1234567890\",\"registrationDate\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newUserDTO.getName())))
                .andExpect(jsonPath("$.phoneNumber", is(newUserDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.registrationDate", is(newUserDTO.getRegistrationDate().toString())));
    }

    @Test
    void updateUser() throws Exception {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO(
                userId,
                "Updated User",
                "0987654321",
                LocalDate.now()
        );

        // Mock the service
        when(userService.updateUser(any(Long.class), any(UserDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType("application/json")
                        .content("{\"id\":1,\"name\":\"Updated User\",\"phoneNumber\":\"0987654321\",\"registrationDate\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedUserDTO.getName())))
                .andExpect(jsonPath("$.phoneNumber", is(updatedUserDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.registrationDate", is(updatedUserDTO.getRegistrationDate().toString())));
    }

    @Test
    void updateUserNotFoundException() throws Exception {
        Long userId = 1L;

        // Mock the service
        when(userService.updateUser(any(Long.class), any(UserDTO.class))).thenThrow(new UserException(new ExceptionEntity(501, "User not found with id: " + userId)));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType("application/json")
                        .content("{\"id\":1,\"name\":\"Updated User\",\"phoneNumber\":\"0987654321\",\"registrationDate\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(501)))
                .andExpect(jsonPath("$.message", is("User not found with id: " + userId)));
    }

    @Test
    void partiallyUpdateUser() throws Exception {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO(
                userId,
                "Partially Updated User",
                "0987654321",
                LocalDate.now()
        );

        // Mock the service
        when(userService.partiallyUpdateUser(any(Long.class), any(Map.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content("{\"name\":\"Partially Updated User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedUserDTO.getName())))
                .andExpect(jsonPath("$.phoneNumber", is(updatedUserDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.registrationDate", is(updatedUserDTO.getRegistrationDate().toString())));
    }

    @Test
    void partiallyUpdateUserNotFoundException() throws Exception {
        Long userId = 1L;

        // Mock the service
        when(userService.partiallyUpdateUser(any(Long.class), any(Map.class))).thenThrow(new UserException(new ExceptionEntity(501, "User not found with id: " + userId)));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content("{\"name\":\"Partially Updated User\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(501)))
                .andExpect(jsonPath("$.message", is("User not found with id: " + userId)));
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        // Mock the service
        doNothing().when(userService).deleteUser(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }
}
