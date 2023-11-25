package com.pms.dto;

import java.util.List;

public record ChartData(
        List<String> labels,
        List<DataSets> datasets
) {
}
