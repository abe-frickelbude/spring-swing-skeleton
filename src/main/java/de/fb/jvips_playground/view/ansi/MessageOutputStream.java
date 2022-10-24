package de.fb.jvips_playground.view.ansi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Small adapter class that automatically dumps its content to the specified consumer upon flush()
 * and then automatically resets itself.
 */
class MessageOutputStream extends ByteArrayOutputStream {

    private final Consumer<String> consumer;

    public MessageOutputStream(final int initialSize, final Consumer<String> consumer) {
        super(initialSize);
        this.consumer = consumer;
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        this.consumer.accept((super.toString()));
        super.reset();
    }
}
