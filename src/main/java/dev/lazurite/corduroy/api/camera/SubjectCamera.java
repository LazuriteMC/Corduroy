package dev.lazurite.corduroy.api.camera;

public interface SubjectCamera<T> {
    void setSubject(T t);
    T getSubject();
}
