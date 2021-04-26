package pl.glmc.api.common;

public interface Callback<T> {
    void done(T callback);
}