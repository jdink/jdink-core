package de.siteof.jdink.util.io;

import java.io.DataOutput;
import java.io.IOException;

/**
 * <p>PacketDataOutput is very similar to {@link DataOutput} but
 * can automatically handle packet alignments. That is particular useful when
 * a native record format was directly written with packet alignment enabled
 * (usually such exported packet formats shouldn't have the alignment enabled though).</p>
 * @see PacketDataInput
 */
public interface PacketDataOutput extends DataOutput {

	/**
	 * Same as calling <em>skipAlignmentFor</em> with the max pack size.
	 */
	void skipAlignment() throws IOException;

	/**
	 * Align for for the given size.
	 * (The smaller of the size and the configured max pack size will be used)
	 * @param size the alignment size
	 */
	void skipAlignmentFor(int size) throws IOException;

	/**
	 * Returns the current position.
	 * @return the position
	 */
	long getPosition() throws IOException;

	/**
	 * Begins a new structure with the given max member size.
	 * @param maxMemberSize the max member size
	 */
	void beginStructure(int maxMemberSize) throws IOException;

	/**
	 * Ends the current structure.
	 * This method throws an exception if the actual max member size doesn't
	 * match the one provided to {@link #beginStructure(int)}.
	 */
	void endStructure() throws IOException;

}
