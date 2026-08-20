package code.exam.service;

import code.exam.exception.AccountNotFoundException;
import code.exam.exception.DatabaseException;
import code.exam.model.Account;
import code.exam.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Throws AccountNotFoundException if the account does not exist.
     * Used by other services/controllers to validate an accountId before proceeding.
     */
    public Account getAccountOrThrow(String accountId) {
        try {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(accountId));
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch account " + accountId, e);
        }
    }
}