package prlorence.antifraud.api.v1.stolencard.services;

import prlorence.antifraud.api.v1.stolencard.entities.CardRequest;
import prlorence.antifraud.api.v1.stolencard.entities.StolenCard;
import prlorence.antifraud.api.v1.stolencard.model.StolenCardRepository;
import prlorence.antifraud.api.v1.stolencard.services.exception.CardNumberException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import prlorence.antifraud.api.v1.stolencard.model.StolenCardRepository;
import prlorence.antifraud.api.v1.stolencard.services.exception.CardNumberException;

import java.util.Arrays;
import java.util.List;

@Setter(onMethod_={@Autowired})
public class ValidateCard {
    private StolenCardRepository stolenCardRepository;


    public void resolve(String card) throws CardNumberException {
        if (!isValidCardNumber(card)) throw new CardNumberException(HttpStatus.BAD_REQUEST);
        if (isExistingCardNumber(card)) throw new CardNumberException(HttpStatus.CONFLICT);
        if (findNumber(card) == null) throw new CardNumberException(HttpStatus.NOT_FOUND);
    }

    public boolean isValidCardNumber(String cardNumber) {
        // check if card.length is 16, else return false
        if (cardNumber.length() < 16) return false;
        // convert str arr to int arr
        int[] parsedCardNumber = Arrays.stream(cardNumber.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();
        // feed cardNumberArr to luhnCheck()
        return luhnCheck(parsedCardNumber);
    }

    private boolean luhnCheck(int[] cardNumberArr) {
        int[] result = Arrays.copyOf(cardNumberArr, cardNumberArr.length);
        for (int i = cardNumberArr.length - 2; i >= 0; i -= 2) {
            int temp = cardNumberArr[i] * 2;
            if (temp > 9) temp -= 9;
            result[i] = temp;
        }
        return Arrays.stream(result).sum() % 10 == 0;
    }

    public boolean isExistingCardNumber(String cardNumber){
        return stolenCardRepository.findByNumber(cardNumber).isPresent();
    }
    public List<StolenCard> findNumber(String number) {
        return stolenCardRepository.findAllByNumberEquals(number)
                .orElseThrow(() -> new CardNumberException(HttpStatus.NO_CONTENT));
    }
}
