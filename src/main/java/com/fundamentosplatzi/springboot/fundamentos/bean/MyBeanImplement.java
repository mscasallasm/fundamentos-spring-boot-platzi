package com.fundamentosplatzi.springboot.fundamentos.bean;

public class MyBeanImplement implements MyBean{
    @Override
    public void print() {
        System.out.println("Hola, implementación con el bean");
    }
}
