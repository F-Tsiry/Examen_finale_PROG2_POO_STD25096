package code.exam.dto;

import code.exam.model.Transaction;
import code.exam.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String accountId;
    private Instant createdAt;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String reason;

    public static TransactionResponse fromEntity(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .createdAt(transaction.getCreatedAt())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .reason(transaction.getReason())
                .build();
    }
}