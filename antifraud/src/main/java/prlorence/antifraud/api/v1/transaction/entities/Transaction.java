package prlorence.antifraud.api.v1.transaction.entities;

import prlorence.antifraud.api.v1.stolencard.entities.CardRequest;
import prlorence.antifraud.api.v1.suspiciousip.entities.RequestIPAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity(name = "transaction")
public class Transaction {
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
    @JsonIgnore
    private long id;
    @NotNull
    @Min(1)
    private Long amount;
    @NotNull
    private String ip;
    @NotNull
    private String number;
    @NotNull
    private String region;
    @NotNull
    private Date date;
    public Long getAmount() {
        return amount != null ? amount : 0L;
    }
    @JsonProperty("date")
    public String getFormattedDate() {
        return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(date);
    }
}