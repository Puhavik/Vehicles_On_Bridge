import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerFair implements TrafficController {
    TrafficRegistrar registrar;
    private Vehicle currentVehicle = null;
    private final Lock lock = new ReentrantLock(true);
    private final Condition bridgeAvailable = lock.newCondition();

    public TrafficControllerFair(TrafficRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public void enterRight(Vehicle v) {
        lock.lock();
        try {
            while (currentVehicle != null) {
                bridgeAvailable.await();
            }
            currentVehicle = v;
            registrar.registerRight(v);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void enterLeft(Vehicle v) {
        lock.lock();
        try {
            while (currentVehicle != null) {
                bridgeAvailable.await();
            }
            currentVehicle = v;
           registrar.registerLeft(v);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leaveLeft(Vehicle v) {
        lock.lock();
        try {
            if (currentVehicle == v) {
                currentVehicle = null;
                registrar.deregisterLeft(v);
                bridgeAvailable.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leaveRight(Vehicle v) {
        lock.lock();
        try {
            if (currentVehicle == v) {
                currentVehicle = null;
                registrar.deregisterRight(v);
                bridgeAvailable.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
