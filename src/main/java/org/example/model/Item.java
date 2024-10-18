package org.example.model;

import java.util.Objects;

public record Item(String name, double price, Category category) {
}