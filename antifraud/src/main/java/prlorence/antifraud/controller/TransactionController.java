package prlorence.antifraud.controller;

import prlorence.antifraud.api.v1.transaction.entities.Transaction;
import prlorence.antifraud.api.v1.transaction.services.AntiFraudService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@Setter(onMethod_={@Autowired})
public class TransactionController {
    private AntiFraudService antiFraudService;
    
    @PostMapping("/api/antifraud/transaction")
    public Object evaluateAmount(@RequestBody @Valid Transaction transaction) {
        return antiFraudService.evaluateTransaction(transaction);
    }
}
