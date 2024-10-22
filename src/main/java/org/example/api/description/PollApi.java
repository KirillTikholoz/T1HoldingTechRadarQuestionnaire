package org.example.api.description;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.dtos.ErrorResponseDto;
import org.example.dtos.ErrorSavePollResponseDto;
import org.example.dtos.PollRequestDto;
import org.example.dtos.SuccessfulSavePollResponseDto;
import org.springframework.http.ResponseEntity;

public interface PollApi {
    @Operation(summary = "Save Poll", description = "Save the poll result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Poll successfully saved",
                    content = @Content(schema = @Schema(implementation = SuccessfulSavePollResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Technology not found",
                    content = @Content(schema = @Schema(implementation = ErrorSavePollResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request, such as missing or invalid parameters",
                    content = @Content(schema = @Schema(implementation = ErrorSavePollResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorSavePollResponseDto.class)))
    })
    ResponseEntity<SuccessfulSavePollResponseDto> addPoolResult(@Valid PollRequestDto pollRequestDto);
}
