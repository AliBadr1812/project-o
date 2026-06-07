package com.ali_b1812.app.service;

import com.ali_b1812.app.dto.response.ReturnResponse;
import com.ali_b1812.app.mockdata.ReturnMockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnService {

    private final ReturnMockData mockData;
    private static final AtomicLong RET_SEQ = new AtomicLong(6);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<ReturnResponse> getAllReturns() {
        return mockData.getAll();
    }

    public ReturnResponse getReturnById(Long id) {
        return mockData.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Return not found with id: " + id));
    }

    public ReturnResponse createReturn(ReturnResponse request) {
        request.setReturnNumber("RET-2024-" + String.format("%03d", RET_SEQ.getAndIncrement()));
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now().format(FMT));
        request.setUpdatedAt(request.getCreatedAt());
        return mockData.save(request);
    }

    public ReturnResponse updateReturn(Long id, ReturnResponse patch) {
        ReturnResponse existing = getReturnById(id);
        if (patch.getStatus()       != null) existing.setStatus(patch.getStatus());
        if (patch.getStaffNotes()   != null) existing.setStaffNotes(patch.getStaffNotes());
        if (patch.getRefundMethod() != null) existing.setRefundMethod(patch.getRefundMethod());
        if (patch.getRefundAmount() != null) existing.setRefundAmount(patch.getRefundAmount());
        existing.setUpdatedAt(LocalDateTime.now().format(FMT));
        return mockData.save(existing);
    }

    public void deleteReturn(Long id) {
        getReturnById(id); // throws 404 if not found
        mockData.delete(id);
    }
}
