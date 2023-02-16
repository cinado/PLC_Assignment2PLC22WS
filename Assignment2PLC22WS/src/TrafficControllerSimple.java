public class TrafficControllerSimple implements TrafficController {
    private final TrafficRegistrar registrar;
    private boolean bridgeFree = true;

    TrafficControllerSimple(TrafficRegistrar registrar){
        this.registrar = registrar;
    }

    @Override
    public synchronized void enterRight(Vehicle v) {
        enterBridge();
        registrar.registerRight(v);
    }

    @Override
    public synchronized void enterLeft(Vehicle v) {
        enterBridge();
        registrar.registerLeft(v);
    }

    @Override
    public synchronized void leaveLeft(Vehicle v) {
        registrar.deregisterLeft(v);
        leaveBridge();
    }

    @Override
    public synchronized void leaveRight(Vehicle v) {
        registrar.deregisterRight(v);
        leaveBridge();
    }

    private void enterBridge(){
        while(!bridgeFree){
            try {
                wait();
            }
            catch (InterruptedException e) {}
        }
        bridgeFree = false;
        notifyAll();
    }

    private void leaveBridge(){
        bridgeFree = true;
        notifyAll();
    }
    
}
