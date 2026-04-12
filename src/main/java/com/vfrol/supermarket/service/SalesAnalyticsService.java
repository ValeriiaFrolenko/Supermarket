package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.SalesAnalyticsDAO;
import com.vfrol.supermarket.dto.analytics.SalesAnalyticsDTO;
import com.vfrol.supermarket.filter.SalesAnalyticsFilter;

import java.util.List;

@Singleton
public class SalesAnalyticsService {

    private final SalesAnalyticsDAO salesAnalyticsDAO;

    @Inject
    public SalesAnalyticsService(SalesAnalyticsDAO salesAnalyticsDAO) {
        this.salesAnalyticsDAO = salesAnalyticsDAO;
    }

    public List<SalesAnalyticsDTO> getByFilter(SalesAnalyticsFilter filter) {
        return salesAnalyticsDAO.findByFilter(filter);
    }
}