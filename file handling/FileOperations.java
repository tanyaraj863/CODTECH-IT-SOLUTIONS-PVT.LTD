import java.io.*;

public class FileOperations {

    // Method to write content to a file
    public static void writeFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            System.out.println("✅ File written successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error writing file: " + e.getMessage());
        }
    }

    // Method to read content from a file
    public static void readFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            System.out.println("\n📖 File Content:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
    }

    // Method to modify file content (replace a word)
    public static void modifyFile(String fileName, String oldWord, String newWord) {
        File file = new File(fileName);
        StringBuilder newContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Replace all occurrences of oldWord with newWord
                line = line.replaceAll(oldWord, newWord);
                newContent.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("❌ Error modifying file: " + e.getMessage());
            return;
        }

        // Write modified content back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(newContent.toString());
            System.out.println("✅ File modified successfully (Replaced '" + oldWord + "' with '" + newWord + "').");
        } catch (IOException e) {
            System.out.println("❌ Error writing modified file: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        String fileName = "sample.txt";

        // Step 1: Write content to file (includes your name)
        String content = """
                         Hello, my name is Manish.
                         This is a sample text file created in Java.
                         File handling in Java is very useful.
                         Have a great day, Manish!""";
        writeFile(fileName, content);

        // Step 2: Read and display file content
        readFile(fileName);

        // Step 3: Modify file (replace word 'Manish' with 'Java Programmer')
        modifyFile(fileName, "Manish", "Java Programmer");

        // Step 4: Read again after modification
        readFile(fileName);
    }
}
