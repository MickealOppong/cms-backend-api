package opp.mic.cms.service;


import opp.mic.cms.model.Order;
import opp.mic.cms.repository.OrdersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {



    private OrdersRepository ordersRepository;

    public OrderService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Page<Order> allOrders(int page){
        PageRequest pageRequest = PageRequest.of(page,10);
        return ordersRepository.findAll(pageRequest);
    }
}
