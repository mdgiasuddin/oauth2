create table refresh_token
(
    id           uuid        not null primary key,
    rotation_key varchar(10) not null,
    expiry_date  timestamp   not null,
    user_id      bigint      not null
        constraint fk__refresh_token_user references _user
);