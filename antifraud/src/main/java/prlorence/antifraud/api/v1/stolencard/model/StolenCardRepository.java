package prlorence.antifraud.api.v1.stolencard.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import prlorence.antifraud.api.v1.stolencard.entities.StolenCard;

import java.util.Optional;

import java.util.List;

@Repository
public interface StolenCardRepository extends CrudRepository<StolenCard, Long> {
    Optional<StolenCard> findByNumber(String number);
    Optional<List<StolenCard>> findAllByNumberEquals(String number);
    void deleteByNumber(String number);
}
