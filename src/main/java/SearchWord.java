import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchWord {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("enter matrix and word trough space or 'q' to exit");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            }
            String[] params = input.split(" ");
            if (params.length > 1) {
                findWord(params[0], params[1]);
            }
        }
    }

    private void findWord(String matrix, String word) {
        int matrixSize = (int) Math.sqrt(matrix.length());
        if (matrixSize * matrixSize != matrix.length()) {
            System.out.println("matrix should be square N * N : "
                    + matrix + " not valid value");
            return;
        }
        System.out.println(findCharsPosition(word.toCharArray(),
                createMatrix(matrix))
                .map(positions -> positions.stream()
                        .map(Position::toString)
                        .collect(Collectors.joining("->")))
                .orElse("word positions can't be found"));
    }

    private Optional<List<Position>> findCharsPosition(char[] word, char[][] matrix) {
        List<List<Position>> chains = new ArrayList<>();
        for (int i = 0; i < word.length; i++) {
            for (Position position : getCharPositions(matrix, word[i])) {
                if (i == 0) {
                    List<Position> chain = new ArrayList<>();
                    chain.add(position);
                    chains.add(chain);
                } else {
                    int j =0;
                    while(j < chains.size()){
                        List<Position> chain = chains.get(j);
                        if (chain.size() >= i
                                && chain.stream().noneMatch(p -> p.equals(position))
                                && chain.get(i - 1).isNear(position)) {
                            if(chain.size() == i) {
                                chain.add(position);
                            }else {
                                List<Position> nChain = new ArrayList<>(chain);
                                nChain.set(i, position);
                                chains.add(nChain);
                            }
                        }
                        j++;
                    }
                }
            }
        }
        return chains.stream()
                .filter(l -> l.size() == word.length)
                .findFirst();
    }

    private List<Position> getCharPositions(char[][] matrix, char letter) {
        List<Position> positions = new ArrayList<>();
        IntStream.range(0, matrix.length).forEach(
                i -> IntStream.range(0, matrix[i].length)
                        .filter(j -> matrix[i][j] == letter)
                        .forEach(j -> positions.add(new Position(i, j))));
        return positions;
    }

    private char[][] createMatrix(String source) {
        int size = (int) Math.sqrt(source.length());
        char[][] matrix = new char[size][size];
        IntStream.range(0, source.length())
                .forEach(i -> matrix[i / size][i % size] = source.charAt(i));
        return matrix;
    }

    private static class Position {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private boolean isNear(Position position) {
            return (Math.abs(position.x - x) == 1 && position.y == y)
                    || (Math.abs(position.y - y) == 1 && position.x == x);
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + ']';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
