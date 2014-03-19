package org.homenet.dnoved1.woms;

/**
 * A 2-tuple. Does not support null elements.
 *
 * @param <A> the type of the first element.
 * @param <B> the type of the second element.
 */
public class Pair<A, B> {

    /**The first element of this pair.*/
    public final A first;
    /**The second element of this pair.*/
    public final B second;

    /**
     * Creates a new pair with the given elements.
     *
     * @param first the first element of the pair.
     * @param second the second element of the pair.
     * @throws IllegalArgumentException if any of the given elements are null.
     */
    public Pair(A first, B second) throws IllegalArgumentException {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Null values are not supported");
        }

        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof Pair)) {
            return false;
        }

        @SuppressWarnings("rawtypes")
        Pair<?, ?> other = (Pair) o;
        return first.equals(other.first) && second.equals(other.second);
    }

    @Override
    public int hashCode() {
        int hashCode = first.hashCode();
        hashCode = hashCode * 31 + second.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append('(');
        string.append(first);
        string.append(", ");
        string.append(second);
        string.append(')');
        return string.toString();
    }
}
