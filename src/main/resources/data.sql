INSERT INTO users(first_name, last_name, password, username, is_active) VALUES('John', 'Trainer', '123456789', 'John.Trainer', true);
INSERT INTO users(first_name, last_name, password, username, is_active) VALUES('John', 'Trainer', '123456789', 'John.Trainer2', true);
INSERT INTO users(first_name, last_name, password, username, is_active) VALUES('John', 'Trainee', '123456789', 'John.Trainee', true);
INSERT INTO users(first_name, last_name, password, username, is_active) VALUES('John', 'Trainee', '123456789', 'John.Trainee2', true);

INSERT INTO training_types( training_type) VALUES( 'CARDIO');
INSERT INTO training_types( training_type) VALUES( 'YOGA');

INSERT INTO trainers(user_id, training_type_id) VALUES(1, 1);
INSERT INTO trainers(user_id, training_type_id) VALUES(2, 2);

INSERT INTO trainees(user_id, date_of_birth, address) VALUES(3, '2000-06-06', 'Av Paz 22');
INSERT INTO trainees(user_id, date_of_birth, address) VALUES(4, '2000-06-06', 'Av Rev 34');



INSERT INTO trainings(training_id, trainee_user_id, trainer_user_id, training_name, training_type_id, training_date, training_duration)
VALUES(1, 3, 1, 'Fit me', 1, '2024-06-06', 60);
INSERT INTO trainings(training_id, trainee_user_id, trainer_user_id, training_name, training_type_id, training_date, training_duration)
VALUES(2, 3, 2, 'Fit me 2.0', 1, '2024-06-07', 60);

INSERT INTO trainee_trainer(trainee_id, trainer_id) VALUES(3,1);
INSERT INTO trainee_trainer(trainee_id, trainer_id) VALUES(3,2);


