This program implements Dijkstra's Algorithm to compute the shortest path between two points and was a project in the CSC172 course at the University of Rochester.

Project 3
Collaborators: Darman Khan (NetID: dkhan2), Bahawar Sharif Dhillon(NetID: bdhillon)

In this project we implemented Dijkstra's Algorithm to compute the shortest path from one vertex to the other. This was done using 3 main classes: Intersection, Road and StreetMap.  

In the Intersection class we create an object "Intersection" which has three variables, latitude, longitude, and the name of the intersection. These will act as the vertices of the graph that we implement to use Dijkstra's. 

In the Road class we create an object "Road" that has three main variables, the name of the two intersections that the road links and the weight of the road. Weight of the road corresponds to the distance and is calculated using the Haversine formula and the coordinates of the two vertices joined by the road. The haversine implementation was taken from another source, cited below. 

In the StreetMap class we process the file containing information about all intersections and roads. We create objects for each intersection and use the road object to fill the adjacency list of the hashmap of linked lists which is how we implemented our Graph. 

Then we process this to calculate an array of indexes in the our Dijkstra's Method which represents the path from source to destination. 

The two methods xRange and yRange are used to calculate the range of values for latitude and longitude. This range helps us scale the coordinated for each vertex so that we can map them on the graph and maintain the map even after resizing. 

The reset method clears all identifiers, parent, visited etc. that were used in Dijkstra's.  

There is another class Canvas inside StreetMap and that is used for the graphics. This class has the functionality for the the java GUI. 


Run Time Analysis: 

This code was written to avoid multiple loops embedded loops to ensure that at any time the complexity for any action was not going above n^2. Therefore we obtained a quick implementation of the program. For mapping we are iterating over all roads therefore the complexity for that is O(E). For the two range methods we have O(V). For creating the map we have a single while loop that reads all files and stores them in a hashmap therefore making complexity solely dependent on the no of lines in the file ie O(V+E). The complexity of Dijkstra's is O(V+E) which is less than N^2. Making over overall complexity O(V+E) ensuring the implementation is quick. 

We initially use a graph implementation using array list and that did not work properly. Our main obstacle was implementing the graphics and adding the extra credit part. 


EXTRA CREDIT:
We have implemented several additional GUI features for extra credit:
(1) Two choices for background. 
(2) GUI where user can directly enter start and end vertices and a path will be calculated and show. This eliminated the need for running the program from command line or terminal again and again for each path. 
(3) Multiple colour options for the path so user can choose what they prefer.
(4) The starting and ending points are also highlighted on the map with a circle. 



Citations:
Haversine distance implementation obtained from Jason Winn
Access: https://github.com/jasonwinn/haversine/blob/master/Haversine.java
