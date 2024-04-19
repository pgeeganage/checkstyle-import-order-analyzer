package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.utility.Utility;

public class ImportValidator {

    public static void main(String[] args) {
        String absoluteFilePath = "REPLACE_THIS_WITH_YOUR_ABSOLUTE_FILE_PATH"; // Eg:- C:\Users\\user\\YourDirectory\\YourFileName.java
        System.out.println("\n=============~ Starting the Application ~===============\n");
        try {
            if (validateAndFixImportOrder(absoluteFilePath)) {
                System.out.println("Import order is already valid!");
            } else {
                System.out.println("Import order has been corrected.");
            }
        } catch (IOException e) {
            System.out.printf("Error reading/writing the file: {%s}%n", e.getMessage());
        }
        System.out.println("\n=============~  ~===============\n");
    }

    private static boolean validateAndFixImportOrder(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
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
                System.out.println("\n=============~ Here are the imports with correct order ~===============\n");
                refactoredImports = refactoredImports.replaceAll("\\n\\s*\\n\\s*\\n", "\n");
                System.out.println(refactoredImports);
                System.out.println("=================================~ End ~=================================\n\n");
            }
            return isImportOrderCorrect;
        } catch (IOException e) {
            System.out.println(String.format("Error reading the file: {%s}", e.getMessage()));
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
