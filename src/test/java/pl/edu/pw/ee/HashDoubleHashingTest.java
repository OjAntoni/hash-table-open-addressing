package pl.edu.pw.ee;

import org.junit.Test;
import pl.edu.pw.ee.services.HashTable;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class HashDoubleHashingTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInitialSizeIsLowerThanOne() {
        int initialSize = 0;

        HashTable<Double> hash = new HashDoubleHashing<>(initialSize);

        assert false;
    }

    @Test
    public void shouldCorrectlyAddNewElemsWhenNotExistInHashTable() {
        HashTable<String> emptyHash = getInstance();
        String newElement = "nothing special";

        int nOfElemsBeforePut = getNumOfElems(emptyHash);
        emptyHash.put(newElement);
        int nOfElemsAfterPut = getNumOfElems(emptyHash);

        assertEquals(0, nOfElemsBeforePut);
        assertEquals(1, nOfElemsAfterPut);
    }

    @Test
    public void shouldCorrectlyAddNewElemsWhenExistInHashTable() {
        HashTable<String> emptyHash = getInstance();
        String elem = "username";
        emptyHash.put(elem);
        int nOfElemsAfterFirstPut = getNumOfElems(emptyHash);
        emptyHash.put("username");
        int nOfElemsAfterSecondPut = getNumOfElems(emptyHash);
        assertEquals(1, nOfElemsAfterFirstPut);
        assertEquals(1, nOfElemsAfterSecondPut);
    }

    @Test
    public void shouldReturnNullIfNotExist(){
        HashTable<Integer> hashTable = getInstance();
        assertNull(hashTable.get(14));
    }

    private int getNumOfElems(HashTable<?> hash) {
        String fieldNumOfElems = "nElems";
        try {
            Field field = hash.getClass().getSuperclass().getDeclaredField(fieldNumOfElems);
            field.setAccessible(true);

            return field.getInt(hash);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Comparable<T>> HashTable<T> getInstance(){
        return new HashDoubleHashing<>();
    }
}