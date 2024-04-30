package org.example.controller;

import org.example.dto.ResponseDto;
import org.example.service.ImportValidatorService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController("importValidatorControllerV1")
@RequestMapping("/v1/imports")
public class ImportValidatorController {

    private final ImportValidatorService importValidatorService;
    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> validateImports(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(importValidatorService.validateFileImports(file));
    }
}