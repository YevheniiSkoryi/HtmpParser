package repository;

import domain.Post;
import exception.ErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgresPostRepositoryImpl extends PostgresRepository implements PostRepository {

    private static final String TABLE_NAME = "posts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_BODY = "body";

    private static final String FIND_BY_SQL = String.format("SELECT %s,%s,%s FROM %s where id=?;", COLUMN_ID, COLUMN_TITLE, COLUMN_BODY, TABLE_NAME);
    private static final String SAVE_SQL = String.format("INSERT INTO %s(%s,%s,%s) VALUES (?, ?, ?);", TABLE_NAME, COLUMN_ID, COLUMN_TITLE, COLUMN_BODY);

    @Override
    public Optional<Post> findById(final String postId) {
        try (Connection connection = openConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_SQL);

            preparedStatement.setString(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();
            connection.commit();
            if (resultSet.next()) {
                return Optional.of(
                        new Post(
                                resultSet.getString(COLUMN_ID),
                                resultSet.getString(COLUMN_TITLE),
                                resultSet.getString(COLUMN_BODY)
                        )
                );

            } else {
                return Optional.empty();
            }

        } catch (SQLException expected) {
            throw new ErrorException("Connection problem.", expected);
        }
    }

    @Override
    public void saveAll(final List<Post> posts) {
        try (Connection connection = openConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL);

            for (Post post : posts) {
                preparedStatement.setString(1, post.getId());
                preparedStatement.setString(2, post.getTitle());
                preparedStatement.setString(3, post.getBody());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException expected) {
            throw new ErrorException("Connection problem.", expected);
        }
    }


}
