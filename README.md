# Construction Site Resource Allocator

A Java console application demonstrating core OOP and DSA concepts 
through a real world construction site resource management system.

## OOP Concepts Used
- Abstract Classes — Resource base class
- Inheritance — HumanResource, EquipmentResource, MaterialResource
- Interfaces — Allocatable, Reportable
- Encapsulation — private fields with getters
- Polymorphism — getCostPerHour() and generateReport() overridden differently
- Custom Exceptions — ResourceNotAvailableException, ResourceNotFoundException

## DSA Concepts Used
- PriorityQueue — task scheduling by priority level
- HashMap — O(1) resource lookup
- Stack — undo last allocation
- ArrayList — allocation history and search

## How to Run
cd src
javac -d ../out models/*.java exceptions/*.java engine/*.java ui/*.java Main.java
java -cp ../out Main

## Features
- Add and manage workers, equipment, materials
- Allocate resources to tasks with conflict detection
- Priority based task scheduling
- Undo last allocation
- Search resources by name
- Generate full site utilization report
