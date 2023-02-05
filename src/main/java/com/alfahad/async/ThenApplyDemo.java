package com.alfahad.async;

import database.EmployeeDatabase;
import dto.Employee;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ThenApplyDemo {

    public CompletableFuture<Void> findEmployee(){
        Executor executor= Executors.newFixedThreadPool(5);
        CompletableFuture<Void> empTrain = CompletableFuture.supplyAsync(() -> {
            System.out.println("Get Employee from database :" + Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployee();
        },executor).thenApplyAsync((emp) -> {
            System.out.println("newEmployee :" + Thread.currentThread().getName());
            return emp.stream().filter((employ) -> "TRUE".equals(employ.getNewJoiner()))
                    .collect(Collectors.toList());
        },executor).thenApplyAsync((emp) -> {
            System.out.println("Emp Pending :" + Thread.currentThread().getName());
            return emp.stream().filter((employ) -> "TRUE".equals(employ.getLearningPending()))
                    .collect(Collectors.toList());
        },executor).thenApplyAsync((emp) -> {
            System.out.println("Emp id :" + Thread.currentThread().getName());
            return emp.stream().map(Employee::getEmail).collect(Collectors.toList());
        },executor).thenAcceptAsync((emp) -> {
            System.out.println("reminder notify :" + Thread.currentThread().getName());
            emp.forEach(ThenApplyDemo::sendEmail);
        },executor);
        return empTrain;
    }
    public static void sendEmail(String email){
        System.out.println("Send Reminder Notify :"+email);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThenApplyDemo EmployeeReminder=new ThenApplyDemo();
        EmployeeReminder.findEmployee().get();
    }
}
