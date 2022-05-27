import {OrderInfo} from "../model/order/order-info";
import {Order} from "../model/order/order";

export class MapperUtility {
  public static mapOrderInfoToOrder(info: OrderInfo) {
    return {
      dishId: info.dishId,
      name: info.name,
      amount: info.amount,
      price: info.price,
    } as Order;
  }
}
