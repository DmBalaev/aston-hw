package dm.bl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {
    private final CustomArrayList<Integer> customArrayList = new CustomArrayList<>();

    @Test
    public void add_shouldAddElement(){
        customArrayList.add(1);

        assertEquals(customArrayList.size(), 1);
    }

    @Test
    public void addWithIndex_shouldAddElement(){
        customArrayList.add(1);
        customArrayList.add(2);
        customArrayList.add(3);
        customArrayList.add(6, 1);

        assertEquals(customArrayList.size(), 4);
        assertEquals(customArrayList.get(1), 6);
    }

    @Test
    public void addWithBadIndex_shouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> customArrayList.add(4, 100));
    }

    @Test
    public void addWithNegativeIndex_shouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> customArrayList.add(4, -1));
    }

    @Test
    public void get_shouldReturnElementByIndex(){
        customArrayList.add(1);
        int expected = customArrayList.get(0);

        assertEquals(expected, 1);
    }

    @Test
    public void getWithBadIndex_shouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> customArrayList.get(100));
    }

    @Test
    public void removeFirst_shouldRemoveFirstElement(){
        customArrayList.add(1);
        customArrayList.add(2);

        customArrayList.removeFirst();

        assertEquals(customArrayList.size(), 1);
        assertEquals(customArrayList.get(0), 2);
    }

    @Test
    public void clear_shouldClearAllElement(){
        customArrayList.add(1);

        customArrayList.clear();

        assertEquals(customArrayList.size(), 0);
    }

    @Test
    public void set_shouldReplaceElementByIndex(){
        customArrayList.add(1);
        customArrayList.add(2);

        customArrayList.set(0, 99);

        assertEquals(customArrayList.get(0), 99);
    }

    @Test
    public void quickSort_shouldSortedElements(){
        customArrayList.add(2);
        customArrayList.add(7);
        customArrayList.add(1);
        CustomArrayList<Integer> sorted = new CustomArrayList<>();
        sorted.add(1);
        sorted.add(2);
        sorted.add(7);

        customArrayList.quickSort();

        assertEquals(customArrayList, sorted);
    }

    @Test
    public void equals_shouldReturnFalse(){
        customArrayList.add(1);
        CustomArrayList<Integer> other = new CustomArrayList<>();

        assertNotEquals(customArrayList, other);
    }
}