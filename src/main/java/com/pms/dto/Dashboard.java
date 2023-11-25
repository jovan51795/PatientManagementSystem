package com.pms.dto;

import com.pms.spel.IPatientReportSpel;

public record Dashboard(
        java.util.List<IPatientReportSpel> status,
        ChartData chartData
) {
}
