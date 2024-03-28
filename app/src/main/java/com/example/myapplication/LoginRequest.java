package com.example.myapplication;

public record LoginRequest (
    String email,
    String password
) {}