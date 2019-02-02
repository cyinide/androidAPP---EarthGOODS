package com.example.guestuser.damirkrkalicearthgoods.helper;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class MyAbstractRunnable<T> implements Serializable {

    public abstract void run(T t);

    public abstract void error(@Nullable Integer statusCode, @Nullable String errorMessage);

    public Class<T> getGenericType()
    {
        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];

        return persistentClass;
    }

}
