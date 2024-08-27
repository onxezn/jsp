//package com.example.demo.controller;
//
//import com.example.demo.dto.ArticleForm;
//import com.example.demo.entity.Article;
//import com.example.demo.repository.ArticleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class ArticleController {
//
//    @Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결
//    private ArticleRepository articleRepository;
//
//
//    @GetMapping("/articles/new")
//    public String newArticleForm() {
//        return "articles/new";
//    }
//
//    @PostMapping("/articles/create")
//    public String createArticle(ArticleForm form) {
//        System.out.println(form.toString());
//        // DTO -> entity로 변환
//        Article article = form.toEntity();
//
//
//        // Repository에게  Entity를 db안에 저장하도록 함.
//        Article saved = articleRepository.save(article);
//
//
//
//        return "";
//    }
//}

//package com.example.demo.controller;
//import com.example.demo.dto.ArticleForm;
//import com.example.demo.entity.Article;
//import com.example.demo.repository.ArticleRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class ArticleController {
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @GetMapping("/articles/new")
//    public String newArticleForm() {
//        return "articles/new";
//    }
//
//    @PostMapping("/articles/create")
//    public String createArticle(ArticleForm form) {
//        System.out.println(form.toString());
//        // DTO를 Entity로 바꾼다.
//        Article article = form.toEntity();
//
//        // 2. Repository에게 Entity를 DB로 저장하게 함
//        Article saved = articleRepository.save(article);
//        System.out.println(saved.toString());
//
//        return "";
//    }
//}


package com.example.demo.controller;

//gpt작성
import com.example.demo.dto.ArticleForm;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ListIndexBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@Slf4j //로깅 위한 어노테이션
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;


    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
//      1. DTO를 엔티티로 변환
        Article article = form.toEntity( );

//      2. 엔티티를 DB에 저장하고, 저장된 엔티티를 반환
        Article saved = articleRepository.save(article);

//      logging으로 사용하여 정보 제대로 들어갔는지 확인.
        log.info(saved.toString());

//      redirect -> Client에게 다시 요청을 하도록 만듦. -> 다른 페이지로 이동하도록 함..
//
        return "redirect:/articles/" + saved.getId();
    }

    // 파라미터로 Model 이용하는 이유 -> view로 전달하기 위해서.
    // 이 함수에서 return "articles/show" show mustache 뷰 템플릿에서 이 model에 들어간 값을 표현할 수 있음.
    // 그러면 model.addAttribute를 통해서 값을 넣을 수 있는데, 두개를 원하는 값? -> List 이용
    @GetMapping("/articles/{id}") // 해당 URL요청을 처리 선언
    public String show(@PathVariable Long id, Model model) { // URL에서 id를 변수로 가져옴
        log.info("id = " + id);

        // 1: id로 데이터를 가져옴!
        Article articleEntity = articleRepository.findById(id).orElse(null);

        List<CommentDto> commentDtos = commentService.comments(id);

        // 2: 가져온 데이터를 모델에 등록!
        model.addAttribute("article", articleEntity);

        model.addAttribute("commentDtos", commentDtos);

        // 3: 보여줄 페이지를 설정!
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
        // 모든 아티클을 가져옴.
        List<Article> articleEntityList = articleRepository.findAll();

        // 가져온 article 묶음을 view로 전달
        model.addAttribute("articleList", articleEntityList);

        return "articles/index";
    }

    // redirect client에게 재요청하는 것

    @PostMapping("/articles/{id}/update")
    public String updateArticle(@PathVariable Long id, ArticleForm form) {
//      1. DTO를 엔티티로 변환
        Article article = form.toEntity( );

        Article deleteArticle = articleRepository.findById(id).orElse(null);

        if (deleteArticle != null) {
            Article saved = articleRepository.save(article);
//          logging으로 사용하여 정보 제대로 들어갔는지 확인.
            log.info(saved.toString());
//          redirect -> Client에게 다시 요청을 하도록 만듦. -> 다른 페이지로 이동하도록 함..
        }
        return "redirect:/articles/" + article.getId();

    }

    @GetMapping("/articles/{id}/edit")
    public String showEditArticle(@PathVariable Long id, Model model) {
        Article articleEntity = articleRepository.findById(id).orElse(null);
        if (articleEntity != null) {
            model.addAttribute("article", articleEntity);
            return "articles/edit";
        } else {
            return "redirect:/articles";
        }
    }

    @GetMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes rttr) {
        Article articleEntity = articleRepository.findById(id).orElse(null);

        if(articleEntity != null) {
            articleRepository.delete(articleEntity);
            rttr.addFlashAttribute("msg", "well deleted!");
        }

        return "redirect:/articles";
    }

}