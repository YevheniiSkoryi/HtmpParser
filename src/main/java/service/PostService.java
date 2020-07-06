package service;

import domain.Post;

import java.util.List;

public interface PostService {

    int calculateNumberOfRepetitions(final String word, final String postId);

    void saveAll(final List<Post> posts);
}
