import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите свои данные (ФИО, дата рождения, номер телефона, пол), разделенные пробелом:");
        String input = scanner.nextLine();

        Map<String, String> userData = parseInput(input);

        if (userData == null) {
            System.err.println("Ошибка");
            return;
        }

        try {
            validateData(userData);
            saveToFile(userData);
            System.out.println("Данные сохранены в файле " + userData.get("lastName") + ".txt");
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static int checkInput(String[] input) {
        if (input.length < 6) {
            return -1;
        } else if (input.length > 6) {
            return 0;
        }
        return 1;
    }

    private static Map<String, String> parseInput(String input) {
        String[] personalInformation = input.split(" ");

        if (checkInput(personalInformation) == -1) {
            System.err.println("Вы ввели не все данные");
            return null;
        } else if (checkInput(personalInformation) == 0) {
            System.err.println("Вы ввели лишние данные");
            return null;
        } else {
            Map<String, String> userData = new HashMap<>();

            for (String info : personalInformation) {

                if (info.contains(".")) {
                    userData.put("birthday", info);

                } else if (info.matches("\\d+")) {
                    userData.put("phoneNumber", info);

                } else if (info.matches("[a-zA-Z]")) {
                    userData.put("gender", info);

                } else {
                    if (userData.get("lastName") == null) {
                        userData.put("lastName", info);

                    } else if (userData.get("firstName") == null) {
                        userData.put("firstName", info);

                    } else {
                        userData.put("patronymic", info);
                    }
                }
            }
            return userData;
        }
    }

    private static void validateData(Map<String, String> userData) {

        if (!userData.get("lastName").matches("[А-ЯЁ][а-яё]*") ||
                !userData.get("firstName").matches("[А-ЯЁ][а-яё]*") ||
                !userData.get("patronymic").matches("[А-ЯЁ][а-яё]*")) {
            throw new IllegalArgumentException("Фамилия, Имя и Отчество должны начинаться с большой буквы.");
        }

        if (!userData.get("birthday").matches(
        "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(19\\d\\d|20(0[0-9]|1[0-9]|2[0-4]))$")) {
            throw new IllegalArgumentException("Неверно записана дата рождения. Ожидаемый формат - 01.12.1995");
        }

        if (!userData.get("phoneNumber").matches("^\\d{11}$")) {
            throw new IllegalArgumentException("Неверно записан номер телефона. Ожидаемый формат - 89250252525");
        }

        if (!userData.get("gender").matches("[mf]")) {
            throw new IllegalArgumentException("Неверное указан пол. Ожидается m или f");
        }
    }

    private static void saveToFile(Map<String, String> userData) throws IOException {
        FileWriter writer = new FileWriter(userData.get("lastName") + ".txt", true);
        writer.write(userData.get("lastName") + " " + userData.get("firstName") + " " + userData.get("patronymic")
                + " " + userData.get("birthday") + " " + userData.get("phoneNumber") + " " + userData.get("gender") + "\n");
        writer.close();
    }
}
