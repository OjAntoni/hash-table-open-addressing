package pl.edu.pw.ee;

import pl.edu.pw.ee.services.HashTable;

import java.lang.reflect.Array;

public abstract class HashOpenAddressing<T extends Comparable<T>> implements HashTable<T> {

    private final T nullElement = null;
    private int size;
    private int nElems;
    private Elem[] hashElems;
    private final double correctLoadFactor;

    private class Elem {
        private final T value;
        private final boolean isFree;

        public Elem(T value){
            this.value = value;
            isFree = false;
        }

        public Elem(){
            value = null;
            isFree = true;
        }

        public T getValue(){
            return value;
        }

        public boolean isFree(){
            return isFree;
        }
    }

    HashOpenAddressing() {
        this(2039); // initial size as random prime number
    }

    HashOpenAddressing(int size) {
        validateHashInitSize(size);
        this.size = size;
        initTable(size);
        this.correctLoadFactor = 0.75;
    }

    @Override
    public void put(T newElem) {
        validateInputElem(newElem);
        resizeIfNeeded();

        int key = newElem.hashCode();
        int i = 0;
        int hashId = hashFunc(key, i);

        while (hashElems[hashId] != null) {
            if(hashElems[hashId].getValue().compareTo(newElem)==0){
                hashElems[hashId] = new Elem(newElem);
                return;
            }
            if(i+1 == size){
                doubleResize();
                i=0;
            }
            i = (i + 1)%size;
            hashId = hashFunc(key, i);
        }

        hashElems[hashId] = new Elem(newElem);
        nElems++;
    }

    @Override
    public T get(T elem) {
        validateInputElem(elem);

        int key = elem.hashCode();
        int i = 0;
        int hashId = hashFunc(key, i);

        while (hashElems[hashId] != null){
            if(hashElems[hashId].isFree){
                i = (i+1)%size;
                hashId = hashFunc(key, i);
            } else {
                if(hashElems[hashId].getValue().compareTo(elem)!=0){
                    i = (i+1)%size;
                    hashId = hashFunc(key, i);
                } else {
                    return hashElems[hashId].getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void delete(T elem) {
        validateInputElem(elem);

        int key = elem.hashCode();
        int i = 0;
        int hashId = hashFunc(key, i);

        while (hashElems[hashId] != null){
            if(hashElems[hashId].isFree){
                i = (i+1)%size;
                hashId = hashFunc(key, i);
            } else {
                if(hashElems[hashId].getValue().compareTo(elem)!=0){
                    i = (i+1)%size;
                    hashId = hashFunc(key, i);
                } else {

                    hashElems[hashId] = new Elem();
                    nElems--;
                }
            }
        }
    }

    private void validateHashInitSize(int initialSize) {
        if (initialSize < 1) {
            throw new IllegalArgumentException("Initial size of hash table cannot be lower than 1!");
        }
    }

    private void validateInputElem(T newElem) {
        if (newElem == null) {
            throw new IllegalArgumentException("Input elem cannot be null!");
        }
    }

    abstract int hashFunc(int key, int i);

    int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        double loadFactor = countLoadFactor();

        if (loadFactor >= correctLoadFactor) {
            doubleResize();
        }
    }

    private double countLoadFactor() {
        return (double) nElems / size;
    }

    protected void doubleResize() {
        Elem[] oldHashElem = ((Elem[]) Array.newInstance(Elem.class, nElems));
        for (int i = 0, j = 0; i < hashElems.length; i++) {
            if(hashElems[i]!=null){
                oldHashElem[j] = new Elem(hashElems[i].getValue());
                j++;
            }
        }

        this.size *= 2;
        initTable(size);

        for (Elem elem : oldHashElem) {
            if(elem != null){
                put(elem.getValue());
            }
        }
    }

    private void initTable(int size){
        hashElems = ((Elem[]) Array.newInstance(Elem.class, size));
    }
}