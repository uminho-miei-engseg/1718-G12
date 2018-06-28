create database auctions;
use auctions;
create user 'auctions_admin'@'localhost' identified by 'passw713';
grant all privileges on auctions.* to 'auctions_admin'@'localhost';


create table user(
    id int auto_increment primary key,
    public_key text,
    random_number bigint
);

create table shamir(
	id int auto_increment primary key,
    part_shamir_key text,
    secret_share_number int,
    prime text,
    quorum int,
    invert boolean,
    shamir_number int
);

create table auction(
    id int auto_increment primary key,
    base_price float,
    description text,
    public_key text,
    private_key text,
    begin_d datetime default CURRENT_TIMESTAMP,
    end_d datetime,
    part_shamir_key int,
    seller int,
    foreign key(seller) references user(id),
    foreign key(part_shamir_key) references shamir(id)
);


create table bids(
    id int auto_increment primary key,
    user_id int,
    auction_id int,
    sealed_offer blob,
    foreign key(user_id) references user(id),
    foreign key(auction_id) references auction(id)
);

SELECT * FROM auctions.auction WHERE end_d > CURRENT_TIMESTAMP;
select * from auction;
select * from shamir;
select * from bids;
drop database auctions;
