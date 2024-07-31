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
    }

    private void setOffsetForWriting() throws IOException {
        long newPos = raf.getFilePointer() - chunkSize;
        if (newPos < 0) {
            newPos = 0;
        }
        raf.seek(newPos);
    }

    public void writeReplacing(byte[] data) throws IOException {
        setOffsetForWriting();
        raf.write(data);
    }

    public void writeAppending(byte[] data) throws IOException {
        setOffsetForWriting();
        byte[] prevData = new byte[data.length];
        int read = raf.read(prevData);
        while (read != -1) {
            setOffsetForWriting();
            raf.write(data);
        }
    }

    private void setOffsetToPreviousChunk() throws IOException {
        long newPos = raf.getFilePointer() - chunkSize * 2L;
        if (newPos < 0) {
            newPos = 0;
        }
        raf.seek(newPos);
    }

    public int readNextChunk() throws IOException {
        int result = raf.read(chunk);
        if (result != -1) {
            ++chunkOrder;
        }
        return result;
    }

    public int readPrevChunk() throws IOException {
        if (chunkOrder > 1) {
            --chunkOrder;
            setOffsetToPreviousChunk();
            return raf.read(chunk);
        } else {
            return -1;
        }
    }

    public byte[] getChunk() {
        return chunk;
    }
    
    public int getChunkOrder() {
        return chunkOrder;
    }
}
