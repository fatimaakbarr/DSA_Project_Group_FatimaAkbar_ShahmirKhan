// src/java/com/campusconnect/CampusConnectJNI.java
package com.campusconnect;

public class CampusConnectJNI {
    
    static {
        try {
            System.loadLibrary("campusconnect");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load native library: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Navigation System (Graph + Dijkstra)
    public native void initNavigation();
    public native void addLocation(String name, double x, double y);
    public native void addPath(String from, String to, double distance);
    public native String findShortestPath(String from, String to);
    public native String getAllLocations();
    public native void clearNavigation();
    
    // Attendance System (HashTable + AVL)
    public native void initAttendance();
    public native void markAttendance(String studentId, String date, String status, String courseCode);
    public native String getAttendanceRecords(String studentId);
    public native double getAttendancePercentage(String studentId, String courseCode);
    public native String getStudentsBelowThreshold(double threshold);
    public native String getAllStudentIds();
    public native void clearAttendance();
    
    // Student Information System (BTree + QuickSort)
    public native void initStudentSystem();
    public native void addStudent(String id, String name, String dept, int semester, 
                                  double cgpa, String email, String phone);
    public native String findStudent(String studentId);
    public native String getAllStudents();
    public native String getStudentsByDepartment(String department);
    public native String getStudentsBySemester(int semester);
    public native String sortStudentsByName();
    public native String sortStudentsByCGPA();
    public native void removeStudent(String studentId);
    public native void clearStudents();
    
    // Course Scheduling System (Priority Queue)
    public native void initScheduling();
    public native void addCourse(String code, String name, String instructor, int credits,
                                int enrolled, int capacity, int priority, String timeSlot, String room);
    public native String getHighestPriorityCourse();
    public native String getAllCourses();
    public native boolean removeCourse(String courseCode);
    public native boolean updateCoursePriority(String courseCode, int newPriority);
    public native void clearScheduling();
    
    // Lost & Found Resources (Trie + KMP)
    public native void initResources();
    public native void addResource(String id, String name, String description, String category,
                                  String location, String status, String reportedBy, String date);
    public native String searchResource(String name);
    public native String searchResourceByPrefix(String prefix);
    public native String searchResourceByDescription(String pattern);
    public native String getResourcesByCategory(String category);
    public native String getResourcesByStatus(String status);
    public native boolean removeResource(String itemId);
    public native void clearResources();
}