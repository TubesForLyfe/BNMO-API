CREATE TABLE users (
    nama VARCHAR(500) NOT NULL,
    username VARCHAR(500),
    password VARCHAR(500) NOT NULL,
    image VARCHAR(500) NOT NULL DEFAULT '-',
    role VARCHAR(500) NOT NULL DEFAULT 'customer',
    token VARCHAR(500) NOT NULL,
    verified VARCHAR(500) NOT NULL DEFAULT 'false',
    saldo FLOAT NOT NULL DEFAULT 0.00,
    PRIMARY KEY (username)
);

INSERT INTO users(nama, username, password, role, token, verified) VALUES ('admin', 'admin', '$2a$10$nC9x.juZ85msD1B63gR0seYFGNV2vcwAvlgeY4BC8jy.ucuKOP0Bm', 'admin', 'adminmahbebas', 'true');

CREATE TABLE saldo_requests (
    username VARCHAR(500) NOT NULL,
    type VARCHAR(500) NOT NULL,
    jumlah FLOAT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(500) NOT NULL DEFAULT '-',
    verified_at TIMESTAMP,
    PRIMARY KEY (username, created_at),
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE TABLE transaction (
    from_username VARCHAR(500) NOT NULL,
    to_username VARCHAR(500) NOT NULL,
    jumlah FLOAT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (from_username, to_username, created_at),
    FOREIGN KEY (from_username) REFERENCES users (username),
    FOREIGN KEY (to_username) REFERENCES users (username)
);