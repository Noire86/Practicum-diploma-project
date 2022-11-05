package ru.soular.ewm.main.util.mapper;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Наследник ModelMapper - нужен для имплементации удобного общего метода
 * маппинга списков.
 */
public class CustomModelMapper extends ModelMapper {
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(element -> map(element, targetClass))
                .collect(Collectors.toList());
    }
}
