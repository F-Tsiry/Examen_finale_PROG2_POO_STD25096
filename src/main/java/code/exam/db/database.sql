
-- Accounts table
CREATE TABLE accounts (
                          id           VARCHAR(36) PRIMARY KEY,
                          account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('STANDARD', 'PREMIUM', 'GOLD'))
);

-- Transactions table
CREATE TABLE transactions (
                              id               VARCHAR(36) PRIMARY KEY,
                              account_id       VARCHAR(36) NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
                              created_at       TIMESTAMP NOT NULL,
                              transaction_type VARCHAR(10) NOT NULL CHECK (transaction_type IN ('IN', 'OUT')),
                              amount           NUMERIC(19, 2) NOT NULL CHECK (amount >= 0),
                              reason           VARCHAR(255)
);

CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_type ON transactions(transaction_type);