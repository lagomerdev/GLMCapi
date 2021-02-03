package pl.glmc.api.common;

public interface Callback<T, E> {
    void done(T callback, E throwable);
}