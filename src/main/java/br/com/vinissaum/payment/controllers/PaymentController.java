package br.com.vinissaum.payment.controllers;

import br.com.vinissaum.payment.dto.PaymentDTO;
import br.com.vinissaum.payment.services.PaymentService;
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

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
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

}
