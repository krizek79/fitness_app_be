CREATE TABLE week (
    id SERIAL PRIMARY KEY,
    cycle_id INTEGER NOT NULL REFERENCES cycle (id) ON DELETE CASCADE,
    order_number INTEGER NOT NULL CHECK (order_number >= 0),
    completed BOOLEAN NOT NULL DEFAULT FALSE
);