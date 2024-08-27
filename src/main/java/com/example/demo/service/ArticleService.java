package com.example.demo.service;

import com.example.demo.dto.ArticleForm;
import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article save(ArticleForm dto) {
        Article article = dto.toEntity();
        if(article.getId() != null) {
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        Article article = dto.toEntity();
        Article target = articleRepository.findById(id).orElse(null);
        if(target == null || !article.getId().equals(target.getId())) {
            return null;
        }
        target.patch(article);
        return articleRepository.save(article);

    }

    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);
        if(target == null) {
            return null;
        }
        articleRepository.delete(target);
        return target;
    }

    @Transactional // 해당 메소드 수행시 오류 발생 -> 롤백시킴
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // dtos 를 entityList로 변환
        List<Article> articleList = dtos.stream().map(dto -> dto.toEntity()).collect(Collectors.toList());

        // entity리스트를 DB에 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // transaction 테스트를 위해서 강제로 예외를 발생.
        articleRepository.findById(-1L).orElseThrow(() -> new IllegalArgumentException("결제 실패"));

//       // 위를 정석? 내가 아는 코드
//        for(int i = 0; i < dtos.size(); i++) {
//            ArticleForm dto = dtos.get(i);
//            Article entity = dto.toEntity();
//            articleList.add(entity);
//        }

//        for(int i = 0; i < articleList.size(); i++) {
//            articleRepository.save(articleList.get(i));
//        }

        return articleList;
    }
}



// 0 1 2 -> 0  0 1 3 -> 1 / 014 -> 2 .... 0 2 3 -> 9번째
