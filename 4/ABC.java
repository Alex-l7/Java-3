package ru.geekbrains.HW4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ABC {
    private static Object key = new Object();
    private static volatile char curChar = 'A';

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3); // сщ
        executorService.submit(() -> { // вызываем submit , создаётся поток
                    try {
                        for (int i = 0; i < 5; i++) {
                            synchronized (key) { // синхронизируемся по ключу
                                while (curChar != 'A') { // если наш чар не А
                                    key.wait(); // то мы ожидаем
                                }
                                System.out.println("A"); // когда его разбудят, то он выведет А. Еще раз пойдет на итерецию, проверит если А = А, то
                                curChar = 'B'; // сменит своё состояние на В
                                key.notifyAll(); // разбудит остальные все потоки
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

            executorService.submit(() -> { // вызываем submit , создаётся поток
                try {
                    for (int i = 0; i < 5; i++) {
                        synchronized (key) {
                            while (curChar != 'B') {
                                key.wait();
                            }
                            System.out.println("B");
                            curChar = 'C';
                            key.notifyAll();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        });

        executorService.submit(() -> { // вызываем submit , создаётся поток
            try {
                for (int i = 0; i < 5; i++) {
                    synchronized (key) {
                        while (curChar != 'C') {
                            key.wait();
                        }
                        System.out.println("C");
                        curChar = 'A';
                        key.notifyAll();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        executorService.shutdown();

    }
}
