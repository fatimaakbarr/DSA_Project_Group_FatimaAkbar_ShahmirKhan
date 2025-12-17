# DSA_Project_Group_FatimaAkbar_ShahmirKhan

## Project Overview
**Campus Connect** is a unified SmartCampus application combining Java Swing GUI with C++ backend DSAs and algorithms. The application allows students to efficiently navigate the campus, manage student records, and track attendance using advanced data structures. Java handles input validation, UI, and visualization, while C++ implements the core algorithms, connected seamlessly via JNI.

---

## Running the Project

### Prerequisites
- **Java 8+** installed
- **C++ compiler** supporting C++17 (or higher)
- **JNI setup** configured for your OS
- Git (optional, for repository clone)

### Steps
1. **Clone repository:**
   ```bash
   git clone https://github.com/fatimaakbarr/CampusConnect.git
   cd CampusConnect
Compile C++ backend library:


cd Cpp-Native
g++ -std=c++17 -shared -o campus_backend.dll native_impl.cpp
On Linux/macOS: campus_backend.so

Run Java GUI:
cd ../Java-GUI
javac -d bin src/com/campusconnect/*.java
java -cp bin com.campusconnect.Main
Java automatically loads the compiled C++ library (campus_backend.dll) via JNI.

Modules
1. Campus Navigator (Graph Module) - Fatima Akbar
Goal: Find the best route between two campus locations.
User Inputs:

Source Location

Destination Location

Algorithm choice (BFS / Dijkstra)

Functionality:

Java sends input to C++ via JNI.

C++ executes:

BFS: Shortest path by fewest hops (unweighted)

Dijkstra: Shortest distance path (weighted)

Java visualizes:

Route (Gate → Library → ...)

Distance/hops

Animated graph (visited order + highlighted path)

DSAs Used:

Level‑1: Linked List (adjacency list), Hash Map (name → index), Queue (BFS)

Level‑2: Graph, BFS/Dijkstra (Min-Heap priority queue)

2. Student Information System (AVL Module) - Fatima Akbar
Goal: Efficiently manage student records.
User Inputs:

Roll Number (unique key)

Student Name

Program, Year/Semester

Actions:

Save: insert or update record

Search: find by roll number

Delete: remove by roll number

Refresh: reload table list

Behavior:

Java validates input.

C++ stores records in AVL Tree

Deleting a student automatically deletes their attendance

Java shows sorted table + AVL tree visualization

DSAs Used:

Level‑2: AVL Tree (insert/search/delete + balancing rotations)

Level‑1: Searching / sorted listing (inorder traversal)

3. Attendance Management (Queue + Heap Module) - Shahmir
Goal: Track attendance and identify defaulters.
User Inputs:

Roll Number

Defaulter threshold (%)

Actions:

New Day: adds one session to all students

Mark Present: marks a student present

Get Summary: shows attendance percentage (animated)

Show Defaulters: lists students below threshold

Behavior:

Java sends input to C++

C++ computes attendance and identifies defaulters via Min-Heap

Java visualizes live attendance and defaulter list

DSAs Used:

Level‑1: Array, Queue, Hash Map

Level‑2: Min-Heap / Priority Queue

Integration Method
Java and C++ work together through Java Native Interface (JNI):

NativeBridge.java declares native methods.

Java loads campus_backend.dll.

GUI events (button clicks) call JNI functions in native_impl.cpp.

C++ executes DSAs and returns results as JSON.

Java updates UI and visualizations dynamically.

Team Collaboration
Fatima Akbar: Campus Navigator & Student Information System (main/harder modules), JNI integration, visualizations.

Shahmir: Attendance Management, auxiliary UI, defaulters logic.

Regular commits to GitHub track progress and ensure continuous integration.

Code is modular to allow each member to work independently.

Data Structures & Algorithms List per Student
Student	Module	Level‑1 DSAs	Level‑2 DSAs
Fatima	Campus Navigator	Linked List, Hash Map, Queue	Graph, BFS/Dijkstra (Min-Heap)
Fatima	Student Information System	Searching, Inorder Traversal	AVL Tree
Shahmir	Attendance Management	Array, Queue, Hash Map	Min-Heap / Priority Queue

Campus Connect combines advanced data structures with a user-friendly interface, enabling smart campus management in a fully integrated Java + C++ environment.
