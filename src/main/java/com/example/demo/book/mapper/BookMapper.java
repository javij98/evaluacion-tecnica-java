package com.example.demo.book.mapper;

import com.example.demo.book.dao.BookDAO;
import com.example.demo.book.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO bookDAOToBookDTO(BookDAO bookDAO);
    List<BookDTO> bookDAOsToBookDTOs(List<BookDAO> listOfBookDAOs);
    BookDAO bookDTOToBookDAO(BookDTO bookDTO);
}
