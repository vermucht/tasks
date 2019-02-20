package tasks.algorithm;

import java.util.Arrays;

/**
 * Прикидочный класс на задачу по динамическому программированию.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DynamicCrossbar {

    /**
     * Выясняем, элементы с какой максимальной суммой мы можем выделить из массива num.
     * Используется методика динамического программирования.
     * 1) Создаем таблицу dp, где отмечено, можно ли выделить данную сумму
     * из постепенно возрастающего набора элементов.
     * 2) Заполняем таблицу до значений максимально возможной суммы и максимального
     * имеющегося числа элементов (всего массива).
     * 3) Каждая ячейка таблицы заполняется на основе предыдущих - в этом и есть
     * динамическое программирование.
     *
     * @param num Переданный массив.
     * @return Максимальная "половина суммы", которую можно выделить.
     */
    public int maxHalfSumPartition(int[] num) {
        boolean[][] dp = this.getInitialDpTable(num);
        // this.printDpTable("Изначальная таблица:", num, dp);
        this.fillDpTable(num, dp);
        // this.printDpTable("Заполненная таблица:", num, dp);
        int maxSum = this.getMaxSum(dp);
        // System.out.println("Max sum = " + maxSum);
        return maxSum;
    }

    /**
     * Создаем таблицу dp, где dp[iSum][j] означает, что x1 + x2 + ... + xj = iSum.
     * <p>
     * Если sum = сумма всех элементов массива num, то iSum меняется от 0 до sum/2.
     * Соответственно, размер по оси сумм (вертикаль) будет 1 + sum/2
     * <p>
     * По другой оси мы постепенно откладываем набор элементов:
     * от "нет элементов вообще" до "все элементы".
     * <p>
     * Все значения в столбце {} = false, кроме sum = 0. Мы не можем без
     * элементов выделить сумму > 0. Это значение не нужно заполнять - массив
     * инициализирован false по умолчанию.
     * <p>
     * Также все значения в строке sum = 0 равны true - поскольку нулевую сумму
     * можно выделить в любом случае. Здесь в цикле инициализируем.
     *
     * @param num Массив элементов.
     * @return Изначальную таблицу dp.
     */
    private boolean[][] getInitialDpTable(int[] num) {
        int sum = Arrays.stream(num).sum();
        boolean[][] dp = new boolean[1 + sum / 2][num.length + 1];
        for (int jEltCount = 0; jEltCount < dp[0].length; jEltCount++) {
            dp[0][jEltCount] = true;
        }
        return dp;
    }

    /**
     * Элементы массива: dp[i][j]. Означает, что сумма элементов (x1+...+xj) = i.
     * Мы проходим по элементам таблицы построчно - то есть, берем определенную сумму dp[i] и смотрим по всем [j].
     * Увеличение j означает добавление одного элемента xj == num(j - 1).
     *
     * <p>
     * dp[i][j] = true, если:
     * 1) dp[i][j - 1] = true - уже в прошлом наборе элементов удалось выделить нужную сумму
     * 2) dp[i - xj][j -1] = true - в прошлом наборе элементов как раз не хватало добавленного xj для выделения суммы
     * <p>
     * <p>
     * Элементы dp[0][...] и dp [...][0] у нас уже определены - это либо сумма = 0 (все true),
     * либо пустой массив (все false, кроме sum == 0).
     * <p>
     * Начинаем с элемента dp[1][1]. Он будет true, если:
     * 1) dp[1][0] = true - этот элемент нам известен
     * 2) dp[i - num[0]][0] = true. num[0] - это элемент x1 - поскольку в dp мы начинаем с индекса 1.
     * В случае, если (i - num[0] < 0) возвращаем false - элемент больше, чем требуемая
     * сумма, составить из него сумму никак не получится.
     * В случае, если (i - num[0] >= 0) мы получаем индекс уже известного нам ранее элемента.
     * <p>
     * Проводя аналогичные рассуждения для всех элементов, мы постепенно заполним всю таблицу.
     * <p>
     * При dp[1][1] мы рассматриваем элемент num[0], при dp[1, 2] элемент num[1] и т.д.
     * То есть, при dp[i][j] мы рассматриваем элемент num[j - 1].
     * <p>
     *
     * @param num Массив, на основе которого заполняется dp.
     * @param dp  Заполняемая таблица dp.
     */
    private void fillDpTable(int[] num, boolean[][] dp) {
        for (int iSum = 1; iSum < dp.length; iSum++) {
            for (int j = 1; j < dp[0].length; j++) {
                int added = num[j - 1];
                dp[iSum][j] = dp[iSum][j - 1];
                if (!(dp[iSum][j]) && (iSum - added >= 0)) {
                    dp[iSum][j] = dp[iSum - added][j - 1];
                }
            }
        }
    }

    /**
     * Рассматриваем самый правый столбец таблицы dp (весь массив)
     * и ищем самое нижнее true-значение (самую большую сумму, что можно выделить).
     *
     * @param dp Таблица dp.
     * @return Максимальная сумма.
     */
    private int getMaxSum(boolean[][] dp) {
        int maxSum = 0;
        for (int i = dp.length - 1; i >= 0; i--) {
            if (dp[i][dp[0].length - 1]) {
                maxSum = i;
                break;
            }
        }
        return maxSum;
    }

    /**
     * Печатает таблицу dp - для справки и проверки, что получилось.
     *
     * @param caption Заголовок (название) таблицы.
     * @param num     Массив элементов, на основе которого сделана таблица.
     * @param dp      Таблица dp.
     */
    private void printDpTable(String caption, int[] num, boolean[][] dp) {
        System.out.println(caption);
        System.out.printf("%15s", "");
        StringBuilder array = new StringBuilder("{");
        for (int j = 0; j < dp[0].length; j++) {
            if (j == 1) {
                array.append(num[0]);
            }
            if (j > 1) {
                array.append(",").append(num[j - 1]);
            }
            System.out.printf("%15s", array.toString() + "}");
        }
        for (int i = 0; i < dp.length; i++) {
            System.out.println();
            System.out.printf("%15s", "sum=" + i);
            for (int j = 0; j < dp[0].length; j++) {
                System.out.printf("%15s", dp[i][j]);
            }
        }
        System.out.println();
        System.out.println();
    }
}
