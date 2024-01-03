CREATE TABLE IF NOT EXISTS user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      password VARCHAR(255),
                      username VARCHAR(255),
                      PRIMARY KEY (id),
                      UNIQUE KEY (username)
) ENGINE=InnoDB;
