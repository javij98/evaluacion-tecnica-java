package com.example.demo.loan.repository;

import com.example.demo.loan.dao.LoanDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanDAO, Long> {
}