package prlorence.antifraud.controller;

import prlorence.antifraud.api.v1.suspiciousip.entities.RequestIPAddress;
import prlorence.antifraud.api.v1.suspiciousip.entities.SuspiciousIp;
import prlorence.antifraud.api.v1.suspiciousip.model.IPAddressRespository;
import prlorence.antifraud.api.v1.suspiciousip.services.IpConfig;
import prlorence.antifraud.api.v1.suspiciousip.services.exception.IpAddressException;
import com.google.common.net.InetAddresses;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/")
@Setter(onMethod_={@Autowired})
public class SuspiciousIpController {

    private IPAddressRespository ipAddressRepo;
    private IpConfig ipConfig;

    @PostMapping("suspicious-ip")
    public ResponseEntity<SuspiciousIp> addIpAddress(@RequestBody RequestIPAddress ip) {
        ipConfig
                .ipControl()
                .saveIp(ip);
        return new ResponseEntity<>(ipConfig.ipControl().getIp(ip), HttpStatus.OK);
    }

    @DeleteMapping("suspicious-ip/{ip}")
    public ResponseEntity<Object> deleteIpAddress(@PathVariable RequestIPAddress ip) {
        if (!InetAddresses.isInetAddress(ip.getIp())) throw new IpAddressException(HttpStatus.BAD_REQUEST);
        if (!ipConfig.validateIp().isExistingIp(ip.getIp())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ipConfig
                .ipControl()
                .deleteIp(ip);

        String result = String.format("IP %s successfully removed!", ip.getIp());
        return new ResponseEntity<>(Map.of("status", result), HttpStatus.OK);
    }

    @GetMapping("suspicious-ip")
    public ResponseEntity<List<SuspiciousIp>> getAllIpAddress() {
        List<SuspiciousIp> ipAddresses = ipConfig
                .ipControl()
                .getAllIpAddress();

        return new ResponseEntity<>(ipAddresses, HttpStatus.OK);
    }
}
