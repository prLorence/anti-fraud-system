package prlorence.antifraud.api.v1.suspiciousip.model;

import prlorence.antifraud.api.v1.suspiciousip.entities.SuspiciousIp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface IPAddressRespository extends CrudRepository<SuspiciousIp, Long> {

    Optional<SuspiciousIp> findByIp(String ip);

    List<SuspiciousIp> findAllByIpEquals(String ip);
    void deleteByIp(String ip);
}
