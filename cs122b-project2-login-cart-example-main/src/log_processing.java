import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class log_processing {
    public static void main(String[] args) {
        String filePath = args[0];
        int totalSamples = 0;
        long totalTS = 0;
        long totalTJ = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TS: ")) {
                    long ts = Long.parseLong(line.substring(4));
                    totalTS += ts;
                    totalSamples++;
                } else if (line.startsWith("TJ: ")) {
                    long tj = Long.parseLong(line.substring(4));
                    totalTJ += tj;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (totalSamples > 0) {
            double averageTS = (double) totalTS / totalSamples;
            double averageTJ = (double) totalTJ / totalSamples;

            System.out.println("Average TS: " + averageTS);
            System.out.println("Average TJ: " + averageTJ);
        }
    }
}

