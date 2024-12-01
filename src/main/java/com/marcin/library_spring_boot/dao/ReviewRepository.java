package com.marcin.library_spring_boot.dao;


import com.marcin.library_spring_boot.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBookIdOrderByDateDesc(Long bookId, Pageable pageable);

    Review findByUserEmailAndBookId(String userEmail, Long bookId);
}
