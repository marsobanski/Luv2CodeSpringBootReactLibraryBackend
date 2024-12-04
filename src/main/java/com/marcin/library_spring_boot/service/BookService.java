package com.marcin.library_spring_boot.service;

import com.marcin.library_spring_boot.dao.BookRepository;
import com.marcin.library_spring_boot.dao.CheckoutRepository;
import com.marcin.library_spring_boot.entity.Book;
import com.marcin.library_spring_boot.entity.Checkout;
import com.marcin.library_spring_boot.responseModels.ShelfCurrentLoansResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;
    private final long DAYS_TO_RETURN_FROM_CHECKOUT_DATE = 7;

    public Book checkoutBook (String userEmail, Long bookId) throws Exception {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Checkout existingCheckoutForBook = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (existingCheckoutForBook != null) {
            throw new IllegalStateException("Book is already checked out by user");
        }

        bookOpt.ifPresentOrElse(book -> {
            if (book.getCopiesAvailable() <= 0) {
                throw new IllegalStateException("Book is unavailable");
            }
            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            bookRepository.save(book);
            Checkout newCheckout = new Checkout(
                    userEmail,
                    LocalDate.now().toString(),
                    LocalDate.now().plusDays(DAYS_TO_RETURN_FROM_CHECKOUT_DATE).toString(),
                    book.getId());
            checkoutRepository.save(newCheckout);
        }, () -> {
            throw new IllegalStateException("Book not found");
        });
        return bookOpt.orElseThrow(() -> new Exception("Error on creating a book"));
    }

    public boolean bookIsCheckedOutByUser(String userEmail, Long bookId) {
        Checkout existingCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        return existingCheckout != null;
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();
        List<Checkout> checkouts = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = checkouts.stream()
                .map(Checkout::getBookId)
                .toList();
        List<Book> books = bookRepository.findAllById(bookIdList);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Book book : books) {
            checkouts.stream()
                    .filter(checkout -> book.getId().equals(checkout.getBookId()))
                    .findAny()
                    .ifPresent(checkout -> {
                        try {
                            Date checkoutReturnDate = simpleDateFormat.parse(checkout.getReturnDate());
                            Date now = simpleDateFormat.parse(LocalDate.now().toString());
                            TimeUnit timeUnit = TimeUnit.DAYS;
                            long differenceInTime = timeUnit.convert(checkoutReturnDate.getTime() - now.getTime(), TimeUnit.MILLISECONDS);
                            shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) differenceInTime));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return shelfCurrentLoansResponses;
    }
}
