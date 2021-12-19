import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class NewProblem {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(in.next(), 1));
            nodes.add(new Node(in.next(), -1));
        }

        Collections.sort(nodes);

        int cnt = 0;
        int maxCnt = 0;
        for (int i = 0; i < nodes.size(); i++) {
            cnt += nodes.get(i).getCome();
            maxCnt = Math.max(maxCnt, cnt);
        }

        System.out.println(maxCnt);
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
