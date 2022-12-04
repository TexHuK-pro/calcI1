import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.print("Введите веражение: ");
            String n = in.nextLine();
            calc(n);
        }
    }

    public static String calc(String input) {

        int result;
        try {

            //переменные выражения
            String val1, val2, operator;

            //конечный результат
            result = 0;

            //запишим выражение в строковой массив
            String[] expression = input.split(" ");

            //проверка валидности выражения

            if (expression[0] == "") {
                throw new IllegalArgumentException("Выражение отсутствует..");
            }

            if (expression.length > 3) {
                throw new IllegalArgumentException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
            }

            if (expression.length != 3) {
                throw new IllegalArgumentException("строка не является математической операцией или выражение было введено не коректно");
            }

            //разложим элементы массива по переменным
            val1 = expression[0];
            val2 = expression[2];
            operator = expression[1];

            //проверяем, есть ли значении арабские цифры
            ArrayList haystack = new ArrayList();
            for (int i = 1; i < 100; i++) {
                haystack.add(Integer.toString(i));
            }
            boolean found1 = haystack.contains(val1);
            boolean found2 = haystack.contains(val2);

            if (found1 && found2) {
                if (Integer.parseInt(val1) > 10 || Integer.parseInt(val2) > 10) {
                    throw new IllegalArgumentException("Выражение имеет операнд больше 10");
                }
            }

            if (!found1 && found2 || found1 && !found2) {
                throw new IllegalArgumentException("Используются одновременно разные системы счисления");
            }

            //если арабских чисел нету, тогда преобразовываем римские числа в арабские
            if (!found1 && !found2) {
                val1 = String.valueOf(romanToArabic(val1));
                val2 = String.valueOf(romanToArabic(val2));
            }

            if (Integer.parseInt(val1) <= 0 || Integer.parseInt(val2) <= 0) {
                throw new IllegalArgumentException("операнд не может быть меньше, или равняться нулю");
            }

            //вычесление оператора и действие
            switch (operator) {
                case "+":
                    result = Integer.parseInt(val1) + Integer.parseInt(val2);
                    break;
                case "-":
                    result = Integer.parseInt(val1) - Integer.parseInt(val2);
                    break;
                case "*":
                    result = Integer.parseInt(val1) * Integer.parseInt(val2);
                    break;
                case "/":
                    result = Integer.parseInt(val1) / Integer.parseInt(val2);
                    break;
                default:
                    System.out.println("Некоректный оператор, используйте только (+ - / *)");
            }
            if (found1 && found2) {
                System.out.println(result);
            } else {
                String romanResult = arabicToRoman(result);
                if (romanResult == "") {
                    romanResult = "0";
                }
                System.out.println(romanResult);
            }

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Используются одновременно разные системы счисления 2");
        }
        return String.valueOf(result);
    }

    //метод арабские в римские
    static String arabicToRoman(int number) {

        //проверка на отрицание
        boolean isPositive = ((number % (number - 0.03125)) * number) / 0.03125 == number;
        if (!isPositive) {
            throw new IllegalArgumentException("в римской системе нет отрицательных чисел");
        }

        if ((number < 0) || (number > 4000)) {
            throw new IllegalArgumentException("Результат не соответствует диапозону римских чисел от 0 до 4000..");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }

    //метод римские в арабские
    static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        return result;
    }
}