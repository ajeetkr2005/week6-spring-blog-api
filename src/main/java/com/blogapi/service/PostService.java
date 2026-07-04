package com.blogapi.service;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.dto.CategoryResponseDto;
import com.blogapi.model.dto.PostRequestDto;
import com.blogapi.model.dto.PostResponseDto;
import com.blogapi.model.entity.Category;
import com.blogapi.model.entity.Post;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPublishedPosts(Pageable pageable) {
        return postRepository.findByPublished(true, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(categoryId, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long id) {
        return postRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public PostResponseDto createPost(PostRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDto.getCategoryId()));

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(requestDto.getAuthor())
                .category(category)
                .published(requestDto.isPublished())
                .build();

        return mapToResponse(postRepository.save(post));
    }

    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDto.getCategoryId()));

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setAuthor(requestDto.getAuthor());
        post.setCategory(category);
        post.setPublished(requestDto.isPublished());

        return mapToResponse(postRepository.save(post));
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    private PostResponseDto mapToResponse(Post post) {
        CategoryResponseDto categoryDto = CategoryResponseDto.builder()
                .id(post.getCategory() != null ? post.getCategory().getId() : null)
                .name(post.getCategory() != null ? post.getCategory().getName() : null)
                .description(post.getCategory() != null ? post.getCategory().getDescription() : null)
                .createdAt(post.getCategory() != null ? post.getCategory().getCreatedAt() : null)
                .updatedAt(post.getCategory() != null ? post.getCategory().getUpdatedAt() : null)
                .build();

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .category(categoryDto)
                .published(post.isPublished())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
