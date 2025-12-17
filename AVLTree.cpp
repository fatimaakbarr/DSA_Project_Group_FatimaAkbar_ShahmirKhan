
// ============================================
// AVLTree.cpp - Implementation
// ============================================
// src/cpp/attendance/AVLTree.cpp
#include "AVLTree.h"

AVLTree::AVLTree() : root(NULL) {}

AVLTree::~AVLTree() {
    clear(root);
}

int AVLTree::height(AVLNode* node) {
    return node == NULL ? 0 : node->height;
}

int AVLTree::max(int a, int b) {
    return (a > b) ? a : b;
}

int AVLTree::getBalance(AVLNode* node) {
    return node == NULL ? 0 : height(node->left) - height(node->right);
}

AVLNode* AVLTree::rightRotate(AVLNode* y) {
    AVLNode* x = y->left;
    AVLNode* T2 = x->right;
    
    x->right = y;
    y->left = T2;
    
    y->height = max(height(y->left), height(y->right)) + 1;
    x->height = max(height(x->left), height(x->right)) + 1;
    
    return x;
}

AVLNode* AVLTree::leftRotate(AVLNode* x) {
    AVLNode* y = x->right;
    AVLNode* T2 = y->left;
    
    y->left = x;
    x->right = T2;
    
    x->height = max(height(x->left), height(x->right)) + 1;
    y->height = max(height(y->left), height(y->right)) + 1;
    
    return y;
}

AVLNode* AVLTree::insert(AVLNode* node, StudentAttendanceStats data) {
    if (node == NULL) {
        return new AVLNode(data);
    }
    
    if (data.attendancePercentage < node->data.attendancePercentage) {
        node->left = insert(node->left, data);
    } else if (data.attendancePercentage > node->data.attendancePercentage) {
        node->right = insert(node->right, data);
    } else {
        if (data.studentId < node->data.studentId) {
            node->left = insert(node->left, data);
        } else {
            node->right = insert(node->right, data);
        }
    }
    
    node->height = 1 + max(height(node->left), height(node->right));
    
    int balance = getBalance(node);
    
    if (balance > 1 && data.attendancePercentage < node->left->data.attendancePercentage) {
        return rightRotate(node);
    }
    
    if (balance < -1 && data.attendancePercentage > node->right->data.attendancePercentage) {
        return leftRotate(node);
    }
    
    if (balance > 1 && data.attendancePercentage > node->left->data.attendancePercentage) {
        node->left = leftRotate(node->left);
        return rightRotate(node);
    }
    
    if (balance < -1 && data.attendancePercentage < node->right->data.attendancePercentage) {
        node->right = rightRotate(node->right);
        return leftRotate(node);
    }
    
    return node;
}

void AVLTree::insert(StudentAttendanceStats data) {
    root = insert(root, data);
}

AVLNode* AVLTree::minValueNode(AVLNode* node) {
    AVLNode* current = node;
    while (current->left != NULL) {
        current = current->left;
    }
    return current;
}

AVLNode* AVLTree::deleteNode(AVLNode* root, string studentId) {
    if (root == NULL) return root;
    
    if (studentId < root->data.studentId) {
        root->left = deleteNode(root->left, studentId);
    } else if (studentId > root->data.studentId) {
        root->right = deleteNode(root->right, studentId);
    } else {
        if (root->left == NULL || root->right == NULL) {
            AVLNode* temp = root->left ? root->left : root->right;
            
            if (temp == NULL) {
                temp = root;
                root = NULL;
            } else {
                *root = *temp;
            }
            delete temp;
        } else {
            AVLNode* temp = minValueNode(root->right);
            root->data = temp->data;
            root->right = deleteNode(root->right, temp->data.studentId);
        }
    }
    
    if (root == NULL) return root;
    
    root->height = 1 + max(height(root->left), height(root->right));
    
    int balance = getBalance(root);
    
    if (balance > 1 && getBalance(root->left) >= 0) {
        return rightRotate(root);
    }
    
    if (balance > 1 && getBalance(root->left) < 0) {
        root->left = leftRotate(root->left);
        return rightRotate(root);
    }
    
    if (balance < -1 && getBalance(root->right) <= 0) {
        return leftRotate(root);
    }
    
    if (balance < -1 && getBalance(root->right) > 0) {
        root->right = rightRotate(root->right);
        return leftRotate(root);
    }
    
    return root;
}

void AVLTree::remove(string studentId) {
    root = deleteNode(root, studentId);
}

void AVLTree::inorderTraversal(AVLNode* node, vector<StudentAttendanceStats>& result) {
    if (node != NULL) {
        inorderTraversal(node->left, result);
        result.push_back(node->data);
        inorderTraversal(node->right, result);
    }
}

vector<StudentAttendanceStats> AVLTree::getSortedStudents() {
    vector<StudentAttendanceStats> result;
    inorderTraversal(root, result);
    return result;
}

vector<StudentAttendanceStats> AVLTree::getStudentsBelowThreshold(double threshold) {
    vector<StudentAttendanceStats> all = getSortedStudents();
    vector<StudentAttendanceStats> result;
    
    for (int i = 0; i < all.size(); i++) {
        if (all[i].attendancePercentage < threshold) {
            result.push_back(all[i]);
        }
    }
    
    return result;
}

void AVLTree::clear(AVLNode* node) {
    if (node != NULL) {
        clear(node->left);
        clear(node->right);
        delete node;
    }
}

void AVLTree::clear() {
    clear(root);
    root = NULL;
}