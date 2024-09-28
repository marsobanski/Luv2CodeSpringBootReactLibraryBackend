package com.marcin.library_spring_boot.controller;

import com.marcin.library_spring_boot.dao.BookRepository;
import com.marcin.library_spring_boot.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final BookRepository bookRepository;

    //INFO: tak napisana metoda zwraca mniej więcej to samo,
    //      co domyślne Spring Data zwraca przy zapytaniu /api/books.
    //      Tak jak domyślnie, można dodawać parametry typu ?page=0&size=9, Pageable je odczytuje

    @GetMapping("/books")
    public Page<Book> getAllBooks(Pageable pageable) {
        int startIndex = pageable.getPageSize() * pageable.getPageNumber();
        int endIndex = pageable.getPageSize() * (pageable.getPageNumber() + 1) -1;
        List<Book> books = bookRepository.findAll().subList(startIndex, endIndex);
        books.forEach(book -> book.setImg(""));
        return new PageImpl<>(books, pageable, books.size());
    }

    /**
     * pageSize = 5 -> 0: 0-4, 1: 5-9, 2: 10-14 -> min = pageSize * pageNum, max = (pageSize * pageNum + 1) -1
     * pageSize = 7 -> 0: 0-6, 1: 7-13, 2: 14-20 ->  */
}
