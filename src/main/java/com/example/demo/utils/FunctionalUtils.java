package com.example.demo.utils;


import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FunctionalUtils {
    //Funcion de orden superior para filtrar listas
    public static <T> List<T> filtrar(List<T> lista, Predicate<T> condicion){
        return lista.stream().filter(condicion).collect(Collectors.toList());
    }

    //Funcion de orden superior para mapear listas
    public static <T,R> List<R> mapear (List<T> lista, Function<T,R> transformacion){
        return lista.stream().map(transformacion).collect(Collectors.toList());
    }

    //Funcion de orden superior para reducir listas
    public static <T> Optional<T> reducir(List<T> lista, BinaryOperator<T> acumulador) {
        return lista.stream().reduce(acumulador);
    }

    //Funcion generica para encontrar el elemento que cumple con determinada funcion
    public static <T> Optional<T> encontrar(List<T> lista, Predicate<T> condicion) {
        return lista.stream().filter(condicion).findFirst();
    }

    // Función de orden superior para ejecutar acción en cada elemento
    public static <T> void forEach(List<T> lista, Consumer<T> accion) {
        lista.forEach(accion);
    }

    // Función para componer funciones
    public static <T, R, S> Function<T, S> componer(Function<T, R> f1, Function<R, S> f2) {
        return f1.andThen(f2);
    }
}
