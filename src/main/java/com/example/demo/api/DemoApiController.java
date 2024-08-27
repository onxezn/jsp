package com.example.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // view 템플릿이 아닌 JSON을 반환하는 Controller
// 일반 컨트롤러와의 차이는? -> GetMapping은 일반 컨트롤러에서는 뷰 템플릿을 반환한다.
// Rest 컨트롤러는 데이터(JSON 형태)를 반환함.
public class DemoApiController {
    @GetMapping("/api/hello")
    public String hello() {
        return "hello world";
    }


}
