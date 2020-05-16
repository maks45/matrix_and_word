public class Main {
    public static void main(String[] args) {

    }

    private static void FindWord(String matrix, String word){
        int sideLength = (int) Math.sqrt(matrix.length());
        if(sideLength * sideLength != matrix.length()){
            System.out.println("matrix should be square ");
        }

    }
}
