package com.driver;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class orderRepository {
    HashMap<String, Order> OrdersList = new HashMap<>();

    HashMap<String, DeliveryPartner> DeliveryPartnerList = new HashMap<>();

    HashMap<String, List<Order>> PartnerPairOrder = new HashMap<>();

    HashMap<String, String> OrderPairPartner = new HashMap<>();



    public void addOrder(Order order) {
        Order ordersToAdd = new Order(order.getId(), Integer.toString(order.getDeliveryTime()));

        OrdersList.put(order.getId(), ordersToAdd);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartnerToAdd = new DeliveryPartner(partnerId);
        DeliveryPartnerList.put(partnerId, deliveryPartnerToAdd);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(!PartnerPairOrder.containsKey(partnerId)){
            // no contains
            PartnerPairOrder.put(partnerId, new ArrayList<>());
        }
        Order orderPaired = OrdersList.get(orderId);
        PartnerPairOrder.get(partnerId).add(orderPaired);

        DeliveryPartner deliveryPartner = DeliveryPartnerList.get(partnerId);
        int currOrder = deliveryPartner.getNumberOfOrders();
        deliveryPartner.setNumberOfOrders(currOrder + 1);

        OrderPairPartner.put(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return OrdersList.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return DeliveryPartnerList.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner deliveryPartner = DeliveryPartnerList.get(partnerId);
        int currOrder = deliveryPartner.getNumberOfOrders();
        return currOrder;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<Order> orders = PartnerPairOrder.get(partnerId);


        List<String> ordersList = new ArrayList<>();

        for(Order order : orders){
            ordersList.add(order.toString());
        }
        return ordersList;
    }

    public List<String> getAllOrders() {
        List<String> allOrders = new ArrayList<>();

        for(Order order: OrdersList.values()){

            allOrders.add(order.toString());
        }
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        int allOrders = OrdersList.size();

        int AllotedOrders = 0;
        for( String partnerId : PartnerPairOrder.keySet()){
            AllotedOrders += PartnerPairOrder.get(partnerId).size();
        }
        return (allOrders - AllotedOrders);
    }

    public void deletePartnerById(String partnerId) {
        if(DeliveryPartnerList.containsKey(partnerId)) {
            DeliveryPartnerList.remove(partnerId);
        }
        if(PartnerPairOrder.containsKey(partnerId)){
            PartnerPairOrder.remove(partnerId);
        }
        for(String orderId : OrderPairPartner.keySet()){
            if(OrderPairPartner.get(orderId) == partnerId){
                OrderPairPartner.remove(orderId);
                break;
            }
        }

    }

    public void deleteOrderById(String orderId) {
        if(OrdersList.containsKey(orderId))
            OrdersList.remove(orderId);

        if(OrderPairPartner.containsKey(orderId)){

            String partnerId = OrderPairPartner.get(orderId);
            List<Order> allOrdersAssignedToPartner = PartnerPairOrder.get(partnerId);

            for(Order order : allOrdersAssignedToPartner){
                if(order.getId() == orderId){
                    allOrdersAssignedToPartner.remove(order);
                    break;
                }
            }
            PartnerPairOrder.put(partnerId, allOrdersAssignedToPartner);
            OrderPairPartner.remove(orderId);
        }
    }
}
