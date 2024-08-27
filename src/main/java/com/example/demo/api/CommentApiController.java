package com.example.demo.api;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {
    @Autowired
    private CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {
        List<CommentDto> dtos = commentService.comments(articleId);

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 댓글 생성
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId, @RequestBody CommentDto dto) {
        CommentDto commentDto = commentService.create(articleId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

    // 댓글 수정
    @PatchMapping("api/comments/{articleId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long articleId, @RequestBody CommentDto dto) {
         CommentDto commentDto = commentService.update(articleId, dto);

         return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id) {
        CommentDto commentDto = commentService.delete(id);

        return null;
    }

}
