package cs2340.woms.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.database.DataSetObservable;

/**
 * An observable list which delegates all actual list calls to a backing list.
 */
public class ObservableList<E> extends DataSetObservable implements List<E> {

    private List<E> backingList;

    public ObservableList(List<E> backing) {
        this.backingList = backing;
    }

    @Override
    public boolean add(E object) {
        boolean ret = backingList.add(object);
        this.notifyChanged();
        return ret;
    }

    @Override
    public void add(int location, E object) {
        backingList.add(location, object);
        this.notifyChanged();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
       boolean ret = backingList.addAll(c);
       this.notifyChanged();
       return ret;
    }

    @Override
    public boolean addAll(int arg0, Collection<? extends E> c) {
        boolean ret = backingList.addAll(arg0, c);
        this.notifyChanged();
        return ret;
    }

    @Override
    public void clear() {
        backingList.clear();
        this.notifyChanged();
    }

    @Override
    public boolean contains(Object object) {
        return backingList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    @Override
    public E get(int location) {
        return backingList.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return backingList.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return backingList.isEmpty();
    }

    // TODO: Implement a custom iterator that notifies the adapter when
    // operations such as remove() are called.
    @Override
    public Iterator<E> iterator() {
        return backingList.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return backingList.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return backingList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int location) {
        return backingList.listIterator(location);
    }

    @Override
    public E remove(int location) {
        E ret = backingList.remove(location);
        this.notifyChanged();
        return ret;
    }

    @Override
    public boolean remove(Object object) {
        boolean ret = backingList.remove(object);
        this.notifyChanged();
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ret = backingList.removeAll(c);
        this.notifyChanged();
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ret = backingList.retainAll(c);
        this.notifyChanged();
        return ret;
    }

    @Override
    public E set(int location, E object) {
        E ret = backingList.set(location, object);
        this.notifyChanged();
        return ret;
    }

    @Override
    public int size() {
        return backingList.size();
    }

    @Override
    public List<E> subList(int start, int end) {
        return backingList.subList(start, end);
    }

    @Override
    public Object[] toArray() {
        return backingList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return backingList.toArray(array);
    }
}
