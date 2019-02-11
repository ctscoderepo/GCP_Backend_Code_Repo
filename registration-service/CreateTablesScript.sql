drop table backend.address;

drop table backend.user;


CREATE TABLE IF NOT EXISTS gcpbackend.address(
    id INT AUTO_INCREMENT,
    firstName VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL,
    email VARCHAR(60) NOT NULL,
    phoneNumber VARCHAR(60),
    dateOfBirth DATE,
    address1 VARCHAR(100),
    address2 VARCHAR(60),
    city VARCHAR(60),
    state VARCHAR(60),
    country VARCHAR(60),
    zipCode VARCHAR(60),
    status VARCHAR(60),
    addressType VARCHAR(60),  
    PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS gcpbackend.user(
    id INT AUTO_INCREMENT,
    logonid VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    create_at DATE,
    updated_at DATE,
    address_id INT,
    PRIMARY KEY (id),
    CONSTRAINT FK_UserAddress FOREIGN KEY (address_id)
    REFERENCES address(id)
);