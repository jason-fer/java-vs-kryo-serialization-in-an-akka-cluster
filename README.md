# java vs kryo serialization

Send serialized messages between nodes in a local Akka cluster with 5x nodes.
-all nodes are on the same machine, but different sockets

Benchmark the result

Serialization
Kryo: 121,800 messages per second
Java: 67,500 messages per second

Summary:
With Kryo serialization, we get roughly double the throughput. 

Possible reasons:
-serializes faster
-unserializes faseter
-it compresses more & consumes less bandwidth

