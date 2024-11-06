INSERT INTO users(user_id, first_name, last_name, password, username, is_active) VALUES(1, 'John', 'Doe', '123456789', 'John.Doe', true);
INSERT INTO users(user_id, first_name, last_name, password, username, is_active) VALUES(2, 'John', 'Doe', '123456789', 'John.Doe2', true);
INSERT INTO users(user_id, first_name, last_name, password, username, is_active) VALUES(3, 'John', 'Doe', '123456789', 'John.Doe3', true);
INSERT INTO users(user_id, first_name, last_name, password, username, is_active) VALUES(4, 'John', 'Doe', '123456789', 'John.Doe4', true);

INSERT INTO trainers(user_id) VALUES(1);
INSERT INTO trainers(user_id) VALUES(2);
INSERT INTO trainees(user_id, date_of_birth, address) VALUES(3, '2000-06-06', 'Av Paz 22');
INSERT INTO trainees(user_id, date_of_birth, address) VALUES(4, '2000-06-06', 'Av Rev 34');

INSERT INTO training_types(id, training_type) VALUES(1, 'CARDIO');
INSERT INTO training_types(id, training_type) VALUES(2, 'YOGA');

INSERT INTO trainings(training_id, trainee_user_id, trainer_user_id, training_name, training_type_id, training_date, training_duration)
VALUES(1, 3, 1, 'Fit me', 1, '2024-06-06', 60);
INSERT INTO trainings(training_id, trainee_user_id, trainer_user_id, training_name, training_type_id, training_date, training_duration)
VALUES(2, 3, 2, 'Fit me 2.0', 1, '2024-06-07', 60);

INSERT INTO trainee_trainer(trainee_id, trainer_id) VALUES(3, 1);
INSERT INTO trainee_trainer(trainee_id, trainer_id) VALUES(3, 2);


