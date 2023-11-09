import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.logging.Logger;

import com.grivera.generator.SensorNetwork;
import com.grivera.solver.ILPModel;
import com.grivera.solver.ILPWeightedModel;
import com.grivera.solver.Model;
import com.grivera.solver.PriorityGreedyModel;

public class GenerateDat {
    private static Logger logger = Logger.getLogger("GenerateDat");

    public static void main(String[] args) {
        createFiles();

        SensorNetwork network = SensorNetwork.from("compare_max_flow_9_21.sn");
        Model greedy;
        Model ilp;
        Model ilpWeighted;

        double joules;

        for (int initialEnergy = 500_000; initialEnergy <= 2_500_000; initialEnergy += 500_000) {
            network.setBatteryCapacity(initialEnergy);
            logger.info(String.format("Running models starting with %,d microJ of energy", initialEnergy));

            greedy = new PriorityGreedyModel(network);
            greedy.run();

            ilp = new ILPModel(network);
            ilp.run();
            
            ilpWeighted = new ILPWeightedModel(network);
            ilpWeighted.run();

            logger.info("Successfully ran models");

            joules = initialEnergy * Math.pow(10, -3);
            
            logger.info("Writting to files");
            writeTo("dats/packets.dat", joules, greedy.getTotalPackets(), ilp.getTotalPackets(), ilpWeighted.getTotalPackets());
            writeTo("dats/value.dat", joules, greedy.getTotalValue() * Math.pow(10, -3), ilp.getTotalValue() * Math.pow(10, -3), ilpWeighted.getTotalValue() * Math.pow(10, -3));
            writeTo("dats/cost.dat", joules, greedy.getTotalCost() * Math.pow(10, -3), ilp.getTotalCost() * Math.pow(10, -3), ilpWeighted.getTotalCost() * Math.pow(10, -3));
            logger.info("Successfully wrote to files");
        }
        System.out.println();
    }

    public static void createFiles() {
        try {
            Files.createDirectory(Path.of("dats/"));
        } catch (IOException ignored) {
        }

        File file;
        PrintWriter pw;
        try {
            file = new File("dats/packets.dat");
            pw = new PrintWriter(file);
            pw.println("# initial_energy\tgreedy\tgreedy_conf\tMF\tMF_conf\tMWF\tMWF_conf");
            pw.close();

            file = new File("dats/value.dat");
            pw = new PrintWriter(file);
            pw.println("# initial_energy\tgreedy\tgreedy_conf\tMF\tMF_conf\tMWF\tMWF_conf");
            pw.close();

            file = new File("dats/cost.dat");
            pw = new PrintWriter(file);
            pw.println("# initial_energy\tgreedy\tgreedy_conf\tMF\tMF_conf\tMWF\tMWF_conf");
            pw.close();
        } catch (IOException ignored) {
        }
    }

    public static void writeTo(String fileName, double ... values) {
        StringJoiner sj = new StringJoiner("\t");
        try (FileWriter fw = new FileWriter(new File(fileName), true)) {
            for (int index = 0; index < values.length; index++) {
                sj.add(Double.toString(values[index]));

                if (index > 0) {
                    // sj.add("0");
                    sj.add("0.001959964");
                }
            }
            fw.append(String.format("%s\n", sj));
        } catch (IOException e) {
            System.err.printf("Failed to write to '%s'\n", fileName);
            System.exit(1);
        }
    }
}
