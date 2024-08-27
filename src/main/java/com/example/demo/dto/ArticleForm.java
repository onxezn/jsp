package com.example.demo.dto;

import com.example.demo.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor // 생성자
@ToString
@Getter
@Setter

public class ArticleForm {
    private Long id;
    private String title;
    private String content;


    // toEntity method to convert DTO to Entity
    public Article toEntity() {
        return new Article(id, title, content);
    }


}
