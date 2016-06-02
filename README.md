# java vs kryo serialization

Send serialized messages between nodes in a local Akka cluster with 5x nodes.<br>
-all nodes are on the same machine, but different sockets

Benchmark the result

Serialization<br>
Kryo: 121,800 messages per second<br>
Java: 67,500 messages per second

Summary:<br>
With Kryo serialization, we get roughly double the throughput. 

Possible reasons:<br>
-serializes faster<br>
-unserializes faseter<br>
-it compresses more & consumes less bandwidth

