// ============================================
// HashTable.h - Attendance Hash Table
// ============================================
// src/cpp/attendance/HashTable.h
#ifndef HASHTABLE_H
#define HASHTABLE_H

#include <string>
#include <vector>

using namespace std;

struct AttendanceRecord {
    string studentId;
    string date;
    string status;
    string courseCode;
    
    AttendanceRecord() {}
    AttendanceRecord(string id, string d, string s, string c) 
        : studentId(id), date(d), status(s), courseCode(c) {}
};

struct HashNode {
    string key;
    vector<AttendanceRecord> records;
    bool occupied;
    
    HashNode() : occupied(false) {}
};

class HashTable {
private:
    static const int TABLE_SIZE = 1009;
    HashNode* table;
    int size;
    int count;
    
    int hashFunction(const string& key);
    int probe(int index, int i);
    void resize();
    
public:
    HashTable();
    ~HashTable();
    
    void insert(const string& studentId, const AttendanceRecord& record);
    vector<AttendanceRecord> search(const string& studentId);
    vector<AttendanceRecord> getRecordsByCourse(const string& courseCode);
    double getAttendancePercentage(const string& studentId, const string& courseCode);
    bool remove(const string& studentId);
    int getCount();
    void clear();
    vector<string> getAllStudentIds();
};

#endif
