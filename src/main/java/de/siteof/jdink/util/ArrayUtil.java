/*
 * Created on 03.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.util;

import java.lang.reflect.Array;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArrayUtil {

    /**
     * Copies the specified array, truncating or padding with nulls (if necessary)
     * so the copy has the specified length.  For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values.  For any indices that are valid in the
     * copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length
     * is greater than that of the original array.
     * The resulting array is of exactly the same class as the original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls
     *     to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    /**
     * Copies the specified array, truncating or padding with zeros (if necessary)
     * so the copy has the specified length.  For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values.  For any indices that are valid in the
     * copy but not the original, the copy will contain <tt>0</tt>.
     * Such indices will exist if and only if the specified length
     * is greater than that of the original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros
     *     to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static int[] copyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with nulls (if necessary)
     * so the copy has the specified length.  For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values.  For any indices that are valid in the
     * copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length
     * is greater than that of the original array.
     * The resulting array is of the class <tt>newType</tt>.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @param newType the class of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls
     *     to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @throws ArrayStoreException if an element copied from
     *     <tt>original</tt> is not of a runtime type that can be stored in
     *     an array of class <tt>newType</tt>
     * @since 1.6
     */
    @SuppressWarnings("unchecked")
	public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }

	public static Object[] ensureCapacity(Object[] a, Class<?> componentType, int requiredLength, int increasingSize) {
		if (a.length < requiredLength) {
			Object[] a2 = (Object[]) Array.newInstance(componentType, requiredLength + increasingSize);
			System.arraycopy(a, 0, a2, 0, a.length);
			return a2;
		} else {
			return a;
		}
	}
	
	public static Object[] ensureCapacity(Object[] a, Class<?> componentType, int requiredLength) {
		return ensureCapacity(a, componentType, requiredLength, 10);
	}
}
