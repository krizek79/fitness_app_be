BEGIN;

-- =========================================================
-- TABLE
-- =========================================================

ALTER TABLE coach_client
    RENAME TO coaching_contract;

-- =========================================================
-- SEQUENCE
-- =========================================================

ALTER SEQUENCE IF EXISTS coach_client_id_seq
    RENAME TO coaching_contract_id_seq;

-- =========================================================
-- PRIMARY KEY CONSTRAINT
-- =========================================================

ALTER TABLE coaching_contract
    RENAME CONSTRAINT coach_client_pkey TO coaching_contract_pkey;

COMMIT;