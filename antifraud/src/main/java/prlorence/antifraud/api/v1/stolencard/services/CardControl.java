package prlorence.antifraud.api.v1.stolencard.services;

import prlorence.antifraud.api.v1.stolencard.entities.CardRequest;
import prlorence.antifraud.api.v1.stolencard.entities.StolenCard;
import prlorence.antifraud.api.v1.stolencard.model.StolenCardRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import prlorence.antifraud.api.v1.stolencard.services.exception.CardNumberException;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Setter(onMethod_={@Autowired})
public class CardControl {
    private ValidateCard validateCard;
    private StolenCardRepository stolenCardRepository;

    public void saveStolenCard(CardRequest card) {
        validateCard.resolve(card.getNumber());
        stolenCardRepository.save(new StolenCard(card));
    }

    @Transactional
    public void deleteCard(CardRequest card) {
        stolenCardRepository.deleteByNumber(card.getNumber());
    }

    public StolenCard getCard(CardRequest card) {
        return stolenCardRepository.findByNumber(card.getNumber())
                .orElseThrow(() -> new CardNumberException(HttpStatus.NOT_FOUND));
    }

    public List<StolenCard> getAllCardNumbers() {
        List<StolenCard> cardNumbers = new ArrayList<>();

        for (StolenCard thisCard : stolenCardRepository.findAll()) {
            cardNumbers.add(thisCard);
        }

        return cardNumbers;
    }

}
