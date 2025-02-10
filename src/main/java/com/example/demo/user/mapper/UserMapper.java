package com.example.demo.user.mapper;

import com.example.demo.user.dao.UserDAO;
import com.example.demo.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userDAOToUserDTO(UserDAO userDAO);
    List<UserDTO> userDAOsToUserDTOs(List<UserDAO> listOfUserDAOs);
    UserDAO userDTOToUserDAO(UserDTO userDTO);
}