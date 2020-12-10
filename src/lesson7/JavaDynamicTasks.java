package lesson7;

import kotlin.NotImplementedError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    //Время(n * m), n и m - длины строк
    //Ресурсы(n * m)
    public static String longestCommonSubSequence(String first, String second) {
        int[][] table = new int[first.length() + 1][second.length() + 1];
        for (int i = 1; i < first.length() + 1; i++)
            for (int j = 1; j < second.length() + 1; j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1))
                    table[i][j] = table[i - 1][j - 1] + 1;
                else table[i][j] = Math.max(table[i][j - 1], table[i - 1][j]);
                //почему не дают тернарный оператор использовать(((
            }

        StringBuilder sb = new StringBuilder();
        int a = first.length();
        int b = second.length();
        while(table[a][b] != 0) {
            if (table[a][b] == table[a][b - 1]) a++;
            else if (table[a][b] == table[a - 1][b]) b++;
            else sb.append(first.charAt(a-1));
            a--;
            b--;
        }
        return sb.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        throw new NotImplementedError();
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    //Время O(m * n), m и n - размерности поля
    //Ресурсы O(m * n)
    public static int shortestPathOnField(String inputName) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(inputName));
        String str;
        List<String> list = new ArrayList<>();
        while((str = bf.readLine()) != null) {
            list.add(str);
        }
        bf.close();

        int[][] table = new int[list.size()][list.get(0).split(" ").length];
        for (int i = 0; i < table.length; i++) {
            String[] tmp = list.get(i).split(" ");
            for (int j = 0; j < table[0].length; j++) {
                table[i][j] = Integer.parseInt(tmp[j]);
            }
        }

        int[][] weight = new int[table.length][table[0].length];
        for (int i = 0; i < table.length; i++)
            for (int j = 0; j < table[0].length; j++){
                if (i == 0 && j == 0) weight[i][j] = 0;
                else if (i == 0) weight[i][j] = weight[i][j - 1] + table[i][j];
                else if (j == 0) weight[i][j] = weight[i - 1][j] + table[i][j];
                else weight[i][j] = table[i][j] +
                        Math.min(Math.min(weight[i - 1][j], weight[i][j - 1]), weight[i - 1][j - 1]);
            }
        return weight[table.length - 1][table[0].length - 1];
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
