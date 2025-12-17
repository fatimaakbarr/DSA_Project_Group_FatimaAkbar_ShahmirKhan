
// ============================================
// AVLTree.h - Attendance Sorting
// ============================================
// src/cpp/attendance/AVLTree.h
#ifndef AVLTREE_H
#define AVLTREE_H

#include <string>
#include <vector>

using namespace std;

struct StudentAttendanceStats {
    string studentId;
    string studentName;
    double attendancePercentage;
    int totalClasses;
    int presentCount;
    
    StudentAttendanceStats() : attendancePercentage(0), totalClasses(0), presentCount(0) {}
    StudentAttendanceStats(string id, string name, double perc, int total, int present)
        : studentId(id), studentName(name), attendancePercentage(perc), 
          totalClasses(total), presentCount(present) {}
};

struct AVLNode {
    StudentAttendanceStats data;
    AVLNode* left;
    AVLNode* right;
    int height;
    
    AVLNode(StudentAttendanceStats d) : data(d), left(NULL), right(NULL), height(1) {}
};

class AVLTree {
private:
    AVLNode* root;
    
    int height(AVLNode* node);
    int max(int a, int b);
    int getBalance(AVLNode* node);
    AVLNode* rightRotate(AVLNode* y);
    AVLNode* leftRotate(AVLNode* x);
    AVLNode* insert(AVLNode* node, StudentAttendanceStats data);
    AVLNode* minValueNode(AVLNode* node);
    AVLNode* deleteNode(AVLNode* root, string studentId);
    void inorderTraversal(AVLNode* node, vector<StudentAttendanceStats>& result);
    void clear(AVLNode* node);
    
public:
    AVLTree();
    ~AVLTree();
    
    void insert(StudentAttendanceStats data);
    void remove(string studentId);
    vector<StudentAttendanceStats> getSortedStudents();
    vector<StudentAttendanceStats> getStudentsBelowThreshold(double threshold);
    void clear();
};

#endif