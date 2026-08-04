package main;

import jakarta.validation.constraints.NotBlank;

public record CreateNoteRequest(

        @NotBlank(message = "Note text cannot be empty") String noteText) {
}
