package com.carrental.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}