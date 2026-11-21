package ca.sheridan.jacob.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridan.jacob.beans.Book;
import ca.sheridan.jacob.beans.Review;
import ca.sheridan.jacob.beans.User;

@Repository
public class DatabaseAccess {

    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- User & Role Methods ---

    public User findUserAccount(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT * FROM users WHERE email = :email";
        parameters.addValue("email", email);

        try {
            return jdbc.queryForObject(query, parameters, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<String> getRolesById(Long userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT roles.rolename FROM user_role, roles "
                + "WHERE user_role.roleid = roles.roleid "
                + "AND user_role.userid = :userId";
        parameters.addValue("userId", userId);

        List<String> roles = new ArrayList<>();
        List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);

        for (Map<String, Object> row : rows) {
            roles.add((String) row.get("rolename"));
        }
        return roles;
    }

    public void addUser(String email, String password) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "INSERT INTO users (email, encryptedpassword, enabled) "
                + "VALUES (:email, :encryptedPassword, 1)";
        parameters.addValue("email", email);
        parameters.addValue("encryptedPassword", passwordEncoder.encode(password));
        jdbc.update(query, parameters);
    }

    public void addRole(Long userId, Long roleId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "INSERT INTO user_role (userid, roleid) VALUES (:userId, :roleId)";
        parameters.addValue("userId", userId);
        parameters.addValue("roleId", roleId);
        jdbc.update(query, parameters);
    }

    // --- Book & Review Methods ---

    public List<Book> getBooks() {
        String query = "SELECT * FROM books";
        return jdbc.query(query, new MapSqlParameterSource(), new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBookById(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books WHERE id = :id";
        parameters.addValue("id", id);

        try {
            Book book = jdbc.queryForObject(query, parameters, new BeanPropertyRowMapper<>(Book.class));
            if (book != null) {
                book.setReviews(getReviewsByBookId(id));
            }
            return book;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Review> getReviewsByBookId(Long bookId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT * FROM reviews WHERE bookId = :bookId";
        parameters.addValue("bookId", bookId);
        return jdbc.query(query, parameters, new BeanPropertyRowMapper<>(Review.class));
    }

    public void addBook(String title, String author) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "INSERT INTO books (title, author) VALUES (:title, :author)";
        parameters.addValue("title", title);
        parameters.addValue("author", author);
        jdbc.update(query, parameters);
    }

    public void addReview(Long bookId, String text) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "INSERT INTO reviews (bookId, text) VALUES (:bookId, :text)";
        parameters.addValue("bookId", bookId);
        parameters.addValue("text", text);
        jdbc.update(query, parameters);
    }
}
