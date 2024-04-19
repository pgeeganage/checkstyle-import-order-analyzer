package org.example.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResponseDto;
import org.example.utility.Utility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ImportValidatorService {

    private String correctedImports;

    public ResponseDto validateFileImports(MultipartFile file) {
        log.info("\n=============~ Starting the Application ~===============\n");
        try {
            if (validateAndFixImportOrder(file)) {
                log.warn("Import order is already valid!");
                return new ResponseDto("Import order is already valid!", null);
            } else {
                log.info("Import order has been corrected.");
                return new ResponseDto("Import order has been corrected.", correctedImports);

            }
        } catch (IOException e) {
            log.error("Error reading/writing the file: {}", e.getMessage());
            return new ResponseDto("Error reading/writing the file: " + e.getMessage(), null);
        } finally {
            log.info("\n=============~ End ~===============\n");
        }
    }

    private boolean validateAndFixImportOrder(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(file.getBytes())))){
            List<String> actualImports = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("import ")) {
                    actualImports.add(line);
                }
            }
            String refactoredImports = correctImportOrder(actualImports);
            boolean isImportOrderCorrect = isImportOrderCorrect(refactoredImports, actualImports);
            if (!isImportOrderCorrect) {
                log.info("\n=============~ Here are the imports with correct order ~===============\n");
                refactoredImports = refactoredImports.replaceAll("\\n\\s*\\n\\s*\\n", "\n");
                log.info(refactoredImports);
                correctedImports = refactoredImports;
                log.info("=================================~ End ~=================================\n\n");
            }
            return isImportOrderCorrect;
        } catch (IOException e) {
            log.error("Error reading the file: {}", e.getMessage());
        }
        return false;
    }

    private static String correctImportOrder(List<String> actualImports) {
        Collections.sort(actualImports);
        String refactoredImports = "";
        for (String expectedImport : Utility.EXPECTED_IMPORT_ORDER) {
            for (String actualImport : actualImports) {
                if (actualImport.contains(expectedImport)) {
                    refactoredImports = refactoredImports.concat(actualImport);
                    refactoredImports = refactoredImports.concat("\n");
                }
            }
            refactoredImports = refactoredImports.concat("\n");
        }
        return refactoredImports;
    }

    private static boolean isImportOrderCorrect(String refactoredImports, List<String> initialActualImportOrder) {
        String strippedString1 = refactoredImports.replaceAll("\\s", "");
        String strippedString2 = String.join("", initialActualImportOrder).replaceAll("\\s", "");
        return strippedString1.equals(strippedString2);
    }
}
