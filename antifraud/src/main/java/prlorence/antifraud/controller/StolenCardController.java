package prlorence.antifraud.controller;

import prlorence.antifraud.api.v1.stolencard.entities.CardRequest;
import prlorence.antifraud.api.v1.stolencard.entities.StolenCard;
import prlorence.antifraud.api.v1.stolencard.model.StolenCardRepository;
import prlorence.antifraud.api.v1.stolencard.services.CardConfig;
import prlorence.antifraud.api.v1.stolencard.services.exception.CardNumberException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/")
@Setter(onMethod_ = {@Autowired})
public class StolenCardController {

    private CardConfig cardConfig;
    private StolenCardRepository stolenCardRepository;
    @PostMapping("stolencard")
    public ResponseEntity<StolenCard> addStolenCard(@RequestBody CardRequest card) {
        cardConfig.cardControl().saveStolenCard(card);
        return new ResponseEntity<>(cardConfig.cardControl().getCard(card), HttpStatus.OK);
    }

    @DeleteMapping("stolencard/{number}")
    public ResponseEntity<Object> deleteIpAddress(@PathVariable CardRequest number) {
        if (!cardConfig.validateCard().isValidCardNumber(number.getNumber())) throw new CardNumberException(HttpStatus.BAD_REQUEST);
        if (!cardConfig.validateCard().isExistingCardNumber(number.getNumber())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        cardConfig
                .cardControl()
                .deleteCard(number);

        String result = String.format("Card %s successfully removed!", number.getNumber());
        return new ResponseEntity<>(Map.of("status", result), HttpStatus.OK);
    }

    @GetMapping("stolencard")
    public ResponseEntity<List<StolenCard>> getAllIpAddress() {
        List<StolenCard> cardNumbers = cardConfig
                .cardControl()
                .getAllCardNumbers();

        return new ResponseEntity<>(cardNumbers, HttpStatus.OK);
    }
}
