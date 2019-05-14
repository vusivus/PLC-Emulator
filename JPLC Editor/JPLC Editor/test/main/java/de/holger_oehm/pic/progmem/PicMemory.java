package de.holger_oehm.pic.progmem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PicMemory {
    public static final int CHUNK_SIZE = 0x40;
    private static final int CHUNK_MASK = ~(CHUNK_SIZE - 1);

    private static class Chunk {
        private final byte[] chunkBytes = new byte[CHUNK_SIZE];

        public Chunk() {
            Arrays.fill(chunkBytes, (byte) 0xff);
        }

        private int setBytes(final int address, final byte[] bytes) {
            int result = 0;
            for (int i = 0; i < bytes.length; i++) {
                final byte b = bytes[i];
                final int localIdx = address + i;
                if (localIdx >= CHUNK_SIZE) {
                    break;
                }
                chunkBytes[localIdx] = b;
                result++;
            }
            return result;
        }

        private byte getByte(final int address) {
            return chunkBytes[address];
        }
    }

    private final SortedMap<Integer, Chunk> chunkMemory = new TreeMap<>();

    public int getByte(final int address) {
        final int chunkAddress = chunkAddress(address);
        final Chunk chunk = chunkMemory.get(chunkAddress);
        if (chunk == null) {
            return 0xff;
        }
        final byte byteValue = chunk.getByte(address - chunkAddress);
        return byteValue & 0xff;
    }

    public void setBytes(final int address, final byte[] bytes) {
        if (!needStore(bytes)) {
            return;
        }
        final int chunkAddress = chunkAddress(address);
        final Chunk chunk;
        if (chunkMemory.containsKey(chunkAddress)) {
            chunk = chunkMemory.get(chunkAddress);
        } else {
            chunk = new Chunk();
            chunkMemory.put(chunkAddress, chunk);
        }
        final int wrote = chunk.setBytes(address - chunkAddress, bytes);
        if (wrote < bytes.length) {
            setBytes(address + wrote, Arrays.copyOfRange(bytes, wrote, bytes.length));
        }
    }

    private boolean needStore(final byte[] bytes) {
        for (final byte b : bytes) {
            if ((b & 0xff) != 0xff) {
                return true;
            }
        }
        return false;
    }

    private int chunkAddress(final int address) {
        return address & CHUNK_MASK;
    }

    public void setBytes(final int address, final int... integers) {
        final byte[] bytes = new byte[integers.length];
        for (int i = 0; i < integers.length; i++) {
            final int value = integers[i];
            if (0 > value || value > 255) {
                throw new IllegalArgumentException("all values must be between 0 and 255: " + Arrays.toString(integers));
            }
            bytes[i] = (byte) value;
        }
        setBytes(address, bytes);
    }

    public List<Integer> getChunkAddresses() {
        final List<Integer> result = new ArrayList<>();
        result.addAll(chunkMemory.keySet());
        return result;
    }

    public byte[] getBytes(final int address, final int length) {
        final byte[] result = new byte[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) getByte(address + i);
        }
        return result;
    }

}
