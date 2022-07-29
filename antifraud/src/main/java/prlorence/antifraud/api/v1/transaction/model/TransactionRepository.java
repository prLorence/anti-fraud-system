package prlorence.antifraud.api.v1.transaction.model;

import prlorence.antifraud.api.v1.transaction.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findByNumber(Transaction transaction);

    List<Transaction> findAllByNumberAndDateBeforeAndDateAfter(@NotNull String number, @NotNull Date date, @NotNull Date date2);
}
