package com.example.demo.user.service;

import com.example.demo.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDTO> getUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    UserDTO partiallyUpdateUser(Long id, Map<String, Object> updates);
    void deleteUser(Long id);
}