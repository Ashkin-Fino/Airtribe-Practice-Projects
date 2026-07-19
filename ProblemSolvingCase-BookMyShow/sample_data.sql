-- Sample data for Theatre Table
INSERT INTO Theatre(theatre_id, name, location, address) VALUES
(1,'PVR Phoenix', 'Chennai', 'Velachery'),
(2,'INOX Marina', 'Chennai', 'Anna Salai');

-- Sample data for Screen Table
INSERT INTO Screen(screen_id, theatre_id, name, capacity) VALUES
(1, 1, 'Screen 1', 180),
(2, 1, 'Screen 2', 150),
(3, 2, 'Screen 1', 220);

-- Sample data for Movie Table
INSERT INTO Movie(movie_id, name, duration, language) VALUES
(1, 'Coolie', 170, 'Tamil'),
(2, 'Superman', 130, 'English'),
(3, 'War 2', 165, 'Hindi');

-- Sample data for Show Table
INSERT INTO Show (show_id, movie_id, screen_id, date, start_time) VALUES
(1, 1, 1, '2026-07-20', '09:30:00'),
(2, 2, 2, '2026-07-20', '11:00:00'),
(3, 1, 1, '2026-07-21', '09:30:00'),
(4, 3, 3, '2026-07-20', '10:00:00'),
(5, 2, 2, '2026-07-21', '09:30:00'),
(6, 1, 1, '2026-07-20', '13:00:00');
