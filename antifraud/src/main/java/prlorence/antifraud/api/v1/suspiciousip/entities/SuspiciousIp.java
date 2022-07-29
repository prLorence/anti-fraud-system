package prlorence.antifraud.api.v1.suspiciousip.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "suspicious_ip")
@Component
public class SuspiciousIp {
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long id;

    @Column(name = "ip_address")
    private String ip;

    public SuspiciousIp(RequestIPAddress ip) {
        this.ip = ip.getIp();
    }
}
