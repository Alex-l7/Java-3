package ru.geekbrains.HW1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Box<T extends Fruit> { //коробка обобщенного типа принимает только типы наследники класса Fruit
    private final List<T> container; // список данных фруктов
    private int capacity; // размер для нашей коробки

    public Box(int capacity, T ...fruits){ // через варарги вмето гетеров и сетеров заполняем фрукты
        this.container = new ArrayList<>(Arrays.asList(fruits));
        this.capacity = capacity;
    }

    public float getWeight() { // получение веса
        float commonWeight = 0.0f;

        for (T fruit : container) {
            commonWeight += fruit.getWeight(); // сложили все веса фруктов в данной коробке
        }
        return commonWeight;
    }

        public boolean weightCompare(Box<?> anotherBox){ // сравнение двух весов
            return Math.abs(this.getWeight() - anotherBox.getWeight()) < 0.01;// чтобы сравнить float значения ищем разность по модулю и проверить, что она меньше какого-то значения
        }

        public void transferFruitsToAnotherBox(Box<T> anotherBox){ // метод пересыпки фруктов
            if(anotherBox == this) return; // проверяем, что коробка которая к нам пришла не является текущим объектом

            int countSize = Math.min(container.size(), anotherBox.capacity);// минимальное значение размера коробки и размерности

            List<T> fruits = container.subList(0, countSize); // берём подсписок нашего объекта из которого хотим пересыпать
            anotherBox.container.addAll(fruits);
            container.removeAll(fruits);

            anotherBox.capacity -= countSize;
            capacity += countSize;
        }

        public void addFruit(T fruit) {
            if(capacity - 1 > 0) {
                container.add(fruit);
                capacity --;
            }
        }
    }

