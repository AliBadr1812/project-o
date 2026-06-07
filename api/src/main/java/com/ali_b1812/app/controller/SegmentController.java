package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.SegmentResponse;
import com.ali_b1812.app.service.SegmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/segments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Segments", description = "Customer segment management")
public class SegmentController {

    private final SegmentService segmentService;

    @GetMapping
    @Operation(summary = "Get all customer segments")
    public ResponseEntity<ApiResponse<List<SegmentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("OK", segmentService.getAllSegments()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get segment by ID")
    public ResponseEntity<ApiResponse<SegmentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("OK", segmentService.getSegmentById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new customer segment")
    public ResponseEntity<ApiResponse<SegmentResponse>> create(@RequestBody SegmentResponse request) {
        SegmentResponse created = segmentService.createSegment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Segment created", created, "/segments/" + created.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer segment")
    public ResponseEntity<ApiResponse<SegmentResponse>> update(
            @PathVariable Long id,
            @RequestBody SegmentResponse patch) {
        return ResponseEntity.ok(ApiResponse.success("Segment updated", segmentService.updateSegment(id, patch)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a customer segment (non-system only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        segmentService.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }
}
