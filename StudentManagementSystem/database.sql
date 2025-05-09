-- SQL script to create database and tables for Student Management System

CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Users table for login system
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL -- store hashed passwords in real apps
);

-- Insert a default admin user (password: admin123)
INSERT INTO users (username, password) VALUES ('admin', 'admin123');

-- Students table for CRUD operations
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    course VARCHAR(100)
);
