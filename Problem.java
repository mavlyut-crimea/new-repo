package other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Problem {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i, timeToMin(in.next()), 1));
            nodes.add(new Node(i, timeToMin(in.next()), -1));
        }

        Collections.sort(nodes);

        /*for (Node i : nodes) {
            System.out.println(i);
        }*/

        int cnt = 0;
        int maxCnt = 0;
        int left = -1, right = -1;
        for (int i = 0; i < nodes.size(); i++) {
            cnt += nodes.get(i).getCome();
            if (cnt > maxCnt) {
                maxCnt = cnt;
                left = nodes.get(i).getTime();
                right = nodes.get(i + 1).getTime();
            }
        }

        System.out.println(minToTime(left) + " " + minToTime(right));

    }

    private static String minToTime(int time) {
        return String.format("%2d:%2d", time / 60, time % 60);
    }

    private static int timeToMin(String s) {
        return Integer.parseInt(s.substring(0, 2)) * 60 + Integer.parseInt(s.substring(3, 5));
    }
}

class Node implements Comparable {
    private final int num;
    private final int time;
    private final int come;

    public Node(int num, int time, int come) {
        this.num = num;
        this.time = time;
        this.come = come;
    }

    public int getNum() {
        return num;
    }

    public int getTime() {
        return time;
    }

    public int getCome() {
        return come;
    }

    @Override
    public String toString() {
        return "(" + num + ", " + time + ", " + come + ")";
    }

    @Override
    public int compareTo(Object o) {
        Node obj = (Node) o;
        int ans = Integer.compare(time, obj.getTime());
        if (ans == 0) {
            ans = Integer.compare(-come, -obj.getCome());
        }
        return ans;
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
