package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ResponseDto;
import org.example.service.ImportValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController("importValidatorController")
@RequestMapping("/v1/imports")
public class ImportValidatorController {

    private final ImportValidatorService importValidatorService;
    @PostMapping("/validate")
    public ResponseEntity<ResponseDto> validateImports(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(importValidatorService.validateFileImports(file));
    }
}