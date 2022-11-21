package br.com.vinissaum.payment.services;

import br.com.vinissaum.payment.dto.PaymentDTO;
import br.com.vinissaum.payment.http.OrderClient;
import br.com.vinissaum.payment.mapper.PaymentMapper;
import br.com.vinissaum.payment.model.Payment;
import br.com.vinissaum.payment.model.Status;
import br.com.vinissaum.payment.repositories.PaymentRepository;
import br.com.vinissaum.payment.services.exceptions.DatabaseException;
import br.com.vinissaum.payment.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PaymentService {

    private PaymentRepository repository;

    private PaymentMapper paymentMapper;

    private OrderClient orderClient;

    @Autowired
    public PaymentService(PaymentRepository repository, PaymentMapper paymentMapper, OrderClient orderClient) {
        this.repository = repository;
        this.paymentMapper = paymentMapper;
        this.orderClient = orderClient;
    }

    public Page<PaymentDTO> findAll(Pageable pageable) {
        Page<Payment> payments = repository.findAll(pageable);

        return new PageImpl<>(payments.stream().map(payment -> paymentMapper.toDto(payment)).toList());
    }

    public PaymentDTO findById(Long id) {
        Payment payment = repository.findById(id).orElseThrow( //
                () -> new ResourceNotFoundException(String.format("Payment id: %s not found", id)) //
        );

        return paymentMapper.toDto(payment);
    }

    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = paymentMapper.toEntity(dto);
        payment.setStatus(Status.CREATED);

        payment = repository.save(payment);

        return paymentMapper.toDto(payment);
    }

    public PaymentDTO updatePayment(Long  id, PaymentDTO dto) {
        Payment payment = repository.getReferenceById(id);

        BeanUtils.copyProperties(dto, payment, "id");
        payment = repository.save(payment);

        return paymentMapper.toDto(payment);
    }

    public void deletePayment(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException | EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Payment id: %s was not found", id));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: " + e.getMessage());
        }
    }

    public void approvePayment(Long id) {
        Optional<Payment> optionalPayment = repository.findById(id);

        Payment payment = optionalPayment.orElseThrow(() -> new ResourceNotFoundException(String.format
                ("Payment id: %s was not found", id)
        ));

        payment.setStatus(Status.CONFIRMED);
        repository.save(payment);

        orderClient.updatePayment(payment.getOrderId());
    }

    public void changeStatus(Long id) {
        Optional<Payment> optionalPayment = repository.findById(id);

        Payment payment = optionalPayment.orElseThrow(() -> new ResourceNotFoundException(String.format
                ("Payment id: %s was not found", id)
        ));

        payment.setStatus(Status.CONFIRMED_WAITING_INTEGRATION);
        repository.save(payment);
    }
}
