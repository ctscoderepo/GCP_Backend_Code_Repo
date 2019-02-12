drop table gcpdemo.address;

drop table gcpdemo.user;

CREATE TABLE IF NOT EXISTS gcpdemo.address(
    id INT AUTO_INCREMENT,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    email VARCHAR(60) NOT NULL,
    phone_number VARCHAR(60),
    date_of_birth DATE,
    address1 VARCHAR(100),
    address2 VARCHAR(60),
    city VARCHAR(60),
    state VARCHAR(60),
    country VARCHAR(60),
    zip_code VARCHAR(60),
    status VARCHAR(60),
    address_type VARCHAR(60),  
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS gcpdemo.user(
    id INT AUTO_INCREMENT,
    logon_id VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    create_at DATE,
    updated_at DATE,
    address_id INT,
    PRIMARY KEY (id),
    CONSTRAINT FK_UserAddress FOREIGN KEY (address_id)
    REFERENCES address(id)
);
commit;