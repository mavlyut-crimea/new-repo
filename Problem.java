package other;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Problem {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        List<Visitor> visitors = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            visitors.add(new Visitor(i, timeToMin(in.next()), timeToMin(in.next())));
        }

        
    }

    private static int timeToMin(String s) {
        return Integer.parseInt(s.substring(0, 2)) * 60 + Integer.parseInt(s.substring(3, 5));
    }
}

class Visitor {
    private final int num;
    private final int timeIn;
    private final int timeOut;

    public Visitor(int num, int timeIn, int timeOut) {
        this.num = num;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public int getNum() {
        return num;
    }

    public int getTimeIn() {
        return timeIn;
    }

    public int getTimeOut() {
        return timeOut;
    }
}
