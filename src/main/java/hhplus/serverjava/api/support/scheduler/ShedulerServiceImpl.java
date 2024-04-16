package hhplus.serverjava.api.support.scheduler;

import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShedulerServiceImpl implements SchedulerService{

    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;
    private final UserValidator userValidator;
    private final UserStore userStore;
    private final UserReader userReader;


    // 좌석이 만료된 예약 확인
    @Override
    public Boolean expiredReservations(LocalDateTime now) {
        // 좌석이 만료된 예약 조회
        List<Reservation> expiredReservations = reservationReader.findExpiredReservaions(now);

        if (!expiredReservations.isEmpty()) {
            return true;
        }
        return false;
    }

    // 좌석이 만료된 예약 처리
    @Override
    public void expireReservations(LocalDateTime now) {
        // 좌석이 만료된 예약 조회
        List<Reservation> expiredReservations = reservationReader.findExpiredReservaions(now);

        if (!expiredReservations.isEmpty()) {
            // 예약 만료 : 좌석 활성화 + 예약 취소
            reservationStore.ExpireReservation(expiredReservations);
        }

    }


    // 서비스에 입장한 후 10분이 지나도록
    // 결제를 안하고 있는 유저가 있는지 확인
    @Override
    public Boolean userTimeValidation (LocalDateTime now) {
        // 서비스 이용중 유저 조회
        List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

        Boolean validation = userValidator.UserActiveTimeValidator(workingUsers, now);

        return validation;
    }

    // 서비스에 입장한 후 10분이 지나도록
    // 결제를 안하고 있는 유저 만료 처리
    public void expireUsers(LocalDateTime now) {
        List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

        if (!workingUsers.isEmpty()) {
            for (User user : workingUsers) {
                // user의 status를 Done으로 변경
                if (now.isAfter(user.getUpdatedAt().plusMinutes(10))) {
                    user.setDone();
                }
            }
        }
    }


    // 서비스를 이용중인 유저가 100명 미만인지 확인
    @Override
    public Boolean workingUserNumValidation (LocalDateTime now) {
        // 서비스 이용중 유저 조회
        List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

        if (workingUsers.size() < 100) {
            return true;
        }
        return false;
    }

    @Override
    public void enterServiceUser() {
        // 서비스 이용중 유저
        List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

        // 대기중인 유저
        List<User> waitingUsers = userReader.findUsersByStatus(User.State.WAITING);

        int num = 0;

        num = 100 - workingUsers.size();

        if (num > 0) {
            userStore.enterService(waitingUsers, num);
        }

    }
}