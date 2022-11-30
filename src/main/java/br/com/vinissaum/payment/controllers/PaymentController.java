package br.com.vinissaum.payment.controllers;

import br.com.vinissaum.payment.dto.PaymentDTO;
import br.com.vinissaum.payment.services.PaymentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService service;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public PaymentController(PaymentService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> index(@PageableDefault Pageable pageable) {
        Page<PaymentDTO> page = service.findAll(pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> show(@PathVariable @NotNull Long id) {
        PaymentDTO dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentDTO dto, UriComponentsBuilder uriBuilder) {
        PaymentDTO paymentDTO = service.createPayment(dto);
        URI uri = uriBuilder.path("/payments/{id").buildAndExpand(paymentDTO.getId()).toUri();

        rabbitTemplate.convertAndSend("payment.ex","", paymentDTO);

        return ResponseEntity.created(uri).body(paymentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDTO dto) {
        PaymentDTO paymentDTO = service.updatePayment(id, dto);

        return ResponseEntity.ok(paymentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        service.deletePayment(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    public void approvePayment(@PathVariable Long id) {
        service.approvePayment(id);
    }

    public void updateOrderWaitingIntegration(Long id) {
        service.changeStatus(id);
    }

}
