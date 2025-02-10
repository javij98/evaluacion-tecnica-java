package com.example.demo.book.repository;

import com.example.demo.book.dao.BookDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookDAO, Long> {
}
