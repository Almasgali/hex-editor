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
        this.read = data.length;
        raf.write(data);
    }

    public void writeAppending(byte[] data) throws IOException {
        int writeChunkSize = Math.max(2048, data.length);
        byte[] prevData = new byte[writeChunkSize];
        int read = raf.read(prevData);
        long revertForRead = raf.getFilePointer();
        if (read == -1) {
            raf.write(data);
            this.read += data.length;
            return;
        }
        setOffsetForWriting(read);
        this.read += data.length;
        raf.write(data);
        long revertForWrite = raf.getFilePointer();
        data = new byte[writeChunkSize];
        long startPos = raf.getFilePointer();
        boolean flag = true;
        while (read != -1) {
            raf.seek(revertForRead);
            int newRead;
            if (flag) {
                newRead = raf.read(data);
                revertForRead = raf.getFilePointer();
                raf.seek(revertForWrite);
                raf.write(prevData, 0, read);
                revertForWrite = raf.getFilePointer();
            } else {
                newRead = raf.read(prevData);
                revertForRead = raf.getFilePointer();
                raf.seek(revertForWrite);
                raf.write(data, 0, read);
                revertForWrite = raf.getFilePointer();
            }
            read = newRead;
            flag = !flag;
        }
        raf.seek(startPos);
    }

    private void setOffsetToPreviousChunk() throws IOException {
        long newPos = raf.getFilePointer() - chunkSize - this.read;
        if (newPos < 0) {
            newPos = 0;
        }
        raf.seek(newPos);
    }

    public int readNextChunk() throws IOException {
        int r = raf.read(chunk);
        if (r != -1) {
            this.read = r;
            ++chunkOrder;
        }
        return this.read;
    }

    public int readPrevChunk() throws IOException {
        if (chunkOrder > 1) {
            --chunkOrder;
            setOffsetToPreviousChunk();
            int r = raf.read(chunk);
            if (r != -1) {
                this.read = r;
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
