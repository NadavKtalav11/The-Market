-- auto-generated definition
create table external_supply_service
(
    supply_url varchar(255) not null
        primary key
);

-- auto-generated definition
create table shipping_dto
(
    shipping_id                 varchar(255) not null
        primary key,
    acquisition_id              varchar(255) not null,
    address                     varchar(255) not null,
    city                        varchar(255) not null,
    country                     varchar(255) not null,
    date                        datetime(6)  not null,
    transaction_id              int          not null,
    user_name                   varchar(255) not null,
    zip                         varchar(255) null,
    external_supply_service_url varchar(255) null,
    constraint FK8i5eoxkvyy28qw36n5thlkt6u
        foreign key (external_supply_service_url) references external_supply_service (supply_url)
);

