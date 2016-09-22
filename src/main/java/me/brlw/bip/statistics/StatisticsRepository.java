package me.brlw.bip.statistics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

/**
 * Created by ww on 09.09.16.
 */


public interface StatisticsRepository extends CrudRepository<Statistics, Long>
{
    @Query("SELECT s FROM Statistics s join s.redirection r join r.account a WHERE a.accountId = :accountId")
    Stream<Statistics> findAllByAccountId(@Param("accountId") String id);
}
