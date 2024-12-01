package com.marcin.library_spring_boot.controller;

import com.marcin.library_spring_boot.requestModels.ReviewRequest;
import com.marcin.library_spring_boot.service.ReviewService;
import com.marcin.library_spring_boot.utils.ExtractJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJwt.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }

    @GetMapping("/secure/user/book")
    public boolean bookReviewedByUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                      @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJwt.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        return reviewService.bookReviewedByUser(userEmail, bookId);
    }
}
