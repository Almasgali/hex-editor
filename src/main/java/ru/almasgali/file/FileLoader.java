package ru.almasgali.file;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLoader {

    private static final String FILE_MODE = "rw";
    private final int chunkSize;
    private final RandomAccessFile raf;
    private final byte[] chunk;
    private int chunkOrder;
    private int read;

    public FileLoader(String path, int chunkSize) throws IOException {
        this.raf = new RandomAccessFile(path, FILE_MODE);
        this.chunkSize = chunkSize;
        this.chunk = new byte[chunkSize];
        this.chunkOrder = 0;
        this.read = 0;
    }

    private void setOffsetForWriting(int off) throws IOException {
        long newPos = raf.getFilePointer() - off;
        if (newPos < 0) {
            newPos = 0;
        }
        raf.seek(newPos);
    }

    public void writeReplacing(byte[] data) throws IOException {
        setOffsetForWriting(data.length);
        raf.write(data);
    }

//    public void writeAppending(byte[] data) throws IOException {
//        int writeChunkSize = 2048;
//        byte[] prevData = new byte[writeChunkSize];
//        int read = raf.read(prevData);
//        raf.write(data);
//        while (read != -1) {
//            setOffsetForWriting();
//            raf.write(data);
//        }
//    }

    private void setOffsetToPreviousChunk() throws IOException {
        long newPos = raf.getFilePointer() - chunkSize - read;
        if (newPos < 0) {
            newPos = 0;
        }
        raf.seek(newPos);
    }

    public int readNextChunk() throws IOException {
        int r = raf.read(chunk);
        if (r != -1) {
            read = r;
            ++chunkOrder;
        }
        return read;
    }

    public int readPrevChunk() throws IOException {
        if (chunkOrder > 1) {
            --chunkOrder;
            setOffsetToPreviousChunk();
            int r =  raf.read(chunk);
            if (r != -1) {
                read = r;
            }
            return r;
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
