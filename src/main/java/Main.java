import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        findWord("129436126", "123");
        findWord("QLGNAEKIRLRNGEAE", "KING");
        findWord("QLGNAEKIRLRNGEAE", "QLGNIKEA");
        findWord("QLGNAEKIRLRNGEAE", "IKEA");
        findWord("QLGNAEKIRLRNGEAE", "ENINGL");
        findWord("QLGNAEKIRLRNGEAE", "AE");
    }

    private static void findWord(String matrix, String word) {
        List<Position> result = findCharsPosition(word.toCharArray(), createMatrix(matrix));
        if (result.size() == 0) {
            System.out.println("word positions can't be found");
        } else {
            System.out.println(result.stream()
                    .map(Position::toString)
                    .collect(Collectors.joining("->")));
        }
    }

    private static List<Position> findCharsPosition(char[] word, char[][] matrix) {
        List<List<Position>> chains = new ArrayList<>();
        for (int i = 0; i < word.length; i++) {
            for (Position position : getCharPositions(matrix, word[i])) {
                if (i == 0) {
                    List<Position> chain = new ArrayList<>();
                    chain.add(position);
                    chains.add(chain);
                } else {
                    for (List<Position> chain : chains) {
                        if (chain.size() == i
                                && chain.stream().noneMatch(p -> p.equals(position))
                                && chain.get(i - 1).isNear(position)) {
                            chain.add(position);
                        }
                    }
                }
            }
        }
        return chains.stream()
                .filter(l -> l.size() == word.length)
                .findFirst()
                .orElse(new ArrayList<>());
    }

    private static List<Position> getCharPositions(char[][] matrix, char letter) {
        List<Position> positions = new ArrayList<>();
        IntStream.range(0, matrix.length).forEach(
                i -> IntStream.range(0, matrix[i].length)
                        .filter(j -> matrix[i][j] == letter)
                        .forEach(j -> positions.add(new Position(i, j))));
        return positions;
    }

    private static char[][] createMatrix(String source) {
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
