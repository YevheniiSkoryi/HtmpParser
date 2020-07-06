package repository;

import domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository{

      Optional<Post> findById(final String postId);

      void saveAll(final List<Post> posts);
}
