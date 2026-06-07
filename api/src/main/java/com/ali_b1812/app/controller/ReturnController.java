package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.ReturnResponse;
import com.ali_b1812.app.service.ReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/returns")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Returns", description = "Return and refund workflow management")
public class ReturnController {

    private final ReturnService returnService;

    @GetMapping
    @Operation(summary = "Get all return requests")
    public ResponseEntity<ApiResponse<List<ReturnResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("OK", returnService.getAllReturns()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get return by ID")
    public ResponseEntity<ApiResponse<ReturnResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("OK", returnService.getReturnById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new return request")
    public ResponseEntity<ApiResponse<ReturnResponse>> create(@RequestBody ReturnResponse request) {
        ReturnResponse created = returnService.createReturn(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Return created", created, "/returns/" + created.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update return status or notes")
    public ResponseEntity<ApiResponse<ReturnResponse>> update(
            @PathVariable Long id,
            @RequestBody ReturnResponse patch) {
        return ResponseEntity.ok(ApiResponse.success("Return updated", returnService.updateReturn(id, patch)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a return request")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        returnService.deleteReturn(id);
        return ResponseEntity.noContent().build();
    }
}
