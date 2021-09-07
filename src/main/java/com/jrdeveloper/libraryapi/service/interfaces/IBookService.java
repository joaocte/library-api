package com.jrdeveloper.libraryapi.service.interfaces;

import com.jrdeveloper.libraryapi.api.model.entity.Book;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface IBookService {
    Book save(Book book);

    Optional<Book> getById(UUID id);
}
