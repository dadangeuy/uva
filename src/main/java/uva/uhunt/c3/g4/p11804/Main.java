package uva.uhunt.c3.g4.p11804;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * 11804 - Argentina
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2904
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.caseId = i + 1;
            input.players = new Player[10];
            for (int j = 0; j < 10; j++) {
                final Player player = new Player(in.next(), in.nextInt(), in.nextInt());
                input.players[j] = player;
            }

            final Output output = process.process(input);
            out.format(
                "Case %d:\n(%s, %s, %s, %s, %s)\n(%s, %s, %s, %s, %s)\n",
                output.caseId,
                output.attackers[0].name,
                output.attackers[1].name,
                output.attackers[2].name,
                output.attackers[3].name,
                output.attackers[4].name,
                output.defenders[0].name,
                output.defenders[1].name,
                output.defenders[2].name,
                output.defenders[3].name,
                output.defenders[4].name
            );
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public Player[] players;
}

class Output {
    public int caseId;
    public Player[] attackers;
    public Player[] defenders;
}

class Process {
    private static final Comparator<Player> ORDER_BY_ABILITIES_AND_NAME = Comparator
        .comparingInt((Player p) -> -p.attack)
        .thenComparingInt((Player p) -> p.defend)
        .thenComparing((Player p) -> p.name);
    private static final Comparator<Player> ORDER_BY_NAME = Comparator
        .comparing((Player p) -> p.name);

    public final Output process(final Input input) {
        final Player[] players = input.players;
        Arrays.sort(players, ORDER_BY_ABILITIES_AND_NAME);

        final Player[] attackers = Arrays.copyOfRange(input.players, 0, 5);
        Arrays.sort(attackers, ORDER_BY_NAME);

        final Player[] defenders = Arrays.copyOfRange(input.players, 5, 10);
        Arrays.sort(defenders, ORDER_BY_NAME);

        final Output output = new Output();
        output.caseId = input.caseId;
        output.attackers = attackers;
        output.defenders = defenders;
        return output;
    }
}

class Player {
    public final String name;
    public final int attack;
    public final int defend;

    public Player(final String name, final int attack, final int defend) {
        this.name = name;
        this.attack = attack;
        this.defend = defend;
    }
}
