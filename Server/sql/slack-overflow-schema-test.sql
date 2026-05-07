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