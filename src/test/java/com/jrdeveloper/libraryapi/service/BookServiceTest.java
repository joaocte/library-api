package com.jrdeveloper.libraryapi.service;

import com.jrdeveloper.libraryapi.api.model.entity.Book;
import com.jrdeveloper.libraryapi.exception.BusinessException;
import com.jrdeveloper.libraryapi.repository.interfaces.IBookRepository;
import com.jrdeveloper.libraryapi.service.interfaces.IBookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {


    @MockBean
    private IBookRepository repository;

    @BeforeEach
    public void setUp()
    {
        this.bookService =new  BookService(repository);
    }


    IBookService bookService;
    @Test
    @DisplayName("Deve Salvar um Livro")
    public void saveBookTest()
    {

        var book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(
                Book.builder().id(UUID.randomUUID())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .isbn(book.getIsbn())
                        .build()
        );
        var savedBook = bookService.save(book);


        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());

    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        var mensagemErro = "ISBN Ja Cadastrado";
        var book = createValidBook();

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        var exception =  Assertions.catchThrowable(()-> bookService.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(mensagemErro);

        Mockito.verify(repository, Mockito.never() ).save(book);
    }


    private Book createValidBook() {
        return Book.builder().isbn("123").author("Autor").title("Titulo").build();
    }

}
