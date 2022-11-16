package br.com.vinissaum.payment.dto;

import br.com.vinissaum.payment.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDTO {

    private Long id;

    private BigDecimal value;

    private String name;

    private String number;

    private String expirationDate;

    private String cvv;

    private Status status;

    private Long orderId;

    private Long paymentTypeId;

}
