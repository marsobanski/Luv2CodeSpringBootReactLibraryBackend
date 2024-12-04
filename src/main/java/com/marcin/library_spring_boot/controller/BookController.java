package com.marcin.library_spring_boot.controller;

import com.marcin.library_spring_boot.dao.CheckoutRepository;
import com.marcin.library_spring_boot.entity.Book;
import com.marcin.library_spring_boot.responseModels.ShelfCurrentLoansResponse;
import com.marcin.library_spring_boot.service.BookService;
import com.marcin.library_spring_boot.utils.ExtractJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// INFO: frontend na React będzie miał dostęp do tego kontrolera bez problemów w CORS policy
//@CrossOrigin(origins = {"${frontend.host}"})
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CheckoutRepository checkoutRepository;

    //INFO: "/secure" zosało zabezpieczone w SecurityConfig, żeby tylko po autoryzacji tokenem JWT dało się wejść
    //INFO: "@RequestHeader(value = "Authorization") String token" dodaje wymagalność tokena
    //      (nazywa to się Bearer Token i String zaczyna się od "Bearer",
    //      a dopiero dalej jest zakodowany String tokena do odbieranego requestu (patrz ExtractJwt)
    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                             @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJwt.payloadJWTExtraction(token, "\"sub\"");
        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public boolean isBookCheckedOutByUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                          @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJwt.payloadJWTExtraction(token, "\"sub\"");
        return bookService.bookIsCheckedOutByUser(userEmail, bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int getCurrentLoanCount(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        String userEmail = ExtractJwt.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) throws Exception {
        String userEmail = ExtractJwt.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);
    }
}
