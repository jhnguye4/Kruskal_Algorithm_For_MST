# Kruskal_Algorithm_For_MST
CSC 505 Project 1: Sorting

## Learning Objective
This task deals with Minimum Spanning Trees (MST) on weighted graphs. More specifically, you will implement Kruskal’s MST algorithm. MST problems arise very often, especially in applications related to computer networks. Furthermore, an efficient implementation of Kruskal’s algorithm requires a number of the data structures we have discussed in class (i.e., heaps, up-trees and the union-find algorithms, and graphs), and you will have the opportunity to integrate all of them in a single program.

## Specifications
### Input
Each line of the input file will represent one edge in an undirected graph. It will contain two integers – the endpoints of the edge – followed by a real number – the weight of the edge. You may assume that the vertices are numbered 0 through n, that n < 1000, and that the number of edges, m, is such that m < 5000. The last line of the file will contain -1, to denote the end of input. You are not expected to check the input for errors of any sort.
Files graph1 and graph2 in the Project3 directory are sample input files. The output of your program will be tested on an input file not available in advance.
### Description
As your program reads in an edge e, it will:
* construct an edge record for it (consisting of four fields, vertex1, vertex2, weight, and
next),
* insert the record for e in the adjacency list of the two vertices,
* insert the edge into a heap.

Since the number of edges will be less than 5000, you can represent the heap as an array of 5000 edge records; the key (priority) of each edge will be its weight. Obviously, you will need to implement the heap operations insert and deleteMin [Note: Kruskal’s algorithm may be implemented without using a heap. One can sort the edges and consider them in increasing weight. This is strongly discouraged in this task, and the output has been designed so that you will need to implement a heap.]
In order to implement Kruskal’s algorithm, you also need to implement the up-tree operations to keep track of how connected components change during the execution of the algorithm. Since vertices will be numbered 0 through n (your program should determine n), you can use the array implementation described in class1. Initially, each vertex is in a connected component by itself, and adding an edge between two components has the effect of a union operation. Make sure that you implement a balanced version of union, but regarding the find operation, you may implement it with or without path compression.

## Running Program
In order to run with input files:

* javac proj3.java
* java proj3
* Enter a filename or Q to quit: example-input.txt
* Results will be in example-output.txt


