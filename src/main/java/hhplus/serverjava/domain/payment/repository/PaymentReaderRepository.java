package hhplus.serverjava.domain.payment.repository;

import java.util.Optional;

import hhplus.serverjava.domain.payment.entity.Payment;

public interface PaymentReaderRepository {
	Optional<Payment> findPayment(Long paymentId);
}
