package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter

public class Article {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // strategy = GenerationType.IDENTIY 는 DB에서 자동으로 번호를 매겨줌.)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;


    public void patch(Article article) {
        if (article.title != null)
            this.title = article.title;
        if (article.content != null)
            this.content = article.content;
    }
}
