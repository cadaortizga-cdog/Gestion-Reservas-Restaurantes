CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    vip BOOLEAN DEFAULT FALSE NOT NULL,
    points INT DEFAULT 0 NOT NULL,
    loyalty_level VARCHAR(20) DEFAULT 'Bronce' NOT NULL
);

CREATE TABLE restaurant_table (
    id SERIAL PRIMARY KEY,
    num_table INT UNIQUE NOT NULL,
    capacity INT NOT NULL,
    table_status VARCHAR(30) DEFAULT 'disponible' NOT NULL,
    vip_exclusive BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE restaurant_schedule (
    id SERIAL PRIMARY KEY,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    day_of_week INT NOT NULL
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,           
    table_id INT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    num_people INT NOT NULL,
    status VARCHAR(30) DEFAULT 'pendiente' NOT NULL,
    confirmed_at TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES client(id),
    FOREIGN KEY (table_id) REFERENCES restaurant_table(id)
);

CREATE TABLE vip_waitlist (
    id SERIAL PRIMARY KEY,
    client_id INT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    num_people INT NOT NULL,
    list_status VARCHAR(30) DEFAULT 'en_espera' NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id)
);



ALTER TABLE client ALTER COLUMN id TYPE BIGINT;
ALTER TABLE restaurant_table ALTER COLUMN id TYPE BIGINT;
ALTER TABLE restaurant_schedule ALTER COLUMN id TYPE BIGINT;
ALTER TABLE reservation ALTER COLUMN id TYPE BIGINT;
ALTER TABLE vip_waitlist ALTER COLUMN id TYPE BIGINT;


ALTER SEQUENCE client_id_seq AS BIGINT;
ALTER SEQUENCE restaurant_table_id_seq AS BIGINT;
ALTER SEQUENCE restaurant_schedule_id_seq AS BIGINT;
ALTER SEQUENCE reservation_id_seq AS BIGINT;
ALTER SEQUENCE vip_waitlist_id_seq AS BIGINT;



ALTER TABLE reservation DROP CONSTRAINT reservation_table_id_fkey;
ALTER TABLE reservation ALTER COLUMN table_id TYPE BIGINT;
ALTER TABLE reservation ADD CONSTRAINT reservation_table_id_fkey
    FOREIGN KEY (table_id) REFERENCES restaurant_table(id);


ALTER TABLE reservation DROP CONSTRAINT reservation_cliente_id_fkey;
ALTER TABLE reservation ALTER COLUMN cliente_id TYPE BIGINT;
ALTER TABLE reservation ADD CONSTRAINT reservation_cliente_id_fkey
    FOREIGN KEY (cliente_id) REFERENCES client(id);


ALTER TABLE vip_waitlist DROP CONSTRAINT vip_waitlist_client_id_fkey;
ALTER TABLE vip_waitlist ALTER COLUMN client_id TYPE BIGINT;
ALTER TABLE vip_waitlist ADD CONSTRAINT vip_waitlist_client_id_fkey
    FOREIGN KEY (client_id) REFERENCES client(id);