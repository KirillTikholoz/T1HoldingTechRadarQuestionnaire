package org.example.controllers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.Dashboard;
import org.example.dtos.ErrorResponseDto;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.services.DashboardService;
import org.example.api.description.DashboardApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class DashboardController implements DashboardApi {
    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard/{tech_id}")
    public ResponseEntity<Dashboard> createDashboard(@PathVariable("tech_id") @Positive long tech_id){
        Dashboard newDashboard = dashboardService.createDashboard(tech_id);
        return new ResponseEntity<>(newDashboard, HttpStatus.OK);
    }

    @ExceptionHandler(TechnologyNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> TechnologyNotFoundExceptionHandler(TechnologyNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto("Not Found",
                        "The technology with the given ID does not exist."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponseDto> MissingParameterExceptionHandler(MissingPathVariableException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto("Bad Request",
                "Invalid query parameters or missing required parameters."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto("Invalid request",
                "The provided technology ID is invalid."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleServerExceptions(Exception ex) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(new ErrorResponseDto("Internal Server Error",
                "An unexpected error occurred on the server. Please try again later."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
