package hhplus.serverjava.api.concert.response;

import hhplus.serverjava.domain.concert.entity.Concert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GetConcertResponse {

    List<ConcertInfo> concertInfoList;

    public GetConcertResponse(List<Concert> concerts) {
        this.concertInfoList = concerts.stream()
                .map(concert -> new ConcertInfo(concert))
                .collect(Collectors.toList());
    }
}