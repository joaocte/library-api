package com.jrdeveloper.libraryapi.service;

import com.jrdeveloper.libraryapi.api.model.entity.Book;
import com.jrdeveloper.libraryapi.exception.BusinessException;
import com.jrdeveloper.libraryapi.repository.interfaces.IBookRepository;
import com.jrdeveloper.libraryapi.service.interfaces.IBookService;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService {
    private IBookRepository repository;

    public BookService(IBookRepository repository) {
        this.repository = repository;
    }


    @Override
    public Book save(Book book) {

        if(repository.existsByIsbn(book.getIsbn()))
        {
            throw new BusinessException("ISBN Ja Cadastrado");
        }
        return repository.save(book);
    }
}
