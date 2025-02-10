package com.example.demo.user.service.impl;

import com.example.demo.user.dao.UserDAO;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private List<UserDAO> userDAOList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDAOList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            UserDAO userDAO = new UserDAO();
            userDAO.setId((long) i);
            userDAO.setName("User " + i);
            userDAO.setPhoneNumber("123456789" + i);
            userDAO.setRegistrationDate(LocalDate.now());
            userDAOList.add(userDAO);
        }
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(userDAOList);
        List<UserDTO> listOfUserDTO = userService.getUsers();
        assertEquals(userDAOList.size(), listOfUserDTO.size());
    }

    @Test
    void getUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userDAOList.get(0)));
        UserDTO userDTO = userService.getUserById(1L);
        assertEquals(userDAOList.get(0).getId(), userDTO.getId());
        assertEquals(userDAOList.get(0).getName(), userDTO.getName());
        assertEquals(userDAOList.get(0).getPhoneNumber(), userDTO.getPhoneNumber());
        assertEquals(userDAOList.get(0).getRegistrationDate(), userDTO.getRegistrationDate());
    }

   @Test
    void createUser() {
        UserDTO newUserDTO = UserDTO.builder()
                .name("New User")
                .phoneNumber("1234567890")
                .registrationDate(LocalDate.now())
                .build();

        UserDAO newUserDAO = UserDAO.builder()
                .name("New User")
                .phoneNumber("1234567890")
                .registrationDate(LocalDate.now())
                .build();

        UserDAO createdUserDAO = UserDAO.builder()
                .id(11L)
                .name("New User")
                .phoneNumber("1234567890")
                .registrationDate(LocalDate.now())
                .build();

        UserDTO createdUserDTO = UserDTO.builder()
                .id(11L)
                .name("New User")
                .phoneNumber("1234567890")
                .registrationDate(LocalDate.now())
                .build();

        when(userMapper.userDTOToUserDAO(newUserDTO)).thenReturn(newUserDAO);
        when(userRepository.save(newUserDAO)).thenReturn(createdUserDAO);
        when(userMapper.userDAOToUserDTO(createdUserDAO)).thenReturn(createdUserDTO);

        UserDTO result = userService.createUser(newUserDTO);

        assertNotNull(result);
        assertEquals(11L, result.getId());
        assertEquals("New User", result.getName());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals(LocalDate.now(), result.getRegistrationDate());
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        UserDTO updatedUserDTO = UserDTO.builder()
                .id(userId)
                .name("Updated User")
                .phoneNumber("9876543210")
                .registrationDate(LocalDate.now())
                .build();

        UserDAO existingUserDAO = userDAOList.get(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUserDAO));
        when(userRepository.save(existingUserDAO)).thenReturn(existingUserDAO);
        when(userMapper.userDAOToUserDTO(existingUserDAO)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateUser(userId, updatedUserDTO);

        assertNotNull(result);
        assertEquals(updatedUserDTO.getId(), result.getId());
        assertEquals(updatedUserDTO.getName(), result.getName());
        assertEquals(updatedUserDTO.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(updatedUserDTO.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    void partiallyUpdateUser_AllFields() {
        Long userId = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Partially Updated User");
        updates.put("phoneNumber", "0987654321");
        updates.put("registrationDate", LocalDate.of(2025, 5, 20));

        UserDAO existingUserDAO = userDAOList.get(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUserDAO));
        when(userRepository.save(existingUserDAO)).thenReturn(existingUserDAO);


        UserDTO updatedUserDTO = UserDTO.builder()
                .id(userId)
                .name("Partially Updated User")
                .phoneNumber("0987654321")
                .registrationDate(LocalDate.of(2025, 5, 20))
                .build();

        when(userMapper.userDAOToUserDTO(existingUserDAO)).thenReturn(updatedUserDTO);

        UserDTO result = userService.partiallyUpdateUser(userId, updates);

        assertNotNull(result);
        assertEquals("Partially Updated User", result.getName());
        assertEquals("0987654321", result.getPhoneNumber());
        assertEquals(LocalDate.of(2025, 5, 20), result.getRegistrationDate());
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUser(userId);
    }
}