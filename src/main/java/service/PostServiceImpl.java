package service;

import domain.Post;
import exception.PostNotFoundException;
import repository.PostRepository;
import repository.PostRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository = new PostRepositoryImpl();
    private volatile Long id_counter = 1L;

    @Override
    public int calculateNumberOfRepetitions(final String word, final Long postId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id = " + postId + " not found."));

        return post.getBody()
                .toLowerCase()
                .split(word)
                .length;
    }

    @Override
    public void saveAll(final List<Post> posts) {
        synchronized (Post.class) {
            List<Post> updatedPosts = posts.stream()
                    .peek(post -> {
                        post.setId(id_counter);
                        id_counter++;
                    })
                    .collect(Collectors.toList());
            postRepository.saveAll(updatedPosts);
        }
    }
}
