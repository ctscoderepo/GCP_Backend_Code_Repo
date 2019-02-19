package com.gcp.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Optional;
@Slf4j
public class Constants {
    public enum SortBy {PRICE_HIGH_2_LOW, PRICE_LOW_2_HIGH, NAME_ASC, NAME_DESC};
    public static SortBy toSortBy(String name) {
        try {
            if (!StringUtils.isEmpty(name)) {
                return SortBy.valueOf(name);
            }
        } catch (Exception e) {
            log.error("Invalid sort option received. Requested=\"{}\"", name);
        }

        return SortBy.PRICE_LOW_2_HIGH;
    }
}
