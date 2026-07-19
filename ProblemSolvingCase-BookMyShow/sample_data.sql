-- Sample data for Theatre Table
INSERT INTO Theatre(theatre_name, city, address) VALUES
('PVR Phoenix', 'Chennai', 'Velachery'),
('INOX Marina', 'Chennai', 'Anna Salai');

-- Sample data for Screen Table
INSERT INTO Screen(theatre_id, screen_name, capacity) VALUES
(1,'Screen 1',180),
(1,'Screen 2',150),
(2,'Screen 1',220);

-- Sample data for Movie Table
INSERT INTO Movie(movie_name,duration_minutes,language,genre) VALUES
('Coolie',170,'Tamil','Action'),
('Superman',130,'English','Sci-Fi'),
('War 2',165,'Hindi','Action');

-- Sample data for Show Table
INSERT INTO ShowDetails (movie_id,screen_id,show_date,start_time,end_time) VALUES
(1,1,'2026-07-20','09:00:00','11:50:00'),
(2,2,'2026-07-20','10:00:00','12:10:00'),
(1,1,'2026-07-20','14:00:00','16:50:00'),
(3,3,'2026-07-20','18:00:00','20:45:00'),
(2,2,'2026-07-21','09:30:00','11:40:00');
