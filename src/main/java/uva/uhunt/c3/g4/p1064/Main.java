package uva.uhunt.c3.g4.p1064;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * 1064 - Network
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3505
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int caseId = 1; in.hasNextInt(); caseId++) {
            final Input input = new Input();
            input.caseId = caseId;
            input.totalMessages = in.nextInt();
            input.totalPackets = in.nextInt();
            if (input.isEOF()) break;

            input.messageSizes = new int[input.totalMessages];
            for (int i = 0; i < input.totalMessages; i++) {
                input.messageSizes[i] = in.nextInt();
            }
            input.packetHeaders = new int[input.totalPackets][3];
            for (int i = 0; i < input.totalPackets; i++) {
                for (int j = 0; j < 3; j++) {
                    input.packetHeaders[i][j] = in.nextInt();
                }
            }

            final Output output = process.process(input);
            out.format("Case %d: %d\n\n", output.caseId, output.bufferSize);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalMessages;
    public int totalPackets;
    public int[] messageSizes;
    public int[][] packetHeaders;

    public boolean isEOF() {
        return totalMessages == 0 && totalPackets == 0;
    }
}

class Output {
    public int caseId;
    public int bufferSize;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;

        final int[] initials = IntStream.range(0, input.totalMessages).toArray();
        final int[] sequences = IntStream.range(0, input.totalMessages).toArray();

        int minBufferSize = Integer.MAX_VALUE;

        do {
            final Message[] messages = createMessages(input, sequences);
            final Packet[] packets = createPackets(input, messages);

            int bufferSize = getBufferSize(packets);
            minBufferSize = Math.min(minBufferSize, bufferSize);

            Permutation.next(sequences);
        } while (!Arrays.equals(initials, sequences));

        output.bufferSize = minBufferSize;
        return output;
    }

    private Message[] createMessages(final Input input, final int[] sequences) {
        final Message[] messages = new Message[input.totalMessages];
        for (int sequence = 0; sequence < sequences.length; sequence++) {
            final int messageId = sequences[sequence];
            final Message message = new Message();
            messages[messageId] = message;

            message.messageId = messageId;
            message.size = input.messageSizes[messageId];
            message.sequence = sequence;
        }

        return messages;
    }

    private Packet[] createPackets(final Input input, final Message[] messages) {
        final Packet[] packets = new Packet[input.totalPackets];
        for (int packetId = 0; packetId < input.totalPackets; packetId++) {
            final int[] header = input.packetHeaders[packetId];
            final int messageId = header[0] - 1, start = header[1], end = header[2];
            final Message message = messages[messageId];

            final Packet packet = new Packet();
            packets[packetId] = packet;

            packet.packetId = packetId;
            packet.start = start;
            packet.end = end;
            packet.message = message;
        }

        return packets;
    }

    private int getBufferSize(final Packet[] packets) {
        final PriorityQueue<Packet> buffer = new PriorityQueue<>(Packet.ORDER_BY_SEQUENCE_AND_START);
        int bufferSize = 0;
        int maxBufferSize = 0;
        int lastSequence = 0;

        for (final Packet pendingPacket : packets) {
            buffer.add(pendingPacket);
            bufferSize += pendingPacket.size();

            while (!buffer.isEmpty()) {
                final Packet packet = buffer.peek();
                final Message message = packet.message;

                final boolean consumable = message.sequence == lastSequence && message.isConsumable(packet);
                if (!consumable) break;

                message.consume(packet);
                if (message.isComplete()) lastSequence++;

                buffer.remove();
                bufferSize -= packet.size();
            }

            maxBufferSize = Math.max(maxBufferSize, bufferSize);
        }

        return maxBufferSize;
    }
}

class Packet {
    public static final Comparator<Packet> ORDER_BY_SEQUENCE_AND_START = Comparator
        .comparingInt((Packet p) -> p.message.sequence)
        .thenComparingInt((Packet p) -> p.start);

    public int packetId;
    public int start;
    public int end;
    public Message message;

    public int size() {
        return end - start + 1;
    }
}

class Message {
    public int messageId;
    public int size;
    public int sequence;
    public LinkedList<Packet> packets = new LinkedList<>();

    public int start() {
        return packets.isEmpty() ? 0 : packets.getFirst().start;
    }

    public int end() {
        return packets.isEmpty() ? 0 : packets.getLast().end;
    }

    public boolean isConsumable(final Packet packet) {
        return packet.start == end() + 1;
    }

    public void consume(final Packet packet) {
        packets.addLast(packet);
    }

    public boolean isComplete() {
        return packets.getLast().end == size;
    }
}

class Permutation {
    public static void next(int[] nums) {
        int index = -1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            reverse(nums, 0, nums.length - 1);
            return;
        }

        for (int i = nums.length - 1; i > index; i--) {
            if (nums[i] > nums[index]) {
                swap(nums, i, index);
                break;
            }
        }

        reverse(nums, index + 1, nums.length - 1);
    }

    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
