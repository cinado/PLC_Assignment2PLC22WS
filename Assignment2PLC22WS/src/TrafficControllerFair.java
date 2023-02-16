import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerFair implements TrafficController {
    private final TrafficRegistrar registrar;
    private boolean bridgeFree = true;
    Lock lock = new ReentrantLock(true);
    Condition emptyBridge = lock.newCondition();


    TrafficControllerFair(TrafficRegistrar registrar){
        this.registrar = registrar;
    }

    @Override
    public void enterRight(Vehicle v) {
        lockBridge();
        registrar.registerRight(v);
    }

    @Override
    public void enterLeft(Vehicle v) {
        lockBridge();
        registrar.registerLeft(v);
    }

    @Override
    public void leaveLeft(Vehicle v) {
        registrar.deregisterLeft(v);
        unlockBridge();
    }

    @Override
    public void leaveRight(Vehicle v) {
        registrar.deregisterRight(v);
        unlockBridge();
    }
    
    public void lockBridge(){
        try{
            lock.lock();
            while(!bridgeFree){
                emptyBridge.await();
            }
            bridgeFree = false;
        }
        catch (InterruptedException e) {}
        finally{
            lock.unlock();
        }
    }

    public void unlockBridge(){
        try{
            lock.lock();
            bridgeFree = true;
            emptyBridge.signalAll();
        }
        finally{
            lock.unlock();
        }
    }
}