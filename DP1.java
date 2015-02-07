public class DP1 extends Thread {

    public static final int NP = 5;

    public static Sem [] fork;

    public int n; // philosopher number

    public DP1 (int n) {
	this.n = n;
	this.fork = fork;
    }

    public int left() {
	if (n == 0)
		return NP - 1;
	return n;
    }

    public int right() {
	if (n == 0)
		return 0;
	return (n+NP-1) % NP;
    }

    private void takeFork(int i) {
	fork[i].semWait();
    }

    private void releaseFork(int i) {
	fork[i].semSignal();
    }

    private void think() {
	System.out.println("Philosopher "+n+" is thinking");
	nap(30);
    }

    private void hungry() {
	System.out.println("Philosopher "+n+" is hungry!");
    }

    private void eat() {
	System.out.println("Philosopher "+n+" is eating");
	nap(20);
    }

    public void run() {
	while(true) {
	    think();

	    hungry();
	    takeFork(left());
	    nap(2);
	    takeFork(right());
	    
	    eat();

	    releaseFork(left());
	    releaseFork(right());
	}
    }

    public static void nap(int i) {
	try {
	    sleep(i);
	} catch (Exception e) {
	}
    }
    
    public static void main(String [] args) {
	fork = new Sem[NP];
	for (int i=0 ; i<NP ; i++)
	    fork[i] = new Sem(1);
	for (int i=0 ; i<NP ; i++)
	    new DP1(i).start();
    }
}
