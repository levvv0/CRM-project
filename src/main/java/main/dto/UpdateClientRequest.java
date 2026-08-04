package main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateClientRequest(
        @NotBlank(message = "Name cannot be empty") String name,

        @NotBlank(message = "Phone cannot be empty") String phone,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email has invalid format") String email) {
}
