// ============================================
// HashTable.cpp - Implementation
// ============================================
// src/cpp/attendance/HashTable.cpp
#include "HashTable.h"

HashTable::HashTable() : size(TABLE_SIZE), count(0) {
    table = new HashNode[size];
}

HashTable::~HashTable() {
    delete[] table;
}

int HashTable::hashFunction(const string& key) {
    unsigned long hash = 5381;
    for (int i = 0; i < key.length(); i++) {
        hash = ((hash << 5) + hash) + key[i];
    }
    return hash % size;
}

int HashTable::probe(int index, int i) {
    return (index + i * i) % size;
}

void HashTable::resize() {
    int oldSize = size;
    size = size * 2 + 1;
    HashNode* oldTable = table;
    table = new HashNode[size];
    count = 0;
    
    for (int i = 0; i < oldSize; i++) {
        if (oldTable[i].occupied) {
            for (int j = 0; j < oldTable[i].records.size(); j++) {
                insert(oldTable[i].records[j].studentId, oldTable[i].records[j]);
            }
        }
    }
    
    delete[] oldTable;
}

void HashTable::insert(const string& studentId, const AttendanceRecord& record) {
    if (count >= size * 0.7) {
        resize();
    }
    
    int index = hashFunction(studentId);
    int i = 0;
    
    while (i < size) {
        int probeIndex = probe(index, i);
        
        if (!table[probeIndex].occupied) {
            table[probeIndex].key = studentId;
            table[probeIndex].records.push_back(record);
            table[probeIndex].occupied = true;
            count++;
            return;
        } else if (table[probeIndex].key == studentId) {
            table[probeIndex].records.push_back(record);
            return;
        }
        i++;
    }
}

vector<AttendanceRecord> HashTable::search(const string& studentId) {
    int index = hashFunction(studentId);
    int i = 0;
    
    while (i < size) {
        int probeIndex = probe(index, i);
        
        if (!table[probeIndex].occupied) {
            return vector<AttendanceRecord>();
        }
        
        if (table[probeIndex].key == studentId) {
            return table[probeIndex].records;
        }
        i++;
    }
    
    return vector<AttendanceRecord>();
}

vector<AttendanceRecord> HashTable::getRecordsByCourse(const string& courseCode) {
    vector<AttendanceRecord> result;
    
    for (int i = 0; i < size; i++) {
        if (table[i].occupied) {
            for (int j = 0; j < table[i].records.size(); j++) {
                if (table[i].records[j].courseCode == courseCode) {
                    result.push_back(table[i].records[j]);
                }
            }
        }
    }
    
    return result;
}

double HashTable::getAttendancePercentage(const string& studentId, const string& courseCode) {
    vector<AttendanceRecord> records = search(studentId);
    int total = 0, present = 0;
    
    for (int i = 0; i < records.size(); i++) {
        if (records[i].courseCode == courseCode) {
            total++;
            if (records[i].status == "Present") {
                present++;
            }
        }
    }
    
    if (total == 0) return 0.0;
    return (present * 100.0) / total;
}

bool HashTable::remove(const string& studentId) {
    int index = hashFunction(studentId);
    int i = 0;
    
    while (i < size) {
        int probeIndex = probe(index, i);
        
        if (!table[probeIndex].occupied) {
            return false;
        }
        
        if (table[probeIndex].key == studentId) {
            table[probeIndex].occupied = false;
            table[probeIndex].records.clear();
            count--;
            return true;
        }
        i++;
    }
    
    return false;
}

int HashTable::getCount() {
    return count;
}

void HashTable::clear() {
    for (int i = 0; i < size; i++) {
        table[i].occupied = false;
        table[i].records.clear();
    }
    count = 0;
}

vector<string> HashTable::getAllStudentIds() {
    vector<string> ids;
    for (int i = 0; i < size; i++) {
        if (table[i].occupied) {
            ids.push_back(table[i].key);
        }
    }
    return ids;
}