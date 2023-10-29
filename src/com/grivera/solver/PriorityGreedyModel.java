package com.grivera.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringJoiner;

import com.grivera.generator.Network;
import com.grivera.generator.sensors.DataNode;
import com.grivera.generator.sensors.SensorNode;
import com.grivera.generator.sensors.StorageNode;
import com.grivera.util.Tuple;

public class PriorityGreedyModel extends AbstractModel {
    private Map<SensorNode, List<Tuple<StorageNode, Integer, List<SensorNode>>>> routes;
    private int totalValue;
    private int totalCost;
    private int totalProfit;

    public PriorityGreedyModel(Network network) {
        super(network);
    }

    public PriorityGreedyModel(String fileName) {
        super(fileName);
    }

    public PriorityGreedyModel(String fileName, int overflowPackets, int storageCapacity) {
        super(fileName, overflowPackets, storageCapacity);
    }

    public void run(int episodes) {
        System.out.println("Warning: Ignoring episodes count; defaulting to 1...");
        this.run();
    }

    @Override
    public void run() {
        super.run();
        this.totalValue = 0;
        this.totalCost = 0;
        this.totalProfit = 0;
        this.routes = new HashMap<>();
        
        Network network = this.getNetwork();
        List<DataNode> sortedDns = new ArrayList<>(network.getDataNodes());
        sortedDns.sort((dn1, dn2) -> -Integer.compare(dn1.getOverflowPacketValue(), dn2.getOverflowPacketValue()));

        Queue<StorageNode> sortedSns;
        StorageNode sn;
        int packetsToSend;
        for (DataNode dn : sortedDns) {
            sortedSns = new PriorityQueue<>((sn1, sn2) -> Integer.compare(network.calculateMinCost(dn, sn1), network.calculateMinCost(dn, sn2)));
            sortedSns.addAll(network.getStorageNodes());
            while (dn.hasEnergy() && !dn.isEmpty() && !sortedSns.isEmpty()) {
                sn = sortedSns.poll();
                if (sn.isFull()) {
                    continue;
                }

                packetsToSend = dn.getPacketsLeft();
                while (!network.canSendPackets(dn, sn, packetsToSend)) {
                    packetsToSend--;
                    if (packetsToSend <= 0) {
                        break;
                    }
                }
                if (packetsToSend <= 0) {
                    continue;
                }

                this.totalValue += dn.getOverflowPacketValue() * packetsToSend;
                this.totalCost += network.calculateMinCost(dn, sn) * packetsToSend;
                routes.putIfAbsent(dn, new ArrayList<>());
                routes.get(dn).add(Tuple.of(sn, packetsToSend, network.getMinCostPath(dn, sn)));

                network.sendPackets(dn, sn, packetsToSend);
            }
        }

        this.totalProfit = this.totalValue - this.totalCost;
    }
    
     @Override
    public int getTotalValue() {
        super.getTotalValue();
        return this.totalValue;
    }

    @Override
    public int getTotalCost() {
        super.getTotalCost();
        return this.totalCost;
    }

    @Override
    public int getTotalProfit() {
        super.getTotalProfit();
        return this.totalProfit;
    }

    @Override
    public int getTotalPackets() {
        super.getTotalPackets();

        int totalPackets = 0;
        for (StorageNode sn : this.getNetwork().getStorageNodes()) {
            totalPackets += sn.getUsedSpace();
        }
        return totalPackets;
    }

    @Override
    public void printRoute() {
        super.printRoute();
        StringJoiner str;

        for (Map.Entry<SensorNode, List<Tuple<StorageNode, Integer, List<SensorNode>>>> entry : this.routes.entrySet()) {
            for (Tuple<StorageNode, Integer, List<SensorNode>> route : entry.getValue()) {
                str = new StringJoiner(" -> ", "[", "]");
                System.out.printf("%s -> %s (flow = %d)\n", entry.getKey().getName(), route.first().getName(), route.second());
                for (SensorNode node : route.third()) {
                    str.add(node.getName());
                }
                System.out.printf("\t%s\n", str);
            }
        }

    }
}
