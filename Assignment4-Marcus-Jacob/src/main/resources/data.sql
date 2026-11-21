-- Passwords for both accounts below: 1234
-- Admin account: student1@sheridancollege.ca / 1234
-- User account:  student2@sheridancollege.ca / 1234

INSERT INTO books (title, author)
VALUES ('The 7 Habits of Highly Effective People', 'Stephen R. Covey');

INSERT INTO books (title, author)
VALUES ('The Martian', 'Andy Weir');

INSERT INTO reviews (text, bookId)
VALUES ('An older book, but still a very good read for principle-centered leadership.', 1);

INSERT INTO reviews (text, bookId)
VALUES ('A great science fiction book about an astronaut stranded on Mars', 2);

-- BCrypt hash for '1234': $2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy
INSERT INTO users (email, encryptedpassword, enabled)
VALUES ('student1@sheridancollege.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', TRUE);

INSERT INTO users (email, encryptedpassword, enabled)
VALUES ('student2@sheridancollege.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', TRUE);

INSERT INTO roles (rolename) VALUES ('ROLE_ADMIN');
INSERT INTO roles (rolename) VALUES ('ROLE_USER');

INSERT INTO user_role (userid, roleid) VALUES (1, 1);
INSERT INTO user_role (userid, roleid) VALUES (1, 2);
INSERT INTO user_role (userid, roleid) VALUES (2, 2);