CREATE TABLE week (
    id BIGSERIAL PRIMARY KEY,
    cycle_id BIGINT,
    order_number INTEGER NOT NULL CHECK (order_number >= 0),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    note VARCHAR(1024),
    CONSTRAINT fk_cycle FOREIGN KEY(cycle_id) REFERENCES cycle(id) ON DELETE CASCADE
);
