package com.marcin.library_spring_boot.dao;


import com.marcin.library_spring_boot.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

//INFO: żeby Spring Data REST eksponowało endpoint trzeba zrobić takie repository
//  można wyłączyć wybrane endpointy w com.marcin.library_spring_boot.config.MyDataRestConfig.disableHttpMethods
public interface BookRepository extends JpaRepository<Book, Long> {

}
