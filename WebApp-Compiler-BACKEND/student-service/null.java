import java.util.Scanner;

public class AddIntegers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double num1 = scanner.nextDouble();
        double num2 = scanner.nextDouble();
        double num3 = scanner.nextDouble();
        double num4 = scanner.nextDouble();
        double sum = num1 + num2 + num3+num4;

        System.out.println(sum);

        // Close the scanner to prevent resource leak
        scanner.close();
    }
}

