package com.example.demo.loan.controller;

import com.example.demo.exceptions.models.ExceptionEntity;
import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.exception.LoanException;
import com.example.demo.loan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    private List<LoanDTO> loanDTOList;

    @BeforeEach
    void setUp() {
        loanDTOList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            LoanDTO loanDTO = LoanDTO.builder()
                    .id((long) i)
                    .loanDate(LocalDate.now())
                    .returnDate(LocalDate.now().plusDays(30))
                    .build();
            loanDTOList.add(loanDTO);
        }
    }

    @Test
    void getLoans() throws Exception {
        when(loanService.getLoans()).thenReturn(loanDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(loanDTOList.size())));
    }

    @Test
    void getLoanById() throws Exception {
        when(loanService.getLoanById(anyLong())).thenReturn(loanDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanDate", is(loanDTOList.get(0).getLoanDate().toString())))
                .andExpect(jsonPath("$.returnDate", is(loanDTOList.get(0).getReturnDate().toString())));
    }

    @Test
    void getLoanById_NotFound() throws Exception {
        when(loanService.getLoanById(anyLong())).thenThrow(new LoanException(new ExceptionEntity(404, "Loan not found")));

        mockMvc.perform(MockMvcRequestBuilders.get("/loans/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Loan not found")));
    }

    @Test
    void createLoan() throws Exception {

        LoanDTO createdLoanDTO = LoanDTO.builder()
                .id(11L)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(30))
                .build();

        when(loanService.createLoan(any(LoanDTO.class))).thenReturn(createdLoanDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/loans")
                        .contentType("application/json")
                        .content("{\"loanDate\":\"" + LocalDate.now() + "\",\"returnDate\":\"" + LocalDate.now().plusDays(30) + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.loanDate", is(createdLoanDTO.getLoanDate().toString())))
                .andExpect(jsonPath("$.returnDate", is(createdLoanDTO.getReturnDate().toString())));
    }

    @Test
    void updateLoan() throws Exception {
        LoanDTO updatedLoanDTO = LoanDTO.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(30))
                .build();

        when(loanService.updateLoan(anyLong(), any(LoanDTO.class))).thenReturn(updatedLoanDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/loans/1")
                        .contentType("application/json")
                        .content("{\"id\":1,\"loanDate\":\"" + LocalDate.now() + "\",\"returnDate\":\"" + LocalDate.now().plusDays(30) + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanDate", is(updatedLoanDTO.getLoanDate().toString())))
                .andExpect(jsonPath("$.returnDate", is(updatedLoanDTO.getReturnDate().toString())));
    }

    @Test
    void updateLoanNotFoundException() throws Exception {
        when(loanService.updateLoan(anyLong(), any(LoanDTO.class))).thenThrow(new LoanException(new ExceptionEntity(404, "Loan not found")));

        mockMvc.perform(MockMvcRequestBuilders.put("/loans/1")
                        .contentType("application/json")
                        .content("{\"id\":1,\"loanDate\":\"" + LocalDate.now() + "\",\"returnDate\":\"" + LocalDate.now().plusDays(30) + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Loan not found")));
    }

    @Test
    void partiallyUpdateLoan() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("returnDate", LocalDate.now().plusDays(60));

        LoanDTO updatedLoanDTO = LoanDTO.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(60))
                .build();

        when(loanService.partiallyUpdateLoan(anyLong(), any(Map.class))).thenReturn(updatedLoanDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/loans/1")
                        .contentType("application/json")
                        .content("{\"returnDate\":\"" + LocalDate.now().plusDays(60) + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnDate", is(updatedLoanDTO.getReturnDate().toString())));
    }

    @Test
    void partiallyUpdateLoanNotFoundException() throws Exception {
        when(loanService.partiallyUpdateLoan(anyLong(), any(Map.class))).thenThrow(new LoanException(new ExceptionEntity(404, "Loan not found")));

        mockMvc.perform(MockMvcRequestBuilders.patch("/loans/1")
                        .contentType("application/json")
                        .content("{\"returnDate\":\"" + LocalDate.now().plusDays(60) + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Loan not found")));
    }

    @Test
    void deleteLoan() throws Exception {
        doNothing().when(loanService).deleteLoan(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/loans/1"))
                .andExpect(status().isOk());
    }
}