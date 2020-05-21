
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchWord implements Runnable{

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("enter matrix and word through space or 'q' to exit");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            }
            String[] params = input.split(" ");
            if (params.length == 2) {
                System.out.println(findWord(params[0], params[1]));
            }
        }
    }

    private String findWord(String matrix, String word) {
        int matrixSize = (int) Math.sqrt(matrix.length());
        if (matrixSize * matrixSize != matrix.length()) {
             return "matrix should be square N * N : "
                    + matrix + " not valid value";
        }
        String result = findCharsPosition(word.toCharArray(), createMatrix(matrix)).stream()
                .map(Position::toString)
                .collect(Collectors.joining("->"));
        return result.length() > 0 ? result : "word positions can't be found";
    }

    private List<Position> findCharsPosition(char[] word, char[][] matrix) {
        List<List<Position>> chains = new ArrayList<>();
        for (int i = 0; i < word.length; i++) {
            for (Position position : getCharPositions(matrix, word[i])) {
                if (i == 0) {
                    List<Position> chain = new ArrayList<>();
                    chain.add(position);
                    chains.add(chain);
                } else {
                    int j = 0;
                    while (j < chains.size()) {
                        List<Position> chain = chains.get(j);
                        if (chain.size() >= i
                                && chain.stream().noneMatch(p -> p.equals(position))
                                && chain.get(i - 1).isNear(position)) {
                            if (chain.size() == i) {
                                chain.add(position);
                            } else {
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
                .findFirst().orElse(new ArrayList<>());
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
        int matrixSize = (int) Math.sqrt(source.length());
        char[][] matrix = new char[matrixSize][matrixSize];
        IntStream.range(0, source.length())
                .forEach(i -> matrix[i / matrixSize][i % matrixSize] = source.charAt(i));
        return matrix;
    }

    private static class Position extends Point {

        public Position(int x, int y) {
            super(x, y);
        }

        private boolean isNear(Position position) {
            return (Math.abs(position.x - x) == 1 && position.y == y)
                    || (Math.abs(position.y - y) == 1 && position.x == x);
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + ']';
        }

    }
}
