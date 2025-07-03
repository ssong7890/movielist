


-- USER
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(120) UNIQUE,
    nickname VARCHAR(60),
    password VARCHAR(60),
    comment_cnt INT DEFAULT 0,
    review_cnt INT DEFAULT 0
);

-- REVIEW
CREATE TABLE review (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    user_id BIGINT,
    content TEXT,
    like_cnt INT DEFAULT 0,
    dislike_cnt INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_review_movie_date (movie_id, created_at)
);

-- REVIEW VIEWS (조회 로그)
CREATE TABLE review_views (
    views_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT NOT NULL,
    user_id BIGINT,
    viewed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_review_user_date (review_id, user_id, DATE(viewed_at)),
    FOREIGN KEY (review_id) REFERENCES review(review_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_review_viewed_at (review_id, viewed_at)
);

-- REVIEW REACTIONS
CREATE TABLE review_reactions (
    review_reactions_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT,
    user_id BIGINT,
    reaction ENUM('like', 'dislike'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_review_user (review_id, user_id),
    FOREIGN KEY (review_id) REFERENCES review(review_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_review_reaction (review_id, reaction)
);

-- COMMENT
CREATE TABLE comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT,
    parent_id BIGINT,
    user_id BIGINT,
    content TEXT,
    like_cnt INT DEFAULT 0,
    dislike_cnt INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES review(review_id),
    FOREIGN KEY (parent_id) REFERENCES comment(comment_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_comment_review_parent_date (review_id, parent_id, created_at)
);

-- COMMENT REACTIONS
CREATE TABLE comment_reactions (
    comment_reactions_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT,
    user_id BIGINT,
    reaction ENUM('like', 'dislike'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_comment_user (comment_id, user_id),
    FOREIGN KEY (comment_id) REFERENCES comment(comment_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- ACTOR COMMENT
CREATE TABLE actor_comment (
    actor_comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    actor_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    actor_content TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- MOVIE VIEWS
CREATE TABLE movie_views (
    movie_views_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    user_id BIGINT,
    viewed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_movie_viewed_at (movie_id, viewed_at)
);

-- MOVIE VIEW DAILY
CREATE TABLE movie_view_daily (
    movie_id INT,
    view_date DATE,
    cnt INT,
    PRIMARY KEY (movie_id, view_date)
);

-- REVIEW REPORTS
CREATE TABLE review_reports (
    review_reports_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT,
    user_id BIGINT,
    reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_review_report (review_id, user_id),
    FOREIGN KEY (review_id) REFERENCES review(review_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_review_report (review_id, created_at)
);

-- COMMENT REPORTS
CREATE TABLE comment_reports (
    comment_reports_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT,
    user_id BIGINT,
    reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_comment_report (comment_id, user_id),
    FOREIGN KEY (comment_id) REFERENCES comment(comment_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_comment_report (comment_id, created_at)
);

-- MOVIE CACHE
CREATE TABLE movie_cache (
    movie_id INT PRIMARY KEY,
    title VARCHAR(255),
    poster_url TEXT,
    overview TEXT,
    release_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_movie_release (release_date)
);

-- USER FAVORITE MOVIES
CREATE TABLE user_favorite (
    user_favorite_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_user_movie (user_id, movie_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_user_favorite (user_id)
);



