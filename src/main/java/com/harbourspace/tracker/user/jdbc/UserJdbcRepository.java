package com.harbourspace.tracker.user.jdbc;

import com.harbourspace.tracker.user.model.NewUser;
import com.harbourspace.tracker.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This is an example of repository implementation using JdbcTemplate.
 */
@Repository
public class UserJdbcRepository {

    private Logger logger = LoggerFactory.getLogger(UserJdbcRepository.class);

    protected final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> selectAll() {
        logger.debug("Selecting all users");
        return jdbcTemplate.query("SELECT * FROM users", (resultSet, index) -> new User(
                resultSet.getLong("id"),
                resultSet.getString("name")
        ));
    }

    public User selectById(Long id) {
        logger.debug("Selecting user " + id);
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ? LIMIT 1", this::rowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public User selectByName(String name) {
        logger.debug("Selecting user " + name);
        return jdbcTemplate.query("SELECT * FROM users WHERE name = ? LIMIT 1", this::rowMapper, name)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public User insert(NewUser user) {
        logger.debug("Inserting new user: " + user);
        return jdbcTemplate.query(
                "SELECT * FROM FINAL TABLE (INSERT INTO users (name) VALUES (?))",
                this::rowMapper,
                user.name()
        ).stream().findFirst().orElse(null);
    }

    public User update(User user) {
        logger.debug("Updating user: " + user);
        return jdbcTemplate.query(
                "SELECT * FROM FINAL TABLE (UPDATE users SET name = ? WHERE id = ?)",
                this::rowMapper,
                user.name(),
                user.id()
        ).stream().findFirst().orElse(null);
    }

    public void delete(Long id) {
        logger.debug("Deleting user " + id);
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    private User rowMapper(ResultSet rs, int i) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
