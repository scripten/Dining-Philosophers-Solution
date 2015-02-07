import java.util.concurrent.Semaphore;

public class Sem {

    private Semaphore sem;
    private int init;
    private int count;
    private int nw;
    private int ns;

    public Sem(int count) {
	sem = new Semaphore(count, true);
	this.init = count;
	this.count = count;
	this.nw = 0;
	this.ns = 0;
    }

    public void semWait() {
	sem.acquireUninterruptibly();
	synchronized(this) {
	    nw++;
	    count--;
	}
    }

    public void semWait(int n) {
	sem.acquireUninterruptibly(n);
	synchronized(this) {
	    nw += n;
	    count -= n;
	}
    }

    public void semSignal() {
	sem.release();
	synchronized(this) {
	    ns++;
	    count++;
	}
    }

    public void semSignal(int n) {
	sem.release(n);
	synchronized(this) {
	    ns += n;
	    count += n;
	}
    }

    public String toString() {
	return "[init="+init+" count="+count+" ns="+ns+" nw="+nw+"]";
    }
}
