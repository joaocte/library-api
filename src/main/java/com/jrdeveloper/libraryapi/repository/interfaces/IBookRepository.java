package com.jrdeveloper.libraryapi.repository.interfaces;

import com.jrdeveloper.libraryapi.api.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IBookRepository extends JpaRepository<Book, UUID> {
    boolean existsByIsbn(String isbn);
}
