package hhplus.serverjava.api.usecase.point.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.user.response.PointHistoryDto;
import hhplus.serverjava.api.user.usecase.GetPointHistoryUseCase;
import hhplus.serverjava.domain.pointhistory.components.PointHistoryReader;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory.State;
import hhplus.serverjava.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class GetPointHistoryUseCaseTest {

	@Mock
	PointHistoryReader pointHistoryReader;

	@InjectMocks
	GetPointHistoryUseCase getPointHistoryUseCase;

	@DisplayName("포인트 내역 조회 테스트")
	@Test
	void test() {
		//given

		Long userId = 2L;
		Long point = 0L;
		User user = new User(userId, point);

		List<PointHistory> list = List.of(new PointHistory[] {
			new PointHistory(1L, user, State.CHARGE, 500L),
			new PointHistory(2L, user, State.CHARGE, 500L + 500),
			new PointHistory(3L, user, State.USE, 500L + 500L - 200L)
		});

		when(pointHistoryReader.readList(userId)).thenReturn(list);

		//when
		PointHistoryDto result = getPointHistoryUseCase.execute(userId);

		//then
		assertNotNull(result);
		assertEquals(500L, list.get(0).getAmount());
		assertEquals(500L + 500L, list.get(1).getAmount());
		assertEquals(1000L - 200L, list.get(2).getAmount());
	}
}
