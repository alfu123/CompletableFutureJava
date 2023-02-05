package com.alfahad.async;

import database.EmployeeDatabase;
import dto.Employee;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class SupplyAsyncDemo {
    public List<Employee> getEmployee() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Employee>> liscompfuture=CompletableFuture.supplyAsync(()-> {
                    System.out.println("Thread :"+Thread.currentThread().getName());
                    return EmployeeDatabase.fetchEmployee();
                }
                );
        return liscompfuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SupplyAsyncDemo sup=new SupplyAsyncDemo();
        List<Employee> employee=sup.getEmployee();
        employee.stream().forEach(System.out::println);
    }
}
