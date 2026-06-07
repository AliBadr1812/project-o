package com.ali_b1812.app.service;

import com.ali_b1812.app.dto.response.SegmentResponse;
import com.ali_b1812.app.mockdata.SegmentMockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SegmentService {

    private final SegmentMockData mockData;

    public List<SegmentResponse> getAllSegments() {
        return mockData.getAll();
    }

    public SegmentResponse getSegmentById(Long id) {
        return mockData.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Segment not found with id: " + id));
    }

    public SegmentResponse createSegment(SegmentResponse request) {
        return mockData.save(request);
    }

    public SegmentResponse updateSegment(Long id, SegmentResponse patch) {
        SegmentResponse existing = getSegmentById(id);
        if (patch.getName()          != null) existing.setName(patch.getName());
        if (patch.getDescription()   != null) existing.setDescription(patch.getDescription());
        if (patch.getColor()         != null) existing.setColor(patch.getColor());
        if (patch.getIcon()          != null) existing.setIcon(patch.getIcon());
        if (patch.getCriteria()      != null) existing.setCriteria(patch.getCriteria());
        if (patch.getCustomerCount() != null) existing.setCustomerCount(patch.getCustomerCount());
        if (patch.getTotalRevenue()  != null) existing.setTotalRevenue(patch.getTotalRevenue());
        if (patch.getAvgOrderValue() != null) existing.setAvgOrderValue(patch.getAvgOrderValue());
        return mockData.save(existing);
    }

    public void deleteSegment(Long id) {
        SegmentResponse seg = getSegmentById(id);
        if (Boolean.TRUE.equals(seg.getIsSystem())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "System segments cannot be deleted");
        }
        mockData.delete(id);
    }
}
