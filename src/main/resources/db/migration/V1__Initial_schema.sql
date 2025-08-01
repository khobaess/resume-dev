CREATE TABLE users (
    vk_id BIGINT UNIQUE NOT NULL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    city VARCHAR(20),
    level int

);


CREATE TABLE achievements(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL REFERENCES users(vk_id) ON DELETE CASCADE
);

CREATE TABLE awards(
    id  BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(vk_id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL
);

CREATE INDEX idx_users_vk_id ON users(vk_id);
CREATE INDEX idx_achievements_user_id ON achievements(user_id);
CREATE INDEX idx_awards_user_id ON awards(user_id);