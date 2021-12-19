package other;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Problem {
    private static Scanner in;
    private static final String INF = "20000000000";

    public static void main(String[] args) throws FileNotFoundException {
        in = new Scanner(new FileReader(
                "C:\\Users\\User\\IdeaProjects\\untitled12\\src\\other\\fin.txt"
        ));
        int n = in.nextInt();


        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String timeIn = nextTime();
            String timeOut = nextTime();
            if (timeIn.compareTo("1633305600") >= 0 && timeOut.compareTo("1633305600") >= 0) {
                nodes.add(new Node(timeIn, 1));
                nodes.add(new Node(timeOut, -1));
            }
        }

        Collections.sort(nodes);

        int cnt = 0;
        int maxCnt = 0;
        String left = "", right = "";
        for (int i = 0; i < nodes.size(); i++) {
            cnt += nodes.get(i).getCome();
            if (cnt > maxCnt) {
                left = nodes.get(i).getTime();
                right = nodes.get(i + 1).getTime();
                maxCnt = cnt;
            }
        }

        System.out.println(maxCnt);
        System.out.println(left + " " + right);
        System.out.println(Long.parseLong(right) - Long.parseLong(left));
    }

    private static String nextTime() {
        String time = in.next();
        if (time.equals("0")) {
            return INF;
        }
        return time;
    }
}

class Node implements Comparable {
    private final String time;
    private final int come;

    public Node(String time, int come) {
        this.time = time;
        this.come = come;
    }

    public String getTime() {
        return time;
    }

    public int getCome() {
        return come;
    }

    @Override
    public int compareTo(Object o) {
        Node obj = (Node) o;
        int ans = time.compareTo(obj.getTime());
        if (ans == 0) {
            ans = Integer.compare(-come, -obj.getCome());
        }
        return ans;
    }
}
