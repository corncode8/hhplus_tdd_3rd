package hhplus.serverjava.api.reservation.response;

import java.time.format.DateTimeFormatter;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationResponse {

	private Long reservationId;

	private String concertName;
	private String concertArtist;
	private String reservationDate;

	private int reservationSeat;
	private String expiredAt;
	private int price;

	public PostReservationResponse(Reservation reservation, Seat seat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		this.reservationId = reservation.getId();
		this.concertName = reservation.getConcertName();
		this.concertArtist = reservation.getConcertArtist();
		this.reservationDate = reservation.getConcertAt().format(formatter);
		this.reservationSeat = reservation.getSeatNum();
		this.expiredAt = seat.getExpiredAt().format(formatter);
		this.price = reservation.getReservedPrice();
	}
}
