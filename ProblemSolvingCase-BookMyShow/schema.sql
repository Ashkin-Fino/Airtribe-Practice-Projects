-- Schema for Theatre Table
CREATE TABLE Theatre (
    theatre_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(50) NOT NULL,
    address VARCHAR(200)
);

-- Schema for Screen Table
CREATE TABLE Screen (
    screen_id INT AUTO_INCREMENT PRIMARY KEY,
    theatre_id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    capacity INT NOT NULL,

    FOREIGN KEY(theatre_id) REFERENCES Theatre(theatre_id)
);

-- Schema for Movie Table
CREATE TABLE Movie (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    language VARCHAR(30)
);

-- Schema for Show Table
CREATE TABLE Show (
    show_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    screen_id INT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,

    FOREIGN KEY(movie_id) REFERENCES Movie(movie_id),
    FOREIGN KEY(screen_id) REFERENCES Screen(screen_id)
);
