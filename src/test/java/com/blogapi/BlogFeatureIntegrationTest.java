package com.blogapi;

import com.blogapi.model.dto.*;
import com.blogapi.service.CategoryService;
import com.blogapi.service.CommentService;
import com.blogapi.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BlogFeatureIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Test
    void shouldCreateCategoryPostAndComment() {
        CategoryRequestDto categoryRequest = CategoryRequestDto.builder()
                .name("Integration Category")
                .description("Tech articles")
                .build();

        CategoryResponseDto savedCategory = categoryService.createCategory(categoryRequest);
        assertThat(savedCategory.getId()).isNotNull();

        PostRequestDto postRequest = PostRequestDto.builder()
                .title("Spring Boot Tips")
                .content("A great guide")
                .author("Alice")
                .categoryId(savedCategory.getId())
                .published(true)
                .build();

        PostResponseDto savedPost = postService.createPost(postRequest);
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getCategory().getId()).isEqualTo(savedCategory.getId());

        CommentRequestDto commentRequest = CommentRequestDto.builder()
                .content("Very helpful")
                .author("Bob")
                .postId(savedPost.getId())
                .build();

        CommentResponseDto savedComment = commentService.createComment(commentRequest);
        assertThat(savedComment.getId()).isNotNull();

        Page<PostResponseDto> page = postService.getAllPosts(PageRequest.of(0, 10, Sort.by("createdAt").descending()));
        assertThat(page.getContent()).isNotEmpty();
    }
}
