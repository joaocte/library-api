package com.jrdeveloper.libraryapi.api.exception;

import com.jrdeveloper.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;
    public ApiErrors(BindingResult result) {
        this.errors = new ArrayList<>();
        result.getAllErrors().forEach(x-> this.errors.add(x.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex) {

        this.errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
