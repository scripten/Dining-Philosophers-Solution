public class DP2 extends Thread {

    public static final int NP = 5;
    public int n; // philosopher number
	public static boolean [] eating = new boolean [NP];
	public static Sem mutex = new Sem(1);
	public static Sem [] phil;
	public static int [] queue;
	public static int qCount;

    public DP2 (int n) {
		this.n = n;
		for(int i = 0; i < NP; i++) {
			eating [i] = false;
		}
    }
	
	public void setEating(boolean b) {
		mutex.semWait();
		eating[n] = b;
		mutex.semSignal();
	}

    private void think() {
		System.out.println("Philosopher " + n + " is thinking");
		nap(30);
    }

    private void hungry() {
		System.out.println("Philosopher " + n + " is hungry!");
    }
	
	private void addToQueue() {
		mutex.semWait();
		queue[qCount++] = n;
		mutex.semSignal();
	}
	
	private static void serviceQueue() {
		mutex.semWait();
		while (0 < qCount) {
			int p = queue[0];
			int pLeft = p + 1;
			int pRight = p + NP - 1;
			if (/* FIXME: either the left or right philosopher is eating */)
				break; // no forks for you!
			// philosopher p can eat -- remove it from the queue
			qCount--;
			/* FIXME: move everything down one slot in the queue */
			eating[p] = true; // don’t call setEating here -- why??
			phil[p].semSignal(); // allow philosopher p to eat
		}
		mutex.semSignal();
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
		serviceQueue();
	}

    public void run() {
		while(true) {
			think();
			hungry();
			eat();
		}
    }

    public static void nap(int i) {
		try {
			sleep(i);
		} catch (Exception e) {
		}
    }
    
    public static void main(String [] args) {
		for (int i = 0 ; i < NP ; i++)
			new DP2(i).start();
		phil = new Sem [NP];
		queue = new int [NP];
		qCount = 0;
    }
}
