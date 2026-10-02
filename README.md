# Vehicles on Bridge

Concurrency exercise: cars approach a single-lane bridge from both sides. A traffic controller guarantees that at most one car is on the bridge at a time. The run is animated in a Swing window.

## Traffic controllers

All implement the `TrafficController` interface (`enterLeft`, `enterRight`, `leaveLeft`, `leaveRight`).

| Class | Approach |
|---|---|
| `TrafficControllerEmpty` | No synchronization — baseline, cars collide |
| `TrafficControllerSimple` | Java monitor: `synchronized` methods with `wait()` / `notifyAll()` |
| `TrafficControllerFair` | `ReentrantLock(true)` + `Condition` — waiting cars cross in arrival order |

`TrafficRegistrar` tracks which car is on the bridge so the GUI can draw it.

## Structure

- `BridgeGUI` — entry point; opens the window, repaints every 25 ms
- `CarWindow`, `CarWorld` — Swing frame and drawing panel
- `Car` — a `Runnable` vehicle running on its own thread
- `image/` — car and bridge sprites

## Run

```bash
javac -d out src/*.java
cp -r image out/
java -cp out BridgeGUI
```

Requires a JDK 8+.
