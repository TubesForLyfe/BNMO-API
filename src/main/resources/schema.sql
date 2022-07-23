CREATE TABLE users (
    nama VARCHAR(500) NOT NULL,
    username VARCHAR(500),
    password VARCHAR(500) NOT NULL,
    image VARCHAR(500) NOT NULL DEFAULT '-',
    role VARCHAR(500) NOT NULL DEFAULT 'customer',
    token VARCHAR(500) NOT NULL,
    verified VARCHAR(500) NOT NULL DEFAULT 'false',
    PRIMARY KEY (username)
);

INSERT INTO users(nama, username, password, role, token, verified) VALUES ('admin', 'admin', '$2a$10$nC9x.juZ85msD1B63gR0seYFGNV2vcwAvlgeY4BC8jy.ucuKOP0Bm', 'admin', 'adminmahbebas', 'true');
