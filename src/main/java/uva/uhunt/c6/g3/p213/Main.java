package uva.uhunt.c6.g3.p213;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 213 - Message Decoding
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=149
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        final LinkedList<String> lines = new LinkedList<>();
        for (String line = io.readLine(); line != null; line = io.readLine()) {
            lines.add(line);
        }

        final Input input = new Input();
        input.lines = lines.toArray(new String[0]);

        final Output output = process.process(input);
        for (final String decodedMessage : output.decodedMessages) {
            io.write("%s\n", decodedMessage);
        }

        io.close();
    }
}

class Input {
    public String[] lines;
}

class Output {
    public String[] decodedMessages;
}

class Process {
    private static final String[] KEYS = createKeys();

    public Output process(final Input input) {
        final Queue<String> lineq = new LinkedList<>(Arrays.asList(input.lines));
        final Queue<Character> messageq = new LinkedList<>();
        final List<String> decodedMessages = new LinkedList<>();

        while (!lineq.isEmpty()){
            final String header = read(lineq);
            final Map<String, Character> decodedKeys = mapDecodedKeys(header);
            final StringBuilder decodedMessage = new StringBuilder();

            while (true) {
                final String encodedLength = read(lineq, messageq, 3);
                final int length = Integer.parseInt(encodedLength, 2);
                if (length == 0) break;

                while (true) {
                    final String key = read(lineq, messageq, length);
                    if (!key.contains("0")) break;
                    final char decodedKey = decodedKeys.get(key);
                    decodedMessage.append(decodedKey);
                }
            }
            decodedMessages.add(decodedMessage.toString());
        }

        final Output output = new Output();
        output.decodedMessages = decodedMessages.toArray(new String[0]);
        return output;
    }

    private Map<String, Character> mapDecodedKeys(final String header) {
        final Map<String, Character> decodedKeys = new HashMap<>(header.length());
        for (int i = 0; i < header.length(); i++) {
            final String key = KEYS[i];
            final char letter = header.charAt(i % header.length());
            decodedKeys.put(key, letter);
        }
        return decodedKeys;
    }

    private static String[] createKeys() {
        final List<String> keys = new LinkedList<>();

        for (int i = 0; Integer.bitCount(i) <= 7; i++) {
            final String binary = Integer.toBinaryString(i);

            final StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(binary);
            for (int length = binary.length(); length <= 7; length++) {
                final String key = keyBuilder.toString();
                if (key.contains("0")) {
                    keys.add(key);
                }
                keyBuilder.insert(0, '0');
            }
        }

        final Comparator<String> orderByLengthAndAlphabetical = Comparator
                .comparingInt(String::length)
                .thenComparing(key -> key);

        return keys.stream()
                .distinct()
                .sorted(orderByLengthAndAlphabetical)
                .toArray(String[]::new);
    }

    private static String read(final Queue<String> lineq) {
        return lineq.poll();
    }

    private static String read(final Queue<String> lineq, final Queue<Character> messageq, final int length) {
        final StringBuilder result = new StringBuilder();

        while (messageq.size() < length) {
            final String line = lineq.poll();
            for (final char letter : line.toCharArray()) {
                messageq.add(letter);
            }
        }

        for (int i = 0; i < length; i++) {
            result.append(messageq.poll());
        }

        return result.toString();
    }
}

final class BufferedIO {
    private final BufferedReader in;
    private final BufferedWriter out;

    public BufferedIO(final InputStream in, final OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String[] readLine(final String separator) throws IOException {
        final String line = readLine();
        return line == null ? null : line.split(separator);
    }

    public String readLine() throws IOException {
        String line = in.readLine();
        while (line != null && line.isEmpty()) line = in.readLine();
        return line;
    }

    public void write(final String format, Object... args) throws IOException {
        final String string = String.format(format, args);
        write(string);
    }

    public void write(final String string) throws IOException {
        out.write(string);
    }

    public void close() throws IOException {
        in.close();
        out.flush();
        out.close();
    }
}
