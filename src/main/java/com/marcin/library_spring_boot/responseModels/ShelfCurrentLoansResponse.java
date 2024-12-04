package com.marcin.library_spring_boot.responseModels;

import com.marcin.library_spring_boot.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShelfCurrentLoansResponse {

    private Book book;
    private int daysLeft;
}
