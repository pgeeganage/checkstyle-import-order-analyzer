package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportValidator {

    private static final String[] EXPECTED_IMPORTS = {
            "import java.",
            "import javax.",
            "import org.",
            "import com.",
            "import static org.",
            "import static com.",
            "import ccp.",
            "import lombok."};


    public static void main(String[] args) {
        String absoluteFilePath = "REPLACE_THIS_WITH_YOUR_ABSOLUTE_FILE_PATH"; // Eg:- C:\Users\\user\\YourDirectory\\YourFileName.java
        try {
            if (validateAndFixImportOrder(absoluteFilePath)) {
                System.out.println("Import order is already valid!");
            } else {
                System.out.println("Import order has been corrected.");
            }
        } catch (IOException e) {
            System.err.println("Error reading/writing the file: " + e.getMessage());
        }
    }

    private static boolean validateAndFixImportOrder(String filePath) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            List<String> actualImports = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().contains("import ")) {
                    actualImports.add(line);
                }
            }
            Collections.sort(actualImports);
            String refactoredImports = "";
            System.out.println("\n=============~ Here are the imports with correct order ~===============\n");
            for (String expectedImport : EXPECTED_IMPORTS) {
                for (String actualImport : actualImports) {
                    if (actualImport.contains(expectedImport)) {
                        refactoredImports = refactoredImports.concat(actualImport);
                        refactoredImports = refactoredImports.concat("\n");
                    }
                }
                refactoredImports = refactoredImports.concat("\n");
            }
            System.out.println(refactoredImports);
            System.out.println("================================~ End ~================================\n\n");
            reader.close(); // Remember to close the reader when done
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }
}
