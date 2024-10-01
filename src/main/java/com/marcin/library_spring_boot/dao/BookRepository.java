package com.marcin.library_spring_boot.dao;


import com.marcin.library_spring_boot.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//INFO: żeby Spring Data REST eksponowało endpoint trzeba zrobić takie repository
//  można wyłączyć wybrane endpointy w com.marcin.library_spring_boot.config.MyDataRestConfig.disableHttpMethods
public interface BookRepository extends JpaRepository<Book, Long> {

    //INFO: Tworzenie metod dodaje endpoint /books/search, gdzie są dostępne do wywołania poniższe metody (kolejne endpointy)
    Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);
        //"http://localhost:8080/api/books/search/findByTitleContaining{?title,page,size,sort*}"

    //INFO: findByCośtamContaiging szuka po .contains, a findByCośtam szuka po .equals
    Page<Book> findByCategoryContaining(@RequestParam("category") String category, Pageable pageable);

    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);

    Page<Book> findByAuthorContaining(@RequestParam("author") String author, Pageable pageable);

    List<Book> findByTitle(String title);
        //http://localhost:8080/api/books/search/findByTitle{?title}",

}
