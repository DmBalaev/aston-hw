package com.dm.bl.demo.service;

import java.util.List;

public interface Service<E, ID> {
    E create (E e);
    E read (ID id);
    E update (E e);
    void delete(ID id);
    List<E> readAll();
}
