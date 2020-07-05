package repository;

import domain.Post;

import java.util.List;
import java.util.Optional;

public abstract class PostRepository extends AbstractRepository {

    public abstract Optional<Post> findById(final Long postId);

    public abstract void saveAll(final List<Post> posts);
}
