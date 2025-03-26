INSERT INTO app_db.user_entity (file_seq, user_seq, passwd, ci_key, email, nick_name, join_type, user_role) VALUES (0, 1, '11111111', 'iXRr94m5pIzb-L5DLAiCboQ7OB75w2nN7h6fV8wcCBs', 'backj123@naver.com', 'user01', 'HOMEPAGE', 'User');
INSERT INTO app_db.user_entity (file_seq, user_seq, passwd, ci_key, email, nick_name, join_type, user_role) VALUES (0, 2, '11111111', 'iXRr94m5pIzb-L5DLAiCboQ7OB75w2nN7h6fV8wcCBs', 'backj123@naver.com', 'user02', 'HOMEPAGE', 'User');
INSERT INTO app_db.user_entity (file_seq, user_seq, passwd, ci_key, email, nick_name, join_type, user_role) VALUES (0, 3, '111111111', 'iXRr94m5pIzb-L5DLAiCboQ7OB75w2nN7h6fV8wcCBs', 'backj123@naver.com', 'user02', 'HOMEPAGE', 'User');

INSERT INTO app_db.user_profile_entity (file_seq, user_profile_seq, user_seq, introduction) VALUES (0, 1, 1, '');
INSERT INTO app_db.user_profile_entity (file_seq, user_profile_seq, user_seq, introduction) VALUES (0, 2, 2, '');
INSERT INTO app_db.user_profile_entity (file_seq, user_profile_seq, user_seq, introduction) VALUES (0, 3, 3, '');

INSERT INTO app_db.user_setting_entity (user_seq, user_setting_seq, event_enabled, notification_enabled, theme_preference) VALUES (1, 1, 'OFF', 'OFF', 'LIGTH');
INSERT INTO app_db.user_setting_entity (user_seq, user_setting_seq, event_enabled, notification_enabled, theme_preference) VALUES (2, 2, 'OFF', 'OFF', 'LIGTH');
INSERT INTO app_db.user_setting_entity (user_seq, user_setting_seq, event_enabled, notification_enabled, theme_preference) VALUES (3, 3, 'OFF', 'OFF', 'LIGTH');

INSERT INTO app_db.user_token_entity (expired_dt, reg_dt, user_seq, user_token_seq, refresh_token) VALUES ('2025-03-25 22:02:00.000000', '2025-03-25 21:02:00.272369', 1, 1, 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyU2VxIjoxLCJlbWFpbCI6ImJhY2tqMTIzQG5hdmVyLmNvbSIsImlhdCI6MTc0MjkwNDEyMCwiZXhwIjoxNzQyOTA3NzIwfQ.QcJc3H3yy6daUjXnzp78UN6MvdvaQLsfJppqvOfXVEQ');
INSERT INTO app_db.user_token_entity (expired_dt, reg_dt, user_seq, user_token_seq, refresh_token) VALUES ('2025-03-25 22:04:43.000000', '2025-03-25 21:04:43.442808', 2, 2, 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyU2VxIjoyLCJlbWFpbCI6ImJhY2tqMTIzQG5hdmVyLmNvbSIsImlhdCI6MTc0MjkwNDI4MywiZXhwIjoxNzQyOTA3ODgzfQ.6zRhJw62SncS00w502ORlyeQjduauLqArmlSVQbgaqc');
INSERT INTO app_db.user_token_entity (expired_dt, reg_dt, user_seq, user_token_seq, refresh_token) VALUES ('2025-03-25 22:07:04.000000', '2025-03-25 21:07:04.707335', 3, 3, 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyU2VxIjozLCJlbWFpbCI6ImJhY2tqMTIzQG5hdmVyLmNvbSIsImlhdCI6MTc0MjkwNDQyNCwiZXhwIjoxNzQyOTA4MDI0fQ.nmOFb5PKjVpwwhJQ18Qq7KMoUiidl5z68KL0GjvJPBI');
