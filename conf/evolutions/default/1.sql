# --- First database schema

# --- !Ups

create table users (
  id                        bigint not null,
  name                      varchar(255) not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_user primary key (id))
;

create table userlogins (
  id                        bigint not null,
  logindate                timestamp,
  user_id                bigint,
  constraint pk_userlogin primary key (id))
;

create sequence user_seq start with 1000;

create sequence userlogin_seq start with 1000;

alter table userlogins add constraint fk_users_logins_1 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_user_logins_1 on userlogins (user_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists users;

drop table if exists userlogins;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_seq;

drop sequence if exists userlogin_seq;