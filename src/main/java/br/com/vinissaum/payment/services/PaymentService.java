package br.com.vinissaum.payment.services;

import br.com.vinissaum.payment.dto.PaymentDTO;
import br.com.vinissaum.payment.model.Payment;
import br.com.vinissaum.payment.model.Status;
import br.com.vinissaum.payment.repositories.PaymentRepository;
import br.com.vinissaum.payment.services.exceptions.DatabaseException;
import br.com.vinissaum.payment.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PaymentService {

    private PaymentRepository repository;

    private ModelMapper modelMapper;

    @Autowired
    public PaymentService(PaymentRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<PaymentDTO> findAll() {
        List<Payment> payments = repository.findAll();

        return payments.stream().map(payment -> modelMapper.map(payment, PaymentDTO.class)).toList();
    }

    public PaymentDTO findById(Long id) {
        Payment payment = repository.findById(id).orElseThrow( //
                () -> new ResourceNotFoundException(String.format("Payment id: %s not found", id)) //
        );

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(Status.CREATED);

        Payment entity = repository.save(payment);

        return modelMapper.map(entity, PaymentDTO.class);
    }

    public PaymentDTO updatePayment(Long  id, PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);

        Payment entity = repository.save(payment);

        return modelMapper.map(entity, PaymentDTO.class);
    }

    public void deletePayment(Long id) {
        try {
            repository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Payment id: %s not found", id));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: " + e.getMessage());
        }
    }

}
