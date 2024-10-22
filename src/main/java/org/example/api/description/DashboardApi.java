package org.example.api.description;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import org.example.common.Dashboard;
import org.example.dtos.ErrorResponseDto;
import org.springframework.http.ResponseEntity;

public interface DashboardApi {
    @Operation(summary = "Get Dashboard", description = "Get dashboard for the given technology ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get dashboard",
                    content = @Content(schema = @Schema(implementation = Dashboard.class))),
            @ApiResponse(responseCode = "404", description = "Technology not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request, such as missing or invalid parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<Dashboard> createDashboard(@Positive long techId);
}
