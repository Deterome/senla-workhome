CREATE TABLE users (
     user_id SERIAL PRIMARY KEY,
     username VARCHAR(255),
     hashed_password VARCHAR(255)
);