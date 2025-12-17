# DSA_Project_Group_FatimaAkbar_ShahmirKhan
Campus Connect – SmartCampus DSA Application

Campus Connect is a unified desktop application that combines Java Swing GUI and C++ data structures & algorithms (DSAs) to manage campus navigation, student information, and attendance efficiently. The project leverages JNI (Java Native Interface) to connect Java and C++ seamlessly.

Table of Contents

Features

Modules

Data Structures & Algorithms

How It Works

Installation

Usage

Folder Structure

License

Features

Campus Navigator: Find the shortest path between two locations.

Student Information System: Manage student records using an AVL tree for balanced performance.

Attendance Management: Track student attendance and identify defaulters using Min-Heaps.

Live Visualizations: Animated graphs, trees, and attendance rings.

Cross-language Integration: Java GUI interacts with high-performance C++ backend via JNI.

Modules
1. Campus Navigator (Graph Module)

Goal: Find the best route between two campus locations.

User Inputs: Source, Destination, Algorithm (BFS/Dijkstra)

C++ Processing:

BFS: Shortest path by fewest hops (unweighted)

Dijkstra: Shortest distance (weighted)

Java Output:

Route: Gate → Library → ...

Distance or hops

Animated graph visualization (visited nodes + highlighted path)

DSAs Used:

Level‑1: Linked List (adjacency list), Hash Map, Queue

Level‑2: Graph, BFS/Dijkstra with Min-Heap

2. Student Information System (AVL Module)

Goal: Efficiently manage student records.

User Inputs:

Roll Number, Name, Program, Year/Semester

Actions: Save, Search, Delete, Refresh

C++ Processing:

Stores records in AVL Tree

Ensures balance via rotations on insert/delete

Cascading delete: removing a student also removes their attendance

Java Output:

Sorted table of students

AVL visualization highlighting searched or updated records

DSAs Used:

Level‑2: AVL Tree (insert/search/delete + rotations)

Level‑1: Inorder traversal for sorted listing

3. Attendance Management (Queue + Heap Module)

Goal: Track attendance and identify defaulters.

User Inputs:

Roll Number

Defaulter threshold (%)

Actions: New Day, Mark Present, Get Summary, Show Defaulters

C++ Processing:

Maintains attendance per roll

Computes percentages

Retrieves defaulters using a Min-Heap

Java Output:

Animated attendance ring for selected roll

Table of defaulters

DSAs Used:

Level‑1: Array (records storage), Queue, Hash Map

Level‑2: Min-Heap / Priority Queue

How It Works (Java + C++ via JNI)

Java declares native methods in NativeBridge.java.

Java loads the compiled C++ library (campus_backend.dll on Windows).

Each user action in the GUI triggers a JNI function implemented in Cpp-Native/native_impl.cpp.

C++ performs DSAs computations and returns results (usually as JSON strings).

Java updates the GUI and animations accordingly.

Installation

Clone the repository:

git clone <repo_url>


Build the C++ backend:

Compile Cpp-Native/native_impl.cpp into a shared library (.dll or .so depending on OS).

Open CampusConnect Java project in your IDE (Eclipse/NetBeans/IntelliJ).

Ensure NativeBridge.java loads the compiled library correctly:

System.loadLibrary("campus_backend");


Run Main.java to launch the GUI.

Usage

Campus Navigator: Enter source, destination, choose BFS or Dijkstra, click "Find Route".

Student Info System: Add/search/update/delete student records.

Attendance Management: Track daily attendance, mark present students, view summary and defaulters.

All visualizations update live based on C++ calculations.
