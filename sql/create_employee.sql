USE moviedb;

-- Create employees table if it doesn't exist
CREATE TABLE IF NOT EXISTS employees (
    email VARCHAR(50) PRIMARY KEY,
    password VARCHAR(128) NOT NULL,
    fullname VARCHAR(100)
);

-- Since we can't easily encrypt the password here, we'll use a pre-encrypted version
-- This is an encrypted version of 'classta' generated with jasypt StrongPasswordEncryptor
INSERT INTO employees (email, password, fullname) VALUES 
('classta@email.edu', 'qdHElLBJU8Y6a6+gjM3iLJeHQiwqoWiu0zDK17nUJUeLuIxtQO+GVS2uWjZBdDxS', 'TA CS122B');