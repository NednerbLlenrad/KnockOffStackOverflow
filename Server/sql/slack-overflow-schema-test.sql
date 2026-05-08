drop database if exists slack_overflow_test;

create database slack_overflow_test;

use slack_overflow_test;

create table
    slack_user (
                   slack_user_id int primary key auto_increment,
                   username varchar(20) not null unique,
                   password_hash varchar(2048) not null,
                   email varchar(50) not null unique,
                   chill_points int not null default 0,
                   created_at timestamp not null default current_timestamp,
                   edited_at timestamp null
);

create table
    slack_question (
                       slack_question_id int primary key auto_increment,
                       title varchar(100) not null,
                       body text null,
                       slack_user_id int not null,
                       chill_points int not null default 0,
                       created_at timestamp not null default current_timestamp,
                       edited_at timestamp null,
                       constraint fk_slack_question_slack_user_id foreign key (slack_user_id) references slack_user (slack_user_id)
);

create table
    slack_answer (
                     slack_answer_id int primary key auto_increment,
                     body text not null,
                     slack_user_id int not null,
                     slack_question_id int not null,
                     chill_points int not null default 0,
                     created_at timestamp not null default current_timestamp,
                     edited_at timestamp null,
                     constraint fk_slack_answer_slack_user_id foreign key (slack_user_id) references slack_user (slack_user_id),
                     constraint fk_slack_answer_slack_question_id foreign key (slack_question_id) references slack_question (slack_question_id)
);

delimiter //

create procedure set_known_good_state()
begin

    set sql_safe_updates = 0;

    delete from slack_answer;
    alter table slack_answer auto_increment = 1;

    delete from slack_question;
    alter table slack_question auto_increment = 1;

    delete from slack_user;
    alter table slack_user auto_increment = 1;

    insert into slack_user
    (username, password_hash, email, chill_points, created_at, edited_at)
    values
        (
            'lazydev',
            '$2a$10$abcdefghijklmnopqrstuv',
            'lazydev@example.com',
            42,
            current_timestamp,
            null
        ),
        (
            'bugfarmer',
            '$2a$10$zyxwvutsrqponmlkjihgfe',
            'bugfarmer@example.com',
            13,
            current_timestamp,
            null
        ),
        (
            'nullpointer',
            '$2a$10$qwertyuiopasdfghjklzxc',
            'nullpointer@example.com',
            99,
            current_timestamp,
            null
        );

    insert into slack_question
    (title, body, slack_user_id, chill_points, created_at, edited_at)
    values
        (
            'How do I pretend to debug?',
            'I want my screen to look busy without accidentally fixing anything.',
            1,
            21,
            current_timestamp,
            null
        ),
        (
            'Best way to nap during standup?',
            'Camera is off but I need to survive the status update.',
            1,
            8,
            current_timestamp,
            null
        ),
        (
            'Can I blame cache for everything?',
            'It worked yesterday and I changed nothing, probably.',
            2,
            15,
            current_timestamp,
            null
        );

    set sql_safe_updates = 1;

end //

delimiter ;