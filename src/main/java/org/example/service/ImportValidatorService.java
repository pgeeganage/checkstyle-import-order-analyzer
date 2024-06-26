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
        try {
            if (validateAndFixImportOrder(file)) {
                log.warn("Import order is already valid!");
                return new ResponseDto("Import order is already valid!", null);
            } else {
                log.info("Import order has been corrected.");
                return new ResponseDto("The import order has been corrected successfully!\n"
                        + "Below are the imports with the corrected order:", correctedImports);
            }
        } catch (IOException e) {
            log.error("Error reading/writing the file: {}", e.getMessage());
            return new ResponseDto("Error reading/writing the file: " + e.getMessage(), null);
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
            correctedImports = correctImportOrder(actualImports);
            return isImportOrderCorrect(correctedImports, actualImports);
        } catch (IOException e) {
            log.error("Error reading the file: {}", e.getMessage());
        }
        return false;
    }

    private static String correctImportOrder(List<String> actualImports) {
        // Order the imports in alphabetical order
        Collections.sort(actualImports);

        // Correct the order for imports like: import java.util.Map; import java.util.Map.Entry;
        List<String> subList = new ArrayList<>();
        for (int i = 0; i < actualImports.size(); i++) {
            String importStmt = actualImports.get(i);
            String[] split = importStmt.split("\\.");
            String parentImportStmt = importStmt.replace("." + split[split.length - 1], ";");
            if (actualImports.contains(parentImportStmt)
                    && i < actualImports.indexOf(parentImportStmt)) {
                subList.addAll(actualImports.subList(actualImports.indexOf(parentImportStmt),
                        actualImports.indexOf(parentImportStmt) + 1));
                subList.addAll(actualImports.subList(i, actualImports.indexOf(parentImportStmt)));
                actualImports.subList(i, actualImports.indexOf(parentImportStmt) + 1).clear();
                actualImports.addAll(i, subList);
            }
            subList = new ArrayList<>();
        }

        // Loop through the actual imports and reorder them based on the expected order
        String refactoredImports = "";
        for (String expectedImport : Utility.EXPECTED_IMPORT_ORDER) {
            for (String actualImport : actualImports) {
                if (actualImport.contains(expectedImport)) {
                    refactoredImports = refactoredImports.concat(actualImport);
                    refactoredImports = refactoredImports.concat("\n");
                }
            }
            if (!refactoredImports.endsWith("\n\n")) {
                refactoredImports = refactoredImports.concat("\n");
            }
        }
        return refactoredImports;
    }

    private static boolean isImportOrderCorrect(String refactoredImports, List<String> initialActualImportOrder) {
        String strippedString1 = refactoredImports.replaceAll("\\s", "");
        String strippedString2 = String.join("", initialActualImportOrder).replaceAll("\\s", "");
        return strippedString1.equals(strippedString2);
    }
}
