package prlorence.antifraud.api.v1.transaction.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter @Setter
@Component
public class TransactionResult {
    private String result;
    private String info;
    @JsonIgnore
    private List<String> infoList = new ArrayList<>();

    public void set(Type type, String info) {
        if (!infoList.isEmpty() && !(Type.PROHIBITED.name().equals(result))) {
            infoList.remove(infoList.size() - 1);
        }
        result = type.name();
        infoList.add(info);
    }

    public void set(String type, String info) {
        if (!infoList.isEmpty() && !(Type.PROHIBITED.name().equals(result))) {
            infoList.remove(infoList.size() - 1);
        }
        result = type;
        infoList.add(info);
    }

    public static final String NONE = "none";
    public static final String AMOUNT = "amount";
    public static final String IP = "ip";
    public static final String CARD_NUMBER = "card-number";

    public static final String IP_CORRELATION = "ip-correlation";
    public static final String REGION_CORRELATION = "region-correlation";
}
