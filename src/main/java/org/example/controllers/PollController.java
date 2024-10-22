package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.ErrorSavePollResponseDto;
import org.example.dtos.PollRequestDto;
import org.example.dtos.SuccessfulSavePollResponseDto;
import org.example.exceptions.RingNotFoundException;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.services.PollService;
import org.example.api.description.PollApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PollController implements PollApi {
    private final PollService pollService;
    @PostMapping("/poll")
    public ResponseEntity<SuccessfulSavePollResponseDto> addPoolResult(@Valid @RequestBody PollRequestDto pollRequestDto){
        pollService.savePoll(pollRequestDto);
        return new ResponseEntity<>(new SuccessfulSavePollResponseDto("Результат опроса успешно добавлен"),
                                    HttpStatus.CREATED);
    }

    @ExceptionHandler(TechnologyNotFoundException.class)
    public ResponseEntity<ErrorSavePollResponseDto> TechnologyNotFoundExceptionHandler(TechnologyNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorSavePollResponseDto(404,"TECHNOLOGY_NOT_FOUND"),
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, RingNotFoundException.class})
    public ResponseEntity<ErrorSavePollResponseDto> otherException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorSavePollResponseDto(400,"BAD_REQUEST"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorSavePollResponseDto> handleServerExceptions(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorSavePollResponseDto(500,"INTERNAL_SERVER_ERROR"),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
