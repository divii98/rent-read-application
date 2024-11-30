package com.crio.rentread.controller;

import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.service.RentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RentControllerTest {

    @Mock
    private RentService rentService;

    @InjectMocks
    private RentController rentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
    }

    @Test
    void testRentBookController_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        String successMessage = "Book rented successfully for the user";
        when(rentService.rentBook(bookId)).thenReturn(successMessage);

        // Act & Assert
        mockMvc.perform(post("/books/{id}/rent", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(successMessage));

        verify(rentService, times(1)).rentBook(bookId);
    }

    @Test
    void testReturnBookController_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        String successMessage = "Book returned successfully for the user";
        when(rentService.returnBook(bookId)).thenReturn(successMessage);

        // Act & Assert
        mockMvc.perform(post("/books/{id}/return", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(successMessage));

        verify(rentService, times(1)).returnBook(bookId);
    }


}
