package com.example.demo.user.service.impl;

import com.example.demo.exceptions.models.ExceptionEntity;
import com.example.demo.user.dao.UserDAO;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public List<UserDTO> getUsers() {
        logger.debug("UserServiceImpl: Getting all users...");
        List<UserDAO> listOfUsersDAO = userRepository.findAll();
        List<UserDTO> listOfUsersDTOs = userMapper.userDAOsToUserDTOs(listOfUsersDAO);
        logger.debug("UserServiceImpl: getUsers() -> {} users obtained.", listOfUsersDAO.size());
        return listOfUsersDTOs;
    }

    @Override
    public UserDTO getUserById(Long id) {
        logger.debug("UserServiceImpl: Getting user with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(501, "User not found with id: " + id);
        UserDAO userDAO = userRepository.findById(id).orElseThrow(() -> new UserException(err));
        UserDTO userDTO = userMapper.userDAOToUserDTO(userDAO);
        logger.debug("UserServiceImpl: getUserById() -> User obtained: {}", userDTO);
        return userDTO;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.debug("UserServiceImpl: Creating a new user...");
        UserDAO userDAO = userMapper.userDTOToUserDAO(userDTO);
        UserDAO createdUserDAO = userRepository.save(userDAO);
        UserDTO createdUserDTO = userMapper.userDAOToUserDTO(createdUserDAO);
        logger.debug("UserServiceImpl: createUser() -> User created: {}", createdUserDTO);
        return createdUserDTO;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.debug("UserServiceImpl: Updating user with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(501, "User not found with id: " + id);
        UserDAO userDAO = userRepository.findById(id).orElseThrow(() -> new UserException(err));

        userDAO.setName(userDTO.getName());
        userDAO.setPhoneNumber(userDTO.getPhoneNumber());
        userDAO.setRegistrationDate(userDTO.getRegistrationDate());
        UserDAO updatedUserDAO = userRepository.save(userDAO);
        UserDTO updatedUserDTO = userMapper.userDAOToUserDTO(updatedUserDAO);

        logger.debug("UserServiceImpl: updateUser() -> User updated: {}", updatedUserDTO);
        return updatedUserDTO;
    }

    @Override
    public UserDTO partiallyUpdateUser(Long id, Map<String, Object> updates) {
        logger.debug("UserServiceImpl: Partially updating user with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(501, "User not found with id: " + id);
        UserDAO userDAO = userRepository.findById(id).orElseThrow(() -> new UserException(err));

        if (updates.containsKey("name")) {
            userDAO.setName((String) updates.get("name"));
        }
        if (updates.containsKey("phoneNumber")) {
            userDAO.setPhoneNumber((String) updates.get("phoneNumber"));
        }
        if (updates.containsKey("registrationDate")) {
            userDAO.setRegistrationDate((LocalDate) updates.get("registrationDate"));
        }

        UserDAO updatedUserDAO = userRepository.save(userDAO);
        UserDTO updatedUserDTO = userMapper.userDAOToUserDTO(updatedUserDAO);

        logger.debug("UserServiceImpl: partiallyUpdateUser() -> User updated: {}", updatedUserDTO);
        return updatedUserDTO;
    }

    @Override
    public void deleteUser(Long id) {
        logger.debug("UserServiceImpl: Deleting user with id {}...", id);
        userRepository.deleteById(id);
        logger.debug("UserServiceImpl: deleteUser() -> User deleted with id: {}", id);
    }
}