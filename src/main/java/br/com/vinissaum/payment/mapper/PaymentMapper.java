package br.com.vinissaum.payment.mapper;

import br.com.vinissaum.payment.dto.PaymentDTO;
import br.com.vinissaum.payment.model.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {

    public PaymentDTO toDto(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setName(entity.getName());
        dto.setCvv(entity.getCvv());
        dto.setStatus(entity.getStatus());
        dto.setNumber(entity.getNumber());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setOrderId(entity.getOrderId());
        dto.setPaymentTypeId(entity.getPaymentTypeId());

        return dto;
    }

    public Payment toEntity(PaymentDTO dto) {
        Payment entity = new Payment();
        entity.setId(dto.getId());
        entity.setValue(dto.getValue());
        entity.setName(dto.getName());
        entity.setCvv(dto.getCvv());
        entity.setStatus(dto.getStatus());
        entity.setNumber(dto.getNumber());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setOrderId(dto.getOrderId());
        entity.setPaymentTypeId(dto.getPaymentTypeId());

        return entity;
    }

}
