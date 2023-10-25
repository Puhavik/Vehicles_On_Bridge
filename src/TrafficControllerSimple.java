public class TrafficControllerSimple implements TrafficController {
    TrafficRegistrar registrar;
    private Vehicle currentVehicle = null;

    public TrafficControllerSimple(TrafficRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public synchronized void enterRight(Vehicle v) {
        try {
            while (currentVehicle != null) {
                wait();
            }
            currentVehicle = v;
            registrar.registerRight(v);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public synchronized void enterLeft(Vehicle v) {
        try {
            while (currentVehicle != null) {
                wait();
            }
            currentVehicle = v;
            registrar.registerLeft(v);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public synchronized void leaveLeft(Vehicle v) {
        if (currentVehicle != null) {
            currentVehicle = null;
            notifyAll();
            registrar.deregisterLeft(v);
        }
    }

    @Override
    public synchronized void leaveRight(Vehicle v) {
        if (currentVehicle != null) {
            currentVehicle = null;
            notifyAll();
            registrar.deregisterRight(v);
        }
    }
}
