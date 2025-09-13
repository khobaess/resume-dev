CREATE TABLE users (
    id BIGINT UNIQUE NOT NULL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    city VARCHAR(20),
    level int,
    description TEXT,
    birth_date DATE,
    job_title VARCHAR(100)

);

CREATE TABLE achievements(
    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    date_start DATE,
    date_end DATE NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE awards(
    id  BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL
);

CREATE TABLE resumes(
    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    achievement_id BIGINT NOT NULL REFERENCES achievements(id) ON DELETE CASCADE
);

CREATE INDEX idx_users_user_id ON users(id);
CREATE INDEX idx_achievements_user_id ON achievements(user_id);
CREATE INDEX idx_awards_user_id ON awards(user_id);
