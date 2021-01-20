package ru.geekbrains.HW1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Box<Apple> appleBox1 = new Box<>(5, new Apple(), new Apple(), new Apple());
        Box<Apple> appleBox2 = new Box<>(2);

        System.out.println("Вес первой коробки с яблоками " + appleBox1.getWeight() + " кг");
        appleBox1.transferFruitsToAnotherBox(appleBox2);
        System.out.println("После пересыпки в первой коробке осталось " + appleBox1.getWeight() + " кг яблок");
        System.out.println("Вместительность второй коробки 2 кг, после пересыпки получилось " + appleBox2.getWeight() + " кг яблок");

        Box<Orange> orangeBox1 = new Box<>(10, new Orange(), new Orange(), new Orange());
        Box<Orange> orangeBox2 = new Box<>(5);

        System.out.println();
        System.out.println("Вес первой коробки с апельсинами " + orangeBox1.getWeight() + " кг");
        orangeBox1.transferFruitsToAnotherBox(orangeBox2);
        System.out.println("После пересыпки в первой коробке осталось " + orangeBox1.getWeight() + " кг апельсинов");
        System.out.println("Вместительность второй коробки 5 кг, после пересыпки получилось " + orangeBox2.getWeight() + " кг апельсинов");
    }

    static <T> List<T> arrayToList(T[] array) { // обобщённый метод <T>, который принимает  массив обобщённого типа T[]
        return new ArrayList<>(Arrays.asList(array)); // преобразуем array в ArrayList
    }

    public static <T> void swapArrayElements(T[] arr, int i1, int i2) { // принять массив обобщённого типа, принять 2 индекса
        T elements = arr[i2];
        arr[i2] = arr[i1];
        arr[i1] = elements; // меняем 2 элемента местами
    }
}
