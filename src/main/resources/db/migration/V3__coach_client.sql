CREATE TABLE coach_client (
    id BIGSERIAL PRIMARY KEY,
    coach_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    started_at TIMESTAMPTZ NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_coach FOREIGN KEY(coach_id) REFERENCES profile(id) ON DELETE CASCADE,
    CONSTRAINT fk_client FOREIGN KEY(client_id) REFERENCES profile(id) ON DELETE CASCADE,
    CONSTRAINT uq_coach_client UNIQUE(coach_id, client_id)
);
