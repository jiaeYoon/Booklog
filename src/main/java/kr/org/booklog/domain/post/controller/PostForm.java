package kr.org.booklog.domain.post.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter @Setter
public class PostForm {

    @NotEmpty(message = "제목은 필수 항목입니다.")
    private String postTitle;
    private String bookTitle;
    private String bookWriter;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate readStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate readEnd;

    private LocalDate postAt;
    private Integer rating;
    private String content;

}