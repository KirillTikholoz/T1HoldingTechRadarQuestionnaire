package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollRequestDto {
    @NotNull
    @JsonProperty("tech_id")
    private Long techId;
    @NotEmpty
    @JsonProperty("ringResult")
    private String ringResult;
}
