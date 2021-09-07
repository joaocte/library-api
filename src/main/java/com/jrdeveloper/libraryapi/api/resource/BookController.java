package com.jrdeveloper.libraryapi.api.resource;

import com.jrdeveloper.libraryapi.api.dto.BookDTO;
import com.jrdeveloper.libraryapi.api.exception.ApiErrors;
import com.jrdeveloper.libraryapi.api.model.entity.Book;
import com.jrdeveloper.libraryapi.exception.BusinessException;
import com.jrdeveloper.libraryapi.service.interfaces.IBookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private IBookService bookService;
    private ModelMapper modelMapper;
    public BookController(IBookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){
        var entity = modelMapper.map(dto, Book.class);
        entity = bookService.save(entity);
        var dtoSaved = modelMapper.map(entity, BookDTO.class);
        return dtoSaved;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        var result = ex.getBindingResult();

        return new ApiErrors(result);

    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex)
    {
        return new ApiErrors(ex);
    }



}
