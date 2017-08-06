package iao.ru.dataTransfer;

/**
 * Geberal background loader interface with two operations - {@link #execute()} and {@link #cancel()}
 *
 */
public interface Loader {

    /**
     * Performs loading operation in new thread
     */
    void execute();

    /**
     * Cancels current operation
     */
    void cancel();
}
