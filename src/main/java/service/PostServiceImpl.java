package service;

import domain.Post;
import exception.PostNotFoundException;
import repository.PostRepository;
import repository.PostgresPostRepositoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository = new PostgresPostRepositoryImpl();

    @Override
    public int calculateNumberOfRepetitions(final String word, final String postId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id = " + postId + " not found."));

        int count = 0;
        Pattern pattern = Pattern.compile(word.toLowerCase(), Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(post.getBody().toLowerCase());
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    @Override
    public synchronized void  saveAll(final List<Post> posts) {
        List<Post> updatedPosts = posts.stream()
                .peek(post -> {
                    post.setId(UUID.randomUUID().toString());
                })
                .collect(Collectors.toList());
        postRepository.saveAll(updatedPosts);

    }
}
