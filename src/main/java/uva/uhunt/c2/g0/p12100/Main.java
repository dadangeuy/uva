package uva.uhunt.c2.g0.p12100;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 12100 - Printer Queue
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3252
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.totalJobs = in.nextInt();
            input.myJob = in.nextInt();
            input.jobs = new int[input.totalJobs];
            for (int j = 0; j < input.totalJobs; j++) {
                input.jobs[j] = in.nextInt();
            }

            final Output output = process.process(input);
            out.println(output.totalMinutes);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalJobs;
    public int myJob;
    public int[] jobs;
}

class Output {
    public int totalMinutes;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.totalMinutes = 0;

        final LinkedList<Job> jobs = new LinkedList<>();
        for (int id = 0; id < input.totalJobs; id++) {
            final Job job = new Job();
            job.id = id;
            job.priority = input.jobs[id];
            jobs.addLast(job);
        }

        while (!jobs.isEmpty()) {
            final int maxPriority = jobs.stream()
                    .mapToInt(j -> j.priority)
                    .max()
                    .orElse(10);
            final Job first = jobs.removeFirst();

            if (first.priority == maxPriority) {
                output.totalMinutes++;
                if (first.id == input.myJob) {
                    return output;
                }
            } else {
                jobs.addLast(first);
            }
        }

        return output;
    }
}

class Job {
    public int id;
    public int priority;
}
