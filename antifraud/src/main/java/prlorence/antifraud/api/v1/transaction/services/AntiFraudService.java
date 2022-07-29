package prlorence.antifraud.api.v1.transaction.services;


import prlorence.antifraud.api.v1.stolencard.entities.CardRequest;
import prlorence.antifraud.api.v1.stolencard.services.CardConfig;
import prlorence.antifraud.api.v1.stolencard.services.ValidateCard;
import prlorence.antifraud.api.v1.stolencard.services.exception.CardNumberException;
import prlorence.antifraud.api.v1.suspiciousip.entities.RequestIPAddress;
import prlorence.antifraud.api.v1.suspiciousip.services.IpConfig;
import prlorence.antifraud.api.v1.suspiciousip.services.exception.IpAddressException;
import prlorence.antifraud.api.v1.transaction.entities.Transaction;
import prlorence.antifraud.api.v1.transaction.entities.TransactionResult;
import prlorence.antifraud.api.v1.transaction.entities.Type;
import prlorence.antifraud.api.v1.transaction.model.TransactionRepository;
import com.github.sisyphsu.dateparser.DateParserUtils;

import lombok.Setter;
import org.hibernate.type.SpecialOneToOneType;
import prlorence.antifraud.api.v1.transaction.entities.TransactionResult;
import prlorence.antifraud.api.v1.transaction.entities.Type;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.stream.Collectors;

@Service
public class AntiFraudService {
    @Setter(onMethod_ = {@Autowired})
    private CardConfig cardConfig;
    @Setter(onMethod_ = {@Autowired})
    private IpConfig ipConfig;
    @Setter(onMethod_ = {@Autowired})
    private TransactionRepository transactionRepository;

    public Object evaluateTransaction(Transaction transactionRequest) {
        HttpStatus status = HttpStatus.OK;

        try {
            ipConfig.validateIp().resolve(transactionRequest.getIp());
            cardConfig.validateCard().resolve(transactionRequest.getNumber());
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (CardNumberException | IpAddressException ignored) {
        }

        transactionRepository.save(transactionRequest);
        return new ResponseEntity<>(validateTransaction(transactionRequest), status);
    }

    private synchronized TransactionResult validateTransaction(Transaction transactionRequest) {
        TransactionResult ipValidated = validateTransactionIp(transactionRequest);
        TransactionResult regionValidated = validateTransactionRegion(transactionRequest);
        TransactionResult amountValidated = validateTransactionAmount(transactionRequest);

        List<TransactionResult> transactionResults = List.of(ipValidated, regionValidated, amountValidated);

        TransactionResult result = new TransactionResult();

        List<String> combinedInfo = new ArrayList<>();

        for (TransactionResult thisResult : transactionResults) {
            result.set(thisResult.getResult(), thisResult.getInfo());
            combinedInfo.addAll(thisResult.getInfoList());
        }

        String info = combinedInfo.stream().sorted().collect(Collectors.joining(", "));

        result.setInfo(info);

        return result;
    }

    private synchronized TransactionResult validateTransactionRegion(Transaction transactionRequest) {
        TransactionResult result = new TransactionResult();

        List<Transaction> recentTransactions = getRecentTransactions(transactionRequest);

        long regionCount = recentTransactions.stream()
                .filter(transaction -> !transactionRequest.getRegion().equals(transaction.getRegion()))
                .count();

        if (regionCount == 2) {
            result.set(Type.MANUAL_PROCESSING, TransactionResult.REGION_CORRELATION);
            return result;
        }

        if (regionCount > 2L) {
            result.set(Type.PROHIBITED, TransactionResult.REGION_CORRELATION);
            return result;
        }

        return result;
    }

    private synchronized TransactionResult validateTransactionIp(Transaction transactionRequest) {
        TransactionResult result = new TransactionResult();

        List<Transaction> recentTransactions = getRecentTransactions(transactionRequest);

        long ipCount = recentTransactions
                .stream().filter(transaction -> !transaction.getIp().equals(transactionRequest.getIp()))
                .count();

        if (ipCount == 2L) {
            result.set(Type.MANUAL_PROCESSING, TransactionResult.IP_CORRELATION);
            return result;
        }

        if (ipCount > 2L) {
            result.set(Type.PROHIBITED, TransactionResult.IP_CORRELATION);
            return result;
        }

        return result;
    }

    private synchronized TransactionResult validateTransactionAmount(Transaction transaction) {
            TransactionResult result = new TransactionResult();
            if (transaction.getAmount() <= 200) {
                result.set(Type.ALLOWED, TransactionResult.NONE);
            } else if (transaction.getAmount() > 200
                    && transaction.getAmount() <= 1500) {
                result.set(Type.MANUAL_PROCESSING, TransactionResult.AMOUNT);
            } else if (transaction.getAmount() > 1500) {
                result.set(Type.PROHIBITED, TransactionResult.AMOUNT);
            }

            if (isSuspiciousIp(transaction.getIp())) {
                result.set(Type.PROHIBITED, TransactionResult.IP);
            }

            if (isStolenCard(transaction.getNumber())) {
                result.set(Type.PROHIBITED, TransactionResult.CARD_NUMBER);
            }

            return result;
        }

        private synchronized List<Transaction> getRecentTransactions (Transaction transactionRequest){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDate = dateFormat.format(transactionRequest.getDate());

            Calendar transactionDate = DateParserUtils.parseCalendar(formattedDate);
            Instant transactionInInstant = transactionDate.toInstant();

            Date transactionAfterOneHour = Date.from(transactionInInstant.plus(1, ChronoUnit.HOURS));

            return transactionRepository
                    .findAllByNumberAndDateBeforeAndDateAfter(transactionRequest.getNumber(),
                            transactionRequest.getDate(), transactionAfterOneHour);
        }

        private boolean isSuspiciousIp(String ipAddress) {
            return ipConfig.validateIp().isExistingIp(ipAddress);
        }

        private boolean isStolenCard(String cardNumber) {
            return cardConfig.validateCard().isExistingCardNumber(cardNumber);
        }

}