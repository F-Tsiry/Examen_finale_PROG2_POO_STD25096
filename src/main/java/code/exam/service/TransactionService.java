package code.exam.service;

import code.exam.dto.CreateTransactionRequest;
import code.exam.exception.DatabaseException;
import code.exam.model.Transaction;
import code.exam.model.TransactionType;
import code.exam.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    /**
     * GET /transactions?type=in|out
     * type == null -> returns all transactions.
     */
    public List<Transaction> getTransactions(TransactionType type) {
        try {
            return transactionRepository.findAll(type);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch transactions", e);
        }
    }

    /**
     * GET /accounts/{id}/transactions
     */
    public List<Transaction> getTransactionsByAccountId(String accountId) {
        // Ensures a 404 is raised if the account doesn't exist, instead of silently returning []
        accountService.getAccountOrThrow(accountId);

        try {
            return transactionRepository.findByAccountId(accountId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch transactions for account " + accountId, e);
        }
    }

    /**
     * GET /account/{id}/balance
     */
    public BigDecimal getBalance(String accountId) {
        accountService.getAccountOrThrow(accountId);

        try {
            return transactionRepository.getBalanceByAccountId(accountId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to compute balance for account " + accountId, e);
        }
    }

    /**
     * POST /transaction
     * The service generates the ID (UUID) and the creation timestamp,
     * as per the "no DB-generated IDs" rule.
     */
    public Transaction createTransaction(CreateTransactionRequest request) {
        if (request.getAccountId() == null || request.getAccountId().isBlank()) {
            throw new IllegalArgumentException("accountId is required");
        }
        if (request.getTransactionType() == null) {
            throw new IllegalArgumentException("transactionType is required");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must be a positive value");
        }

        // Validates the account exists before inserting the transaction
        accountService.getAccountOrThrow(request.getAccountId());

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId(request.getAccountId())
                .createdAt(Instant.now())
                .transactionType(request.getTransactionType())
                .amount(request.getAmount())
                .reason(request.getReason())
                .build();

        try {
            return transactionRepository.save(transaction);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save transaction", e);
        }
    }
}