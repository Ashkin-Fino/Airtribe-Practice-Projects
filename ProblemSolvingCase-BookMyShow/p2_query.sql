SELECT t.name, sc.name, m.name, sh.date, sh.start_time
FROM Show sh
JOIN Movie m
    ON sh.movie_id = m.movie_id
JOIN Screen s
    ON sh.screen_id = sc.screen_id
JOIN Theatre t
    ON sc.theatre_id = t.theatre_id
WHERE t.name = 'PVR Phoenix' AND sh.date = '2026-07-20';