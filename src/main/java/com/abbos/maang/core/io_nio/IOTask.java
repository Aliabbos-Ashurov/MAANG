package com.abbos.maang.core.io_nio;

import java.io.IOException;

public interface IOTask<I, O> {
    void processInput(I input) throws IOException;

    void processOutput(O output) throws IOException;
}
