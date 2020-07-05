package service;

import domain.Post;

import java.util.List;

public interface PostService {

    int calculateNumberOfRepetitions(final String word, final Long postId);

    void saveAll(final List<Post> posts);
}
