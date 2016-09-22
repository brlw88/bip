drop table if exists accounts;

create table accounts
(
    recno bigint not null auto_increment,
    version int not null default 0,
    id varchar(25) not null,
    password char(60) not null,
    unique uq_id (id),
    primary key (recno)
);

drop table if exists redirections;

create table redirections
(
    recno bigint not null auto_increment,
    version int not null default 0,
    url varchar(1024) not null,
    shorturl varchar(6) not null,
    type int not null,
    acc_recno bigint not null,
    unique uq_shorturl (shorturl),
    primary key (recno)
);

alter table redirections
    add constraint fk_redirections_acc_recno
    foreign key (acc_recno) references accounts;

drop table if exists statistics;

create table statistics
(
    recno bigint not null auto_increment,
    version int not null default 0,
    rdr_recno bigint,
    numredirects bigint,
    primary key (recno),
    unique uq_rdr_recno (rdr_recno)
);

alter table statistics
    add constraint fk_statistics_rdr_recno
    foreign key (rdr_recno) references redirections;
