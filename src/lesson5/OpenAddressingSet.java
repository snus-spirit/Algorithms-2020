package lesson5;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }
    // Смещает max int на (31 - bits), далее побитовый And

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;// Делает вместимость 2^bits
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }//Если я ищу несуществующий элемент в полной таблице, то зациклится
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;

    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != DELETED) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    // Время O(n)
    // Ресурсы O(1)
    @Override
    public boolean remove(Object o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != DELETED) {
            if (current.equals(o)) {
                storage[index] = DELETED;
                size--;
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) return false;
            current = storage[index];
        }
        return false;
    }

    private final Object DELETED = new Object();

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingSetIterator();
    }

    public class OpenAddressingSetIterator implements Iterator<T> {
        private int index = 0;
        private int count = 0;
        private T lastCurrent;

        //Время O(1)
        //Ресурсы O(1)
        @Override
        public boolean hasNext() {
            return count < size;
        }

        //Время O(n)
        //Ресурсы O(1)
        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext())
                throw new IllegalStateException();
            while (storage[index] == null || storage[index] == DELETED)
                index++;
            lastCurrent = (T) storage[index];
            index++;
            count++;
            return lastCurrent;
        }

        //Время O(1)
        //Ресурсы O(1)
        @Override
        public void remove() {
            if (lastCurrent == null)
                throw new IllegalStateException();
            storage[index - 1] = DELETED;
            lastCurrent = null;
            size--;
            count--;
        }

    }
}
