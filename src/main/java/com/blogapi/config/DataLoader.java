package com.blogapi.config;

import com.blogapi.model.entity.Category;
import com.blogapi.model.entity.Comment;
import com.blogapi.model.entity.Post;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.repository.CommentRepository;
import com.blogapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (categoryRepository.count() > 0) {
            log.info("Sample data already exists. Skipping DataLoader.");
            return;
        }

        Category tech = categoryRepository.save(Category.builder()
                .name("Technology")
                .description("Articles about software and tech")
                .build());

        Category lifestyle = categoryRepository.save(Category.builder()
                .name("Lifestyle")
                .description("Personal growth and everyday tips")
                .build());

        Post postOne = postRepository.save(Post.builder()
                .title("Getting Started with Spring Boot")
                .content("Spring Boot simplifies creating production-ready applications.")
                .author("Asha")
                .category(tech)
                .published(true)
                .build());

        Post postTwo = postRepository.save(Post.builder()
                .title("Daily Habits for Better Focus")
                .content("Small routines can make a big difference in productivity.")
                .author("Ravi")
                .category(lifestyle)
                .published(true)
                .build());

        commentRepository.save(Comment.builder()
                .content("Very helpful article!")
                .author("Mina")
                .post(postOne)
                .build());

        commentRepository.save(Comment.builder()
                .content("I loved the tips in this post.")
                .author("Leo")
                .post(postTwo)
                .build());

        log.info("Loaded sample categories, posts, and comments for development profile.");
    }
}
