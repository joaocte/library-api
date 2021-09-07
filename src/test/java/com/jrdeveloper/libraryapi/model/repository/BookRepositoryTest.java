package com.jrdeveloper.libraryapi.model.repository;

import com.jrdeveloper.libraryapi.api.model.entity.Book;
import com.jrdeveloper.libraryapi.repository.interfaces.IBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    IBookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN Informado")
    public void returnTrueWhenIsbnExists()
    {
        var isbn = "123";
        var book = Book.builder().title("title").isbn(isbn).author("autor").build();
        entityManager.persist(book);
        var exists = bookRepository.existsByIsbn(isbn);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o ISBN Informado")
    public void returnFalseWhenIsbnDoesntExists()
    {
        var isbn = "123";
        var exists = bookRepository.existsByIsbn(isbn);
        assertThat(exists).isFalse();
    }
}
