package com.jrdeveloper.libraryapi.api.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrdeveloper.libraryapi.api.dto.BookDTO;
import com.jrdeveloper.libraryapi.api.model.entity.Book;
import com.jrdeveloper.libraryapi.exception.BusinessException;
import com.jrdeveloper.libraryapi.service.interfaces.IBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    IBookService service;

    @Test
    @DisplayName("Deve Criar um livro com Sucesso")
    public void createBookTest() throws Exception{

        var dto = createNewBookDTO();
        var entity = Book.builder().author("Artur").id(UUID.randomUUID()).isbn("0001").title("Titulo").build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(entity);
        var json = new ObjectMapper().writeValueAsString(dto);

        var request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }



    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados para criação do livro")
    public void createInvalidBookTest() throws Exception{

        var json = new ObjectMapper().writeValueAsString(new BookDTO());

        var request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andExpect(status().isBadRequest())
               .andExpect(jsonPath("errors",  hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já cadastrado")
    public void createBookWithDuplicatedIsbn() throws Exception {

        var dto = createNewBookDTO();
        var json = new ObjectMapper().writeValueAsString(dto);
        var mensagemErro = "ISBN Ja Cadastrado";
        BDDMockito
                .given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(mensagemErro));

        var request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));

    }
    private BookDTO createNewBookDTO() {
        return BookDTO.builder().author("Artur").title("Titulo").isbn("0001").build();
    }
}
