package code.exam.controller;

import code.exam.dto.BalanceResponse;
import code.exam.dto.TransactionResponse;
import code.exam.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final TransactionService transactionService;

    /**
     * GET /accounts/{id}/transactions
     */
    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@PathVariable("id") String id) {
        List<TransactionResponse> response = transactionService.getTransactionsByAccountId(id)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /accounts/{id}/balance
     */
    @GetMapping("/accounts/{id}/balance")
    public ResponseEntity<BalanceResponse> getAccountBalance(@PathVariable("id") String id) {
        var balance = transactionService.getBalance(id);

        return ResponseEntity.ok(
                BalanceResponse.builder()
                        .accountId(id)
                        .balance(balance)
                        .build()
        );
    }
}