package com.marcin.library_spring_boot.entity;


import jakarta.persistence.*;
import lombok.Data;

//INFO: Spring Data automatycznie ukrywa id encji, więc trzeba dodać widoczność
//      w MyDataRestConfig w config.exposeIdsFor oraz ustawić widoczność metod HTTP
@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "author", length = 45)
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "copies")
    private Integer copies;

    @Column(name = "copies_available")
    private Integer copiesAvailable;

    @Column(name = "category", length = 11)
    private String category;

    @Column(name = "img")
    private String img;

}