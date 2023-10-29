import com.grivera.generator.SensorNetwork;
import com.grivera.solver.ILPModel;
import com.grivera.solver.ILPWeightedModel;
import com.grivera.solver.Model;
import com.grivera.solver.PriorityGreedyModel;

public class CompareMaxFlow {
    public static void main(String[] args) {
        SensorNetwork network = SensorNetwork.from("compare_max_flow_9_21.sn");
        Model model;
        // System.out.println("Min detected is " + binarySearch(network, 2_500_000));
        // System.out.println("Min detected is " + binarySearch(network, Integer.MAX_VALUE));

        System.out.println();
        System.out.printf("%-16s | %19s | %23s | %-26s | %26s\n", "Model Type", "initial energy (\u00b5J)", "total packets offloaded", "total value collected (\u00b5J)", "total preservation cost (\u00b5J)");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        for (int initialEnergy = 500_000; initialEnergy <= 2_500_000; initialEnergy += 500_000) {
            network.setBatteryCapacity(initialEnergy);

            model = new PriorityGreedyModel(network);
            model.run();
            System.out.printf("%-16s   %,19d   %,23d   %,26d   %,26d\n", "Priority Greedy", initialEnergy, model.getTotalPackets(), model.getTotalValue(), model.getTotalCost());

            model = new ILPModel(network);
            model.run();
            System.out.printf("%-16s   %,19d   %,23d   %,26d   %,26d\n", "ILP", initialEnergy, model.getTotalPackets(), model.getTotalValue(), model.getTotalCost());
            
            model = new ILPWeightedModel(network);
            model.run();
            System.out.printf("%-16s   %,19d   %,23d   %,26d   %,26d\n", "ILP (Weighted)", initialEnergy, model.getTotalPackets(), model.getTotalValue(), model.getTotalCost());
            System.out.println();
        }
        System.out.println();
    }

    // public static int binarySearch(SensorNetwork network, int max) {
    //     Model model = new ILPModel(network);
    //     network.setBatteryCapacity(max);
    //     network.resetPackets();
    //     network.resetEnergy();
    //     model.run();
    //     boolean isFeasible = model.getTotalPackets() == network.getDataNodes().stream().mapToInt(n -> n.getOverflowPackets()).sum();
    //     if (!isFeasible) {
    //         System.err.println("is not max flow feasible as-is");
    //         return -1;
    //     }

    //     int low = 0;
    //     int high = max;

    //     int mid;
    //     int solution = -1;
    //     while (low <= high) {
    //         mid = (low + high) / 2;
    //         network.setBatteryCapacity(mid);
    //         model.run();
    //         isFeasible = model.getTotalPackets() == network.getDataNodes().stream().mapToInt(n -> n.getOverflowPackets()).sum();
    //         if (isFeasible) {
    //             solution = mid;
    //             high = mid - 1;
    //         } else {
    //             low = mid + 1;
    //         }
    //     }
    //     return solution;
    // }
}
