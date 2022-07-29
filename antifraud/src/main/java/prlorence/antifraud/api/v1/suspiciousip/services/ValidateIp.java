package prlorence.antifraud.api.v1.suspiciousip.services;

import prlorence.antifraud.api.v1.suspiciousip.entities.RequestIPAddress;
import prlorence.antifraud.api.v1.suspiciousip.entities.SuspiciousIp;
import prlorence.antifraud.api.v1.suspiciousip.model.IPAddressRespository;
import prlorence.antifraud.api.v1.suspiciousip.services.exception.IpAddressException;
import com.google.common.net.InetAddresses;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Setter(onMethod_={@Autowired})
public class ValidateIp {
    private IPAddressRespository ipAddressRepo;

    public void resolve(String ip) throws IpAddressException {
        if (!InetAddresses.isInetAddress(ip)) throw new IpAddressException(HttpStatus.BAD_REQUEST);
        if (isExistingIp(ip)) throw new IpAddressException(HttpStatus.CONFLICT);
        if (findIp(ip).isEmpty()) throw new IpAddressException(HttpStatus.NOT_FOUND);
    }

    public List<SuspiciousIp> findIp(String ip) {
        return ipAddressRepo.findAllByIpEquals(ip);
    }

    public boolean isExistingIp(String ip) {
        return ipAddressRepo.findByIp(ip).isPresent();
    }
}
