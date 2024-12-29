package com.example.foodordering.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.foodordering.entity.Revenue;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {

        // get amount every month in year
        @Query("SELECT MONTH(r.date) AS month, SUM(r.amount) AS totalAmount " +
                        "FROM Revenue r " +
                        "WHERE YEAR(r.date) = :year " +
                        "GROUP BY MONTH(r.date)")
        List<Object[]> getRevenueAmountsByMonthForYear(int year);
        // get amount every week in month , year

        @Query(value = "SELECT r.date, SUM(r.amount) FROM revenues r WHERE WEEK(r.date) = WEEK(CURRENT_DATE()) GROUP BY r.date ORDER BY r.date", nativeQuery = true)
        List<Object[]> getDailyRevenuesForCurrentWeek();

}
