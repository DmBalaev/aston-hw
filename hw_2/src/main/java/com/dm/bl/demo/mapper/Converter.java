package com.dm.bl.demo.mapper;

import java.util.List;

public interface Converter<E,D>{
    D entityToDto(E e);
    E dtoToEntity(D d);
    List<D> entitiesToDtos(List<E> e);
}
