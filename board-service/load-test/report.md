# Report

## Table of Contents

- [Result](#result)
  - [sp](#shortestpathx-y)
  - [sp-neo4j](#shortestpathneo4jx-y)
  - [athop](#athopnodes-types)
  - [athop-neo4j](#athopneo4jnodes-types)
- [Conclusions](#conclusions)

## Result

This section details test results generated from running a set of loads tests on 
a subset of the endpoints declared by the board-service module.  

### shortestPath(x, y)
 
docker run --rm -v C:/wrk:/data williamyeh/wrk -t 10 -c 200 -d 60s --latency -s script-sp.lua 
http://host.docker.internal:7000/shortestPath    

Running 1m test @ http://host.docker.internal:7000/shortestPath   
10 threads and 200 connections    

| Thread  Stats | Avg      | Stdev    | Max    | +/- Stdev |
| :---          | :---:    | :---:    | :---:  | ---:      |
| Latency       | 810.30ms | 469.52ms | 2.00s  | 63.91%    |
| Req/Sec       | 27.30    | 18.97    | 130.00 | 75.51%    |

| Latency | Distribution | 
| :---    | ---:         |
| 50%     | 728.42ms     |
| 75%     | 1.13s        |
| 90%     | 1.52s        |
| 99%     | 1.91s        |
   
13143 requests in 1.00m, 3.07MB read    
Socket errors: connect 0, read 0, write 0, timeout 829    
Requests/sec: 218.68    
Transfer/sec: 52.24KB    

### shortestPathNeo4j(x, y)

docker run --rm -v C:/wrk:/data williamyeh/wrk -t 10 -c 200 -d 60s --latency -s script-sp-neo.lua 
http://host.docker.internal:7000/shortestPathNeo4j   
 
Running 1m test @ http://host.docker.internal:7000/shortestPathNeo4j    
10 threads and 200 connections    

| Thread  Stats | Avg      | Stdev    | Max      | +/- Stdev |
| :---          | :---:    | :---:    | :---:    | ---:      |
| Latency       | 133.13ms | 35.79ms  | 381.59ms | 78.30%    |
| Req/Sec       | 149.50   | 34.99    | 272.00   | 70.56%    |

| Latency | Distribution | 
| :---    | ---:         |
| 50%     | 123.00ms     |
| 75%     | 143.60ms     |
| 90%     | 187.80ms     |
| 99%     | 253.57ms     |

89296 requests in 1.00m, 19.61MB read    
Requests/sec: 1486.45    
Transfer/sec: 334.23KB      

### athop(nodes, types)
docker run --rm -v C:/wrk:/data williamyeh/wrk -t 10 -c 200 -d 60s --latency -s script-athop.lua 
http://host.docker.internal:7000/athop    

Running 1m test @ http://host.docker.internal:7000/athop    
10 threads and 200 connections    

| Thread  Stats | Avg      | Stdev    | Max      | +/- Stdev |
| :---          | :---:    | :---:    | :---:    | ---:      |
| Latency       | 59.57ms  | 17.50ms  | 168.93ms | 88.13%    |
| Req/Sec       | 334.50   | 54.05    | 520.00   | 66.23%    |

| Latency | Distribution | 
| :---    | ---:         |
| 50%     | 54.47ms      |
| 75%     | 61.73ms      |
| 90%     | 82.39ms      |
| 99%     | 124.69ms     |
   
199977 requests in 1.00m, 67.02MB read    
Requests/sec: 3327.78    
Transfer/sec: 1.12MB    

### athopNeo4j(nodes, types)

docker run --rm -v C:/wrk:/data williamyeh/wrk -t 10 -c 200 -d 60s --latency -s script-athop-neo.lua 
http://host.docker.internal:7000/athopNeo4j    
    
Running 1m test @ http://host.docker.internal:7000/athopNeo4j    
10 threads and 200 connections

| Thread  Stats | Avg      | Stdev     | Max      | +/- Stdev |
| :---          | :---:    | :---:     | :---:    | ---:      |
| Latency       | 149.45ms | 46.80ms   | 734.40ms | 80.61%    |
| Req/Sec       | 134.36   | 38.63     | 260.00   | 69.21%    |

| Latency | Distribution | 
| :---    | ---:         |
| 50%     | 135.15ms     |
| 75%     | 162.28ms     |
| 90%     | 214.14ms     |
| 99%     | 303.85ms        |

80085 requests in 1.00m, 31.41MB read    
Requests/sec: 1332.56    
Transfer/sec: 535.17KB

## Conclusions

- A high performance increase relating to latency and throughput was measured for the db-driven 
shortest-path implementation. 

- The load-tests did not measure a performance increase related to latency or throughput for 
the db-driven athop implementation.    
The reason for this I believe is that the non db-driven implementation does not scale poorly 
with the number of nodes in a graph. Instead, it scales with the number of average links per node. 
Which, was only around 4 in the graph used as a test graph.