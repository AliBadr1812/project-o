package com.ali_b1812.app.dto.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateMapper {
    public LocalDateTime asLocalDateTime(Date date) {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
    
    public Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant());
    }
}