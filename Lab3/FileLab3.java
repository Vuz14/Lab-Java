package Lab3;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FileLab3 {

    // 1. Đọc và ghi dữ liệu từ file
    public static void copyFile(String source, String destination) throws IOException {
        File inputFile = new File(source);
        if (!inputFile.exists()) {
            System.out.println("File nguồn không tồn tại!");
            return;
        }

        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }

    // 2. Đọc dữ liệu từ bàn phím và ghi vào file
    public static void writeToFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             FileWriter fw = new FileWriter(fileName)) {
            System.out.println("Nhập dữ liệu (nhập 'exit' để thoát):");
            String line;
            while (!(line = br.readLine()).equalsIgnoreCase("exit")) {
                fw.write(line + "\n");
            }
        }
    }

    // 3. Tính số dòng trong một file
    public static int countLines(String fileName) throws IOException {
        File inputFile = new File(fileName);
        if (!inputFile.exists()) {
            System.out.println("File không tồn tại!");
            return 0;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            int lines = 0;
            while (br.readLine() != null) lines++;
            return lines;
        }
    }

    // 4. Ghi danh sách số nguyên vào file và đọc lại
    public static void writeIntegers(String fileName, List<Integer> numbers) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            for (int num : numbers) {
                dos.writeInt(num);
            }
        }
    }

    public static List<Integer> readIntegers(String fileName) throws IOException {
        File inputFile = new File(fileName);
        if (!inputFile.exists()) {
            System.out.println("File không tồn tại!");
            return Collections.emptyList();
        }

        List<Integer> numbers = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            while (dis.available() > 0) {
                numbers.add(dis.readInt());
            }
        }
        return numbers;
    }

    // 5. Liệt kê tất cả các file trong một thư mục
    public static void listFiles(String directoryPath) {
        File folder = new File(directoryPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Thư mục không tồn tại!");
            return;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Sử dụng file có sẵn là Lab3.txt
        String inputFile = "src//Lab3//Lab3.txt";
        File file = new File(inputFile);
        if (!file.exists()) {
            System.out.println("File Lab3.txt không tồn tại! Hãy tạo hoặc đảm bảo file có sẵn.");
            return;
        }

        copyFile("src//Lab3//Lab3.txt", "output.txt");
        writeToFile("user_input.txt");
        System.out.println("Số dòng: " + countLines("Lab3.txt"));
        writeIntegers("numbers.dat", Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("Số nguyên đọc được: " + readIntegers("numbers.dat"));
        listFiles(".");
    }
}
