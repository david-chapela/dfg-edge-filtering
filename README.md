# Directly Follows Graph Edge Filtering

Implementation of the three techniques to efficiently filter a Directly-Follows Graph (DFG) keeping the soundness and spanning properties presented in *\<url-paper-pending\>*.

##### Greedy:

Remove the edges in weight ascending (or descending) order, ensuring that after each removal all nodes are still on a path from *source* to *sink*.

##### Two Ways Edmonds:

Joint an optimum branching rooted in *source* (using Chu-Liu-Edmonds' algorithm) with a reversed optimum branching rooted in *sink*.

##### Two Ways Edmonds + Greedy:

Combination of Two Ways Edmonds and Greedy, applying the latter to the result of the former.

## Example of use

Download the latest release and execute using Java 8 (u271) or higher.

```java -jar dfg-edge-filtering-1.0.jar --tweg dfgs/example-01.dgf example-01-tweg-.dgf```
