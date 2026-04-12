package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.EmployeePerformanceDAO;
import com.vfrol.supermarket.dto.analytics.EmployeePerformanceDTO;
import com.vfrol.supermarket.filter.EmployeePerformanceFilter;

import java.util.List;

@Singleton
public class EmployeePerformanceService {

    private final EmployeePerformanceDAO employeePerformanceDAO;

    @Inject
    public EmployeePerformanceService(EmployeePerformanceDAO employeePerformanceDAO) {
        this.employeePerformanceDAO = employeePerformanceDAO;
    }

    public List<EmployeePerformanceDTO> getByFilter(EmployeePerformanceFilter filter) {
        return employeePerformanceDAO.findByFilter(filter);
    }
}