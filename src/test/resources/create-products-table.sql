CREATE TABLE products
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    entryDate    VARCHAR(255),
    itemCode     VARCHAR(255),
    itemName     VARCHAR(255),
    itemQuantity VARCHAR(255),
    status       VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;