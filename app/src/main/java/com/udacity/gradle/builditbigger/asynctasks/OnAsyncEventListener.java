package com.udacity.gradle.builditbigger.asynctasks;

public interface OnAsyncEventListener<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}
