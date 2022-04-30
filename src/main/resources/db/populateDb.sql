INSERT INTO users(email, password) VALUES ('user@yandex.ru', 'password'),
                                          ('admin@gmail.com', 'admin');

INSERT INTO words(user_id, word, translation) VALUES (1, 'apple', 'яблоко'),
                                                     (1, 'orange', 'апельсин'),
                                                     (1, 'pear', 'груша'),
                                                     (2, 'earth', 'земля'),
                                                     (2, 'pluto', 'плутон'),
                                                     (2, 'mars', 'марс');

INSERT INTO wordsets (user_id, name) VALUES (1, 'fruits'),
                                            (2, 'planets');

INSERT INTO wordset_word(word_id, wordset_id) VALUES (1, 1),
                                                     (2, 1),
                                                     (3, 1),
                                                     (4, 2),
                                                     (5, 2),
                                                     (6, 2);

INSERT INTO trainings (name) VALUES ('rus-eng'),
                                    ('eng-rus'),
                                    ('writing'),
                                    ('know-dunno');

INSERT INTO user_word_training(user_id, word_id, training_id) VALUES (1, 1, 1),
                                                                     (1, 1, 2),
                                                                     (1, 1, 3),
                                                                     (1, 1, 4),

                                                                     (1, 2, 1),
                                                                     (1, 2, 2),
                                                                     (1, 2, 3),
                                                                     (1, 2, 4),

                                                                     (1, 3, 1),
                                                                     (1, 3, 2),
                                                                     (1, 3, 3),
                                                                     (1, 3, 4);

INSERT INTO random_words (word) VALUES ('public'), ('private'), ('protected'), ('package-private'),
                                       ('class'), ('method'), ('argument'), ('value'),
                                       ('creation'), ('planet'), ('problem'), ('local'),
                                       ('random'), ('library'), ('population'), ('ant'),
                                       ('maven'), ('gradle'), ('tomcat'), ('jackson'),
                                       ('spring'), ('summer'), ('autumn'), ('winter'),
                                       ('scratches'), ('ignore'), ('git'), ('security'),
                                       ('mvc'), ('table'), ('word'), ('time'),
                                       ('date'), ('hour'), ('minute'), ('second'),
                                       ('first'), ('third'), ('fourth'), ('fifth');

INSERT INTO random_translations (translation) VALUES ('общедоступный'), ('частный'), ('защищенный'), ('пакет-частный'),
                                                     ('класс'), ('метод'), ('аргумент'), ('значение'),
                                                     ('творение'), ('планета'), ('проблема'), ('местный'),
                                                     ('случайный'), ('библиотека'), ('население'), ('муравей'),
                                                     ('maven'), ('gradle'), ('tomcat'), ('jackson'),
                                                     ('весна'), ('лето'), ('осень'), ('зима'),
                                                     ('царапины'), ('игнорировать'), ('git'), ('безопасность'),
                                                     ('mvc'), ('таблица'), ('слово'), ('время'),
                                                     ('дата'), ('час'), ('минута'), ('секунда'),
                                                     ('первый'), ('третий'), ('четвертый'), ('пятый');


