import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int k = Integer.parseInt(args[0]);
        List<List<String>> treningData = ReadDataFromFile(args[1]);
        List<List<String>> testData = ReadDataFromFile(args[2]);

        int correct = 0;
        for (List<String> ele : testData) {
            String data = ele.get(ele.size() - 1);
            String countedData = knnResoult(treningData, k, ele);
            if (data.equals(countedData)) correct++;
            System.out.println(ele.get(ele.size() - 1) + ' ' + countedData);
        }
        double accuracy = (double) (correct) / testData.size() * 100;
        System.out.println("accuracy = " + BigDecimal.valueOf(accuracy).setScale(2, RoundingMode.HALF_UP));

        System.out.println("Teraz będziesz mógł podawać własne wartości (oddzielone średnikiem)");
        System.out.println("Liczba podawanych atrybutów: " + (treningData.get(0).size() - 1));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Podaj wektor: ");
            String line = scanner.next();
            line = line.replace(',', '.');
            List<String> readList = new ArrayList<>(Arrays.asList(line.split(";")));
            System.out.println("Najprawdopodobniej jest to: " + knnResoult(treningData, k, readList));
        }

    }


    public static List<List<String>> ReadDataFromFile(String loc) throws FileNotFoundException {
        File trainignFile = new File(loc);
        Scanner scanner = new Scanner(trainignFile);
        List<List<String>> lists = new ArrayList<>();
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            String[] tmpTab = line.split(";");
            lists.add(new ArrayList<>(Arrays.asList(tmpTab)));
        }
        return lists;
    }


    public static String knnResoult(List<List<String>> trainingSet, int k, List<String> toClasyfy) throws Exception {
        if (k > trainingSet.size()) {
            throw new Exception("Nie może być sytuacji, gdzie hiperparametr K jest większy niż zbiór treningowy");
        }
        String odp = "";
        List<Double> distances = new ArrayList<>();
        for (List<String> ele : trainingSet) {
            distances.add(distanceBetween(ele, toClasyfy));
        }
        List<String> neightbours = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            double min = distances.get(0);
            int minIndex = 0;
            for (int j = 1; j < distances.size(); j++) {
                if (distances.get(j) < min) {
                    min = distances.get(j);
                    minIndex = j;
                }
            }
            neightbours.add(trainingSet.get(minIndex).get(trainingSet.get(minIndex).size() - 1));
            distances.remove(minIndex);
        }
        int max = 0;
        for (int i = 0; i < neightbours.size(); i++) {
            int count = 0;
            for (int j = i; j < neightbours.size(); j++) {
                if (neightbours.get(i).equals(neightbours.get(j))) count++;
            }
            if (count > max) {
                max = count;
                odp = neightbours.get(i);
            }
        }
        return odp;
    }

    public static double distanceBetween(List<String> train, List<String> test) {
        double res = 0;
        for (int i = 0; i < train.size() - 1; i++) {
            res += Math.pow(Double.parseDouble(train.get(i)) - Double.parseDouble(test.get(i)), 2);
        }
        return res;
    }
}

