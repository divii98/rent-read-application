package com.crio.rentread.exchange;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAddRequest {
    @NotBlank(message = "title cannot be null")
    private String title;
    @NotBlank(message = "author name cannot be blank")
    private String author;
    @NotBlank(message = "genre cannot be blank")
    private String genre;
}
