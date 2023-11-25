package com.pms.dto;

import java.util.List;

public record DataSets(
        String label,
        List<Integer> data,
        Boolean fill,
        String backgroundColor,
        String borderColor,
        double tension
) {
}
