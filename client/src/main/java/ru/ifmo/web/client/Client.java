package ru.ifmo.web.client;

import ru.ifmo.web.client.util.Command;
import ru.ifmo.web.client.util.UsersResourceIntegration;
import ru.ifmo.web.database.dto.UserDTO;
import ru.ifmo.web.database.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;


public class Client {
    public static void main(String... args) throws SQLException_Exception, IOException {
        UsersResourceIntegration usersResourceIntegration =  new UsersResourceIntegration();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int currentState = 0;
        Command command;
        UserDTO userDTO;
        Long id;

        writeHelp();
        while (true) {
            currentState = readState(reader);
            if (currentState < 0 || currentState > Command.values().length) {
                continue;
            } else if (currentState == 0) {
                writeHelp();
                continue;
            }
            command = Command.values()[currentState - 1];
            switch (command) {
                case FIND_ALL:
                    usersResourceIntegration.findAll().stream().map(Client::userToString).forEach(System.out::println);
                    break;
                case FIND_BY_FILTERS:
                    System.out.println("Введите значения полей, по которым хотите производить фильтрацию.\n" +
                            "Чтобы не применять фильтр, оставьте значение пустым");
                    System.out.print("id: ");
                    id = readLong(reader);
                    userDTO = readUser(reader);

                    usersResourceIntegration.findWithFilters(id, userDTO.getLogin(), userDTO.getPassword(),
                            userDTO.getEmail(), userDTO.getGender(), userDTO.getRegisterDate())
                            .stream().map(Client::userToString).forEach(System.out::println);
                    break;
                case INSERT:
                    System.out.println("Введите поля нового пользователя:");
                    userDTO = readUser(reader);
                    System.out.println("Пользоваетль успешно добавлен. Его id: " + usersResourceIntegration.insert(userDTO));
                    break;
                case UPDATE:
                    System.out.print("Введите id пользователя, которого хотите изменить: ");
                    id = readLong(reader);
                    System.out.println("Введите новые поля пользователя");
                    userDTO = readUser(reader);
                    System.out.println(String.format(
                            "Обновлено %s пользователей",
                            usersResourceIntegration.update(id, userDTO)
                            )
                    );
                    break;
                case DELETE:
                    System.out.print("Введите id пользователя, которого хотите удалить: ");
                    id = readLong(reader);
                    System.out.println(usersResourceIntegration.delete(id));
                    break;
                case QUIT:
                    return;
            }
        }
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static UserDTO readUser(BufferedReader reader) {
        System.out.print("login: ");
        String login = readString(reader);
        System.out.print("password: ");
        String password = readString(reader);
        System.out.print("email: ");
        String email = readString(reader);
        System.out.print("gender: ");
        Boolean gender = readBoolean(reader);
        System.out.print("registerDate(yyyy-mm-dd): ");
        Date registerDate = readDate(reader);
            String date = null;
            if (registerDate != null) {
                date = sdf.format(registerDate);
            }
        return new UserDTO(login, password, email, gender,  date);
    }

    private static String readString(BufferedReader reader) {
        String trim = null;
        try {
            trim = reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (trim.isEmpty()) {
            return null;
        }
        return trim;
    }

    private static Date readDate(BufferedReader reader) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date rd = sdf.parse(reader.readLine());
            return rd;
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    private static Long readLong(BufferedReader reader) {
        try {
            return Long.parseLong(reader.readLine());
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    private static Boolean readBoolean(BufferedReader reader) {
        try {
            String s = reader.readLine();
            if (s.equals("")) {
                return null;
            }
            return Boolean.parseBoolean(s);
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    private static int readState( BufferedReader reader) {
        try {
            System.out.print("> ");
            String s = reader.readLine();
            return "help".equals(s) ? 0 : Integer.parseInt(s);
        } catch (java.lang.Exception e) {
            return -1;
        }
    }

    private static void writeHelp() {
        System.out.println("\nВыберите один из пунктов:");
        System.out.println("0. Вывести help");
        for (Command value : Command.values()) {
            System.out.println(1 + value.ordinal() + ". " + value.getHelp());
        }
    }

    public static String userToString(User user) {
        return "User{" +
                "id=" + user.getId() +
                ", login='" + user.getLogin() + '\'' +
                ", email='" + user.getEmail() + '\'' +
                ", gender=" + user.getGender() +
                ", registerDate=" + Optional.ofNullable(user.getRegisterDate()).map(sdf::format).orElse("null") +
                '}';
    }

}
