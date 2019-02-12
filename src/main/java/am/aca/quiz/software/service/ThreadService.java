package am.aca.quiz.software.service;


import am.aca.quiz.software.entity.HistoryEntity;
import am.aca.quiz.software.entity.enums.Status;
import am.aca.quiz.software.service.dto.HistoryDto;
import am.aca.quiz.software.service.implementations.HistoryServiceImp;
import am.aca.quiz.software.service.implementations.TestServiceImp;
import am.aca.quiz.software.service.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ThreadService {
    private HistoryServiceImp historyServiceImp;

    private HistoryMapper historyMapper;

    private TestServiceImp testServiceImp;


    public ThreadService(HistoryServiceImp historyServiceImp, HistoryMapper historyMapper, TestServiceImp testServiceImp) {
        this.historyServiceImp = historyServiceImp;
        this.historyMapper = historyMapper;
        this.testServiceImp = testServiceImp;
    }


    @Async("threadPoolTaskExecutor")
    public void findUser() throws InterruptedException {


        while (true) {
            Set<HistoryDto> upcomingTest = new HashSet<>();

            List<HistoryDto> userHistories = historyMapper
                    .mapEntitiesToDto(historyServiceImp.findAllByStatus(Status.UPCOMING));
            userHistories.forEach(i -> upcomingTest.add(i));

            while (!upcomingTest.isEmpty()) {
                upcomingTest.clear();
                List<HistoryDto> checker = historyMapper
                        .mapEntitiesToDto(historyServiceImp.findAllByStatus(Status.UPCOMING));
                checker.forEach(i -> upcomingTest.add(i));

//                upcomingTest.forEach(j -> {
//                        synchronized (upcomingTest) {
//                            try {
//                                if (historyServiceImp.getById(j.getId()) == null) {
//                                    upcomingTest.remove(j);
//                                }
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    });
                    if(!upcomingTest.isEmpty()) {

                        LocalDateTime now = LocalDateTime.now();

                        System.out.println(now);
                        System.out.println("SET SIZE "+upcomingTest.size());

                        upcomingTest.forEach(i -> {
                            LocalDateTime userStartTime = i.getStartTime();
                            try {
                                synchronized (upcomingTest) {
                                    if (now.isAfter(userStartTime.plusMinutes(testServiceImp.getById(i.getTestId()).getDuration()))) {
                                        HistoryEntity historyEntity = historyServiceImp.getById(i.getId());
                                        historyEntity.setScore(0);
                                        historyEntity.setStatus(Status.COMPLETED);
                                        historyEntity.setEndTime(now);
                                        historyServiceImp.addHistory(historyEntity);
                                        upcomingTest.remove(i);
                                    }
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        });
                    }
            }
        }
    }
}