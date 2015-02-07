public class DP1a extends Thread {

    public static final int NP = 10;

    public static Sem [] fork;

    public int n; // philosopher number
	
	public static boolean [] eating = new boolean [NP];
	
	public static Sem mutex = new Sem(1);

    public DP1a (int n) {
	this.n = n;
	this.fork = fork;
	for(int i = 0; i < NP; i++) {
		eating [i] = false;
	}
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
	
	public void setEating(boolean b) {
	mutex.semWait();
	eating[n] = b;
	mutex.semSignal();
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
	
	public void conspireWith(int p) {
	while (true) {
		mutex.semWait();
		if (eating[p]) {
			eating[n] = false;
			mutex.semSignal();
			break;
		}
		mutex.semSignal();
		nap(20);
	}
	}

	public void eat() {
	setEating(true);
	System.out.println("Philosopher " + n + " is eating");
	nap(20);
	if (n == 0)
		conspireWith(2);
	else if (n == 2)
		conspireWith(0);
	else
		setEating(false);
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
	    new DP1a(i).start();
    }
}
