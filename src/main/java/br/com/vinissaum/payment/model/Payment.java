package br.com.vinissaum.payment.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal value;

    @Size(max = 100)
    private String name;

    @Size(max = 19)
    private String number;

    @Size(max = 7)
    private String expirationDate;

    @Size(min = 3, max = 3)
    private String cvv;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Long orderId;

    @NotNull
    private Long paymentTypeId;

}
