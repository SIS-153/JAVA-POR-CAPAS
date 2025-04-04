-- data.sql
INSERT INTO role (id, name) VALUES (0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO role (id, name) VALUES (1, 1) ON CONFLICT (id) DO NOTHING;