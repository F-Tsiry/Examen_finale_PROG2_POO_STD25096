package code.exam.controller;

import code.exam.dto.CreateTransactionRequest;
import code.exam.dto.TransactionResponse;
import code.exam.model.Transaction;
import code.exam.model.TransactionType;
import code.exam.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * GET /transactions?type=in|out
     * No "type" param -> returns every transaction.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestParam(name = "type", required = false) String type) {

        TransactionType transactionType = parseType(type);

        List<TransactionResponse> response = transactionService.getTransactions(transactionType)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /transaction
     */
    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody CreateTransactionRequest request) {

        Transaction created = transactionService.createTransaction(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionResponse.fromEntity(created));
    }

    private TransactionType parseType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }
        try {
            return TransactionType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type value: " + type + " (expected 'in' or 'out')");
        }
    }
}