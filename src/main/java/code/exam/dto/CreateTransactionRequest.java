package code.exam.dto;

import code.exam.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    private String accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String reason;
}