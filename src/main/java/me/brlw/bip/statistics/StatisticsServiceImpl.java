package me.brlw.bip.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by ww on 09.09.16.
 */

@Service("statisticsService")
@Repository
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public Stream<Statistics> findAllByAccountId(String id) {
        return statisticsRepository.findAllByAccountId(id);
    }
}
