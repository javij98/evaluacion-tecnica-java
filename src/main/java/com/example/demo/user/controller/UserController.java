package com.example.demo.user.controller;

import com.example.demo.exceptions.models.ExceptionEntity;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping
    List<UserDTO> getUsers() {
        logger.info("Getting all users...");
        List<UserDTO> listOfUsers = userService.getUsers();
        logger.info("UserController: getUsers() -> {} users obtained.", listOfUsers);
        return listOfUsers;
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getUserById(@PathVariable Long id) {
        logger.info("Getting user with id {}...", id);
        try {
            UserDTO user = userService.getUserById(id);
            logger.info("UserController: getUserById() -> User obtained: {}", user);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @PostMapping
    UserDTO createUser(@RequestBody UserDTO userDTO) {
        logger.info("Creating a new user...");
        UserDTO createdUser = userService.createUser(userDTO);
        logger.info("UserController: createUser() -> User created: {}", createdUser);
        return createdUser;
    }

    @PutMapping("/{id}")
    ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        logger.info("Updating user with id {}...", id);
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            logger.info("UserController: updateUser() -> User updated: {}", updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (UserException e) {
            logger.error("UserController: Error updating user with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<Object> partiallyUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        logger.info("Partially updating user with id {}...", id);
        try {
            UserDTO updatedUser = userService.partiallyUpdateUser(id, updates);
            logger.info("UserController: partiallyUpdateUser() -> User updated: {}", updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (UserException e) {
            logger.error("UserController: Error partially updating user with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with id {}...", id);
        userService.deleteUser(id);
        logger.info("UserController: deleteUser() -> User deleted with id: {}", id);
    }
}