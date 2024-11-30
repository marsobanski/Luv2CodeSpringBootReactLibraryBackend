package com.marcin.library_spring_boot.service;

import com.marcin.library_spring_boot.dao.ReviewRepository;
import com.marcin.library_spring_boot.entity.Review;
import com.marcin.library_spring_boot.requestModels.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void postReview(String email, ReviewRequest reviewRequest) throws Exception {
        if (bookReviewedByUser(email, reviewRequest.getBookId())) {
            throw new Exception("Review already exists");
        }
        Review review = new Review();
        review.setUserEmail(email);
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setDescription(reviewRequest.getDescription());
        review.setDate(new Date());
        reviewRepository.save(review);
    }

    public boolean bookReviewedByUser(String userEmail, Long bookId) {
        return reviewRepository.findByUserEmailAndBookId(userEmail, bookId) != null;
    }
}
