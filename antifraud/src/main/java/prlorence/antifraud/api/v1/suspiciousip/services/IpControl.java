package prlorence.antifraud.api.v1.suspiciousip.services;

import prlorence.antifraud.api.v1.suspiciousip.entities.RequestIPAddress;
import prlorence.antifraud.api.v1.suspiciousip.entities.SuspiciousIp;
import prlorence.antifraud.api.v1.suspiciousip.model.IPAddressRespository;
import prlorence.antifraud.api.v1.suspiciousip.services.exception.IpAddressException;
import com.google.common.net.InetAddresses;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Setter(onMethod_={@Autowired})
public class IpControl {
    private ValidateIp validateIp;
    private IPAddressRespository ipAddressRepo;

    public void saveIp(RequestIPAddress ip) {
        validateIp.resolve(ip.getIp());
        ipAddressRepo.save(new SuspiciousIp(ip));
    }

    @Transactional
    public void deleteIp(RequestIPAddress ip) {
        if (!InetAddresses.isInetAddress(ip.getIp())) throw new IpAddressException(HttpStatus.BAD_REQUEST);
        if (validateIp.findIp(ip.getIp()).isEmpty()) throw new IpAddressException(HttpStatus.NOT_FOUND);
        ipAddressRepo.deleteByIp(ip.getIp());
    }

    public SuspiciousIp getIp(RequestIPAddress ip) {
        return ipAddressRepo.findByIp(ip.getIp()).orElseThrow(() -> new IpAddressException(HttpStatus.NOT_FOUND));
    }

    public List<SuspiciousIp> getAllIpAddress() {
        List<SuspiciousIp> ipAddresses = new ArrayList<>();

        for (SuspiciousIp thisIp : ipAddressRepo.findAll()) {
            ipAddresses.add(thisIp);
        }

        return ipAddresses;
    }
}
