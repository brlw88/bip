package me.brlw.bip.statistics;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by ww on 09.09.16.
 */
public interface StatisticsService {
    Stream<Statistics> findAllByAccountId(String id);
}
