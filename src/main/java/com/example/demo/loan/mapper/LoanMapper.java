package com.example.demo.loan.mapper;

import com.example.demo.loan.dao.LoanDAO;
import com.example.demo.loan.dto.LoanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    LoanDTO loanDAOToLoanDTO(LoanDAO loanDAO);
    List<LoanDTO> loanDAOsToLoanDTOs(List<LoanDAO> listOfLoanDAOs);
    LoanDAO loanDTOToLoanDAO(LoanDTO loanDTO);
}