package com.bookshopweb.dao;

import java.util.List;
import java.util.Optional;

public interface IDAO<T> {
    int insert(T t, String ip);

    int update(T t, String ip);

    int delete(T t, String ip);

    List<T> getAll();

    List<T> getPart(int limit, int offset);

    List<T> getOrderedPart(int limit, int offset, String orderBy, String orderDir);

    T selectPrevalue(Long id);

}