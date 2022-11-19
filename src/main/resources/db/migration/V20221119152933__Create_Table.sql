CREATE TABLE payments (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 value decimal(19,2) NOT NULL,
 name varchar(100) DEFAULT NULL,
 number varchar(19) DEFAULT NULL,
 expiration_date varchar(7) DEFAULT NULL,
 cvv varchar(3) DEFAULT NULL,
 status varchar(255) NOT NULL,
 payment_type_id bigint(20) NOT NULL,
 order_id bigint(20) NOT NULL,
PRIMARY KEY (id)
);