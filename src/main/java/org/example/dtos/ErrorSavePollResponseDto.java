package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorSavePollResponseDto {
    private Integer code;
    private String message;
}
