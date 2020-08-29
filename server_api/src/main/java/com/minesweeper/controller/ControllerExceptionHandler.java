package com.minesweeper.controller;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.minesweeper.model.BoardException;
import com.minesweeper.model.CellException;
import com.minesweeper.model.CellPositionException;
import com.minesweeper.model.GameException;
import com.minesweeper.service.GameNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GameException.class)
    protected ResponseEntity<Object> handleGameException(GameException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameNotFoundException.class)
    protected ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardException.class)
    protected ResponseEntity<Object> handleBoardException(BoardException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CellException.class)
    protected ResponseEntity<Object> handleCellException(CellException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CellPositionException.class)
    protected ResponseEntity<Object> handleCellPositionException(CellPositionException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> createErrorBody(HttpStatus status, Object error) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalTime.now());
        body.put("status", status.value());
        body.put("error", error);
        return body;
    }
}
