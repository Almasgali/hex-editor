package ru.almasgali.file;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLoader {

    private static final String FILE_MODE = "rw";
    private final int chunkSize;
    private final RandomAccessFile raf;
    private final byte[] chunk;
    private int chunkOrder;

    public FileLoader(String path, int chunkSize) throws IOException {
        this.raf = new RandomAccessFile(path, FILE_MODE);
        this.chunkSize = chunkSize;
        this.chunk = new byte[chunkSize];
        this.chunkOrder = 0;
//        raf.read(chunk);
    }

    private void setOffsetToPreviousChunk() throws IOException {
        long newPos = raf.getFilePointer() - chunkSize * 2;
        if (newPos < 0) {
            newPos = 0;
        }
        raf.seek(newPos);
    }

    public int readNextChunk() throws IOException {
        ++chunkOrder;
        return raf.read(chunk);
    }

    public int readPrevChunk() throws IOException {
        --chunkOrder;
        setOffsetToPreviousChunk();
        return raf.read(chunk);
    }

    public byte[] getChunk() {
        return chunk;
    }

    public int getChunkOrder() {
        return chunkOrder;
    }
}
