package prlorence.antifraud.api.v1.suspiciousip.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestIPAddress {
    @NotEmpty
    private String ip;
}
