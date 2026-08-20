package code.exam.repository;

import code.exam.db.DatabaseConnection;
import code.exam.model.Account;
import code.exam.model.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final DatabaseConnection databaseConnection;

    /**
     * Needed to validate that an account exists before:
     * - listing its transactions (GET /accounts/{id}/transactions)
     * - computing its balance (GET /account/{id}/balance)
     * - creating a transaction against it (POST /transaction)
     */
    public Optional<Account> findById(String id) throws SQLException {
        String sql = "SELECT id, account_type FROM accounts WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    private Account mapRow(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .id(resultSet.getString("id"))
                .accountType(AccountType.valueOf(resultSet.getString("account_type")))
                .build();
    }
}