CREATE TABLE accounts(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT,
    branch VARCHAR(3) NOT NULL,
    number VARCHAR(6) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    account_type VARCHAR(31) NOT NULL,
    overdraft_limit DECIMAL(19, 2),
    interest_rate DECIMAL(5, 4),

    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT chk_account_logic CHECK (
        (account_type = 'CHECKING' AND interest_rate IS NULL AND balance >= -COALESCE(overdraft_limit, 0))
        OR
        (account_type = 'SAVINGS' AND interest_rate IS NOT NULL AND overdraft_limit IS NULL AND balance >= 0)
    )
);